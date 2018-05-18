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



    public Move getGoodMove(){


        return null;
    }

    private int minimax(GameBoard configuration, int depth, boolean maximizingPlayer){


        int bestValue;
        int value;

        if(depth == 0){
            return RatingFunction.evaluate(configuration.playField, myPlayerNumber);
        }

        if(maximizingPlayer){
            bestValue = -9999;
            HashSet<MyMove> children = GameManager.getAllPossibleMoves(configuration.playField, myPlayerNumber, configuration.myPawnPositions);

            for(Move move : children){

                GameBoard nextConfiguration = new GameBoard(myPlayerNumber, configuration);

                value = minimax(nextConfiguration, depth-1, false);

            }





        } else {
            bestValue = +9999;


        }


        return 0;
    }


//    public Move getBestMove(int[][] playField, int playerNumber){
//
//
//        this.myPlayerNumber = playerNumber;
//
//        moveCounter = new MoveCounter(playerNumber, playerNumber);
//
//
//        HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(playField, playerNumber, BoardGameKI.gameBoard.myPawnPositions);
//        GameBoard currentGameBoard = new GameBoard(myPlayerNumber, playField, BoardGameKI.gameBoard.myPawnPositions, BoardGameKI.gameBoard.enemy1PawnPositions, BoardGameKI.gameBoard.enemy2PawnPositions);
//
//
//        int currentOutcome;
//        int bestOutCome = -99999;
//
//        Move bestMove = null;
//
//        for (MyMove move: possibleMoves) {
//
//
//            currentGameBoard.applyMove(move, moveCounter.count);
//            moveCounter.increment(1);
//
//
//
//            currentOutcome = alphabeta(currentGameBoard, 2, -9999, +9999, false);
//
//
//
//            if(currentOutcome > bestOutCome){
//                bestMove = move;
//                bestOutCome = currentOutcome;
//            }
//        }
//
//
//        return bestMove;
//    }
//
//    public int alphabeta(GameBoard configuration, int depth, int alpha, int beta, boolean maximizingPlayer){
//
//        int v;
//
//        if(depth == 0){
//            return RatingFunction.evaluate(configuration.playField, configuration.myPlayerNumber);
//        }
//
//        if(maximizingPlayer){
//            v = -9999;
//
//            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration.playField, moveCounter.count, configuration.myPawnPositions);
//
//
//            for (Move nextMove : possibleMoves) {
//
//                GameBoard nextConfiguration = new GameBoard(moveCounter.count, configuration);
//                nextConfiguration.applyMove(nextMove, moveCounter.count);
//                moveCounter.increment(1);
//
//
//                v = Integer.max(v, alphabeta(nextConfiguration, depth-1, alpha, beta, moveCounter.isCountEqualTo(myPlayerNumber)));
//                alpha = Integer.max(alpha, v);
//
//                if(beta <= alpha){
//                    break;
//                }
//            }
//            return v;
//
//        } else { //Minimizing Player (our 2 opponents)
//            v = +9999;
//
//            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration.playField, moveCounter.count, configuration.getPawnsOfEnemy(moveCounter.count, myPlayerNumber));
//
//            for (Move nextMove : possibleMoves) {
//
//                GameBoard nextConfiguration = new GameBoard(moveCounter.count, configuration);
//                nextConfiguration.applyMove(nextMove, moveCounter.count);
//                moveCounter.increment(1);
//
//                v = Integer.min(v, alphabeta(nextConfiguration, depth-1, alpha, beta, moveCounter.isCountEqualTo(myPlayerNumber))); // methode um den nächsten player zu bestimmen
//                beta = Integer.min(beta, v);
//
//                if(beta <= alpha){
//                    break;
//                }
//            }
//            return v;
//        }
//    }

}
