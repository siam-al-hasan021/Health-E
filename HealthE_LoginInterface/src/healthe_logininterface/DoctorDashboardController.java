package healthe_logininterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DoctorDashboardController implements Initializable {

    @FXML
    private Label doctorLabel;

    @FXML
    private Button viewPostsButton;

    private String doctorName;
    private String doctorEmail;

    public void setDoctorData(String name, String email) {
        this.doctorName = name;
        this.doctorEmail = email;

        if (doctorLabel != null && name != null) {
            doctorLabel.setText("Welcome, Dr. " + name + "!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // You may also test setting the label here (optional)
    }

    @FXML
    private void goToReplyPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReplyToPosts.fxml"));
            Parent root = loader.load();

            ReplyToPostsController controller = loader.getController();
            controller.setDoctorEmail(doctorEmail); // Pass email

            Stage stage = (Stage) viewPostsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Failed to load ReplyToPosts.fxml");
        }
    }
}
