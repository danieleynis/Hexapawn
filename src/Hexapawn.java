/*
Author: Daniel Eynis
E-mail: eynis@pdx.edu

Online resources consulted:
https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Hexapawn {

    private char currentTurn = 'W';
    private char[][] board;
    private int cols, rows;
    private ArrayList<Pawn> whitePawns = new ArrayList<>();
    private ArrayList<Pawn> blackPawns = new ArrayList<>();

    Hexapawn() {
        Scanner input = new Scanner(System.in);
        currentTurn = input.nextLine().charAt(0);
        assert currentTurn == 'W' || currentTurn == 'B';

        ArrayList<String> inputLines = new ArrayList<>();
        String lineRead;
        while(input.hasNextLine()) {
            lineRead = input.nextLine();
            if(lineRead.isEmpty())
                break;
            inputLines.add(lineRead);
            ++rows;
        }
        assert inputLines.size() != 0 && rows != 0 && inputLines.size() == rows;

        board = new char[rows][];

        char[] pieceArray;
        cols = inputLines.get(0).length();
        for (int i = 0; i < inputLines.size(); ++i) {
            pieceArray = inputLines.get(i).toCharArray();
            assert pieceArray.length == cols;
            board[i] = pieceArray;
            for (int j = 0; j < pieceArray.length; ++j) {
                char curPos = pieceArray[j];
                assert curPos == '.' || curPos == 'P' || curPos == 'p';
                if(curPos == '.')
                    continue;
                if(curPos == 'P') {
                    assert i != 0; // white pawn cannot start on opponent side
                    whitePawns.add(new Pawn(i, j, 'P'));
                }
                else {
                    assert i != rows-1;  // black pawn cannot start on opponent side
                    blackPawns.add(new Pawn(i, j, 'p'));
                }
            }
        }

        assert whitePawns.size() <= cols;
        assert blackPawns.size() <= cols;
    }

    private void printBoard(char[][] boardToPrint){
        for(char[] row : boardToPrint){
            System.out.println(row);
        }
    }
}
