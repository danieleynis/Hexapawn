import java.util.ArrayList;
import java.util.Scanner;

public class Hexapawn {

    private char currentTurn = 'W';
    private char [][] board;


    Hexapawn(){
        Scanner input = new Scanner(System.in);
        currentTurn = input.nextLine().charAt(0);
        assert currentTurn == 'W' || currentTurn == 'B';

        ArrayList<String> inputLines = new ArrayList<>();
        while(input.hasNextLine()){
            inputLines.add(input.nextLine());
        }

        board = new char[inputLines.size()][];

    }
}
