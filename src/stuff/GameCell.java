package stuff;

import java.util.EmptyStackException;
import java.util.Stack;

public class GameCell {

    private Stack<Integer> pawns;
    private boolean eliminated;

    // 0 --> Yourself 1--> Next Player 2 --> third player


    public GameCell(){
        this.eliminated = false;
        this.pawns = new Stack();
    }

    public GameCell(int playerId){
        this.eliminated = false;
        this.pawns = new Stack();
        addPawn(playerId);
    }

    public void addPawn(int playerId){
        pawns.push(playerId);
    }

    public int removePawn(){
        return pawns.pop();
    }

    public int getTopPawn(){
        try {
            return pawns.peek();
        }catch (EmptyStackException e){
            return 3; //TODO Change to 0 and increment player numbers?!
        }
    }

    public boolean isEliminated(){
        return eliminated;
    }

    public void eleminate(){
        this.eliminated = true;
    }

}
