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

public class SignupController implements Initializable {

    @FXML
    private TextField nameField, emailField, ageField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private Button submitButton;

    @FXML
    private Hyperlink loginLink;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void handleSignup() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String age = ageField.getText();
        String gender = genderBox.getValue();

        // Connect to DB
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertQuery = "INSERT INTO users (name, email, password, age, gender) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertQuery);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, Integer.parseInt(age));
            statement.setString(5, gender);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✅ User registered successfully!");
                showAlert("Success", "User registered successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Registration failed: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
}
