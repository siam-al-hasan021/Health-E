package healthe_logininterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class HomeController {

    @FXML
    private void goToUserLogin(ActionEvent event) {
        loadScene("FXMLDocument.fxml", event);
    }

    @FXML
    private void goToDoctorLogin(ActionEvent event) {
        loadScene("DoctorLogin.fxml", event);
    }

    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Health-E");
            stage.show();

            // Close current window
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
