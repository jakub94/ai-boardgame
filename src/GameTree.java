import lenz.htw.gawihs.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameTree {





    private int myPlayerNumber;

    private MoveCounter moveCounter;


    private MyMove bestMove;



    public Move getBestMove(int[][] playField, int playerNumber){


        this.myPlayerNumber = playerNumber;

        moveCounter = new MoveCounter();
        moveCounter.setPlayerNumber(playerNumber);


        HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(playField, playerNumber, BoardGameKI.gameBoard.myPawnPositions);
        GameBoard currentGameBoard = new GameBoard(myPlayerNumber, playField, BoardGameKI.gameBoard.myPawnPositions, BoardGameKI.gameBoard.enemy1PawnPositions, BoardGameKI.gameBoard.enemy2PawnPositions);


        int currentOutcome;
        int bestOutCome = -99999;

        Move bestMove = null;

        for (MyMove move: possibleMoves) {


            currentGameBoard.applyMove(move);
            moveCounter.increment(1);



            currentOutcome = alphabeta(currentGameBoard, 3, -9999, +9999, false);



            if(currentOutcome > bestOutCome){
                bestMove = move;
                bestOutCome = currentOutcome;
            }
        }


        return bestMove;
    }

    public int alphabeta(GameBoard configuration, int depth, int alpha, int beta, boolean maximizingPlayer){

        int v;

        if(depth == 0){
            return RatingFunction.evaluate(configuration.playField, configuration.myPlayerNumber);
        }

        if(maximizingPlayer){
            v = -9999;

            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration.playField, configuration.myPlayerNumber, configuration.myPawnPositions);


            for (Move nextMove : possibleMoves) {

                GameBoard nextConfiguration = configuration;
                nextConfiguration.applyMove(nextMove);
                moveCounter.increment(1);


                v = Integer.max(v, alphabeta(nextConfiguration, depth-1, alpha, beta, moveCounter.isCountEqualTo(myPlayerNumber)));
                alpha = Integer.max(alpha, v);

                if(beta <= alpha){
                    break;
                }
            }
            return v;

        } else { //Minimizing Player (our 2 opponents)
            v = +9999;

            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration.playField, configuration.myPlayerNumber, configuration.myPawnPositions);

            for (Move nextMove : possibleMoves) {

                GameBoard nextConfiguration = configuration;
                nextConfiguration.applyMove(nextMove);
                moveCounter.increment(1);

                v = Integer.min(v, alphabeta(nextConfiguration, depth-1, alpha, beta, moveCounter.isCountEqualTo(myPlayerNumber))); // methode um den nächsten player zu bestimmen
                beta = Integer.min(beta, v);

                if(beta <= alpha){
                    break;
                }
            }
            return v;
        }
    }








}
