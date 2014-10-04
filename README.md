WUJumblr
========

A word jumble solver.  Takes a series of letters as an input and returns a list of words that could be made 
using only the letters in the input.

The Fundamental Theorem of Arithmetic (http://en.wikipedia.org/wiki/Fundamental_theorem_of_arithmetic)
says that all numbers have one and only one unique prime factorization.  That means that given any positive number, 
there is only one set of prime numbers that when multiplied gives that number.  Which means that if you multiply 
a set of prime numbers, you will get a number that you cannot get by multiplying any other set of prime numbers.  So 
if we represent the alphabet with the first 26 prime numbers,multiplying each letter in a string will give us a 
hash that is guaranteed to be unique to that set of letters. 

The beauty of this system is that if we run into a string that uses a subset of our string's letters, then our hash must 
be a multiple of its hash. For example, the hash of "dog" must be the hash of "do" times the hash of "g".  That's how we 
defined our hash function to work; by multiplying prime number representatives of each letter of the string.  And since 
multiplication is commutative, the order of the letters doesn't matter to the program either.

So to solve the jumbled string puzzle, this program goes through the all the words in the list, hashes them, and stores each 
"word,hash" pair in a hash file once at the beginning of the program.  Then, when the user enters an input, it divides the 
jumbled string's hash by each hash in the hash file and the ones who return a remainder of 0 are printed.

The program is O(n) in the length of dictionary.  Technically multiplying the prime representatives of the strings together
is not O(1) but words are usually short enough that for all practical purposes, it is.

To compile, navigate to this directory in your shell and type in "javac JumbleSolver.java".
For an interactive session, type "java JumbleSolver".
To use as a command line utility, type "java JumbleSolver strings" where strings is a space separated list of jumbled strings to be 
solved by the program.  

Congratulations, you are now unstoppable at Words With Friends/Scrabble/most word games.