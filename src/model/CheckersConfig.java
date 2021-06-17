package model;

import solver.Configuration;
import java.util.Collection;

public class CheckersConfig implements Configuration {
    private static final char RED = 'R';
    private static final char BLACK = 'B';
    private static final char EMPTY = '-';
    private static final int DIM = 8;
    int checkerRow, checkerCol;
    private Checker[][] board;

    public CheckersConfig() {
        board = new Checker[DIM][DIM];
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < DIM; j += 2) {
                    board[i][j] = new Checker(RED);
                    board[i][j-1] = new Checker(EMPTY);
                }
            } else {
                for (int j = 0; j < DIM; j += 2) {
                    board[i][j] = new Checker(RED);
                    board[i][j+1] = new Checker(EMPTY);
                }
            }
        }
        for (int i = 3; i < 5; i++) {
            for (int j = 0; j < DIM; j++) {
                board[i][j] = new Checker(EMPTY);
            }
        }
        for (int i = 5; i < DIM; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < DIM; j += 2) {
                    board[i][j] = new Checker(BLACK);
                    board[i][j-1] = new Checker(EMPTY);
                }
            } else {
                for (int j = 0; j < DIM; j += 2) {
                    board[i][j] = new Checker(BLACK);
                    board[i][j+1] = new Checker(EMPTY);
                }
            }
        }
    }

    @Override
    public boolean isSolution() {
        int numRed = 0;
        int numBlack = 0;

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (board[i][j].getName() == 'B')
                    numBlack++;
                if (board[i][j].getName() == 'R')
                    numRed++;
            }
        }
        return (numBlack == 0 && numRed != 0) || (numBlack != 0 && numRed == 0);
    }

    public boolean move(int currRow, int currCol,
                        int nextRow, int nextCol) {
        if (!isValidMove(currRow, currCol, nextRow, nextCol)) {
            return false;
        } else if (Math.abs(currRow-nextRow) != 1) {
            board[checkerRow][checkerCol] = new Checker(EMPTY);
            board[nextRow][nextCol] = board[currRow][currCol];
            board[currRow][currCol] = new Checker(EMPTY);
            return true;
        } else {
            board[nextRow][nextCol] = board[currRow][currCol];
            board[currRow][currCol] = new Checker(EMPTY);
            return true;
        }
    }

    public boolean isValidMove(int currRow, int currCol,
                               int nextRow, int nextCol) {
        // checks if piece to move is a checker
        if (board[currRow][currCol].getName() == EMPTY) {
            return false;
            // checks if position to move to is in bounds
        } else if (nextRow >= DIM || nextRow < 0
                || nextCol >= DIM || nextCol < 0) {
            return false;
            // checks that the position to move to is empty
        } else if (board[nextRow][nextCol].getName() != EMPTY) {
            return false;
        } else if (Math.abs(nextRow-currRow) == 2 &&
                   Math.abs(nextCol-currCol) == 2) {
            checkerRow = (nextRow + currRow) / 2;
            checkerCol = (nextCol + currCol) / 2;
            if (board[currRow][currCol].getName() == BLACK)
                return board[checkerRow][checkerCol].getName() == RED;
            else
                return board[checkerRow][checkerCol].getName() == BLACK;
        } else if (board[currRow][currCol].getName() == BLACK
                   && nextRow-currRow > 1) {
            return false;
        } else if (board[currRow][currCol].getName() == RED
                && nextRow-currRow < 1) {
            return false;
        } else {
            return Math.abs(nextRow - currRow) == 1 && Math.abs(nextCol - currCol) == 1;
        }
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("  ");
        for (int i = 0; i < DIM; i++) {
            result.append(i).append(" ");
        }
        result.append("\n");
        for (int i = 0; i < DIM; i++) {
            result.append(i).append(" ");
            for (int j = 0; j < DIM; j++) {
                result.append(board[i][j].getName()).append(" ");
            }
            result.append("\n");
        }

        return result.toString();
    }
}
