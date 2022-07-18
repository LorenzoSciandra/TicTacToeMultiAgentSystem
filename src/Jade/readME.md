# Jade implementation

[Jade](https://jade.tilab.com/)'s version of the multi-agent system.
To run the program, open the terminal in the main folder and execute (using bash):

```bash
    javac -cp ../../lib/jade-4.5.0/jade.jar:. -d Classes $(find ../Jade/* | grep .java)
    java -cp ../../lib/jade-4.5.0/jade.jar:Classes jade.Boot -agents "m1:Jade.Agents.MasterArbiterAgent;a1:Jade.Agents.ArbiterAgent;s1:Jade.Agents.StupidPlayerAgent;i1:Jade.Agents.IntelligentPlayerAgent"
```

If you want to play with 4 players competing in a tournament just type:

```bash
    java -cp ../../lib/jade-4.5.0/jade.jar:Classes jade.Boot -agents "m1:Jade.Agents.MasterArbiterAgent;a1:Jade.Agents.ArbiterAgent;a2:Jade.Agents.ArbiterAgent;a3:Jade.Agents.ArbiterAgent;a4:Jade.Agents.ArbiterAgent;s1:Jade.Agents.StupidPlayerAgent;s2:Jade.Agents.StupidPlayerAgent;s3:Jade.Agents.StupidPlayerAgent;s4:Jade.Agents.StupidPlayerAgent;i1:Jade.Agents.IntelligentPlayerAgent;i2:Jade.Agents.IntelligentPlayerAgent;i3:Jade.Agents.IntelligentPlayerAgent;i4:Jade.Agents.IntelligentPlayerAgent"
```

And if you want a massive 8 player tournament:

```bash
java -cp ../../lib/jade-4.5.0/jade.jar:Classes jade.Boot -agents "m1:Jade.Agents.MasterArbiterAgent;a1:Jade.Agents.ArbiterAgent;a2:Jade.Agents.ArbiterAgent;a3:Jade.Agents.ArbiterAgent;a4:Jade.Agents.ArbiterAgent;s1:Jade.Agents.StupidPlayerAgent;s2:Jade.Agents.StupidPlayerAgent;s3:Jade.Agents.StupidPlayerAgent;s4:Jade.Agents.StupidPlayerAgent;i1:Jade.Agents.IntelligentPlayerAgent;i2:Jade.Agents.IntelligentPlayerAgent;i3:Jade.Agents.IntelligentPlayerAgent;i4:Jade.Agents.IntelligentPlayerAgent"
```

Try it with a non-correct configuration of arbiters or players:

```bash
    java -cp ../../lib/jade-4.5.0/jade.jar:Classes jade.Boot -agents "m1:Jade.Agents.MasterArbiterAgent;a1:Jade.Agents.ArbiterAgent;a2:Jade.Agents.ArbiterAgent;s1:Jade.Agents.StupidPlayerAgent;s2:Jade.Agents.StupidPlayerAgent;s3:Jade.Agents.StupidPlayerAgent;s4:Jade.Agents.StupidPlayerAgent;i1:Jade.Agents.IntelligentPlayerAgent;i2:Jade.Agents.IntelligentPlayerAgent;i3:Jade.Agents.IntelligentPlayerAgent;i4:Jade.Agents.IntelligentPlayerAgent"
```
