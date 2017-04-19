public class Pawn {
    private int[] position;
    private char type;

    Pawn(int row, int col, char type){
        position = new int[]{row, col};
        this.type = type;
    }

    public int[] getPosn(){
        return position;
    }

    public char getType() {
        return type;
    }
}
