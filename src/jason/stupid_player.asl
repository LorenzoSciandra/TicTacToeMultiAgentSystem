/* Facts */

grid(["$","$","$","$","$","$","$","$","$"]).
arbiter(arbiter).
step(1).

move([Head | Tail], 1, ToInsert, PrevHead, NewGrid) :-
    NewGrid = [PrevHead | ToInsert] &
    NewGrid = [NewGrid | Tail].

move([Head | Tail], X, ToInsert, PrevHead, NewGrid) :-
    X > 1 &
    move(Tail, X-1, ToInsert, [Head | PrevHead], NewGrid).

is_free(["$" | Tail], 1, "true").

is_free([Head | Tail], 1, Y) :-
    Head \== "$" &
    Y = "false".

is_free([Head | Tail], X , Y) :-
    X > 1 &
    is_free(Tail, X-1, Y).

choose_spot(G, Spot, Free):-
    .random([1,2,3,4,5,6,7,8,9],Spot) &
    is_free(G, Spot, Free).

/* Initial goal */

!want_to_play.

/* Plans */

+end_game("tie")
    : .my_name(Me) & avversario(OtherPlayer)
    <- .print(Me, " and ", OtherPlayer, " have tied!").

+end_game(Winner)
    : .my_name(Me) & Me == Winner
    <- .print("Agent ", Me, ": I win!").

+end_game(Winner)
    : .my_name(Me) & Me \== Winner
    <- .print("Agent ", Me, ": I lose!").

+!want_to_play
    : arbiter(Arbiter) & step(X) & X = 1 & .my_name(Me)
    <- .print("Agent ", Me, ": I want to play!");
       Msg = "I want to play!";
       .send(Arbiter, tell, want_to_play_too(Msg));
       -step(1); +step(2).

+proposal(OtherPlayer, GoFirst, Symbol)[source(Arbiter)]
    : step(2) & arbiter(Arbiter) & .my_name(Me)
    <- Msg1 = "I accept the proposal";
       .send(Arbiter, tell, proposal_accepted(Msg1));
       .print("Agent ", Me, ". I will play against ", OtherPlayer, " my symbol will be ", Symbol, ".");
       +go_first(GoFirst); +symbol(Symbol);
       +avversario(OtherPlayer); -proposal(OtherPlayer);
       -step(2); +step(3).

+avversario(OtherPlayer) /* Siamo primi nelle mosse */
    : go_first(GoFirst) & GoFirst = "true" & .my_name(Me)
    <- .random([1,2,3,4,5,6,7,8,9],X); /* Mossa random tra 1 e 9 */
       !move_to(X);
       grid(G);
       .send(OtherPlayer, tell, set_new_grid(G));
       .print("Agent ", Me, ": I set my point to ", X, ".").

+set_new_grid(NewGrid)[source(OtherPlayer)]
    : true & grid(G) & step(S) & S >= 3 & not end_game(Winner)
    <- -grid(G);
       +grid(NewGrid);
       -set_new_grid(NewGrid);
       !fai_mossa(OtherPlayer).
      
+!fai_mossa(OtherPlayer)
    : grid(G) & arbiter(Arbiter) & .my_name(Me) & choose_spot(G, Spot, Free) & Free == "true" 
    <- !move_to(Spot);
       .send(OtherPlayer, tell, set_new_grid(G));
       .send(Arbiter, tell, check_end_game(G, OtherPlayer));
       .print("Agent ", Me, ": I set my point to ", Spot, ".").

+!move_to(X)
    : step(S) & S >= 3 & grid(G) & symbol(Symbol) & move(G, X, Symbol, [], NG) & .my_name(Me)
    <- .print("Agent ", Me, ": I choose to mark ", X);
       -grid(G);
       +grid(NG).