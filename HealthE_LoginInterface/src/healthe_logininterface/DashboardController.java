package healthe_logininterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Button postProblemButton, viewPostsButton, logoutButton;

    private String userName;
    private String userEmail;

    public void setUserData(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        welcomeLabel.setText("Welcome, " + name + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void goToPostProblem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PostProblem.fxml"));
            Parent root = loader.load();

            PostProblemController controller = loader.getController();
            controller.setUserEmail(userEmail);

            Stage stage = (Stage) postProblemButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToViewPosts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewPosts.fxml"));
            Parent root = loader.load();

            ViewPostsController controller = loader.getController();
            controller.setUserEmail(userEmail);

            Stage stage = (Stage) viewPostsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
