/*
Author: Daniel Eynis
E-mail: eynis@pdx.edu

Online resources consulted:
https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
http://stackoverflow.com/questions/13942701/take-a-char-input-from-the-scanner
http://stackoverflow.com/questions/17606839/creating-a-set-of-arrays-in-java
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Hexapawn {

    private char currentTurn = 'W';
    private char[][] board;
    private int cols, rows;
    private HashSet<String> wPawns = new HashSet<>();
    private HashSet<String> bPawns = new HashSet<>();

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
        for (int i = 0; i < inputLines.size(); ++i){
            pieceArray = inputLines.get(i).toCharArray();
            assert pieceArray.length == cols;
            board[i] = pieceArray;

            for (int j = 0; j < pieceArray.length; ++j){
                char curPos = pieceArray[j];
                assert curPos == '.' || curPos == 'P' || curPos == 'p';

                if(curPos == '.')
                    continue;
                if(curPos == 'P'){
                    assert i != 0; // white pawn cannot start on opponent side
                    wPawns.add(i + "" + j);
                }
                else{
                    assert i != rows-1;  // black pawn cannot start on opponent side
                    bPawns.add(i + "" + j);
                }
            }
        }
        assert wPawns.size() <= cols;
        assert bPawns.size() <= cols;
    }

    public int solveBoard(){
        return 0;
    }

    public int solveBoard(HashSet<String> wPawns, HashSet<String> bPawns){
        return 0;
    }

    private ArrayList<int[]> generateMoves(HashSet<String> wPawns, HashSet<String> bPawns){
        ArrayList<int[]> moves = new ArrayList<>();
        int row, col;
        HashSet<String> onMovePawns;

        if(currentTurn == 'W')
            onMovePawns = wPawns;
        else
            onMovePawns = bPawns;

        for(String pos : onMovePawns){
            row = Character.getNumericValue(pos.charAt(0));
            col = Character.getNumericValue(pos.charAt(1));

            if(currentTurn == 'W'){
                if(!wPawns.contains((row-1) + "" + col)){
                    moves.add(new int[]{row, col, row-1, col});
                }
                if(bPawns.contains((row-1) + "" + (col-1))){
                    moves.add(new int[]{row, col, row-1, col-1});
                }
                if(bPawns.contains((row-1) + "" + (col+1))){
                    moves.add(new int[]{row, col, row-1, col-1});
                }
            }
            else{
                if(!wPawns.contains((row+1) + "" + col)){
                    moves.add(new int[]{row, col, row-1, col});
                }
                if(wPawns.contains((row+1) + "" + (col-1))){
                    moves.add(new int[]{row, col, row-1, col-1});
                }
                if(wPawns.contains((row+1) + "" + (col+1))){
                    moves.add(new int[]{row, col, row-1, col-1});
                }
            }
        }
        return moves;
    }

    private void printBoard(char[][] boardToPrint){
        for(char[] row : boardToPrint){
            System.out.println(row);
        }
    }
}
