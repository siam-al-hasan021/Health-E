package healthe_logininterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;

    private String loggedInName;

    public void setLoggedInName(String name) {
        this.loggedInName = name;
        welcomeLabel.setText("Welcome, " + name + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // name is set dynamically
    }

    @FXML
    private void handleLogout() {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
