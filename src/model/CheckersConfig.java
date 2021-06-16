package model;

import solver.Configuration;
import java.util.Collection;

public class CheckersConfig implements Configuration {
    private static final char RED = 'R';
    private static final char BLACK = 'B';
    private static final char EMPTY = '-';
    private static final int DIM = 8;
    private Checker[][] board;

    public CheckersConfig() {
        board = new Checker[DIM][DIM];
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < DIM; j += 2) {
                    board[i][j] = new Checker(RED);
                }
            } else {
                for (int j = 0; j < DIM; j += 2) {
                    board[i][j] = new Checker(RED);
                }
            }
        }
        for (int i = 5; i < DIM; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < DIM; j += 2) {
                    board[i][j] = new Checker(BLACK);
                }
            } else {
                for (int j = 0; j < DIM; j += 2) {
                    board[i][j] = new Checker(BLACK);
                }
            }
        }
    }

    @Override
    public boolean isSolution() {
        return false;
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
                if (board[i][j] == null) {
                    result.append('-').append(" ");
                } else {
                    result.append(board[i][j].getName()).append(" ");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }
}
