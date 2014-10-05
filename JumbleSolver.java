import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
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
	 * No arguments starts an interactive session in
	 * the shell.  Arguments cause the program to 
	 * behave as a command line utility like "cat" 
	 * or "ls" where the arguments are the strings
	 * to be unjumbled.
	 */
	public static void main(String[] args) {
		JumbleSolver js = new JumbleSolver();
		if(args.length < 1){
			js.startInteractiveSession();
		}
		else { // else process each argument
			for(String argument: args){
				BigInteger hash = js.calcHash(argument);
				if(hash.compareTo(negativeOne) != 0){
					StringBuffer results = js.getHashDivisbleWords(hash) ;
					System.out.println(argument.toUpperCase()+": "+results.toString()) ;
				} else  {
					System.out.println(argument.toUpperCase()+invalidInputError) ;
				}
			}
		}
	}

	private BufferedWriter hashFile;
	private BigInteger[] primes = { BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(5), 
			BigInteger.valueOf(7), BigInteger.valueOf(11), BigInteger.valueOf(13), BigInteger.valueOf(17), 
			BigInteger.valueOf(19), BigInteger.valueOf(23), BigInteger.valueOf(29), BigInteger.valueOf(31), 
			BigInteger.valueOf(37), BigInteger.valueOf(41),	BigInteger.valueOf(43), BigInteger.valueOf(47), 
			BigInteger.valueOf(53), BigInteger.valueOf(59), BigInteger.valueOf(61), BigInteger.valueOf(67), 
			BigInteger.valueOf(71), BigInteger.valueOf(73), BigInteger.valueOf(79), BigInteger.valueOf(83), 
			BigInteger.valueOf(89), BigInteger.valueOf(97), BigInteger.valueOf(101) };
	
	private static BigInteger negativeOne = BigInteger.valueOf(-1);
	private static final String emptyToExit = "TYPE EMPTY INPUT TO EXIT." ;
	private static final String welcome= "GREETINGS PROFESSOR FALKEN. SHALL WE PLAY A GAME?" +
			"\nTYPE A STRING TO CONTINUE: " ;
	private static final String prompt = "TYPE A STRING TO CONTINUE: ";
	private static final String bye = "GOODBYE." ;
	private static final String hashFileName = "wordsWithHashes.txt" ;
	private static final String wordsDirectory = "words/" ;
	private static final String invalidInputError = " IS INVALID. PLEASE USE ONLY ENGLISH LETTERS." ;
	
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
				BigInteger hash = calcHash(line);
				// If word read is a contraction or
				// is invalid, ignore it.
				if (hash.compareTo(negativeOne) == 0)
					continue;
				// buffer into stringbuffer for
				// performance
				hashBuffer.append(line + "," + hash.toString() + "\n");
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
			BigInteger hash = calcHash(inputString);
			// empty input ends program
			if (hash.compareTo(BigInteger.ONE) == 0)
				break;
			
			if(hash.compareTo(negativeOne) == 0) {
				System.out.println(inputString.toUpperCase()+invalidInputError) ;
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
	public StringBuffer getHashDivisbleWords(BigInteger jumbledStringHash) {
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
				BigInteger wordHash = new BigInteger(wordhashCouple[1]);
				// If divisible, then the word can be made 
				// with input letters
				if (jumbledStringHash.mod(wordHash).compareTo(BigInteger.ZERO) == 0)
					buffer.append(wordhashCouple[0]+",");
			}
			hashes.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			// File Format is probably wrong
			e.printStackTrace();
			System.out.print("Who took my hashfile?") ;
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
	public BigInteger calcHash(String line) {
		if (line == null){
			return negativeOne ;
		}
		
		BigInteger hash = BigInteger.valueOf(1) ;
		line = line.toLowerCase(); // For comparision simplicity
		for (int i = 0; i < line.length(); i++) {
			char character = line.charAt(i);
			// ignore possesives
			if (character == '\'')
				return negativeOne;

			int letter = character - 'a';
			// english words may only have the 26 
			// letters in the alphabet.  
			if (letter < 0 || letter > 25)
				return negativeOne;
			// Hyphenated word(s)/phrase(s) are actually
			// spelled in dictionaries or aren't real words
			// without the hyphen.  Not sure which one, but
			// either way, we don't need to worry about that.
			hash = hash.multiply(primes[letter]) ;
		}
		return hash;
	}
}