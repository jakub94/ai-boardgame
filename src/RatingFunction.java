import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class RatingFunction {


    private static ArrayList<Point> myPawns = new ArrayList<>();


    private static int availablePawnsMultiplier = 1;
    private static int pinnedEnemyPawnsMultiplier = 1;
    private static int openNeighborMultiplier = 1;


    public static int evaluate(int[][] playField, int playerIndicator){


        int myAvailablePawns = 0;
        int pinnedEnemyPawns = 0;
        int neighbors = 0;


        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                int cellValue = playField[x][y];

                int isOneIfPawnCanMove = add1IfPawnCanMove(cellValue, playerIndicator);

                myAvailablePawns += isOneIfPawnCanMove;
                pinnedEnemyPawns += add1IfEnemyPawnIsPinned(cellValue, playerIndicator);

                if(isOneIfPawnCanMove == 1){
                    myPawns.add(new Point(x, y));
                }
            }
        }

        neighbors =  getNumberOfAvailableNeighbors(playField);



        System.out.println("MyAvailablePawns " + myAvailablePawns);
        System.out.println("PinnedEnemyPawns " + pinnedEnemyPawns);
        System.out.println("Neighbors " + neighbors);



        System.out.println("CURRENT RATING for Player " + playerIndicator + " is " + (myAvailablePawns*availablePawnsMultiplier + pinnedEnemyPawns*pinnedEnemyPawnsMultiplier + neighbors*openNeighborMultiplier));

        return myAvailablePawns*availablePawnsMultiplier + pinnedEnemyPawns*pinnedEnemyPawnsMultiplier + neighbors*openNeighborMultiplier;
    }


    private static int add1IfPawnCanMove(int cellValue, int playerIndicator){
        if(cellValue > 8){
            cellValue = cellValue >> 4;
        }
        if(cellValue == 1 && playerIndicator == 0){
            return 1;
        } else if(cellValue == 2 && playerIndicator == 1){
            return 1;
        }else if(cellValue == 4 && playerIndicator == 2){
            return 1;
        }
        return 0;
    }


    private static int add1IfEnemyPawnIsPinned(int cellValue, int playerIndicator){

        if(cellValue <= 8){
            return 0;
        } else {
            int cellValueBottom = cellValue & 0x0f;

            if(playerIndicator == 0 && cellValueBottom != 1){
                return 1;
            }
            if(playerIndicator == 1 && cellValueBottom != 2){
                return 1;
            }
            if(playerIndicator == 2 && cellValueBottom != 4){
                return 1;
            }
        }

        return 0;
    }

    private static int getNumberOfAvailableNeighbors(int[][] playField){

        ArrayList<Point> neighborsWithDuplicates = new ArrayList<>();
        HashSet<Point> availableNeighbors = new HashSet<>();

        for(int i = 0; i < myPawns.size(); i++){
            neighborsWithDuplicates.addAll(GameManager.getPointsOfNeighborsThatCanBeVisited(myPawns.get(i), playField, myPawns));
        }

        availableNeighbors.addAll(neighborsWithDuplicates);

        return availableNeighbors.size();
    }




}
