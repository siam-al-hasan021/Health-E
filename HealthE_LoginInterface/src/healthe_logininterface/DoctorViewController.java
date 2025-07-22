package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class DoctorViewController implements Initializable {

    @FXML
    private TableView<Post> postTable;

    @FXML
    private TableColumn<Post, String> emailCol, titleCol, descCol, replyCol;

    @FXML
    private TextArea replyField;

    private ObservableList<Post> postList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        replyCol.setCellValueFactory(data -> data.getValue().replyProperty());

        loadPosts();
    }

    private void loadPosts() {
        postList.clear();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT * FROM posts";

        try {
            Statement stmt = connectDB.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                postList.add(new Post(
                    rs.getInt("id"),
                    rs.getString("user_email"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("reply")
                ));
            }

            postTable.setItems(postList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendReply() {
        Post selectedPost = postTable.getSelectionModel().getSelectedItem();
        String reply = replyField.getText();

        if (selectedPost == null || reply.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a post and write a reply.");
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String updateQuery = "UPDATE posts SET reply = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connectDB.prepareStatement(updateQuery);
            stmt.setString(1, reply);
            stmt.setInt(2, selectedPost.getId());
            stmt.executeUpdate();

            selectedPost.setReply(reply);
            postTable.refresh();
            replyField.clear();

            showAlert(Alert.AlertType.INFORMATION, "Reply submitted!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
