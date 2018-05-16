import lenz.htw.gawihs.Move;

public class MoveCounter {

    public  int playerNumber;
    public  int count = 0;

    private  boolean redInGame = true;
    private  boolean greenInGame = true;
    private  boolean blueInGame = true;



    public MoveCounter(int initialCount, int playerNumber){
        this.count = initialCount;
        this.playerNumber = playerNumber;
    }


    public void setPlayerNumber(int num){
        playerNumber = num;
    }



    public boolean isMyMove(Move move){

        if(move == null && playerNumber == count){
            return true;
        } else {
            return false;
        }
    }


    public boolean isCountEqualTo(int number){
        return count == number;
    }

    public void increment(int amount){
        count = count + amount;
        count = count % 3;

        System.out.println("Incrementing Counter by " + amount + " to ---->" + count);
    }

    public int getLast(){

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

    public int getNext(){


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


    public boolean isEnemy1(){
        return count == ((playerNumber +1) % 3);
    }

    public boolean isEnemy2(){
        return count == ((playerNumber +2) % 3);
    }

    public void setMyTurn(){
        count = playerNumber;
        System.out.println("Setting my Turn: New Count=" + count);

    }

    public void manageKickedPlayers(){

        if(!redInGame){
            if(this.count == 0){
                this.increment(1);
            }
        }
        if(!greenInGame){
            if(this.count == 1){
                this.increment(1);
            }
        }
        if(!blueInGame){
            if(this.count == 2){
                this.increment(1);
            }
        }
    }

    public void removePlayer(int playerIndicator){

        if(playerIndicator == 0){
            System.out.println("REMOVING PLAYER WITH INDICATOR " + playerIndicator + "With Color RED");
            redInGame = false;
        }
        if(playerIndicator == 1){
            System.out.println("REMOVING PLAYER WITH INDICATOR " + playerIndicator + "With Color GREEN");
            greenInGame = false;
        }
        if(playerIndicator == 2){
            System.out.println("REMOVING PLAYER WITH INDICATOR " + playerIndicator + "With Color BLUE");
            blueInGame = false;
        }
    }






}
