import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        teamName = args[0];

        client = new NetworkClient(null, teamName, ImageIO.read(new File("./img/logo.png")));

        int myPlayerNumber = client.getMyPlayerNumber();


        gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();


        while(true) {


            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);




            if(gameBoard.isMyMove(move)) {

//                  GameTree gameTree = new GameTree();
//                  Move nextMove = gameTree.getBestMove(gameBoard.playField, myPlayerNumber);
//                  client.sendMove(nextMove);

              sendRandomMove();

            } else {

                if(move == null){
                    if(gameBoard.isEnemy1Turn()){
                        gameBoard.removeEnemy1();
                        gameBoard.removeEnemy2();
                        gameBoard.setMyTurn();
                        sendRandomMove();
                    } else if(gameBoard.isEnemy2Turn()) {
                        gameBoard.removeEnemy2();
                        gameBoard.setMyTurn();
                        sendRandomMove();
                    }
                } else {

                    if (gameBoard.isMyTurn()) {
                        gameBoard.applyMove(move);
                    } else {
                        int playerWhoMadeTheMove = gameBoard.whoDidTheMove(move);

                        if(gameBoard.isEnemy1Turn()){
                            if(playerWhoMadeTheMove == gameBoard.moveCounter.getNext(myPlayerNumber)){
                                gameBoard.applyMove(move);
                                continue;
                            }
                            if(playerWhoMadeTheMove == gameBoard.moveCounter.getLast(myPlayerNumber)){
                                gameBoard.removeEnemy1();
                                gameBoard.moveCounter.increment(1);
                                gameBoard.applyMove(move);
                                continue;
                            }
                        }
                        if(gameBoard.isEnemy2Turn()){
                            if(playerWhoMadeTheMove == gameBoard.moveCounter.getLast(myPlayerNumber)){
                                gameBoard.applyMove(move);
                                continue;
                            }
                            if(playerWhoMadeTheMove == gameBoard.moveCounter.getNext(myPlayerNumber)){
                                gameBoard.removeEnemy2();
                                gameBoard.moveCounter.increment(2);
                                gameBoard.applyMove(move);
                                gameBoard.moveCounter.increment(2);
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void sendRandomMove(){
        Move ourNextMove = gameBoard.getRandomMove();
        //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);

        try {
            Thread.sleep(111);
            client.sendMove(ourNextMove);
            System.out.println(teamName + " Makes move" + ourNextMove);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}




