import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;


//java -Djava.library.path=lib/native -jar gawihs.jar

public class BoardGameKI {

    public static GameBoard gameBoard;
    public static NetworkClient client;
    private static String teamName;

    public static void main(String[] args) throws IOException {

        System.out.println("Initializing AI");

        teamName = args[0];

        client = new NetworkClient(null, teamName, ImageIO.read(new File("./img/logo.png")));

        int myPlayerNumber = client.getMyPlayerNumber();
        gameBoard = new GameBoard(myPlayerNumber);
        client.getTimeLimitInSeconds();
        client.getExpectedNetworkLatencyInMilliseconds();

        while(true) {
            Move move = client.receiveMove();
            System.out.println("Move Received: " + move);

            if(move == null) {

                gameBoard.manageTurn();


//                GameTree gameTree = new GameTree();
//                Move nextMove = gameTree.getBestMove(gameBoard.playField, myPlayerNumber);
//                System.out.println(teamName + " Makes move" + nextMove);
//                client.sendMove(nextMove);


                Move myNextMove = gameBoard.getRandomMove();
                System.out.println(teamName + " Makes move" + myNextMove);
                client.sendMove(myNextMove);

            } else {
                gameBoard.validateAndApplyMove(move);
            }
        }
    }



}




