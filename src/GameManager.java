import java.awt.*;
import java.util.ArrayList;

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


}
