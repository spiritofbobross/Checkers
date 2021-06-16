package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import model.CheckersClientData;
import model.CheckersModel;
import model.Observer;

public class CheckersGUI extends Application implements Observer<CheckersModel, CheckersClientData> {
    public static void main(String[] args) {
        System.out.println("Hello");
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void update(CheckersModel checkersModel, CheckersClientData checkersClientData) {

    }
}
