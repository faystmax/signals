import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import util.SignalsGeneratorUtil;

import java.util.Objects;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SignalsGeneratorUtil.generate();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/main.fxml")));
        Parent root = loader.load();
        ((MainController) loader.getController()).setStage(primaryStage);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icons/icon.png")));
        primaryStage.setTitle("1 Лабораторная работа");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
