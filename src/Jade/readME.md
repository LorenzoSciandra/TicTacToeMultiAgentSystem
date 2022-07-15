[Jade](https://jade.tilab.com/)'s version of the multi-agent system.
To run the program, open the terminal in the main folder and execute (using bash):
```bash
    javac -cp ./lib/jade-4.5.0/jade.jar:. -d Classes $(find ./src/Jade/* | grep .java)
    java -cp ./lib/jade-4.5.0/jade.jar:Classes jade.Boot -gui -agents "m1:Jade.Agents.MasterArbiterAgent;a1:Jade.Agents.ArbiterAgent;s1:Jade.Agents.StupidPlayerAgent;i1:Jade.Agents.IntelligentPlayerAgent"
```
