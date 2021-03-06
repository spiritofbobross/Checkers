package model;

import solver.Configuration;
import solver.Solver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CheckersModel {
    private final List<Observer<CheckersModel, CheckersClientData>> observers = new LinkedList<>();
    private CheckersConfig currentConfig;
    private final CheckersClientData data;
    /** if this is true, it will lock operations until
     *  the puzzle is reset or a move is undone */
    private boolean configSolved;
    /** determines if a select move was already made */
    private boolean onePosIn;
    /** temp variables for the two selected positions */
    private final int[] pos1, pos2;
    private final ArrayList<CheckersConfig> previousMoves;
    private char turn;
    private final char INITIAL_TURN;
    private int chainCount;

    public CheckersModel(char turn) {
        this.currentConfig = new CheckersConfig();
        this.turn = turn;
        this.INITIAL_TURN = turn;
        this.configSolved = false;
        this.onePosIn = false;
        this.chainCount = 0;
        this.pos1 = new int[2];
        this.pos2 = new int[2];
        this.previousMoves = new ArrayList<>();
        previousMoves.add(new CheckersConfig(currentConfig));
        this.data = new CheckersClientData(null);
    }

    public void select(String[] currPos) {
        if (configSolved) {
            data.setMessage("Game won!");
            alertObservers(data);
        } else {
            if (Integer.parseInt(currPos[0]) < 0
                    || Integer.parseInt(currPos[0]) >= currentConfig.getDim()
                    || Integer.parseInt(currPos[1]) < 0
                    || Integer.parseInt(currPos[1]) >= currentConfig.getDim()) {
                data.setMessage("Invalid selection (" + currPos[0] + ", " + currPos[1] + ")");
            } else {
                if (!onePosIn) {
                    pos1[0] = Integer.parseInt(currPos[0]);
                    pos1[1] = Integer.parseInt(currPos[1]);
                    if (currentConfig.getBoard()[pos1[0]][pos1[1]].getName() == '-') {
                        data.setMessage("Invalid selection (" + currPos[0] + ", " + currPos[1] + ")");
                    } else if (turn == 'R' && currentConfig.getBoard()[pos1[0]][pos1[1]].getName() == 'B') {
                        data.setMessage("Not black turn");
                    } else if (turn == 'B' && currentConfig.getBoard()[pos1[0]][pos1[1]].getName() == 'R') {
                        data.setMessage("Not red turn");
                    } else if (chainCount != 0 && !Arrays.equals(pos1, pos2)) {
                        data.setMessage("In chain capture: must move same piece");
                    } else {
                        onePosIn = true;
                        data.setMessage("Selected (" + currPos[0] + ", " + currPos[1] + ")");
                    }
                } else {
                    pos2[0] = Integer.parseInt(currPos[0]);
                    pos2[1] = Integer.parseInt(currPos[1]);
                    boolean moveSuccessful =
                            currentConfig.move(pos1[0], pos1[1], pos2[0], pos2[1]);
                    if (!moveSuccessful) {
                        data.setMessage("Cannot jump from (" + pos1[0] + ", " + pos1[1]
                                + ") to (" + pos2[0] + ", " + pos2[1] + ")");
                    } else {
                        data.setMessage("Jumped from (" + pos1[0] + ", " + pos1[1]
                                + ") to (" + pos2[0] + ", " + pos2[1] + ")");
                        previousMoves.add(new CheckersConfig(currentConfig));
                        if (currentConfig.getBoard()[pos2[0]][pos2[1]].getName() == 'B' &&
                            pos2[0] == 0 && !currentConfig.getBoard()[pos2[0]][pos2[1]].isKing()) {
                            currentConfig.getBoard()[pos2[0]][pos2[1]].setKing();
                            data.setMessage("Piece at (" + pos2[0] + ", " +
                                            pos2[1] + ") has become king!");
                            alertObservers(data);
                        }
                        if (currentConfig.getBoard()[pos2[0]][pos2[1]].getName() == 'R' &&
                                pos2[0] == 7) {
                            currentConfig.getBoard()[pos2[0]][pos2[1]].setKing();
                            data.setMessage("Piece at (" + pos2[0] + ", " +
                                    pos2[1] + ") has become king!");
                            alertObservers(data);
                        }
                        if (!(Math.abs(pos2[0]-pos1[0]) == 2) || !possibleMove(pos2[0], pos2[1])) {
                            changeTurn();
                            chainCount = 0;
                        }
                        if (currentConfig.isSolution()) {
                            configSolved = true;
                            if (turn == 'R') data.setMessage("Black wins!");
                            else data.setMessage("Red wins!");
                            alertObservers(data);
                        }
                    }
                    onePosIn = false;
                }
            }
            alertObservers(data);
            System.out.println(currentConfig.toString());
        }
    }

    public void hint() {
        Solver solver = new Solver();
        List<Configuration> path = solver.solve(currentConfig);
        if (path == null) {
            data.setMessage("No solution");
            alertObservers(data);
        } else if (path.size() == 1) {
            if (currentConfig.isSolution()) {
                configSolved = true;
                data.setMessage("Game won!");
                alertObservers(data);
            }
        } else {
            this.currentConfig = (CheckersConfig) path.get(1);
            previousMoves.add(currentConfig);
            data.setMessage("Next step!");
            alertObservers(data);
            changeTurn();
            System.out.println(currentConfig.toString());
            if (currentConfig.isSolution()) {
                configSolved = true;
                if (turn == 'R') data.setMessage("Black wins!");
                else data.setMessage("Red wins!");
                alertObservers(data);
            }
        }
    }

    public void undo() {
        if (previousMoves.size() == 1) {
            data.setMessage("Can't undo");
        } else {
            this.currentConfig = new CheckersConfig(previousMoves.get(previousMoves.size()-2));
            previousMoves.remove(previousMoves.size()-1);
            if (configSolved) configSolved = false;
            if (chainCount != 0) chainCount--;
            else changeTurn();
            System.out.println(this.currentConfig.toString());
        }
        alertObservers(data);
    }

    public void reset() {
        this.currentConfig = new CheckersConfig();
        this.turn = INITIAL_TURN;
        configSolved = false;
        this.chainCount = 0;
        this.previousMoves.clear();
        data.setMessage("Puzzle reset!");
        alertObservers(data);
        System.out.println(this);
    }

    private boolean possibleMove(int row, int col) {
        if (currentConfig.isValidMove(row, col, row+2, col+2)) {
            chainCount++;
            return true;
        }
        else if (currentConfig.isValidMove(row, col, row+2, col-2)) {
            chainCount++;
            return true;
        }
        else if (currentConfig.isValidMove(row, col, row-2, col+2)) {
            chainCount++;
            return true;
        }
        else if (currentConfig.isValidMove(row, col, row-2, col-2)) {
            chainCount++;
            return true;
        } else return false;
    }

    private void changeTurn() {
        if (turn == 'B') turn = 'R';
        else turn = 'B';
        data.setMessage("Next turn (" + turn + ")");
    }

    public boolean isSolved() { return configSolved; }


    public void addObserver(Observer<CheckersModel, CheckersClientData> observer) { this.observers.add(observer); }

    public int getDim() { return currentConfig.getDim(); }

    private void alertObservers(CheckersClientData data) {
        for (var observer : observers)
            observer.update(this, data);
    }

    @Override
    public String toString() { return currentConfig.toString(); }

    public Checker[][] getBoard() { return currentConfig.getBoard(); }
}
