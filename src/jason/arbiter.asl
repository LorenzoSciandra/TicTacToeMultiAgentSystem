/* Facts */

step(1).

choose_first(Head, [Head | Tail]).
choose_second(Head2, [Head | [Head2 | Tail]], Tail).

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

!start_game.

/* Plans */

+!winner(Grid, Player, OtherPlayer, Winner, Win) 
   : game(Player, Symbol1, OtherPlayer, Symbol2)
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

+want_to_play_too(Msg)[source(Player)]
    : step(1)
    <- +player(Player).

+proposal_accepted(Msg)[source(Player)]
    : true
    <- .print("Proposal accepted by ", Player, " with message: ", Msg).

+!start_game
    : step(1)
    <- .print("Starting game");
       .wait(1000);
       .findall(Player, player(Player), Players);
       .length(Players, PlayersLength);
       -step(1); +step(2);
       !choose_player(Players, PlayersLength);
       -step(2); +step(3).
       

+!choose_player(Players, PL)
    : step(2) & 
      PL == 2 &
      choose_first(First, Players) & 
      choose_second(Second, Players, _)
    <- .send(First, tell, proposal(Second, "true", "X"));
       .send(Second, tell, proposal(First, "false", "O"));
       +game(First, "X", Second, "O");
       +game(Second, "O", First, "X").

+!choose_player(Players, PL)
    : step(2) & 
      PL >= 2 &
      choose_first(First, Players) & 
      choose_second(Second, Players, RemainingPlayers)
    <- .send(First, tell, proposal(Second, "true", "X"));
       .send(Second, tell, proposal(First, "false", "O"));
       !choose_player(RemainingPlayers, PL - 2).

+check_end_game(Grid, OtherPlayer)[source(Player)]
    : step(X) & X >= 3
    <-  !winner(Grid, Player, OtherPlayer, Winner, Win);
        .print("A send a message to ", OtherPlayer, " with message the winner is: ", Winner);
        .send(OtherPlayer, tell, end_game(Winner, Win, X));
        -check_end_game(Grid, OtherPlayer);
        -step(X);
        +step(X + 1).