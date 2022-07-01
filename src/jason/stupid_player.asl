/* Facts */

/* Prepariamo la griglia per il gioco: 9x9  dove $ vuol dire una cella vuota */
grid(["$","$","$","$","$","$","$","$","$"]).
arbiter(arbiter).
/* Lo step ci servirà per passare da uno stato di proposta ad uno di accettazione per poi giocare */
step(1).
mosse_possibili([1,2,3,4,5,6,7,8,9]).

append([], X, X).
append([X], [Y], Z):- Z = [X,Y].
append([X | Xs], Y, Z) :- 
    append(Xs, Y, W) &
    Z = [X|W]. 

delete_element([Head|Tail], Head, Tail).

delete_element([Head|Tail], X, NewMosse):-
    Head \== X &
    delete_element(Tail, X, NewTail) &
    NewMosse = [Head|NewTail].

nuova_head(Head, PrevHead, NewHead) :-
    append(PrevHead, [Head], NewHead).

/* Initial goal */

!want_to_play.

/* Plans */
/* Comunichiamo all'arbitro che vogliamo giocare */
+!want_to_play
    : arbiter(Arbiter) & step(X) & X = 1 & .my_name(Me)
    <- .print("Agent ", Me, ": I want to play!");
       Msg = "I want to play!";
       .send(Arbiter, tell, want_to_play_too(Msg));
       -step(1); +step(2).

/* L'arbitro ci risponde, comunicandoci il nostro avversario contro cui dovremo giocare */
+proposal(OtherPlayer, GoFirst, Symbol)[source(Arbiter)]
    :   step(2) & arbiter(Arbiter) & .my_name(Me)
    <-  Msg1 = "I accept the proposal";
        .send(Arbiter, tell, proposal_accepted(Msg1));
        .print("Agent ", Me, ". I will play against ", OtherPlayer, " my symbol will be ", Symbol, ".");
        +go_first(GoFirst);
        +symbol(Symbol);
        +avversario(OtherPlayer);
        -proposal(OtherPlayer);
        -step(2); +step(3);
        if(GoFirst == "true") {
            !prima_mossa;
        }.

/* Siamo primi nelle mosse: facciamo la mossa random, aggiorniamo le mosse possibili e notifichiamo la mossa all'arbitro */
+!prima_mossa
    :   mosse_possibili(Mosse)
    <-  .random(Mosse,Mossa); /* Mossa random tra 1 e 9 */
        !move_to(Mossa);
        !update_mosse_possibili(Mossa); 
        !notifica_mossa(Mossa).

/* Aggiorniamo le mosse che possiamo fare, per non sovrascrivere mosse già esistenti */
+!update_mosse_possibili(Mossa)
    : mosse_possibili(Mosse) & delete_element(Mosse, Mossa, NewMosse)
    <-  -mosse_possibili(Mosse);
        +mosse_possibili(NewMosse).

+!notifica_mossa(Mossa)
    : grid(G) & avversario(OtherPlayer) & .my_name(Me) & arbiter(Arbiter)
    <-  .send(OtherPlayer, tell, update_the_game(G, Mossa));
        .send(Arbiter, tell, check_end_game(G, OtherPlayer));
        .print("Agent ", Me, ": I send the move to ", OtherPlayer, " and to the ", Arbiter, ": ", G).     

+update_the_game(NewGrid, NuovaMossa)
    : true & grid(G) & step(S) & S >= 3 & not end_game(Winner) & avversario(OtherPlayer)
    <- -grid(G);
       +grid(NewGrid);
       !update_mosse_possibili(NuovaMossa);
       -update_the_game(NewGrid, NuovaMossa).

+end_game(Winner, Win, Step)[source(Source)]
    : Win == "false" & arbiter(Arbiter) & Arbiter == Source 
    <-  .wait(500);
        !fai_mossa; 
        -end_game(Winner, Win).

+end_game(Winner, Win, Step)[source(Source)]
    : Win == "true" & Winner \== "tie" & arbiter(Arbiter) & Arbiter == Source
    <-  .print("The game ended with a winner: ", Winner, " at iteration ", Step).

+end_game(Winner, Win, Step)[source(Source)]
    : Win == "true" & Winner == "tie" & arbiter(Arbiter) & Arbiter == Source
    <- .print("The game ended with a tie at iteration ", Step, " WE MUST REPEAT");
        !repeat_game.

+!fai_mossa
    : mosse_possibili(Mosse) 
    <-  .random(Mosse,Spot);
        !move_to(Spot);
        !update_mosse_possibili(Spot);
        !notifica_mossa(Spot).

+!move_to(X)
    : step(S) & S >= 3 & grid(G) & symbol(Symbol) & .my_name(Me)
    <-  .print("Agent ", Me, ": I choose to mark ", X);
        !move(G, X, Symbol, []).

+!move([Head|Tail], Pos, ToInsert, PrevHead)
    : Pos == 1 & append(PrevHead, [ToInsert], NewHead) & append(NewHead, Tail, NewGrid) & grid(G)
    <-  -grid(G);
        +grid(NewGrid).
        

+!move([Head|Tail], Pos, ToInsert, PrevHead)
    : Pos > 1 & nuova_head(Head, PrevHead, NewHead)
    <-  !move(Tail, Pos-1, ToInsert, NewHead).

+!repeat_game
    : grid(G) & mosse_possibili(Mosse) & avversario(OtherPlayer)
    <-  -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]);
        .send(OtherPlayer, tell, reset_gioco);
        !prima_mossa.

+reset_gioco
    : grid(G) & mosse_possibili(Mosse)
    <- -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]).