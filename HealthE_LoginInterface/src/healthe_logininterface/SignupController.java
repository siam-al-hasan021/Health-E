package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.collections.FXCollections;

public class SignupController implements Initializable {

    @FXML private TextField nameField, emailField, ageField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> genderBox;
    @FXML private Button submitButton;
    @FXML private Hyperlink loginLink;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
    }

    @FXML
    private void handleSignup() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String age = ageField.getText();
        String gender = genderBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty() || gender == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields.");
            return;
        }

        try (Connection conn = new DatabaseConnection().getConnection()) {
            String sql = "INSERT INTO users (name, email, password, age, gender) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setInt(4, Integer.parseInt(age));
            stmt.setString(5, gender);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "🎉 Account created successfully!");
            goToLogin();
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert(Alert.AlertType.ERROR, "Email already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Signup failed.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Signup");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
