/* Facts */

step(1).

choose_first(Head, [Head | Tail]).
choose_second(Head2, [Head | [Head2 | Tail]], Tail).

check([], "true").

check([Head | Tail], End) :-
    Head == "$" &
    End = "false".

check([Head | Tail], End) :-
    Head \== "$" &
    check(Tail, End).

check_horizontal_for_symbol(Grid, Symbol, Win) :-
    ((.nth(1, Grid, Symbol) &
    .nth(2, Grid, Symbol) &
    .nth(3, Grid, Symbol) ) |
    (.nth(4, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    .nth(6, Grid, Symbol) ) |
    (.nth(7, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    .nth(9, Grid, Symbol))) &
    Win = "true".

check_vertical_for_symbol(Grid, Symbol, Win) :-
    ((.nth(1, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    .nth(7, Grid, Symbol) ) |
    (.nth(2, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    .nth(8, Grid, Symbol) ) |
    (.nth(3, Grid, Symbol) &
    .nth(6, Grid, Symbol) &
    .nth(9, Grid, Symbol))) &
    Win = "true".

check_diagonal_for_symbol(Grid, Symbol, Win) :-
    ((.nth(1, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    .nth(9, Grid, Symbol) ) |
    (.nth(3, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    .nth(7, Grid, Symbol) )) &
    Win = "true".

/* Initial Goal */

!start_game.

/* Plans */

+!winner(Grid, Player, OtherPlayer, Winner) 
   : game(Player, Symbol1, OtherPlayer, Symbol2)
    <- if(check_horizontal_for_symbol(Grid, Symbol1, Win) & Win == "true") {
        Winner = Player;
    } else {
        if(check_vertical_for_symbol(Grid, Symbol1, Win) & Win == "true") {
            Winner = Player;
        } else {
            if(check_diagonal_for_symbol(Grid, Symbol1, Win) & Win == "true") {
                Winner = Player;
            } else {
                if(check_horizontal_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                    Winner = OtherPlayer;
                } else {
                    if(check_vertical_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                        Winner = OtherPlayer;
                    } else {
                        if(check_diagonal_for_symbol(Grid, Symbol2, Win) & Win == "true") {
                            Winner = OtherPlayer;
                        } else {
                            Winner = "tie";
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
       .wait(500);
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

+!check_end_game(Grid, OtherPlayer)[source(Player)]
    : step(3) & check(Grid, End) & End == "true"
    <- !winner(Grid, Player, OtherPlayer, Winner);
       .send(Player, tell, end_game(Winner));
       .send(OtherPlayer, tell, end_game(Winner)).