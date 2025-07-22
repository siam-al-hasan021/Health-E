package healthe_logininterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button postProblemButton;

    private String userName;
    private String userEmail;

    public void setUserData(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        welcomeLabel.setText("Welcome, " + name + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Nothing yet
    }

    @FXML
    private void goToPostProblem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PostProblem.fxml"));
            Parent root = loader.load();

            PostProblemController controller = loader.getController();
            controller.setUserEmail(userEmail);  // ✅ send email to post screen

            Stage stage = (Stage) postProblemButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
