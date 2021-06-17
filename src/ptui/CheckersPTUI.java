package ptui;

import model.*;

import java.util.Scanner;

public class CheckersPTUI implements Observer<CheckersModel, CheckersClientData> {
    private CheckersConfig checkersConfig;

    public CheckersPTUI() { this.checkersConfig = new CheckersConfig(); }

    public static void main(String[] args) {
        CheckersPTUI ptui = new CheckersPTUI();
        Scanner in = new Scanner(System.in);
        for ( ; ; ) {
            System.out.println(ptui.checkersConfig.toString());
            System.out.print("Enter move: ");
            String line = in.nextLine();
            if (line.startsWith("q")) {
                break;
            } else {
                String[] temp = line.split("\\s+");
                int[] nums = new int[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    nums[i] = Integer.parseInt(temp[i]);
                }
                if (ptui.checkersConfig.move(nums[0], nums[1], nums[2], nums[3])) {
                    System.out.println("Move successful!");
                } else {
                    System.out.println("Move failed!");
                }
            }
        }
    }

    @Override
    public void update(CheckersModel checkersModel, CheckersClientData checkersClientData) {
        System.out.println("Implement");
    }
}
