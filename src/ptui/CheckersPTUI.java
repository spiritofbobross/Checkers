package ptui;

import model.CheckersClientData;
import model.CheckersModel;
import model.Observer;

public class CheckersPTUI implements Observer<CheckersModel, CheckersClientData> {
    public static void main(String[] args) {
        System.out.println("Hola");
    }

    @Override
    public void update(CheckersModel checkersModel, CheckersClientData checkersClientData) {
        System.out.println("Implement");
    }
}
