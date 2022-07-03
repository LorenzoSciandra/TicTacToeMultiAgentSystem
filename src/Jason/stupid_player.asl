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

/* Initial goal */

!register.

/* Plans */
/* Comunichiamo all'arbitro che vogliamo giocare */

+!register
    : true
    <-  .df_register("stupid_player");
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
    <-  .random(Mosse,Spot);
        !move_to(Spot);
        !update_mosse_possibili(Spot);
        !notifica_mossa;
        -step(Step); +step(Step+1).

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