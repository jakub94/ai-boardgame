import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;



//java -Djava.library.path=lib/native -jar gawihs.jar

public class BoardGameKI {

    private static int moveCounter;

    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        NetworkClient client = new NetworkClient(null, "TeamJ", ImageIO.read(new File("./img/logo.png")));


        int myPlayerNumber = client.getMyPlayerNumber();

        moveCounter = 0;



        GameBoard gameBoard = new GameBoard(myPlayerNumber);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        Random random = new Random();

        while(true) {


            //Move move = null;

//            try {
              Move move = client.receiveMove();
//            } catch (RuntimeException e){
//
//                System.out.println("PLAYER WITH PLAYERIDICATOR: " + (moveCounter%2) + " LOST" + "THE NEXT PLAYER AFTER THE LOOSE IS PLAYER: " + myPlayerNumber);
//
//
//            }

            System.out.println(move);


            if (move == null) {
                //ich bin dran

                Move ourNextMove = gameBoard.getRandomMove();
                //System.out.println("PlayerNumber: "+ myPlayerNumber + " makes move: " + ourNextMove);
                client.sendMove(ourNextMove);

            } else {
                //TODO baue zug in meine spielfeldrepräsentation ein
                //System.out.println("Putting other players move into my board");

                gameBoard.applyMove(move, moveCounter%3);
                moveCounter++;
            }


            //moveCounter = (moveCounter +1) % 3;

        }
    }
}




