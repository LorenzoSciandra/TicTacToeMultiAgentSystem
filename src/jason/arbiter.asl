/* Facts */
step(1).

check_empty([], "true").
check_empty(["$" | Tail], "false").
check_empty([Head | Tail], End) :-
    Head \== "$" &
    check_empty(Tail, End).

check_horizontal_for_symbol(Grid, Symbol, Win) :-
    ((.nth(0, Grid, Symbol) &
    .nth(1, Grid, Symbol) &
    .nth(2, Grid, Symbol) ) |
    (.nth(3, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    .nth(5, Grid, Symbol) ) |
    (.nth(6, Grid, Symbol) &
    .nth(7, Grid, Symbol) &
    .nth(8, Grid, Symbol))) &
    Win = "true".

check_vertical_for_symbol(Grid, Symbol, Win) :-
    ((.nth(0, Grid, Symbol) &
    .nth(3, Grid, Symbol) &
    .nth(6, Grid, Symbol) ) |
    (.nth(1, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    .nth(7, Grid, Symbol) ) |
    (.nth(2, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    .nth(8, Grid, Symbol))) &
    Win = "true".

check_diagonal_for_symbol(Grid, Symbol, Win) :-
    ((.nth(0, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    .nth(8, Grid, Symbol) ) |
    (.nth(2, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    .nth(6, Grid, Symbol) )) &
    Win = "true".

/* Initial Goal */

!register.

/* Plans */


+!register
    : true
    <-  .df_register("arbiter");
        .df_subscribe("master_arbiter");
        !want_to_arbiter.

+!want_to_arbiter
    : step(Step) & Step == 1 & .my_name(Me)
    <-  .print("Agent ", Me, ": I want to arbiter!");
        .df_search("master_arbiter", MasterArbiters);
        .nth(0, MasterArbiters, MasterArbiter);
        +master_arbiter(MasterArbiter);
        Msg = "I want to arbiter!";
        .send(MasterArbiter, tell, want_to_arbiter(Msg));
        -step(1); +step(2).

+proposal(FirstPlayer, SecondPlayer, Stage)[source(Arb)]
    :  step(Step) & Step==2 & master_arbiter(MasterArbiter) & .my_name(Me) & MasterArbiter == Arb
    <-  Msg1 = "I accept the proposal";
        .send(MasterArbiter, tell, proposal_accepted(Msg1, Stage));
        .print("Agent ", Me, ". I will arbiter the match between ", FirstPlayer, " and ", SecondPlayer, ". At round ", Stage);
        +game(FirstPlayer, "X", SecondPlayer, "O", 0);
        +game(SecondPlayer, "O", FirstPlayer, "X", 0);
        +stage(Stage);
        -step(2);
        +step(3);
        .send(FirstPlayer, tell, proposal(SecondPlayer, "true", "X", Stage));
        .send(SecondPlayer, tell, proposal(FirstPlayer, "false", "O", Stage)).


+proposal_accepted(Msg, Stage)[source(Player)]
    : stage(MyStage) & Stage==MyStage
    <-  .print("Proposal accepted by the player ", Player, " with message: ", Msg, " at stage: ", Stage).

+!winner(Grid, Player, OtherPlayer, Winner, Win) 
   : game(Player, Symbol1, OtherPlayer, Symbol2, Turn)
    <- if(check_horizontal_for_symbol(Grid, Symbol1, Win) & Win == "true") {
        Winner = Player;
        Win = "true";
        .print("THERE'S A WINNER!!!")
    } else {
        if(check_vertical_for_symbol(Grid, Symbol1, Win) & Win == "true") {
            Winner = Player;
            Win = "true";
            .print("THERE'S A WINNER!!!")
        } else {
            if(check_diagonal_for_symbol(Grid, Symbol1, Win) & Win == "true") {
                Winner = Player;
                Win = "true";
                .print("THERE'S A WINNER!!!")
            } else {
                if(check_horizontal_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                    Winner = OtherPlayer;
                    Win = "true";
                    .print("THERE'S A WINNER!!!")
                } else {
                    if(check_vertical_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                        Winner = OtherPlayer;
                        Win = "true";
                        .print("THERE'S A WINNER!!!")
                    } else {
                        if(check_diagonal_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                            Winner = OtherPlayer;
                            Win = "true";
                            .print("THERE'S A WINNER!!!")
                        } else {
                            if(check_empty(Grid, Empty) & Empty == "true") {
                                Winner = "tie";
                                Win = "true";
                                .print("THERE'S A TIE!!!")
                            } else {
                                Winner = "nobody";
                                Win = "false";
                                .print("THE GAME MUST GO ON")
                            }
                        }
                    }
                }
            }
        }
    }.
       
+check_end_game(Grid, Player, OtherPlayer, Stage)
    : step(MyStep) & MyStep >= 3 & stage(MyStage) & Stage == MyStage & game(Player, Symbol1, OtherPlayer, Symbol2, Turn)
    <-  
        !winner(Grid, Player, OtherPlayer, Winner, Win);
        -game(Player, Symbol1, OtherPlayer, Symbol2, Turn);
        -game(OtherPlayer, Symbol2, Player, Symbol1, Turn);
        +game(Player, Symbol1, OtherPlayer, Symbol2, Turn+1);
        +game(OtherPlayer, Symbol2, Player, Symbol1, Turn+1);
        .send(OtherPlayer, tell, end_game(Winner, Win, MyStage, Turn+1));
        !save_winner(Winner,Win);
        -step(MyStep);
        +step(MyStep+1).

+!save_winner(Winner,Win)
    : Win == "false" | (Win == "true" & Winner == "tie")
    <- true.

+!save_winner(Winner,Win)
    : Win == "true" & Winner \== "tie" & stage(Stage)
    <-  +winner(Winner, Stage);
        .print("THE WINNER IS: ", Winner);
        !notify_master.

+!notify_master
    : winner(Winner, Stage) & master_arbiter(MasterArbiter) & game(Winner, _, OtherPlayer, _, _) & .my_name(Me)
    <-  .send(MasterArbiter, tell, theres_a_winner(Winner, Stage, OtherPlayer)).

+new_game(Stage)[source(Arb)]
    :   stage(MyStage) & MyStage+1 == Stage & master_arbiter(Arbiter) & Arbiter == Arb & step(MyStep)
        & winner(Winner, MyStage)
    <-  .print("I'm getting ready to play a new game");
        -step(MyStep);
        +step(2);
        .send(Winner,tell,new_game(Stage)).