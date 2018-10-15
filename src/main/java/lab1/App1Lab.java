package lab1;

import common.App;
import javafx.stage.Stage;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 08.10.2018
 */
public class App1Lab extends App {

    @Override
    public void init() {
        fxmlName = "lab1.fxml";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("1 Лабораторная работа");
        primaryStage.show();
    }

    public void show(String... args) {
        launch(args);
    }
}
