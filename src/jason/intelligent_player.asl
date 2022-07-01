/* Facts */

/* Prepariamo la griglia per il gioco: 9x9  dove $ vuol dire una cella vuota */
grid(["$","$","$","$","$","$","$","$","$"]).
/* Lo step ci servirà per passare da uno stato di proposta ad uno di accettazione per poi giocare */
step(1).
stage(1).
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

check_horizontal_for_symbol_win1(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(1, Grid, Symbol) &
    Spot = 3.

check_horizontal_for_symbol_win2(Grid, Symbol, Spot) :-
    .nth(1, Grid, Symbol) &
    .nth(2, Grid, Symbol) &
    Spot = 1.

check_horizontal_for_symbol_win3(Grid, Symbol, Spot) :-
    .nth(3, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    Spot = 6.

check_horizontal_for_symbol_win4(Grid, Symbol, Spot) :-
    .nth(4, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    Spot = 4.

check_horizontal_for_symbol_win5(Grid, Symbol, Spot) :-
    .nth(6, Grid, Symbol) &
    .nth(7, Grid, Symbol) &
    Spot = 9.

check_horizontal_for_symbol_win6(Grid, Symbol, Spot) :-
    .nth(7, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 7.

check_vertical_for_symbol_win1(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(3, Grid, Symbol) &
    Spot = 7.

check_vertical_for_symbol_win2(Grid, Symbol, Spot) :-
    .nth(3, Grid, Symbol) &
    .nth(6, Grid, Symbol) &
    Spot = 1.

check_vertical_for_symbol_win3(Grid, Symbol, Spot) :-
    .nth(1, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    Spot = 8.

check_vertical_for_symbol_win4(Grid, Symbol, Spot) :-
    .nth(4, Grid, Symbol) &
    .nth(7, Grid, Symbol) &
    Spot = 2.

check_vertical_for_symbol_win5(Grid, Symbol, Spot) :-
    .nth(2, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    Spot = 9.

check_vertical_for_symbol_win6(Grid, Symbol, Spot) :-
    .nth(5, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 3.

check_diagonal_for_symbol_win1(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    Spot = 9.

check_diagonal_for_symbol_win2(Grid, Symbol, Spot) :-
    .nth(4, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 1.

check_diagonal_for_symbol_win3(Grid, Symbol, Spot) :-
    .nth(2, Grid, Symbol) &
    .nth(4, Grid, Symbol) &
    Spot = 7.

check_diagonal_for_symbol_win4(Grid, Symbol, Spot) :-
    .nth(4, Grid, Symbol) &
    .nth(6, Grid, Symbol) &
    Spot = 3.

check_void_for_symbol_win1(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(2, Grid, Symbol) &
    Spot = 2.

check_void_for_symbol_win2(Grid, Symbol, Spot) :-
    .nth(3, Grid, Symbol) &
    .nth(5, Grid, Symbol) &
    Spot = 5.

check_void_for_symbol_win3(Grid, Symbol, Spot) :-
    .nth(6, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 8.

check_void_for_symbol_win4(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(6, Grid, Symbol) &
    Spot = 4.

check_void_for_symbol_win5(Grid, Symbol, Spot) :-
    .nth(1, Grid, Symbol) &
    .nth(7, Grid, Symbol) &
    Spot = 5.

check_void_for_symbol_win6(Grid, Symbol, Spot) :-
    .nth(2, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 6.

check_void_for_symbol_win7(Grid, Symbol, Spot) :-
    .nth(0, Grid, Symbol) &
    .nth(8, Grid, Symbol) &
    Spot = 5.

check_void_for_symbol_win8(Grid, Symbol, Spot) :-
    .nth(2, Grid, Symbol) &
    .nth(6, Grid, Symbol) &
    Spot = 5.

/* Initial goal */

!register.

/* Plans */
/* Comunichiamo all'arbitro che vogliamo giocare */

+!register
    : true
    <-  .df_register("intelligent_player");
        .df_subscribe("master_arbiter");
        .df_subscribe("arbiter");
        !want_to_play.

+!want_to_play
    : step(Step) & Step == 1 & .my_name(Me)
    <-  .wait(200);
        .print("Agent ", Me, ": I want to play!");
        .df_search("master_arbiter", MasterArbiters);
        .nth(0, MasterArbiters, MasterArbiter);
        +master_arbiter(MasterArbiter);
        Msg = "I want to play!";
        .send(MasterArbiter, tell, want_to_play(Msg));
        -step(1); +step(2).

/* L'arbitro ci risponde, comunicandoci il nostro avversario contro cui dovremo giocare */
+proposal(OtherPlayer, GoFirst, Symbol, Stage)[source(Arbiter)]
    :   step(Step) & Step==2 & .my_name(Me) & stage(MyStage) & MyStage==Stage
    <-  Msg1 = "I accept the proposal";
        .send(Arbiter, tell, proposal_accepted(Msg1, MyStage));
        .print("Agent ", Me, ". I will play against ", OtherPlayer, " my symbol will be ", Symbol, ". At round ", Stage);
        +go_first(GoFirst);
        +arbiter(Arbiter);
        +symbol(Symbol);
        +avversario(OtherPlayer);
        -step(2); +step(3);
        if(GoFirst == "true") {
            !fai_mossa;
        }.

+!fai_mossa
    : mosse_possibili(Mosse) & step(Step)
    <-  !decidi_mossa(Mosse, Spot);
        !move_to(Spot);
        !update_mosse_possibili(Spot);
        !notifica_mossa;
        -step(Step); +step(Step+1).

+!decidi_mossa(Mosse, Spot)
    : true
    <-  .length(Mosse, MPL);
        TurnoCorrente = 10 - MPL;
        .print("Turno corrente: ", TurnoCorrente);
        !decidi_mossa_turno(Mosse, Spot, TurnoCorrente).

/* Siamo al primo turno: vogliamo sempre iniziare con un corner */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 1
    <- .random([1,3,7,9], Spot).

/* Siamo al secondo turno: se il centro non e' occupato, dobbiamo occuparlo, altrimenti perdiamo sicuro! */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 2
    <- if(.member(5, Mosse)) {
            Spot = 5;
        } else {
            .random([1,3,7,9], Spot);
        }.

/* Siamo al terzo turno: vogliamo scegliere, se siamo in un corner, di occupare il corner opposto al mio */
+!decidi_mossa_turno(Mosse, Spot, TurnoCorrente)
    : TurnoCorrente == 3 & symbol(MySymbol) & grid(Grid)
    <- .nth(0, Grid, SymbolFound1); /* nth sarebbe n - 1 */
       .nth(2, Grid, SymbolFound3);
       .nth(6, Grid, SymbolFound7);
       .nth(8, Grid, SymbolFound9);
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
    : TurnoCorrente >= 4 & symbol(MySymbol) & grid(Grid)
    <- if(check_horizontal_for_symbol_win1(Grid, MySymbol, ProposedSpot)) {
            Spot = ProposedSpot;
        } elif(check_horizontal_for_symbol_win2(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_horizontal_for_symbol_win3(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_horizontal_for_symbol_win4(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_horizontal_for_symbol_win5(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_horizontal_for_symbol_win6(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win1(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win2(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win3(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win4(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win5(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_vertical_for_symbol_win6(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_diagonal_for_symbol_win1(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_diagonal_for_symbol_win2(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_diagonal_for_symbol_win3(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_diagonal_for_symbol_win4(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win1(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win2(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win3(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win4(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win5(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win6(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win7(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } elif(check_void_for_symbol_win8(Grid, MySymbol, ProposedSpot) & .member(ProposedSpot, Mosse)) {
            Spot = ProposedSpot;
        } else {
            .random(Mosse, Spot);
        }.

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

/* Aggiorniamo le mosse che possiamo fare, per non sovrascrivere mosse già esistenti */
+!update_mosse_possibili(Mossa)
    : mosse_possibili(Mosse) & delete_element(Mosse, Mossa, NewMosse)
    <-  -mosse_possibili(Mosse);
        +mosse_possibili(NewMosse).

+!notifica_mossa
    : grid(G) & avversario(OtherPlayer) & .my_name(Me) & arbiter(Arbiter) & stage(Stage) & mosse_possibili(Mosse)
    <-  .send(OtherPlayer, tell, update_the_game(G, Mosse, Stage));
        .send(Arbiter, tell, check_end_game(G, Me, OtherPlayer, Stage));
        .print("Agent ", Me, ": I send the move to ", OtherPlayer, " and to the ", Arbiter, ": ", G).     

+update_the_game(NewGrid, Mosse, Stage)
    : grid(G) & step(MyStep) & MyStep >= 3 & stage(MyStage) & MyStage == Stage & mosse_possibili(MyMosse)
    <-  -grid(G);
        +grid(NewGrid);
        -mosse_possibili(MyMosse);
        +mosse_possibili(Mosse).

+end_game(Winner, Win, Stage, Turn)
    : Win == "false" & stage(MyStage) & MyStage == Stage
    <-  .wait(200);
        !fai_mossa.

+end_game(Winner, Win, Stage, Turn)
    : Win == "true" & Winner \== "tie" & stage(MyStage) & MyStage == Stage
    <-  .print("THE GAME ENDED WITH A WINNER: ", Winner, " AT TURN ", Turn).

+end_game(Winner, Win, Stage, Turn)
    : Win == "true" & Winner == "tie" & stage(MyStage) & MyStage == Stage
    <- .print("THE GAME ENDED WITH A TIE WE MUST REPEAT THE GAME AT TURN ", Turn);
        !repeat_game.

+!repeat_game
    : grid(G) & mosse_possibili(Mosse) & avversario(OtherPlayer) & stage(Stage) & step(Step)
    <-  -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]);
        .send(OtherPlayer, tell, reset_game(Stage, Step));
        !fai_mossa.

+reset_game(Stage, Step)
    : grid(G) & mosse_possibili(Mosse) & stage(MyStage) & MyStage == Stage
    <-  .print("I'm getting ready to restart the game");
        -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]).

+new_game(Stage)[source(Arb)]
    : grid(G) & mosse_possibili(Mosse) & step(MyStep) & stage(MyStage) & MyStage+1 == Stage & arbiter(Arbiter) & Arbiter == Arb
    <-  .print("I'm getting ready to play a new game");
        -grid(G);
        -mosse_possibili(Mosse);
        +grid(["$","$","$","$","$","$","$","$","$"]);
        +mosse_possibili([1,2,3,4,5,6,7,8,9]);
        -step(MyStep);
        +step(2);
        -stage(MyStage);
        +stage(Stage).