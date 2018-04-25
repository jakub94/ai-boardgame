package stuff;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

    HashMap<String, GameCell> state = new HashMap();

    ArrayList<String> startPositionsPlayer0;
    ArrayList<String> startPositionsPlayer1;
    ArrayList<String> startPositionsPlayer2;
    ArrayList<String> initializeOutOfBoundsPositions;

    ArrayList<String> pawnPositionsPlayer0;
    ArrayList<String> pawnPositionsPlayer1;
    ArrayList<String> pawnPositionsPlayer2;

    int myPlayerNumber;





    public GameBoard(int myPlayerNumber){

        this.myPlayerNumber = myPlayerNumber;

        initializeStartPositions(myPlayerNumber);
        initializeOutOfBoundsPositions();
        initializeBoard();

    }

    public void initializeStartPositions(int myPlayerNumber){

        String[] startPositionsPlayer0 = new String[]{"0-0", "1-0", "2-0", "3-0", "4-0"};
        String[] startPositionsPlayer1 = new String[]{"0-4", "1-5", "2-6", "3-7", "4-8"};
        String[] startPositionsPlayer2 = new String[]{"8-8", "8-7", "8-6", "8-5", "8-4"};

        this.startPositionsPlayer0 = new ArrayList<String>();
        this.startPositionsPlayer1 = new ArrayList<String>();
        this.startPositionsPlayer2 = new ArrayList<String>();
        this.pawnPositionsPlayer0 = new ArrayList<String>();
        this.pawnPositionsPlayer1 = new ArrayList<String>();
        this.pawnPositionsPlayer2 = new ArrayList<String>();

        for(int i = 0; i < 5; i++){

            this.startPositionsPlayer0.add(startPositionsPlayer0[i]);
            this.startPositionsPlayer1.add(startPositionsPlayer1[i]);
            this.startPositionsPlayer2.add(startPositionsPlayer2[i]);

        }

       if(myPlayerNumber == 0){
           pawnPositionsPlayer0 = this.startPositionsPlayer0;
           pawnPositionsPlayer1 = this.startPositionsPlayer1;
           pawnPositionsPlayer2 = this.startPositionsPlayer2;
       }

        if(myPlayerNumber == 1){
            pawnPositionsPlayer0 = this.startPositionsPlayer1;
            pawnPositionsPlayer1 = this.startPositionsPlayer2;
            pawnPositionsPlayer2 = this.startPositionsPlayer0;
        }

        if(myPlayerNumber == 2){
            pawnPositionsPlayer0 = this.startPositionsPlayer2;
            pawnPositionsPlayer1 = this.startPositionsPlayer0;
            pawnPositionsPlayer2 = this.startPositionsPlayer1;
        }


    }

    public void initializeOutOfBoundsPositions(){
        String[] outOfBoundsPositions = new String[]{
                "5-0", "6-0", "7-0", "8-0", "6-1", "7-1", "8v1", "7-2", "8-2", "8-3",
                "0-5", "0-6", "0-7", "0-8", "1-6", "1-7", "1-8", "2-7", "2-8", "3-8",
                "4-4"
        };

        this.initializeOutOfBoundsPositions = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            this.initializeOutOfBoundsPositions.add(outOfBoundsPositions[i]);
        }
    }

    public void initializeBoard(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                String pos = x+"-"+y;
                if(initializeOutOfBoundsPositions.contains(pos)){
                    //Do nothing
                } else if(startPositionsPlayer0.contains(pos)){
                    state.put(pos, new GameCell(0));
                } else if(startPositionsPlayer1.contains(pos)){
                    state.put(pos, new GameCell(1));
                } else if(startPositionsPlayer2.contains(pos)){
                    state.put(pos, new GameCell(2));
                } else {
                    state.put(pos, new GameCell());
                }
            }
        }
    }

    public ArrayList<String> getPossibleTargets(){

        ArrayList<String> possibleTargets = new ArrayList<>();

        for(int i = 0; i < pawnPositionsPlayer0.size() ; i++){

            String key = pawnPositionsPlayer0.get(i);
            int currentX = Integer.parseInt(key.substring(0,1));
            int currentY = Integer.parseInt(key.substring(2,3));

            for( int x = currentX-1; x <= currentX+1; x++){
                for( int y = currentY-1; y <= currentY+1; y++){
                    String pos = x+"-"+y;

                    System.out.println(pos);

                   if(state.containsKey(pos)){

                       GameCell cell = state.get(pos);

                       if(!cell.isEliminated() && cell.getTopPawn() != myPlayerNumber){
                           possibleTargets.add(pos);
                       }
                   }

                }
            }

        }

        return  possibleTargets;
    }

    public ArrayList<String> getPawnPositionsPlayer0(){
        return pawnPositionsPlayer0;
    }

}




