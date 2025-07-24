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
    private TableColumn<Post, String> replyCol;

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
        replyCol.setCellValueFactory(data -> data.getValue().replyProperty());

        loadPostsFromDatabase();
    }

    private void loadPostsFromDatabase() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT id, user_email, title, description, reply FROM posts";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("id");
                String email = result.getString("user_email");
                String title = result.getString("title");
                String description = result.getString("description");
                String reply = result.getString("reply");

                postList.add(new Post(id, email, title, description, reply != null ? reply : ""));
            }

            postsTable.setItems(postList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submitReply() {
        Post selectedPost = postsTable.getSelectionModel().getSelectedItem();
        String replyText = replyField.getText().trim();

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

        // Save in `replies` table
        String insertReply = "INSERT INTO replies (post_id, doctor_email, reply_text) VALUES (?, ?, ?)";

        // Also update `posts` table so reply appears in all views
        String updatePost = "UPDATE posts SET reply = ? WHERE id = ?";

        try {
            // Insert reply to `replies` table
            PreparedStatement insertStmt = connectDB.prepareStatement(insertReply);
            insertStmt.setInt(1, selectedPost.getId());
            insertStmt.setString(2, doctorEmail);
            insertStmt.setString(3, replyText);
            insertStmt.executeUpdate();

            // Update reply in `posts` table
            PreparedStatement updateStmt = connectDB.prepareStatement(updatePost);
            updateStmt.setString(1, replyText);
            updateStmt.setInt(2, selectedPost.getId());
            updateStmt.executeUpdate();

            // Update UI
            selectedPost.setReply(replyText);
            postsTable.refresh();
            replyField.clear();

            showAlert(Alert.AlertType.INFORMATION, "✅ Reply submitted successfully!");

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
