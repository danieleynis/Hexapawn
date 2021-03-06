/*
Author: Daniel Eynis
E-mail: eynis@pdx.edu

Online resources consulted:
https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
http://stackoverflow.com/questions/13942701/take-a-char-input-from-the-scanner
http://stackoverflow.com/questions/17606839/creating-a-set-of-arrays-in-java
http://stackoverflow.com/questions/19388037/converting-characters-to-integers-in-java
http://stackoverflow.com/questions/12940663/does-adding-a-duplicate-value-to-a-hashset-hashmap-replace-the-previous-value
http://stackoverflow.com/questions/16784347/transposition-table-for-game-tree-connect-4
 */

import java.util.*;

public class Hexapawn {
    private char currentTurn; // keeps track of the current player turn 'W' or 'B'
    private int cols, rows;
    private HashSet<String> wPawns = new HashSet<>(); // hash set of all white pawns
    private HashSet<String> bPawns = new HashSet<>(); // hash set of all black pawns
    private HashMap<Long, Integer> transTable = new HashMap<>();
    private HashMap<String, Long> wZobrist = new HashMap<>();
    private HashMap<String, Long> bZobrist = new HashMap<>();
    private long wPlayerZob, bPlayerZob;

    private int nodesExplored = 0;

    Hexapawn() {
        Scanner input = new Scanner(System.in);
        String curTurn = input.nextLine();
        if(!curTurn.isEmpty())
            currentTurn = curTurn.charAt(0);
        assert currentTurn == 'W' || currentTurn == 'B';

        ArrayList<String> inputLines = new ArrayList<>();  // read in input lines into an array list

        String lineRead;
        while(input.hasNextLine()) {
            lineRead = input.nextLine();
            if(lineRead.isEmpty())  // one encounter empty line it is end of input, used for console input
                break;
            inputLines.add(lineRead);
            ++rows;
        }

        assert inputLines.size() != 0 && rows != 0 && inputLines.size() == rows;

        char[] pieceArray;
        cols = inputLines.get(0).length();  // get the number of columns
        for (int i = 0; i < inputLines.size(); ++i){
            pieceArray = inputLines.get(i).toCharArray();
            assert pieceArray.length == cols;

            for (int j = 0; j < pieceArray.length; ++j){
                char curPos = pieceArray[j];
                assert curPos == '.' || curPos == 'P' || curPos == 'p';

                if(curPos == '.')
                    continue;
                if(curPos == 'P'){
                    assert i != 0; // white pawn cannot start on opponent side
                    wPawns.add(i + "" + j);  // add pawn position in format "rowcol" to hash set
                }
                else{
                    assert i != rows-1;  // black pawn cannot start on opponent side
                    bPawns.add(i + "" + j);
                }
            }
        }
        assert wPawns.size() <= cols;
        assert bPawns.size() <= cols;

        Random random = new Random();
        wPlayerZob = random.nextLong();
        bPlayerZob = random.nextLong();
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                wZobrist.put(i + "" + j, random.nextLong());
                bZobrist.put(i + "" + j, random.nextLong());
            }
        }
    }

    public int solveBoard(){
        int nValue = solveBoard(wPawns, bPawns);
		System.out.println("Nodes explored: " + nodesExplored);
        return nValue;
    }

    private int solveBoard(HashSet<String> wPawns, HashSet<String> bPawns){

        long zobKey = getZobristKey(wPawns, bPawns);
        if(transTable.containsKey(zobKey)){
            return transTable.get(zobKey);
        }

        ArrayList<int[]> moves = generateMoves(wPawns, bPawns);  // get list of moves

        ++nodesExplored;

        if(moves == null || moves.size() == 0) {  // if no moves left, signifies a loss for side on move
            transTable.put(zobKey, -1);
            return -1;
        }

        int max = -1;
        int val;
        for(int[] move : moves){
            HashSet<String> copyPawnsW = new HashSet<>(wPawns);  // create copy of white and black pawn hash sets
            HashSet<String> copyPawnsB = new HashSet<>(bPawns);

            boolean win = executeMove(copyPawnsW, copyPawnsB, move);  // execute move on copies
            long moveZobKey = getZobristKey(copyPawnsW, copyPawnsB);

            if(win) { // if execute move function returns true it is a win, return 1 no use in finding other wins
                transTable.put(moveZobKey, 1);
                return 1;
            }

            currentTurn = (currentTurn == 'W' ? 'B' : 'W');  // flip the current turn before recursive call
            val = - solveBoard(copyPawnsW, copyPawnsB);  // negate the return value of the recursive call (negamax)
            currentTurn = (currentTurn == 'W' ? 'B' : 'W');  // flip back on recursive return

            max = Math.max(max, val);
        }

        transTable.put(zobKey, max);
        return max;
    }

    /*
       This function will take hash sets of white and black pawns and an int array of size 4 of a move which
       gives the starting position row and column and the position to move to row and column. This function will
       modify the hash sets directly as it assumes copies are saved if necessary.
     */
    private boolean executeMove(HashSet<String> wPawns, HashSet<String> bPawns, int[] move){
        if(move[2] == 0 || move[2] == rows-1)  // if there is a winning move (pawn reached end) return true
            return true;

        HashSet<String> onMovePawns;
        HashSet<String> waitingPawns;

        if(currentTurn == 'W') {  // figure out who is on move and who is waiting
            onMovePawns = wPawns;
            waitingPawns = bPawns;
        }
        else {
            onMovePawns = bPawns;
            waitingPawns = wPawns;
        }

        String startLoc = move[0] + "" + move[1];
        String endLoc = move[2] + "" + move[3];

        if(waitingPawns.contains(endLoc)){  // if the position to move to has an opponent piece remove it
            waitingPawns.remove(endLoc);
        }

        onMovePawns.remove(startLoc);  // remove the old position for on move side
        onMovePawns.add(endLoc);  // add the new position for on move side

        return false;  // return no win
    }

    /*
       This function will generate a list of all possible moves for the side that is on move. An ArrayList of
       int arrays will be returned specifying the starting position coordinated and the coordinates of position
       to move to.
     */
    private ArrayList<int[]> generateMoves(HashSet<String> wPawns, HashSet<String> bPawns){
        ArrayList<int[]> moves = new ArrayList<>();
        int row, col;
        HashSet<String> onMovePawns;

        if(currentTurn == 'W')
            onMovePawns = wPawns;
        else
            onMovePawns = bPawns;

        for(String pos : onMovePawns){  // for each pawn for the side on move
            row = Character.getNumericValue(pos.charAt(0));  // get the pawn exact coordinates
            col = Character.getNumericValue(pos.charAt(1));

            if(currentTurn == 'W'){
                if(!bPawns.contains((row-1) + "" + col)){  // if the opposing side is not blocking forward move
                    // add move to beginning of ArrayList as checking moves forward speeds up the potential of finding
                    // a winning move
                    moves.add(0, new int[]{row, col, row-1, col});
                }
                if(bPawns.contains((row-1) + "" + (col-1))){  // if there is an opponent pawn across from the pawn
                    moves.add(new int[]{row, col, row-1, col-1});  // add a move to attack that pawn
                }
                if(bPawns.contains((row-1) + "" + (col+1))){
                    moves.add(new int[]{row, col, row-1, col+1});
                }
            }
            else{
                if(!wPawns.contains((row+1) + "" + col)){
                    moves.add(0, new int[]{row, col, row+1, col});
                }
                if(wPawns.contains((row+1) + "" + (col-1))){
                    moves.add(new int[]{row, col, row+1, col-1});
                }
                if(wPawns.contains((row+1) + "" + (col+1))){
                    moves.add(new int[]{row, col, row+1, col+1});
                }
            }
        }
        return moves;  // return the list of moves
    }

    long getZobristKey(HashSet<String> wPawns, HashSet<String> bPawns){
        long zobristKey = 0;

        if(currentTurn == 'W')
            zobristKey ^= wPlayerZob;
        else
            zobristKey ^= bPlayerZob;

        for(String wPos : wPawns)
            zobristKey ^= wZobrist.get(wPos);

        for(String bPos: bPawns)
            zobristKey ^= bZobrist.get(bPos);

        return zobristKey;
    }
}
