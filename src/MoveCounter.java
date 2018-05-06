import lenz.htw.gawihs.Move;

public class MoveCounter {

    public static int playerNumber;
    public static int count = 0;



    public static void setPlayerNumber(int num){
        playerNumber = num;
    }

    public static void put(Move move){


        if(move == null){



        } else {
            count++;
        }


    }

    public static boolean isMyMove(){


        return true;
    }






}
