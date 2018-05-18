import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;


//java -Djava.library.path=lib/native -jar gawihs.jar




//TODO:
/*
    MoveCounter im GameTree anpassen, wenn ein Spieler raus ist.
    MyPawnPositions fehlerhaft (enthält enemyPawns)
    MyPlayerNumber wird evtl. auch fehlerhaft geupdated

    Bewertungsfunktion anpassen/erweitern
    Lernmechanismus für Bewertungsfunktion

 */




public class BoardGameKI { 




    public static GameBoard gameBoard;
    private static NetworkClient client;
    private static String teamName;

    public static MoveCounter moveCounter;

    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        teamName = args[0];

        client = new NetworkClient(null, teamName, ImageIO.read(new File("./img/logo.png")));

        int myPlayerNumber = client.getMyPlayerNumber();

        moveCounter = new MoveCounter(0, myPlayerNumber);
        gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();


        while(true) {


            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);


            moveCounter.manageKickedPlayers();

            if(moveCounter.isMyMove(move)) {
//                  GameTree gameTree = new GameTree();
//                  Move nextMove = gameTree.getBestMove(gameBoard.playField, myPlayerNumber);
//                  client.sendMove(nextMove);

              sendRandomMove();

            } else {

                System.out.println("PI: " + moveCounter.count);

                if(move == null){ //It is "My" turn, but the Movecounter is not correct. Somebody must have made a mistake! Remove him!

                    if(moveCounter.isEnemy1()){
                        System.out.println("Removing both enemies");
                        moveCounter.removePlayer(moveCounter.getNext());
                        gameBoard.removePlayer(moveCounter.getNext());
                        moveCounter.removePlayer(moveCounter.count);
                        gameBoard.removePlayer(moveCounter.count);
                        moveCounter.setMyTurn();
                        sendRandomMove();
                        continue;

                    } else if(moveCounter.isEnemy2()){

                        System.out.println("Removing Enemy 2");
                        moveCounter.removePlayer(moveCounter.count);
                        gameBoard.removePlayer(moveCounter.count);
                        moveCounter.setMyTurn();
                        sendRandomMove();
                        continue;
                    }
                } else {

                    if(moveCounter.isEnemy1()){

                        if(gameBoard.isValidMove(move)){
                            gameBoard.applyMove(move, moveCounter.count);
                        } else {

                            moveCounter.increment(1);

                            if(gameBoard.isValidMove(move)) {
                                moveCounter.removePlayer(moveCounter.getLast());
                                gameBoard.removePlayer(moveCounter.getLast());
                                gameBoard.applyMove(move, moveCounter.count);
                            }
                        }
                    } else if(moveCounter.isEnemy2()){

                        if(gameBoard.isValidMove(move)){
                            gameBoard.applyMove(move, moveCounter.count);
                        } else {
                            moveCounter.removePlayer(moveCounter.count);
                            gameBoard.removePlayer(moveCounter.count);
                        }

                    } else { //"My" turn
                        gameBoard.applyMove(move, moveCounter.count);
                    }
                }
                moveCounter.increment(1);
                gameBoard.printPlayField();

            }
        }
    }

    private static void sendRandomMove(){
        Move ourNextMove = gameBoard.getRandomMove();
        //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);

        try {
            Thread.sleep(1);
            client.sendMove(ourNextMove);
            System.out.println(teamName + " Makes move" + ourNextMove);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}




