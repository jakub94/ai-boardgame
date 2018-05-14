import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;


//java -Djava.library.path=lib/native -jar gawihs.jar

public class BoardGameKI {


    private static boolean redInGame = true;
    private static boolean greenInGame = true;
    private static boolean blueInGame = true;

    private static GameBoard gameBoard;
    private static NetworkClient client;
    private static String teamName;

    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        teamName = args[0];

        client = new NetworkClient(null, teamName, ImageIO.read(new File("./img/logo.png")));

        int myPlayerNumber = client.getMyPlayerNumber();

        MoveCounter.setPlayerNumber(myPlayerNumber);

        gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();

        while(true) {



            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);


            if(!redInGame){
                if(MoveCounter.count == 0){
                    MoveCounter.increment(1);
                }
            }
            if(!greenInGame){
                if(MoveCounter.count == 1){
                    MoveCounter.increment(1);
                }
            }
            if(!blueInGame){
                if(MoveCounter.count == 2){
                    MoveCounter.increment(1);
                }
            }



            if(MoveCounter.isMyMove(move)) {
                 sendRandomMove();
            } else {

                System.out.println("PI: " + MoveCounter.count);

                if(move == null){ //It is "My" turn, but the Movecounter is not correct. Somebody must have made a mistake! Remove him!

                    if(MoveCounter.isEnemy1()){
                        System.out.println("Removing both enemies");
                        removePlayer(MoveCounter.getNext());
                        gameBoard.removePlayer(MoveCounter.getNext());
                        removePlayer(MoveCounter.count);
                        gameBoard.removePlayer(MoveCounter.count);
                        MoveCounter.setMyTurn();
                        sendRandomMove();
                        continue;

                    } else if(MoveCounter.isEnemy2()){

                        System.out.println("Removing Enemy 2");
                        removePlayer(MoveCounter.count);
                        gameBoard.removePlayer(MoveCounter.count);
                        MoveCounter.setMyTurn();
                        sendRandomMove();
                        continue;

                    }
                } else {

                    if(MoveCounter.isEnemy1()){

                        if(gameBoard.isValidMove(move)){
                            gameBoard.applyMove(move);
                        } else {

                            MoveCounter.increment(1);

                            if(gameBoard.isValidMove(move)) {
                                removePlayer(MoveCounter.getLast());
                                gameBoard.removePlayer(MoveCounter.getLast());
                                gameBoard.applyMove(move);
                            }
                        }
                    } else if(MoveCounter.isEnemy2()){

                        if(gameBoard.isValidMove(move)){
                            gameBoard.applyMove(move);
                        } else {
                            removePlayer(MoveCounter.count);
                            gameBoard.removePlayer(MoveCounter.count);
                        }

                    } else { //"My" turn
                        gameBoard.applyMove(move);
                    }
                }
                MoveCounter.increment(1);
                gameBoard.printPlayField();
            }
        }
    }

    private static void sendRandomMove(){
        Move ourNextMove = gameBoard.getRandomMove();
        //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);

        try {
            Thread.sleep(200);
            client.sendMove(ourNextMove);
            System.out.println(teamName + " Makes move" + ourNextMove);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void removePlayer(int playerIndicator){

        if(playerIndicator == 0){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color RED");
            redInGame = false;
        }
        if(playerIndicator == 1){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color GREEN");
            greenInGame = false;
        }
        if(playerIndicator == 2){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color BLUE");
            blueInGame = false;
        }
    }

}




