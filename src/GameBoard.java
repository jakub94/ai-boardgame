import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import lenz.htw.gawihs.Move;

//TODO DIE GEGNER LISTEN WERDEN FALSCH GEMERKT

public class GameBoard {


    int [][] playField = new int [9][9];

    ArrayList<Point> pawnPositionsRed = new ArrayList();
    ArrayList<Point> pawnPositionsGreen = new ArrayList();
    ArrayList<Point> pawnPositionsBlue = new ArrayList();

    public ArrayList<Point> myPawnPositions = new ArrayList();
    public ArrayList<Point> enemy1PawnPositions = new ArrayList();
    public ArrayList<Point> enemy2PawnPositions = new ArrayList();


    public int myPlayerNumber;

    Random random = new Random();

    HashMap<Integer, String> symbolMap = new HashMap<Integer, String>();

    MoveCounter moveCounter;


    public GameBoard(int myPlayerNumber){

        this.myPlayerNumber = myPlayerNumber;

        moveCounter = new MoveCounter(0, myPlayerNumber);


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

        initSymbolMap();
        printPlayField();
    }

    public GameBoard(GameBoard cloneMe){

        moveCounter = new MoveCounter(cloneMe.moveCounter);

        this.myPlayerNumber = cloneMe.myPlayerNumber;
        this.myPawnPositions =     (ArrayList<Point>) cloneMe.myPawnPositions.clone();
        this.enemy1PawnPositions = (ArrayList<Point>) cloneMe.enemy1PawnPositions.clone();
        this.enemy2PawnPositions = (ArrayList<Point>) cloneMe.enemy2PawnPositions.clone();


        this.playField = new int [9][9];

        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                cloneMe.playField[x][y] = cloneMe.playField[x][y];
            }
        }

        initSymbolMap();
        printPlayField();
    }



    public int getInitialCellState(int x, int y){

        if(GameManager.isInBounds(x,y)){

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
    }

    public Move getRandomMove(){
        HashSet<MyMove> allPossibleMoves = GameManager.getAllPossibleMoves(this);
        if(allPossibleMoves.size() < 1) return new Move(0, 0, 8, 8);
        MyMove nextMyMove = (MyMove) allPossibleMoves.toArray()[random.nextInt(allPossibleMoves.size())];
        return new Move(nextMyMove.fromX, nextMyMove.fromY, nextMyMove.toX, nextMyMove.toY);
    }

    private Point getPointOfPawnThatCanMove(){
        boolean foundPossiblePawn = false;
        Point pointOfPawnThatWantsToMove;

        do{
            pointOfPawnThatWantsToMove = myPawnPositions.get(random.nextInt(5));
            foundPossiblePawn = GameManager.canMyPawnAtPointMove(pointOfPawnThatWantsToMove, playField, myPlayerNumber);
            //System.out.println("FOUND POSSIBLE PAWN: "+ foundPossiblePawn);

        }while(!foundPossiblePawn);

        return pointOfPawnThatWantsToMove;
    }

    public void manageTurn(){

        moveCounter.manageKickedPlayers();

        if(isEnemy1Turn()){
            removeEnemy();
            removeNextEnemy();
            setMyTurn();
        } else if(isEnemy2Turn()) {
            removeEnemy();
            setMyTurn();
        }
    }

    public void validateAndApplyMove(Move move){
        if (isMyTurn()) {
            applyMove(move);
        } else {
            int playerWhoMadeTheMove = whoDidTheMove(move);

            if(isEnemy1Turn()){
                if(playerWhoMadeTheMove == moveCounter.getNext(myPlayerNumber)){
                    applyMove(move);
                    return;
                }
                if(playerWhoMadeTheMove == moveCounter.getLast(myPlayerNumber)){
                    removeEnemy();
                    moveCounter.increment(1);
                    applyMove(move);
                    return;
                }
            }
            if(isEnemy2Turn()){
                if(playerWhoMadeTheMove == moveCounter.getLast(myPlayerNumber)){
                    applyMove(move);
                    return;
                }
                if(playerWhoMadeTheMove == moveCounter.getNext(myPlayerNumber)){
                    removeEnemy();
                    moveCounter.increment(2);
                    applyMove(move);
                    moveCounter.increment(2);
                    return;
                }
            }
        }
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
            updatePawnPositions(move, moveCounter.count);
            moveCounter.increment(1);

            printPlayField();
    }

    public void updatePawnPositions(Move move, int playerIndicator){

        Point fromPoint = new Point(move.fromX, move.fromY);
        Point toPoint = new Point(move.toX, move.toY);


        if(playerIndicator == 0){
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

        if(playerIndicator == 1){
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

        if(playerIndicator == 2){
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

//        String myPawns = "";
//        String enemy1Pawns = "";
//        String enemy2Pawns = "";
//
//        for(int i = 0; i < 5 ; i++){
//            if(myPawnPositions.size() > 0)
//               myPawns += myPawnPositions.get(i).toString() + " // ";
//            if(enemy1PawnPositions.size() > 0)
//                enemy1Pawns += enemy1PawnPositions.get(i).toString() + " // ";
//            if(enemy2PawnPositions.size() > 0)
//                enemy2Pawns += enemy2PawnPositions.get(i).toString() + " // ";
//        }
//
//        //System.out.println("CurrentPlayerIndicator " + playerIndicator);
//        System.out.println("MyPawns: " + myPawns);
//        System.out.println("enemy1Pawns: " + enemy1Pawns);
//        System.out.println("enemy2Pawns: " + enemy2Pawns);

    }

    public int whoDidTheMove(Move move){

        int cellValue = playField[move.fromX][move.fromY];

        if(cellValue > 8){
            cellValue = cellValue >> 4;
        }

        if(cellValue == 1){
            return 0;
        }

        if(cellValue == 2){
            return 1;
        }

        if(cellValue == 4){
            return 2;
        }

        return -1; // SHOULD NEVER EVER HAPPEN
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

    public void printPlayField(){

        int cellValue;
        int topVal;
        int bottomVal;
        String line;
        String lines = "";


        System.out.println("    0   1   2   3   4   5   6   7   8");

        for(int y = 0; y < 9; y++){
            line = y + "  ";
            for(int x = 0; x < 9; x++){

                cellValue = playField[x][y];
                topVal = cellValue >> 4;
                bottomVal = cellValue & 0x0f;


                line += ("|" + symbolMap.get(topVal) + symbolMap.get(bottomVal) + "|");

            }
            //System.out.println(line);
            //System.out.println("__________________________________________");
            lines = line + "\n" + lines;
        }

        System.out.println(lines);


    }

    private void initSymbolMap(){
        this.symbolMap.put(0, "0");
        this.symbolMap.put(1, "R");
        this.symbolMap.put(2, "G");
        this.symbolMap.put(4, "B");
        this.symbolMap.put(8, "X");
    }


    public boolean isMyTurn(){
        return moveCounter.isMyTurn();
    }

    public boolean isEnemy1Turn(){
        return moveCounter.isEnemy1();
    }

    public boolean isEnemy2Turn(){
        return moveCounter.isEnemy2();
    }

    public void removeEnemy(){
        moveCounter.removePlayer(moveCounter.count);
        removePlayer(moveCounter.count);
    }
    public void removeNextEnemy(){
        moveCounter.removePlayer(moveCounter.getNext());
        removePlayer(moveCounter.getNext());
    }

    public void setMyTurn(){
        moveCounter.setMyTurn();
    }


    public ArrayList<Point> getPawnsOfEnemy(int currentMoveCounter, int currentPlayerIndicator){

        if( ((currentPlayerIndicator + 1) % 3) == currentMoveCounter){
            return enemy1PawnPositions;
        }

        if( ((currentPlayerIndicator + 2) % 3) == currentMoveCounter){
            return enemy2PawnPositions;
        }
        return null; //SHOULD NEVER EVER HAPPEN!!
    }


}




