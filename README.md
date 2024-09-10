# proiect-pa-2024-pink-bandits
## Java Bot
The class named MyBot is the main implementation of the assigment.
Here is a brief description of the key functionalities of the class:

1. The class starts by initializing the InitPackage which contains the ID of 
the player and the map of the game.
2. Within a continuous loop, the bot updates and sends a frame for each new 
move.
3. For each location owned by the bot on the map, we compare the
neighboring sites, based on their strength. If all of the neighboring sites 
have the same owner as the bot, then it tries to find the closest site that 
it can conquer.
4. After the bot found the closest enemy sites in all directions, it compares
the distances with a priority queue and then it adds the best move to the array.
5. After evaluating all options, the bot sends the list of moves to be executed.

The complexity of the algorithm is O(n ^ 2) for the 2 "for-loops".

The Networking class handles the input strings, with functions that serialize and 
deserialize the game map, creating the board on which the bot operates.

The GameMap class initialises and calculates locations on the map and distances
between them.
