package healthe_logininterface;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PostProblemController implements Initializable {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button submitButton;

    @FXML
    private Hyperlink goBack;

    // You can replace this with actual logged-in user's email
    private String userEmail = "r@gmail.com"; // ✅ Later make dynamic!

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Nothing needed for now
    }

    @FXML
    private void submitProblem() {
        String title = titleField.getText();
        String description = descriptionField.getText();

        if (title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in both fields.");
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertQuery = "INSERT INTO posts (user_email, title, description) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertQuery);
            statement.setString(1, userEmail);
            statement.setString(2, title);
            statement.setString(3, description);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "✅ Your problem has been posted.");
                titleField.clear();
                descriptionField.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) goBack.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Post Problem");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
