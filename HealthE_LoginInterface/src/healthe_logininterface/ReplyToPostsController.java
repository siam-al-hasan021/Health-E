package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class ReplyToPostsController implements Initializable {

    @FXML
    private TableView<Post> postsTable;

    @FXML
    private TableColumn<Post, String> emailCol;

    @FXML
    private TableColumn<Post, String> titleCol;

    @FXML
    private TableColumn<Post, String> descCol;

    @FXML
    private TextArea replyField;

    private String doctorEmail;

    private ObservableList<Post> postList = FXCollections.observableArrayList();

    public void setDoctorEmail(String email) {
        this.doctorEmail = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());

        loadPostsFromDatabase();
    }

    private void loadPostsFromDatabase() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT id, user_email, title, description FROM posts";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("id");
                String email = result.getString("user_email");
                String title = result.getString("title");
                String description = result.getString("description");

               postList.add(new Post(id, email, title, description, ""));

            }

            postsTable.setItems(postList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submitReply() {
        Post selectedPost = postsTable.getSelectionModel().getSelectedItem();
        String replyText = replyField.getText();

        if (selectedPost == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a post to reply.");
            return;
        }

        if (replyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please write a reply before submitting.");
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertQuery = "INSERT INTO replies (post_id, doctor_email, reply_text) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertQuery);
            statement.setInt(1, selectedPost.getId());
            statement.setString(2, doctorEmail);
            statement.setString(3, replyText);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "✅ Reply submitted successfully!");
                replyField.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error submitting reply: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Reply");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
