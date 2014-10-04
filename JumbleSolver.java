import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Author - Vishnu Indukuri 
 * This is a fairly simple program that takes as 
 * input a string of letters and returns a list of english words that can be
 * made using each given letter only once.
 * 
 * Obviously, there are a great number of words in the English language; some
 * words can be spelled in multiple ways, spellings differ between dialects, and
 * there are plural and possessive versions of words so some optimization and
 * pruning of the word list was necessary to keep this program from outputing
 * eight different spellings for every word. As a result, I cut out the less
 * used spellings, the british and canadian versions, contractions,
 * abbreviations, and a large number of words that are almost never used or
 * aren't even considered words anymore (the 70-95 lists in the given SCOWL
 * dump).
 * 
 * For explanation on how this program works, read the README.
 */

public class JumbleSolver {
	/**
	 * Main method of the program. Exec starts here.
	 */
	public static void main(String[] args) {
		JumbleSolver js = new JumbleSolver();
		if(args.length < 1){
			js.startInteractiveSession();
		}
		else { // else process each argument
			for(String argument: args){
				long hash = js.calcHash(argument);
				if(hash != -1){
					StringBuffer results = js.getHashDivisbleWords(hash) ;
					System.out.println(argument.toUpperCase()+": "+results.toString()) ;
				} else  {
					System.out.println(argument.toUpperCase()+" IS INVALID. " +
							"PLEASE USE ONLY ENGLISH LETTERS.") ;
				}
			}
		}
	}

	private BufferedWriter hashFile;
	private long[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101 };
	
	private static final String emptyToExit = "TYPE EMPTY INPUT TO EXIT." ;
	private static final String welcome= "GREETINGS PROFESSOR FALKEN. SHALL WE PLAY A GAME?" +
			"\nTYPE A STRING TO CONTINUE: " ;
	private static final String prompt = "TYPE A STRING TO CONTINUE: ";
	private static final String bye = "GOODBYE." ;
	private static final String hashFileName = "wordsWithHashes.txt" ;
	private static final String wordsDirectory = "words/" ;
	
	/**
	 * Constructor. Creates hashfile.
	 * 
	 * @throws IOException
	 */
	public JumbleSolver() {
		// Init our custom hash file
		try {
			hashFile = new BufferedWriter(new FileWriter(new File(hashFileName), false));
		} catch (IOException e) {
			// WTF? exit.
			e.printStackTrace();
			System.out.println("Cannot open hash file!  Do I have permission?");
			System.exit(100);
		}

		// Get list of files to read
		// AKA files in words directory
		File folder = new File(wordsDirectory);
		File[] listOfFiles = folder.listFiles();
		// Read each file in the directory
		for (File file : listOfFiles) {
			if (file.isFile()) {
				readFile(file);
			}
		}

		// Close output file. 
		try {
			hashFile.close();
		} catch (IOException e) { e.printStackTrace();}
	}

	/**
	 * Reads a file, calculating a hash for every word and writing 
	 * back to output file. Format: "word,hash\n"
	 * 
	 * @param file A word list file.
	 */
	private void readFile(File file) {
		StringBuffer hashBuffer = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			for (String line; (line = br.readLine()) != null;) {
				long hash = calcHash(line);
				// If word read is a contraction or
				// is invalid, ignore it.
				if (hash == -1)
					continue;
				// buffer into stringbuffer for
				// performance
				hashBuffer.append(line + "," + hash + "\n");
			}
			// write it out only once per file.
			// for performance.
			hashFile.write(hashBuffer.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // nothing we can do about it
			System.out.print("Looks like I don't have permission to read "+file.toString()) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Blocks.  Sits around waiting for user input. 
	 * Empty input ends program.
	 */
	public void startInteractiveSession() {
		String inputString;
		Scanner inputScanner = new Scanner(System.in);
		// Prompt for input 
		System.out.println(emptyToExit) ;
		System.out.print(welcome);
		while ((inputString = inputScanner.nextLine()) != null) {
			long hash = calcHash(inputString);
			// empty input ends program
			if (hash == 1)
				break;
			
			if(hash == -1) {
				System.out.println(inputString.toUpperCase()+" IS INVALID. " +
						"PLEASE USE ONLY ENGLISH LETTERS.") ;
			} else {
				StringBuffer buffer = getHashDivisbleWords(hash);
				System.out.print(buffer.toString() + "\n");
			}
			// Prompt for next input
			System.out.print(prompt);
		}

		// Clean up file resources
		inputScanner.close();
		System.out.println(bye) ;
	}

	/** 
	 * The search function.  Takes a hash and 
	 * looks for words in our hash file with 
	 * hashes that are divisible.
	 * @param jumbledStringHash Hash of jumbled string
	 * @return StringBuffer comma separated list of words  
	 */
	public StringBuffer getHashDivisbleWords(long jumbledStringHash) {
		String wordcommahash;
		StringBuffer buffer = new StringBuffer();
		// open a read stream to our hash file.
		try (BufferedReader hashes = new BufferedReader(new FileReader(
				new File(hashFileName)));) {
			// Loop through file
			while ((wordcommahash = hashes.readLine()) != null) {
				String[] wordhashCouple = wordcommahash.split(",");
				if(wordhashCouple[0].length() < 2 && !wordhashCouple[0].equals("a"))
					continue ;
				long wordHash = Long.parseLong(wordhashCouple[1]);
				// If divisible, then the word can be made 
				// with input letters
				if (jumbledStringHash % wordHash == 0)
					buffer.append(wordhashCouple[0]+",");
			}
			hashes.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Get rid of that last comma
		// Just to be a little cleaner
		int lastComma = buffer.lastIndexOf(",") ;
		if(lastComma != -1) buffer.deleteCharAt(lastComma);
		return buffer ;
	}
	
	/**
	 * Calculates prime number hash of string. If line is null, line
	 * contains an apostrophe or anything but letters then this
	 * returns -1 (so that contractions, abbrevs., and  possessive 
	 * forms of real words don't show up in our hash file).
	 * 
	 * Plural forms will still show up. That is an optimization I will make in
	 * the future. TODO
	 * 
	 * @param line String to be hashed
	 * @return the hash
	 */
	public long calcHash(String line) {
		if (line == null){
			return -1;
		}
		
		long hash = 1;
		line = line.toLowerCase(); // For comparision simplicity
		for (int i = 0; i < line.length(); i++) {
			char character = line.charAt(i);
			// ignore possesives
			if (character == '\'')
				return -1;

			int letter = character - 'a';
			// english words may only have the 26 
			// letters in the alphabet.  
			if (letter < 0 || letter > 25)
				return -1;
			// Hyphenated words are apparently
			// actually spelled in dictionaries without the
			// hyphen so we don't need to worry about that
			hash = hash * primes[letter];
		}
		return hash;
	}
}