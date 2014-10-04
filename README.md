WUJumblr
========

A word jumble solver.  Takes a series of letters as an input and returns a list of words that could be made 
using only the letters in the input.

To compile, navigate to this directory in your shell and type in "javac JumbleSolver.java".
To run, type "java JumbleSolver"

The Fundamental Theorem of Arithmetic (http://en.wikipedia.org/wiki/Fundamental_theorem_of_arithmetic)
says that all numbers have one and only one unique prime factorization.  Which
means that given any positive number, there is only one sequence of prime numbers that when multiplied give
that number.  Which means that if you multiply a sequence of prime numbers, you will get a number that you cannot 
get by multiplying any other sequence of prime numbers.  So if we represent the alphabet with the first 26 prime numbers,
multiplying each letter in a string will give us a hash that is guaranteed to be unique to that set of letters. 

The beauty of this system is that if we run into a string that uses a subset of our string's letters, then our hash must 
be a multiple of its hash. For example, the hash of "dog" must be the hash of "do" times the hash of "g".  That's how we 
defined our hash function to work; by multiplying prime number representatives of each letter of the string.  And since 
multiplication is commutative, the order of the letters doesn't matter to the program either.

As such, to solve the jumbled string puzzle, just look through the English word list hashing each 
word and see if the jumbled string is divisible by the word.  

This program goes through the all the words in the list, hashes them, and stores each "word,hash" pair in a hash file once at 
the beginning of the program.  Then, when the user enters an input, it mods the jumbled string's hash with each hash
in the hash file and the ones who return a remainder of 0 are printed.