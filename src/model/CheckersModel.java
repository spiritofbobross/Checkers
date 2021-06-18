package model;

import solver.Configuration;
import solver.Solver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CheckersModel {
    private final List<Observer<CheckersModel, CheckersClientData>> observers = new LinkedList<>();
    private CheckersConfig currentConfig;
    private CheckersClientData data;
    /** if this is true, it will lock operations until
     *  the puzzle is reset or a new file is loaded */
    private boolean configSolved;
    /** determines if a select move was already made */
    private boolean onePosIn;
    /** temp variables for the two selected positions */
    private int[] pos1, pos2;
    private List<CheckersConfig> previousMoves;

    public CheckersModel() {
        this.currentConfig = new CheckersConfig();
        this.configSolved = false;
        this.onePosIn = false;
        this.pos1 = new int[2];
        this.pos2 = new int[2];
        this.previousMoves = new LinkedList<>();
        this.data = new CheckersClientData(null);
    }

    public void select(String[] currPos) {
        if (configSolved) {
            data.setMessage("Already solved!");
            alertObservers(data);
        } else {
            if (Integer.parseInt(currPos[0]) < 0
                    || Integer.parseInt(currPos[0]) >= currentConfig.getDim()
                    || Integer.parseInt(currPos[1]) < 0
                    || Integer.parseInt(currPos[1]) >= currentConfig.getDim()
                    || currentConfig.getBoard()[Integer.parseInt(currPos[0])][Integer.parseInt(currPos[1])]
                       .getName() == '-') {
                data.setMessage("Invalid selection (" + currPos[0] + ", " + currPos[1] + ")");
                alertObservers(data);
            } else {
                if (!onePosIn) {
                    pos1[0] = Integer.parseInt(currPos[0]);
                    pos1[1] = Integer.parseInt(currPos[1]);
                    if (currentConfig.getBoard()[pos1[0]][pos1[1]].getName() == '-') {
                        data.setMessage("Invalid selection (" + currPos[0] + ", " + currPos[1] + ")");
                    } else {
                        onePosIn = true;
                        data.setMessage("Selected (" + currPos[0] + ", " + currPos[1] + ")");
                    }
                    alertObservers(data);
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
                    }
                    onePosIn = false;
                    alertObservers(data);
                    if (currentConfig.isSolution()) {
                        configSolved = true;
                        data.setMessage("Puzzle solved!");
                        alertObservers(data);
                    }
                }
            }
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
                data.setMessage("Already solved!");
                alertObservers(data);
            }
        } else {
            this.currentConfig = (CheckersConfig) path.get(1);
            data.setMessage("Next step!");
            alertObservers(data);
            System.out.println(currentConfig.toString());
            if (currentConfig.isSolution()) {
                configSolved = true;
                data.setMessage("Puzzle solved!");
                alertObservers(data);
            }
        }
    }

    public void undo() {
        if (previousMoves.size() == 0) {
            System.out.println("Can't undo");
        } else {
            previousMoves.remove(previousMoves.size()-1);
            this.currentConfig = new CheckersConfig(previousMoves.get(previousMoves.size()-2));
        }
    }

    public void reset() {
        this.currentConfig = new CheckersConfig();
        configSolved = false;
        data.setMessage("Puzzle reset!");
        alertObservers(data);
        System.out.println(this);
    }

    public boolean isSolved() { return configSolved; }

    public int getDim() { return currentConfig.getDim(); }

    public Checker[][] getBoard() { return currentConfig.getBoard(); }

    public void addObserver(Observer<CheckersModel, CheckersClientData> observer) { this.observers.add(observer); }

    private void alertObservers(CheckersClientData data) {
        for (var observer : observers)
            observer.update(this, data);
    }

    @Override
    public String toString() { return currentConfig.toString(); }
}
