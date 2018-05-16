import lenz.htw.gawihs.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameTree {


    private int alpha;
    private int beta;

    private int[][] playField;
    private int currentPlayerIndicator;
    private ArrayList<Point> myPawns = new ArrayList<>();

    private MyMove bestMove;



    public GameTree(int[][] playField, int currentPlayerIndicator, ArrayList<Point> myPawns){
        this.playField = playField;
        this.currentPlayerIndicator = currentPlayerIndicator;
        this.myPawns = myPawns;
    }



    public Move getBestMove(int[][] playField, int playerNumber){

        HashSet<MyMove> possibleMoves = GameManager.getAllPossibleMoves(playField, currentPlayerIndicator, myPawns);



        GameBoard currentGameBoard = BoardGameKI.gameBoard;




        int currentOutcome;
        int bestOutCome = -99999;

        Move bestMove;

        for (MyMove move: possibleMoves) {


            currentGameBoard.applyMove(move);



            currentOutcome = alphabeta(currentGameBoard, 3, -9999, +9999, true);



            if(currentOutcome > bestOutCome){
                bestMove = move;
                bestOutCome = currentOutcome;
            }



        }


        return null;
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

                v = Integer.max(v, alphabeta(nextConfiguration, depth-1, alpha, beta, false));
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

                v = Integer.min(v, alphabeta(nextConfiguration, depth-1, alpha, beta, true)); // methode um den nächsten player zu bestimmen
                beta = Integer.min(beta, v);

                if(beta <= alpha){
                    break;
                }
            }
            return v;
        }
    }








}
