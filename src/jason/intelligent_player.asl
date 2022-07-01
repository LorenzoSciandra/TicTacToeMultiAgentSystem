/* Facts */

/* Prepariamo la griglia per il gioco: 9x9  dove $ vuol dire una cella vuota */
grid(["$","$","$","$","$","$","$","$","$"]).
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

!register.

/* Plans */
/* Comunichiamo all'arbitro che vogliamo giocare */

+!register
    : true
    <-  .df_register("intelligent_player");
        .df_subscribe("arbiter");
        !want_to_play.

+!want_to_play
    : step(X) & X = 1 & .my_name(Me)
    <-  .print("Agent ", Me, ": I want to play!");
        .df_search("arbiter", Arbiters);
        .nth(0, Arbiters, Arbiter);
        +arbiter(Arbiter);
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
            !fai_mossa;
        }.

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
    : Win == "true" & Winner \== "tie" & arbiter(Arbiter) & Arbiter == Source & .my_name(Me)
    <-  .print("THE GAME ENDED WITH A WINNER: ", Winner).
        //.send(Arbiter, tell, winner_game(G, OtherPlayer)).

+end_game(Winner, Win, Step)[source(Source)]
    : Win == "true" & Winner == "tie" & arbiter(Arbiter) & Arbiter == Source
    <- .print("THE GAME ENDED WITH A TIE WE MUST REPEAT THE GAME");
        !repeat_game.

+!fai_mossa
    : mosse_possibili(Mosse) 
    <-  !decidi_mossa(Mosse, Spot);
        !move_to(Spot);
        !update_mosse_possibili(Spot);
        !notifica_mossa(Spot).

+!decidi_mossa(Mosse, Spot)
    : mosse_possibili(MP)
    <-  .length(MP, MPL);
        TurnoCorrente = 9 - MPL;
        !decidi_mossa_turno(Mosse, Spot, TurnoCorrente).

/* Siamo al primo turno: vogliamo sempre iniziare con un corner */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 1
    <- .random([1,3,7,9], Spot).

/* Siamo al secondo turno: se il centro non e' occupato, dobbiamo occuparlo, altrimenti perdiamo sicuro! */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 2
    <- if(.member(Mosse, 5)) {
            Spot = 5;
        } else {
            .random([1,3,7,9], Spot);
        }.

/* Siamo al terzo turno: vogliamo scegliere, se siamo in un corner, di occupare il corner opposto al mio */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 3 & symbol(MySymbol) & grid(Grid)
    <- .nth(1, Grid, SymbolFound1);
       .nth(3, Grid, SymbolFound3);
       .nth(7, Grid, SymbolFound7);
       .nth(9, Grid, SymbolFound9);
        if(SymbolFound1 == MySymbol & SymbolFound7 == "$") {
            Spot = 7;
        } elif(SymbolFound3 == MySymbol & SymbolFound9 == "$") {
            Spot = 9;
        } elif(SymbolFound7 == MySymbol & SymbolFound9 == "$") {
            Spot = 3;
        } elif(SymbolFound9 == MySymbol & SymbolFound3 == "$") {
            Spot = 1;
        } else {
            /* Altrimenti, se non eravamo in un corner, scegliamo un corner a caso */
            .random([1,3,7,9], Spot);
        }.

+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente >= 4 & symbol(MySymbol)
    <- .random(Mosse, Spot). /* TODO: fare un filler per unire i corner */

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
        !fai_mossa.

+reset_gioco
    : grid(G) & mosse_possibili(Mosse)
    <- -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]).