import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import lenz.htw.gawihs.Move;

//TODO DIE GEGNER LISTEN WERDEN FALSCH GEMERKT

public class GameBoard {


    int [][] playField = new int [9][9];

    ArrayList<Point> pawnPositionsRed = new ArrayList();
    ArrayList<Point> pawnPositionsGreen = new ArrayList();
    ArrayList<Point> pawnPositionsBlue = new ArrayList();

    ArrayList<Point> myPawnPositions = new ArrayList();
    ArrayList<Point> enemy1PawnPositions = new ArrayList();
    ArrayList<Point> enemy2PawnPositions = new ArrayList();


    int myPlayerNumber;

    Random random = new Random();


    public GameBoard(int myPlayerNumber){

        this.myPlayerNumber = myPlayerNumber;

        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                playField[x][y] = getInitialCellState(x,y);
            }
        }

        if(myPlayerNumber == 0){
            myPawnPositions = pawnPositionsRed;
            enemy1PawnPositions = pawnPositionsGreen;
            enemy2PawnPositions = pawnPositionsBlue;
        }

        if(myPlayerNumber == 1){
            myPawnPositions = pawnPositionsGreen;
            enemy1PawnPositions = pawnPositionsBlue;
            enemy2PawnPositions = pawnPositionsRed;
        }

        if(myPlayerNumber == 2){
            myPawnPositions = pawnPositionsBlue;
            enemy1PawnPositions = pawnPositionsRed;
            enemy2PawnPositions = pawnPositionsGreen;
        }
    }

    public int getInitialCellState(int x, int y){

        if(isInBounds(x,y)){

            if(y == 0){ //Red
                pawnPositionsRed.add(new Point(x, y));
                return 1;
            }

            if(x == 8){ // Blue
                pawnPositionsBlue.add(new Point(x, y));
                return 4;
            }

            if( (x == 0 && y == 4) || (x == 1 && y == 5) || (x == 2 && y == 6) || (x == 3 && y == 7) || (x == 4 && y == 8)){ //Green
                pawnPositionsGreen.add(new Point(x, y));
                return 2;
            }

            return 0; //Empty cell but in bounds

        } else {
            return 8;
        }


/*      int oben = (cell >> 4) & 0x0f;
        int unten = cell & 0x0f;
        cell = (oben << 4) || unten;
*/

    }

    public boolean isInBounds(int x, int y){


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


    private int randomMoveCallStackCounter = 0;
    public Move getRandomMove(){


        Point pointOfPawnThatWillMakeAMove = getPointOfPawnThatCanMove();

        ArrayList<Point> possibleTargets = new ArrayList<>();
        ArrayList<Point> pointsOfNeighborsThatCanBeVisited = new ArrayList<>();


        Point pawnThatWillNotMakeAMove;

        for(int i = 0 ; i < 5; i++){

            if(!myPawnPositions.get(i).equals(pointOfPawnThatWillMakeAMove)){

                pawnThatWillNotMakeAMove = myPawnPositions.get(i);


                if(canMyPawnAtPointMove(pawnThatWillNotMakeAMove)){ //If that point can move, we can also visit its neighbors (weil dann ist er nicht unten, also sind seine nachbarn available)
                    pointsOfNeighborsThatCanBeVisited = getPointsOfNeighborsThatCanBeVisited(pawnThatWillNotMakeAMove);
                    possibleTargets.addAll(pointsOfNeighborsThatCanBeVisited);
                }

            }
        }


        if(randomMoveCallStackCounter > 70){
            return new Move(0,0,8,8);
        }

        if(possibleTargets.size() < 1){
            randomMoveCallStackCounter++;
            return getRandomMove();
        } else {
            randomMoveCallStackCounter = 0;
        }

        Point randomTarget = possibleTargets.get(random.nextInt(possibleTargets.size()));

        return new Move(pointOfPawnThatWillMakeAMove.x, pointOfPawnThatWillMakeAMove.y, randomTarget.x, randomTarget.y);
    }



    private Point getPointOfPawnThatCanMove(){
        boolean foundPossiblePawn = false;
        Point pointOfPawnThatWantsToMove;

        do{
            pointOfPawnThatWantsToMove = myPawnPositions.get(random.nextInt(5));
            foundPossiblePawn = canMyPawnAtPointMove(pointOfPawnThatWantsToMove);
            //System.out.println("FOUND POSSIBLE PAWN: "+ foundPossiblePawn);

        }while(!foundPossiblePawn);

        return pointOfPawnThatWantsToMove;
    }

    private boolean canMyPawnAtPointMove(Point pawnAtPoint){
        int cellValue = playField[pawnAtPoint.x][pawnAtPoint.y];

        if(cellValue == 8){ //TODO Remove for Tournament
            throw new RuntimeException("There is no pawn at this point, because the point has been eleminated");
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

    private ArrayList<Point> getPointsOfNeighborsThatCanBeVisited(Point pointThatWantsToCheckItsNeighbors){

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

    public void applyMove(Move move){



        int startCellValue = playField[move.fromX][move.fromY];
        int targetCellValue = playField[move.toX][move.toY];
        int playerCode;

        if(startCellValue < 8){ //Es steht nur ein Pawn hier. Zelle kann "elemniniert" werden
            playField[move.fromX][move.fromY] = 8;
            playerCode = startCellValue; //wir sind alleine hier, also speichert die Cell unseren SpielerCode
        } else {
            playField[move.fromX][move.fromY] = startCellValue & 0x0f;
            playerCode = startCellValue >> 4;
        }

        if(targetCellValue > 0){ //Hier steht schon ein Pawn, wir tragen uns oben ein
            playField[move.toX][move.toY] = (playerCode << 4) | targetCellValue;
        } else { //Keiner hier, also sind wir unten
            playField[move.toX][move.toY] = playerCode;
        }
        updatePawnPositions(move);
    }

    public void updatePawnPositions(Move move){

        Point fromPoint = new Point(move.fromX, move.fromY);
        Point toPoint = new Point(move.toX, move.toY);


        if(MoveCounter.count == 0){
            if(myPlayerNumber == 0){
                myPawnPositions.remove(fromPoint);
                myPawnPositions.add(toPoint);

            }
            if(myPlayerNumber == 1){
                enemy2PawnPositions.remove(fromPoint);
                enemy2PawnPositions.add(toPoint);
            }
            if(myPlayerNumber == 2){
                enemy1PawnPositions.remove(fromPoint);
                enemy1PawnPositions.add(toPoint);

            }
        }

        if(MoveCounter.count == 1){
            if(myPlayerNumber == 0){
                enemy1PawnPositions.remove(fromPoint);
                enemy1PawnPositions.add(toPoint);

            }
            if(myPlayerNumber == 1){
                myPawnPositions.remove(fromPoint);
                myPawnPositions.add(toPoint);
            }
            if(myPlayerNumber == 2){
                enemy2PawnPositions.remove(fromPoint);
                enemy2PawnPositions.add(toPoint);
            }
        }

        if(MoveCounter.count == 2){
            if(myPlayerNumber == 0){
                enemy2PawnPositions.remove(fromPoint);
                enemy2PawnPositions.add(toPoint);
            }
            if(myPlayerNumber == 1){
                enemy1PawnPositions.remove(fromPoint);
                enemy1PawnPositions.add(toPoint);
            }
            if(myPlayerNumber == 2){
                myPawnPositions.remove(fromPoint);
                myPawnPositions.add(toPoint);

            }
        }

        String myPawns = "";
        String enemy1Pawns = "";
        String enemy2Pawns = "";

        for(int i = 0; i < 5 ; i++){
            if(myPawnPositions.size() > 0)
               myPawns += myPawnPositions.get(i).toString() + " // ";
            if(enemy1PawnPositions.size() > 0)
                enemy1Pawns += enemy1PawnPositions.get(i).toString() + " // ";
            if(enemy2PawnPositions.size() > 0)
                enemy2Pawns += enemy2PawnPositions.get(i).toString() + " // ";
        }

        //System.out.println("CurrentPlayerIndicator " + playerIndicator);
        System.out.println("MyPawns: " + myPawns);
        System.out.println("enemy1Pawns: " + enemy1Pawns);
        System.out.println("enemy2Pawns: " + enemy2Pawns);

    }

    public boolean isValidMove(Move move) {


        System.out.println(move + " IS VALID MOVE START? With PI: " + MoveCounter.count + " " + isValidMoveStart(move));
        System.out.println(move + " IS VALID MOVE TARGET? WITH PI: " + MoveCounter.count + " " + isValidMoveTarget(move));

        return isValidMoveStart(move) && isValidMoveTarget(move);
    }


    public boolean isValidMoveStart(Move move){




        int cellValue = playField[move.fromX][move.fromY];

        if(cellValue > 8){
            cellValue = cellValue >> 4;
        }

        if(cellValue == 1 && MoveCounter.count == 0){
            return true;
        }
        if(cellValue == 2 && MoveCounter.count == 1){
            return true;
        }
        if(cellValue == 4 && MoveCounter.count == 2){
            return true;
        }

        return false;
    }

    public boolean isValidMoveTarget(Move move) {

        int cellValue = playField[move.toX][move.toY];

        if(cellValue >= 8){
            return false; //Oben schon besetzt
        } else {

            if(cellValue == 1 && MoveCounter.count == 0){
                return false; //wir stehen schon hier (Im Ziel)
            }
            if(cellValue == 2 && MoveCounter.count == 1){
                return false; //wir stehen schon hier (Im Ziel)
            }
            if(cellValue == 4 && MoveCounter.count == 2){
                return false; //wir stehen schon hier (Im Ziel)
            }

            for(int x = move.toX - 1; x <= move.toX + 1; x++){

                for(int y = move.toY - 1; y <= move.toY + 1; y++){


                    if(!isInBounds(x, y)){
                        //Do nothing
                    } else if( ( (move.toX -1 == x) && (move.toY +1 == y) ) || ( (move.toX +1 == x) && (move.toY -1 == y) ) ){

                        //Do nothing, not neighbors on the field

                    } else {

                        int neighborCellValue = playField[x][y];

                        if(neighborCellValue > 8){
                            neighborCellValue = neighborCellValue >> 4;
                        }

                        if(neighborCellValue == 1 && MoveCounter.count == 0){
                            return true;
                        }
                        if(neighborCellValue == 2 && MoveCounter.count == 1){
                            return true;
                        }
                        if(neighborCellValue == 4 && MoveCounter.count == 2){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void removePlayer(int playerIndicator){

        if(playerIndicator == 0){//Rot fliegt

            if(myPlayerNumber == 1){//Wir sind Grün
                removeEnemy(playerIndicator, 2);
            }

            if(myPlayerNumber == 2){//Wir sind Blau
                removeEnemy(playerIndicator, 1);
            }

        }

        if(playerIndicator == 1){//Grün fliegt

            if(myPlayerNumber == 0){//Wir sind Rot
                removeEnemy(playerIndicator,1);
            }

            if(myPlayerNumber == 2){//Wir sind Blau
                removeEnemy(playerIndicator,2);
            }

        }

        if(playerIndicator == 2){//Blau fliegt

            if(myPlayerNumber == 1){//Wir sind Grün
                removeEnemy(playerIndicator,1);
            }

            if(myPlayerNumber == 0){//Wir sind Rot
                removeEnemy(playerIndicator,2);
            }

        }
    }

    private void removeEnemy(int playerIndicator, int enemyNumber){

        if(enemyNumber == 1){
            removePawns(playerIndicator, enemy1PawnPositions);
            enemy1PawnPositions.clear();
            System.out.print("Removing Enemy 1");

        } else if(enemyNumber == 2){
            removePawns(playerIndicator, enemy2PawnPositions);
            enemy2PawnPositions.clear();
            System.out.print("Removing Enemy 2");
        }
    }

    private void removePawns(int playerIndicator, ArrayList<Point> pawnsToRemove){


        int currentX = 0;
        int currentY = 0;
        int currentCellValue = 0;

        for(Point pawn : pawnsToRemove){

            currentX = pawn.x;
            currentY = pawn.y;
            currentCellValue = playField[currentX][currentY];

            if(isPlayerOnTop(playerIndicator, currentCellValue)){

             //Do nothing. Die obere bleibt einfach liegen, falls jedoch die untere ausscheidet, kann man wieder auf dieses Feld


            } else {
                if(currentCellValue > 8){//Jemand anderes ist oben als der zu löschende Pawn
                    playField[currentX][currentY] = currentCellValue >> 4;
                } else {
                    playField[currentX][currentY] = 8;
                }

            }
        }
    }

    private boolean isPlayerOnTop(int playerIndicator, int cellValue){

        if(playerIndicator == 0){
            if(cellValue >> 4 == 1) return true;
        }
        if(playerIndicator == 1){
            if(cellValue >> 4 == 2) return true;
        }
        if(playerIndicator == 2){
            if(cellValue >> 4 == 4) return true;
        }

        return false;

    }





}




