import lenz.htw.gawihs.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameTree {


    private int myPlayerNumber;
    private MyMove bestMove;


    public GameTree(int playerNumber){
        this.myPlayerNumber = playerNumber;
    }


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
            HashSet<MyMove> children = GameManager.getAllPossibleMoves(configuration);

            for(Move move : children){

                GameBoard nextConfiguration = new GameBoard(configuration);

                value = minimax(nextConfiguration, depth-1, false);

            }


        } else {
            bestValue = +9999;


        }


        return 0;
    }


    public Move getBestMove(GameBoard configuration){

        HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration);
        GameBoard currentGameBoard = new GameBoard(configuration);

        int currentOutcome;
        int bestOutCome = -99999;

        Move bestMove = null;

        for (MyMove move: possibleMoves) {


            currentGameBoard.applyMove(move);
            currentOutcome = alphabeta(currentGameBoard, 2, -9999, +9999, false);


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

            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration);

            for (Move nextMove : possibleMoves) {

                GameBoard nextConfiguration = new GameBoard(configuration);
                nextConfiguration.applyMove(nextMove);

                v = Integer.max(v, alphabeta(nextConfiguration, depth-1, alpha, beta, configuration.isMyTurn()));
                alpha = Integer.max(alpha, v);

                if(beta <= alpha){
                    break;
                }
            }
            return v;

        } else { //Minimizing Player (our 2 opponents)
            v = +9999;

            HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(configuration);

            for (Move nextMove : possibleMoves) {

                GameBoard nextConfiguration = new GameBoard(configuration);
                nextConfiguration.applyMove(nextMove);

                v = Integer.min(v, alphabeta(nextConfiguration, depth-1, alpha, beta, configuration.isMyTurn())); // methode um den nächsten player zu bestimmen
                beta = Integer.min(beta, v);

                if(beta <= alpha){
                    break;
                }
            }
            return v;
        }
    }

}
