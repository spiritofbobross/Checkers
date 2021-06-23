package ptui;

import model.*;
import java.util.Random;
import java.util.Scanner;

public class CheckersPTUI implements Observer<CheckersModel, CheckersClientData> {
    private CheckersModel model;

    public CheckersPTUI(char turn) { this.model = new CheckersModel(turn); }

    private void run() {
        CheckersClientData data = new CheckersClientData(null);
        Scanner in = new Scanner( System.in );
        System.out.println(model.toString());
        help();
        for ( ; ; ) {
            System.out.print("game command: ");
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if (words.length > 0) {
                if (words[0].startsWith("q")) {
                    break;
                    /*
                } else if (words[0].startsWith("h")) {
                    if (model.isSolved()) {
                        data.setMessage("Already solved!");
                    } else {
                        model.hint();
                    }
                     */
                } else if (words[0].startsWith("r")) {
                    model.reset();
                } else if (words[0].startsWith("s")) {
                    if (model.isSolved()) {
                        data.setMessage("Already solved!");
                    } else {
                        if (words.length < 3) {
                            System.out.println("Invalid argument");
                            System.out.println(model.toString());
                        } else {
                            String[] pos = new String[2];
                            pos[0] = words[1];
                            pos[1] = words[2];
                            model.select(pos);
                        }
                    }
                } else if (words[0].startsWith("u")) {
                    model.undo();
                } else if (words[0].startsWith("?")) {
                    help();
                } else {
                    System.out.println("ERROR: invalid command");
                    help();
                }
                update(model, data);
            }
        }
    }

    @Override
    public void update(CheckersModel model, CheckersClientData data) {
        if (data.getMessage() != null) {
            System.out.println(data.getMessage());
        }
    }

    public void help() {
        System.out.println();
        System.out.println("s(elect) r c\t-- select cell at r, c");
        System.out.println("u(ndo)\t-- undo current move");
        System.out.println("h(int)\t-- hint next move");
        System.out.println("q(uit)\t-- quit the game");
        System.out.println("r(eset)\t-- reset the current game");
        System.out.println("?\t-- show this menu");
    }

    public static void main(String[] args) {
        CheckersPTUI ptui;
        if (args.length != 1) System.out.println("Java usage: 1st move (R B N)");
        else if (args[0].equals("N")) {
            Random rand = new Random();
            int num = rand.nextInt() % 2;
            if (num == 0) ptui = new CheckersPTUI('R');
            else ptui = new CheckersPTUI('B');
            ptui.model.addObserver(ptui);
            ptui.run();
        } else if (args[0].equals("B") || args[0].equals("R")) {
            ptui = new CheckersPTUI(args[0].charAt(0));
            ptui.model.addObserver(ptui);
            ptui.run();
        }
        else System.out.println("Java usage: 1st move (R B N)");
    }
}
