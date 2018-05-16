
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class GameManager {



    public static ArrayList<Point> getPointsOfNeighborsThatCanBeVisited(Point pointThatWantsToCheckItsNeighbors, int[][] playField, ArrayList<Point> myPawnPositions){

        ArrayList<Point> pointsOfNeighborsThatCanBeVisited = new ArrayList<>();

        for(int x = pointThatWantsToCheckItsNeighbors.x -1 ; x <= pointThatWantsToCheckItsNeighbors.x +1; x++){

            for(int y = pointThatWantsToCheckItsNeighbors.y -1 ; y <= pointThatWantsToCheckItsNeighbors.y +1; y++){

                if(x < 0 || x > 8 ){
                    //Do nothing
                }

                else if(y < 0 || y > 8 ){
                    //Do nothing
                } else { //Inside Bounds

                    //Aussortieren der Nachbarn im Array, welche keine auf dem Spielfeld sind
                    if( ((pointThatWantsToCheckItsNeighbors.x -1 == x) && ((pointThatWantsToCheckItsNeighbors.y +1 == y)))     ||
                            ((pointThatWantsToCheckItsNeighbors.x +1 == x) && ((pointThatWantsToCheckItsNeighbors.y -1 == y))) ){
                        //Do Nothing
                    } else {
                        Point pointThatMightBeAddedToTargets = new Point(x, y);
                        int cellValue = playField[x][y];

                        if(cellValue == 0){ //Cell ist frei
                            pointsOfNeighborsThatCanBeVisited.add(pointThatMightBeAddedToTargets);
                        }
                        else if(cellValue < 8){ //Mindestens oben ist noch frei (vielleicht auch unten, aber das haben wir vorher schon gecheckt) & außerdem ist die Cell existent (nicht out of bounds, oder gelöscht)

                            if(! myPawnPositions.contains(pointThatMightBeAddedToTargets)){ //Wenn wir hier nicht stehen, dann können wir hier hingehen
                                pointsOfNeighborsThatCanBeVisited.add(pointThatMightBeAddedToTargets);
                            }
                        }
                    }
                }
            }
        }
        return pointsOfNeighborsThatCanBeVisited;
    }

    public static boolean isInBounds(int x, int y){


        if(x > 8 || x < 0 || y > 8 || y < 0){
            return false;
        }

        if(y == 0 && x > 4){
            return false;
        }

        if(y == 1 && x > 5){
            return false;
        }

        if(y == 2 && x > 6){
            return false;
        }

        if(y == 3 && x > 7){
            return false;
        }

        if(y == 4 && x == 4){
            return false;
        }

        if(y == 5 && x == 0){
            return false;
        }

        if(y == 6 && x < 2){
            return false;
        }

        if(y == 7 && x < 3){
            return false;
        }

        if(y == 8 && x < 4){
            return false;
        }

        return true;

    }

    public static boolean canMyPawnAtPointMove(Point pawnAtPoint, int[][] playField, int myPlayerNumber){
        int cellValue = playField[pawnAtPoint.x][pawnAtPoint.y];

        if(cellValue == 8){ //TODO Remove for Tournament
            //throw new RuntimeException("There is no pawn at this point, because the point has been eleminated");
            return false;
        }

        if(cellValue > 8){ // OBEN STEHT EINER

            //Sind wir das?
            if( ((cellValue >> 4) == 1) && myPlayerNumber == 0){
                //Red
                return true;
            }
            if( ((cellValue >> 4) == 2) && myPlayerNumber == 1){
                //Green
                return true;
            }
            if( ((cellValue >> 4) == 4) && myPlayerNumber == 2){
                //Blue
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    public static HashSet<MyMove> getAllPossibleMoves(int[][] playField, int playerIndicator, ArrayList<Point> myPawns){

        HashSet<MyMove> possibleMoves = new HashSet<>();

        ArrayList<Point> pawnsThatMayMove = new ArrayList<>();

        for (Point point : myPawns){
            if(canMyPawnAtPointMove(point, playField, playerIndicator)){
                pawnsThatMayMove.add(point);
            }
        }

        ArrayList<Point> availableNeighbors = new ArrayList<>();
        for (Point point : pawnsThatMayMove){
            availableNeighbors = getPointsOfNeighborsThatCanBeVisited(point, playField, myPawns);
            for(Point p: pawnsThatMayMove){
                if(p != point){
                    for(Point pointOfTargetForP : availableNeighbors){
                        possibleMoves.add(new MyMove(p.x, p.y, pointOfTargetForP.x, pointOfTargetForP.y));
                    }
                }
            }
        }
        return possibleMoves;
    }
}
