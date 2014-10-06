WUJumblr
========

A word jumble solver.  Takes a series of letters as an input and returns a list of words that could be made 
using only the letters in the input.  

The Fundamental Theorem of Arithmetic (http://en.wikipedia.org/wiki/Fundamental_theorem_of_arithmetic)
says that all numbers have one and only one unique prime factorization.  This means that given any positive number, 
there is only one set of prime numbers that when multiplied gives that number and so if you multiply 
a set of prime numbers, you will get a number that you cannot get by multiplying any other set of prime numbers.  Therefore, 
if we represent the alphabet with the first 26 prime numbers, multiplying each letter in the string together will 
give us a hash that is guaranteed to be unique to that set of letters. 

The beauty of this system is if we run into a string that uses a subset of our string's letters, our hash must 
be a multiple of its hash. For example, the hash of "dog" must be the hash of "do" times the hash of "g".  That's how we 
defined our hash function to work; by multiplying prime number representatives of each letter of the string.  And since 
multiplication is commutative, the order of the letters doesn't matter to the program either.

So to solve the jumbled string puzzle, this program goes through the all the words in the list, hashes them, and stores each 
"word,hash" pair in a hash file once at the beginning of the program and when the user enters an input, it divides the 
jumbled string's hash by each hash in the hash file and the ones who return a remainder of 0 are printed. This is O(n) in the 
length of dictionary.    

I used SCOWL word lists which can be found at http://wordlist.aspell.net/.  They are a great list of words.  My only gripe is that
they include a lot of very strange short words even in the recommended 60 and below lists (i.e. "gs" in the 40 list). 

Anyway, you're welcome world, you are now unstoppable at Words With Friends/Scrabble/most word games. 

Vish
