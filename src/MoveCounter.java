import lenz.htw.gawihs.Move;

public class MoveCounter {

    public static int playerNumber;
    public static int count = 0;

    private static boolean redInGame = true;
    private static boolean greenInGame = true;
    private static boolean blueInGame = true;


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

        System.out.println("Incrementing Counter by " + amount + " to ---->" + count);
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


    public static boolean isEnemy1(){
        return count == ((playerNumber +1) % 3);
    }

    public static boolean isEnemy2(){
        return count == ((playerNumber +2) % 3);
    }

    public static void setMyTurn(){
        count = playerNumber;
        System.out.println("Setting my Turn: New Count=" + count);

    }

    public static void manageKickedPlayers(){

        if(!redInGame){
            if(MoveCounter.count == 0){
                MoveCounter.increment(1);
            }
        }
        if(!greenInGame){
            if(MoveCounter.count == 1){
                MoveCounter.increment(1);
            }
        }
        if(!blueInGame){
            if(MoveCounter.count == 2){
                MoveCounter.increment(1);
            }
        }
    }

    public static void removePlayer(int playerIndicator){

        if(playerIndicator == 0){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color RED");
            redInGame = false;
        }
        if(playerIndicator == 1){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color GREEN");
            greenInGame = false;
        }
        if(playerIndicator == 2){
            System.out.println("REMOVING PLAYER WIHT INDICATOR " + playerIndicator + "With Color BLUE");
            blueInGame = false;
        }
    }






}
