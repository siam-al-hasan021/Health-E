package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class PostProblemController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private Button submitButton;

    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void submitProblem() {
        String title = titleField.getText();
        String description = descriptionField.getText();

        if (title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        if (userEmail == null || userEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "User email is missing. Please log in again.");
            return;
        }

        try (Connection conn = new DatabaseConnection().getConnection()) {
            String sql = "INSERT INTO posts (user_email, title, description) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userEmail);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "✅ Problem posted successfully.");
            titleField.clear();
            descriptionField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error posting problem.");
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserData("User", userEmail);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Post Problem");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
