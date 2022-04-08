We have a secure 'Vault' where planning to store money. 
We have 'Hackers' who try to guess code to the 'Vault'.
We have few 'Hacker' threads trying to attack my code concurrently.

In addition to that we are going to have 'Police' thread.
That thread is going to rescue by counting down 10 times.
If hackers haven't broken to the vault by then, 
the policeman is going to arrest them. 

Police - PoliceThread
Hacker - AscendingHackerThread
Hacker - DescendingHackerThread
