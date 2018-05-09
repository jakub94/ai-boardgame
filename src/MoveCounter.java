import lenz.htw.gawihs.Move;

public class MoveCounter {

    public static int playerNumber;
    public static int count = 0;



    public static void setPlayerNumber(int num){
        playerNumber = num;
    }



    public static boolean isMyMove(Move move){

        if(move == null && playerNumber == count){
            return true;
        } else {
            return false;
        }
    }

    public static void increment(int amount){
        count = count + amount;
        count = count % 3;
    }

    public static int getLast(){


        if(count == 0){
            return 2;
        }
        if(count == 1){
            return 0;
        }
        if(count == 2){
            return 1;
        }

        return  count;  //should never happen
    }

    public static int getNext(){


        if(count == 0){
            return 1;
        }
        if(count == 1){
            return 2;
        }
        if(count == 2){
            return 0;
        }

        return  count;  //should never happen
    }






}
