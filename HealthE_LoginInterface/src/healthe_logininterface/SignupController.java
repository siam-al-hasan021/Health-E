package healthe_logininterface;

import java.net.URL;
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
        // You can populate ComboBox here if not using Scene Builder's items
    }

    @FXML
    private void handleSignup() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String age = ageField.getText();
        String gender = genderBox.getValue();

        System.out.println("Signed up: " + name + " | " + email + " | " + password + " | " + age + " | " + gender);
        
        // TODO: Save to MongoDB or MySQL
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
