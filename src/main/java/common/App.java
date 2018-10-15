package common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 08.10.2018
 */
public class App extends Application {

    protected String fxmlName;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/" + fxmlName)));
        Parent root = loader.load();
        ((BaseController) loader.getController()).setStage(primaryStage);

        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("icons/icon.png")));
    }
}
