package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Checker;
import model.CheckersClientData;
import model.CheckersModel;
import model.Observer;
import java.util.Random;

public class CheckersGUI extends Application implements Observer<CheckersModel, CheckersClientData> {
    /** model that will execute all operations */
    private CheckersModel model;
    private GridPane grid;
    private BorderPane borderPane;
    private Label command;
    private Stage stage;

    // make sure these go through
    private final String RESOURCES_DIR = "resources/";
    private final Image black = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blackpiece.png"));
    private final Image blackKing = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blackking.png"));
    private final Image red = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"redpiece.png"));
    private final Image redKing = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"redking.png"));
    private final Image checkerboard = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"checkerboard.png"));

    public void init() {
        char turn = getParameters().getRaw().get(0).charAt(0);
        if (turn == 'N') {
            Random rand = new Random();
            int num = rand.nextInt() % 2;
            if (num == 0) this.model = new CheckersModel('R');
            else this.model = new CheckersModel('B');
            model.addObserver(this);
        } else if (turn == 'B' || turn == 'R') {
            this.model = new CheckersModel(turn);
            model.addObserver(this);

        } else {
            System.out.println("Usage: java CheckersGUI R B N");
        }
    }

    @Override
    public void start(Stage stage) {
        grid = makeButtons(model);
        borderPane = new BorderPane();
        Button hint = new Button();
        Button reset = new Button();
        Button undo = new Button();
        this.stage = stage;

        reset.setText("Reset");
        reset.addEventHandler(ActionEvent.ANY, (ActionEvent event) -> model.reset());
        undo.setText("Undo");
        undo.addEventHandler(ActionEvent.ANY, (ActionEvent event) -> model.undo());
        hint.setText("Hint");
        hint.addEventHandler(ActionEvent.ANY, (ActionEvent event) -> model.hint());
        HBox hBox = new HBox();
        hBox.getChildren().addAll(undo, reset/*, hint*/);
        hBox.setAlignment(Pos.CENTER);
        command = new Label();
        command.setAlignment(Pos.CENTER);

        borderPane.setCenter(grid);
        borderPane.setTop(command);
        borderPane.setBottom(hBox);

        Scene scene = new Scene(borderPane);
        stage.setTitle("Checkers GUI");
        stage.setScene(scene);
        stage.show();
    }

    public GridPane makeButtons(CheckersModel model) {
        Checker[][] board = model.getBoard();
        grid = new GridPane();

        for (int i = 0; i < model.getDim(); i++) {
            for (int j = 0; j < model.getDim();j++) {
                Button button = new Button();
                switch (board[i][j].getName()) {
                    case 'R' -> {
                        if (board[i][j].isKing()) button.setGraphic(new ImageView((redKing)));
                        else button.setGraphic(new ImageView(red));
                    }
                    case 'B' -> {
                        if (board[i][j].isKing()) button.setGraphic(new ImageView(blackKing));
                        else button.setGraphic(new ImageView(black));
                    }
                    case '-' -> button.setGraphic(new ImageView());
                }

                int ICON_SIZE = 75;
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                String[] temp = {String.valueOf(i),String.valueOf(j)};
                button.addEventHandler(ActionEvent.ANY, (ActionEvent event) -> model.select(temp));
                grid.add(button, j, i);
            }
        }
        return grid;
    }

    @Override
    public void update(CheckersModel checkersModel, CheckersClientData checkersClientData) {
        grid = makeButtons(checkersModel); // makes new grid
        borderPane.setCenter(grid); // displays new grid just created
        stage.sizeToScene();
        command.setText(checkersClientData.getMessage());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CheckersGUI 1st_turn");
        } else {
            Application.launch(args);
        }
    }
}
