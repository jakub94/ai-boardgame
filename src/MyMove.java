import lenz.htw.gawihs.Move;


public class MyMove extends Move {


    public MyMove(int fromX, int fromY, int toX, int toY) {
        super(fromX, fromY, toX, toY);
    }

    @Override
    public boolean equals(Object other){
        return (this.toString().equals(other.toString()));
    }

    @Override
    public int hashCode(){
        //return 1;
        return this.toString().hashCode();
    }
}
