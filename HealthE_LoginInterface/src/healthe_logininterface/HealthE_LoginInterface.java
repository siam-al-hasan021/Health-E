package healthe_logininterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HealthE_LoginInterface extends Application {

@Override
public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/healthe_logininterface/FXMLDocument.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    stage.setTitle("Health-E Login");
    stage.setScene(scene);
    stage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}