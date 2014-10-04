/* 
 * Author - Vishnu Indukuri
 * This is a fairly simple program that takes
 * as an input a string of letters and returns
 * a list of english words that can be made 
 * using each given letter only once.
 * 
 * Obviously, there are a great number of words
 * in the English language; some words can be 
 * spelled in multiple ways, spellings differ 
 * between dialects, and there are plural and 
 * possessive versions of words so some 
 * optimization and pruning of the word list was
 * necessary to keep this program from outputing
 * eight different spellings for every word.  As 
 * a result, I cut out the less used spellings, 
 * the british and canadian versions, 
 * contractions, abbreviations, and a large
 * number of words that are almost never used or 
 * aren't even considered words anymore (the 80
 * -95 lists in the given SCOWL dump).
 * 
 * If you need more or less words to be added,
 * unpack the included archive, read the README
 * and add the files you want to the words directory!
 *  In the future, I'll add command line options 
 * to configure the level of words you want included 
 * at runtime.  However, for 99.9% of the time, 
 * the current config will work great.
 *
 * Anyway, here goes the program.
 */
 
 public class UnJumbler {
 
    /*
     * Main method of the program.  Exec starts here.
     */
    public static void main(String[] args){
        
    }
} 
