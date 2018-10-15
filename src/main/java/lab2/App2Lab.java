package lab2;

import common.App;
import javafx.stage.Stage;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 08.10.2018
 */
public class App2Lab extends App {

    @Override
    public void init() {
        fxmlName = "lab2.fxml";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("2 Лабораторная работа");
        primaryStage.show();
    }

    public void show(String... args) {
        launch(args);
    }
}
