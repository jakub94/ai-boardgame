import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;



//java -Djava.library.path=lib/native -jar gawihs.jar


//TODO GENAU nachdem 1 Spieler rausfliegt, weiß der Spieler danach im unmittelbar 1. Zug nicht das er raus ist
//--> Spieler Grün Fliegt raus --> Blau bekommt "keinen Move" (also auch keinen ungültigen) --> Blau ist dran --> bewegt sich auf Feld wo vohre Grün war --> verloren

public class BoardGameKI {


    private static boolean redInGame = true;
    private static boolean greenInGame = true;
    private static boolean blueInGame = true;


    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        NetworkClient client = new NetworkClient(null, args[0], ImageIO.read(new File("./img/logo.png")));


        int myPlayerNumber = client.getMyPlayerNumber();


        MoveCounter.setPlayerNumber(myPlayerNumber);


        GameBoard gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();

        while(true) {



            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);



            if (MoveCounter.isMyMove(move)) {


                Move ourNextMove = gameBoard.getRandomMove();
                //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);


                try {
                    Thread.sleep(200);
                    client.sendMove(ourNextMove);
                    System.out.println(args[0] + " Makes move" + ourNextMove);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            } else {

                System.out.println("PI: " + MoveCounter.count);

                if(gameBoard.isValidMove(move)){
                    gameBoard.applyMove(move);
                } else {


                    MoveCounter.increment(1);

                    if(gameBoard.isValidMove(move)){
                        gameBoard.applyMove(move);

                        removePlayer(MoveCounter.getLast());
                        gameBoard.removePlayer(MoveCounter.getLast());

                    }else {
                        MoveCounter.increment(1);
                        if(gameBoard.isValidMove(move)) {
                            gameBoard.applyMove(move);
                            removePlayer(MoveCounter.getNext());
                            gameBoard.removePlayer(MoveCounter.getNext());
                        }
                    }
                }

                incrementMoveCounter();

            }
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

    private static void incrementMoveCounter(){

        if(redInGame && greenInGame && blueInGame){
            MoveCounter.increment(1);
            System.out.println("ALLE NOCH DRIN, movecounter++");
            return;
        }

        if(!redInGame){
            System.out.println("Rot ist raus!");
            if(blueInGame && greenInGame){
                if( (MoveCounter.count) == 2){
                    System.out.println("Blau und Grün NOCH DRIN, BLAU WAR AM ZUG. Erhöhe Movecounter um 2");
                    MoveCounter.increment(2);
                }
                else {
                    System.out.println("Blau und Grün NOCH DRIN, GRÜN WAR AM ZUG. Erhöhe Movecounter um 1");
                    MoveCounter.increment(1);
                }
            } else if(blueInGame || greenInGame){ //nur noch einer ist im Game (Grün oder Blau)
                System.out.println("nur noch einer ist im Game (Grün oder Blau) Erhöhe Movecounter um 3");
                MoveCounter.increment(3);

            }
        }
        else if(!greenInGame){
            System.out.println("Grün ist raus!");
            if(blueInGame && redInGame){
                if( (MoveCounter.count) == 0){
                    System.out.println("Blau und Rot NOCH DRIN, Rot WAR AM ZUG. Erhöhe Movecounter um 2");
                    MoveCounter.increment(2);
                } else {
                    System.out.println("Blau und Rot NOCH DRIN, Blau WAR AM ZUG. Erhöhe Movecounter um 1");
                    MoveCounter.increment(1);
                }
            } else if(blueInGame || redInGame){ //nur noch einer ist im Game (Rot oder Blau)
                System.out.println("nur noch einer ist im Game (Rot oder Blau) Erhöhe Movecounter um 3");
                MoveCounter.increment(3);
            }
        }
        else if(!blueInGame){
            System.out.println("Blau ist raus!");
            if(redInGame && greenInGame){
                if( (MoveCounter.count) == 1){
                    System.out.println("Rot und Grün NOCH DRIN, Grün WAR AM ZUG. Erhöhe Movecounter um 2");
                    MoveCounter.increment(2); //Increment second time, after green has played
                } else {
                    System.out.println("Rot und Grün NOCH DRIN, Rot WAR AM ZUG. Erhöhe Movecounter um 1");
                    MoveCounter.increment(1);
                }
            } else if(redInGame || greenInGame){ //nur noch einer ist im Game (Red oder Green)
                System.out.println("nur noch einer ist im Game (Rot oder Grün) Erhöhe Movecounter um 3");
                MoveCounter.increment(3);
            }
        }
    }
}




