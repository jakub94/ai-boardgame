import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;



//java -Djava.library.path=lib/native -jar gawihs.jar

public class BoardGameKI {

    private static int moveCounter;

    private static boolean redInGame = true;
    private static boolean greenInGame = true;
    private static boolean blueInGame = true;


    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        NetworkClient client = new NetworkClient(null, args[0], ImageIO.read(new File("./img/logo.png")));


        int myPlayerNumber = client.getMyPlayerNumber();

        moveCounter = 0;



        GameBoard gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();

        while(true) {



            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);


            if (move == null) {
                //ich bin dran

                Move ourNextMove = gameBoard.getRandomMove();
                //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);


                try {
                    Thread.sleep(1000);
                    client.sendMove(ourNextMove);
                    System.out.println(args[0] + " Makes move" + ourNextMove);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




            } else {
                //TODO baue zug in meine spielfeldrepräsentation ein
                //System.out.println("Putting other players move into my board");

                System.out.println("PI: " + moveCounter%3);

                if(gameBoard.isValidMove(move, moveCounter%3)){
                    gameBoard.applyMove(move, moveCounter%3);
                } else {


                    moveCounter++;

                    if(gameBoard.isValidMove(move, moveCounter%3)){
                        gameBoard.applyMove(move, moveCounter%3);
                        removePlayer((moveCounter-1)%3);
                        gameBoard.removePlayer((moveCounter-1)%3);
                    }else {
                        moveCounter++;
                        if(gameBoard.isValidMove(move, moveCounter%3)) {
                            gameBoard.applyMove(move, moveCounter%3);
                            removePlayer((moveCounter - 2) % 3);
                            gameBoard.removePlayer((moveCounter - 2) % 3);
                        }

                    }

                }

                incrementMoveCounter();


            }


            //moveCounter = (moveCounter +1) % 3;

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

        moveCounter++;
    }

    private static void incrementMoveCounter(){

        if(redInGame && greenInGame && blueInGame){
            moveCounter++;
            System.out.println("ALLE NOCH DRIN, movecounter++");
            return;
        }

        if(!redInGame){
            System.out.println("Rot ist raus!");
            if(blueInGame && greenInGame){
                if( (moveCounter%3) == 2){
                    System.out.println("Blau und Grün NOCH DRIN, BLAU WAR AM ZUG. Erhöhe Movecounter um 2");
                    moveCounter = moveCounter +2; //Increment second time, after blue has played
                }
                else {
                    System.out.println("Blau und Grün NOCH DRIN, GRÜN WAR AM ZUG. Erhöhe Movecounter um 1");
                    moveCounter++;
                }
            } else if(blueInGame || greenInGame){ //nur noch einer ist im Game (Grün oder Blau)
                System.out.println("nur noch einer ist im Game (Grün oder Blau) Erhöhe Movecounter um 3");
                moveCounter = moveCounter + 3;
            }
        }
        else if(!greenInGame){
            System.out.println("Grün ist raus!");
            if(blueInGame && redInGame){
                if( (moveCounter%3) == 0){
                    System.out.println("Blau und Rot NOCH DRIN, Rot WAR AM ZUG. Erhöhe Movecounter um 2");
                    moveCounter++; //Increment second time, after red has played
                } else {
                    System.out.println("Blau und Rot NOCH DRIN, Blau WAR AM ZUG. Erhöhe Movecounter um 1");
                    moveCounter++;
                }
            } else if(blueInGame || redInGame){ //nur noch einer ist im Game (Rot oder Blau)
                System.out.println("nur noch einer ist im Game (Rot oder Blau) Erhöhe Movecounter um 3");
                moveCounter = moveCounter + 3;
            }
        }
        else if(!blueInGame){
            System.out.println("Blau ist raus!");
            if(redInGame && greenInGame){
                if( (moveCounter%3) == 1){
                    System.out.println("Rot und Grün NOCH DRIN, Grün WAR AM ZUG. Erhöhe Movecounter um 2");
                    moveCounter++; //Increment second time, after green has played
                } else {
                    System.out.println("Rot und Grün NOCH DRIN, Rot WAR AM ZUG. Erhöhe Movecounter um 1");
                    moveCounter++;
                }
            } else if(redInGame || greenInGame){ //nur noch einer ist im Game (Red oder Green)
                System.out.println("nur noch einer ist im Game (Rot oder Grün) Erhöhe Movecounter um 3");
                moveCounter = moveCounter + 3;
            }
        }
    }
}




