stage(1).
step(1).
winners(0,1).
available.

choose_first(Head, [Head | Tail], Tail).
choose_second(Head2, [Head | [Head2 | Tail]], Tail).

/* Initial Goal */

!register.

/* Plans */

+!register
    : step(1)
    <-  .df_register("master_arbiter");
        -step(1);
        +step(2);
        !start_game.

+want_to_play(Msg)[source(Player)]
    : step(2)
    <-  +player(Player);
        .print("Register player ", Player).

+want_to_arbiter(Msg)[source(Arbiter)]
    : step(2)
    <-  +arbiter(Arbiter);
        .print("Register arbiter ", Arbiter).

+proposal_accepted(Msg, Stage)[source(Arbiter)]
    : stage(MyStage) & Stage==MyStage
    <-  .print("Proposal accepted by the arbiter ", Arbiter, " with message: ", Msg, " at stage: ", Stage).

+!start_game
    :   step(2)
    <-  .print("I'm waiting all the proposal! From players and arbiters");
        .wait(1000);
        .print("Starting game");
        .findall(Player, player(Player), Players);
        .length(Players, PlayersLength);
        .findall(Arbiter, arbiter(Arbiter), Arbiters);
        .length(Arbiters, ArbitersLength);
        +num_game(PlayersLength/2);
        +num_players(PlayersLength);
        +num_arbiters(ArbitersLength);
        .print("Players: ", PlayersLength, " Arbiters: ", ArbitersLength, " Game: ", PlayersLength/2);
        -step(2); +step(3);
        !choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength);
        -step(3); +step(4).

+!choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength)
    :   step(3) & num_game(NumGame) & num_arbiters(NumArbiters) & NumArbiters < NumGame
    <-  .print("THE TOURNAMENT DOES NOT HAVE THE CORRECT NUMBER OF ARBITERS").

+!choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength)
    :   step(3) & num_game(NumGame) & PlayersLength >= 2 & PlayersLength mod 2 == 1
    <-  .print("THE TOURNAMENT DOES NOT HAVE THE CORRECT NUMBER OF PLAYERS").

+!choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength)
    :   step(3) & num_game(NumGame) & num_arbiters(NumArbiters) & NumArbiters >= NumGame &
        PlayersLength == 2 & stage(Stage) & choose_first(Arbiter, Arbiters, RemainingArbiters) &
        choose_first(First, Players, _) & choose_second(Second, Players,_) 
    <-  .send(Arbiter, tell, proposal(First, Second, Stage));
        +game(First, "X", Second, "O", Arbiter, Stage);
        +game(Second, "O", First, "X", Arbiter, Stage).

+!choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength)
    :   step(3) & num_game(NumGame) & num_arbiters(NumArbiters) & NumArbiters >= NumGame &
        PlayersLength >= 2 & PlayersLength mod 2 == 0 & stage(Stage) &
        choose_first(First, Players, _) & choose_second(Second, Players,RemainingPlayers) &  
        choose_first(Arbiter, Arbiters, RemainingArbiters)
    <-  .send(Arbiter, tell, proposal(First, Second, Stage));
        +game(First, "X", Second, "O", Arbiter, Stage);
        +game(Second, "O", First, "X", Arbiter, Stage);
        !choose_player_arbiter(RemainingPlayers, PlayersLength-2, RemainingArbiters, ArbitersLength-1).

+theres_a_winner(Winner, Stage, OtherPlayer)[source(Arbiter)]
    : stage(MyStage) & Stage==MyStage & winners(NumWinners, MyStage) & available
    <-  -available;
        +winner(Winner, Stage);
        -player(OtherPlayer);
        -winners(NumWinners, MyStage);
        +winners(NumWinners+1, MyStage);
        !check_next_round(Stage);
        +available.

+theres_a_winner(Winner, Stage, OtherPlayer)[source(Arbiter)]
    : stage(MyStage) & Stage==MyStage & winners(NumWinners, MyStage) & not available
    <-  .print("RECOVERY....");
        .wait(200);
        !recovery_cycle(Winner, Stage, OtherPlayer).

+!recovery_cycle(Winner, Stage, OtherPlayer)
    : stage(MyStage) & Stage==MyStage & winners(NumWinners, MyStage) & available
    <-  -available;
        +winner(Winner, Stage);
        -player(OtherPlayer);
        -winners(NumWinners, MyStage);
        +winners(NumWinners+1, MyStage);
        !check_next_round(Stage);
        +available.

+!recovery_cycle(Winner, Stage, OtherPlayer)
    : stage(MyStage) & Stage==MyStage & winners(NumWinners, MyStage) & not available
    <-  .print("RECOVERY....");
        .wait(200);
        !recovery_cycle(Winner, Stage, OtherPlayer).



+!check_next_round(Stage)
    : num_game(Games) & Games >=2 & winners(NumWinners, Stage) & NumWinners\==Games
    <- true.

+!check_next_round(Stage)
    :   num_game(NumGame) & NumGame >=2 & winners(NumWinners, Stage) & NumWinners==NumGame & 
        step(Step) & num_players(NumPlayers)
    <-  .findall(Player, player(Player), Players);
        .length(Players, PlayersLength);
        .print("THE WINNERS ARE: ", Players);
        .print("THE GAME WILL CONTINUE TO THE NEXT STAGE");
        -stage(Stage);
        +stage(Stage + 1);
        -step(Step);
        -num_game(NumGame);
        -num_players(NumPlayers);
        .findall(Arbiter, arbiter(Arbiter), Arbiters);
        .length(Arbiters, ArbitersLength);
        +num_game(PlayersLength/2);
        +num_players(PlayersLength);
        +winners(0, Stage+1);
        +step(3);
        .send(Arbiters, tell, new_game(Stage+1));
        //.print("PLAYERS: ", Players, " ARBITERS: ", Arbiters, " GAME: ", PlayersLength/2);
        .wait(1500);
        !choose_player_arbiter(Players, PlayersLength, Arbiters, ArbitersLength);
        -step(3); +step(4).

+!check_next_round(Stage)
    : num_game(Games) & Games ==1 &  winner(Winner, Stage)
    <-  .print("THE WINNER IS: ", Winner);
        .print("THE GAME IS OVER AT STAGE ", Stage).