![LICENSE SHIELD](https://img.shields.io/badge/license-MIT-orange)
# TicTacToeMultiAgentSystem
A multi-agent system where agents compete in tic-tac-toe tournaments.
The following implementation was made in two different languages:
- Jade: Java Agent Development Framework
- Jason: A Java-based interpreter for an extended version of AgentSpeak

## Tournament
For each tournament, 3 different types of agents are created:
- **Master Arbiter**: 1 master arbiter who organizes all the games by choosing the two players and the arbiter who controls the progress of the game. After all the matches of one round, he rearranges the winners so that they can compete against each other until there is only one left;
- **Arbiter**: if $N$ is the number of players, at least $N/2$ Arbiters must be defined. At each turn they check whether or not there is a winner in their game, if so they notify the Master Arbiter, if not, they continue the game. If there is a tie, they repeat the game;
- **Player**: the number $N$ of players must be a power of two in order to have an even number of players for each round and pairs that can always be built. We can actually define two different types of Players:
  - **Stupid Player**: chooses the move randomly;
  - **Intelligent Player**: if he has the chance to win he catches it and if he succeeds he avoids making the opponent win. If he is the first to make the move, he always chooses a corner, otherwise the central cell.

## State Transition Systems
### Master Arbiter
![MasterArbiterSchema](https://raw.githubusercontent.com/LorenzoSciandra/TicTacToeMultiAgentSystem/main/documentation/Drawio/MasterArbiterSchema.png)
