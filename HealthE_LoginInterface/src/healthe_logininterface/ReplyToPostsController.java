package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReplyToPostsController implements Initializable {

    @FXML private TableView<Post> postsTable;
    @FXML private TableColumn<Post, String> emailCol, titleCol, descCol, replyCol;
    @FXML private TextArea replyField;

    private String doctorEmail;
    private ObservableList<Post> postList = FXCollections.observableArrayList();

    public void setDoctorEmail(String email) {
        this.doctorEmail = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        postsTable.setFixedCellSize(-1); // allow dynamic row height

        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        replyCol.setCellValueFactory(data -> data.getValue().replyProperty());

        descCol.setCellFactory(column -> createWrappedCell(380));
        replyCol.setCellFactory(column -> createWrappedCell(360));

        loadPostsFromDatabase();
    }

    private TableCell<Post, String> createWrappedCell(double maxWidth) {
        return new TableCell<>() {
            private final Label label = new Label();

            {
                label.setWrapText(true);
                label.setMaxWidth(maxWidth);
                label.setStyle("-fx-padding: 5px;");
                setGraphic(label);
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.setText(empty || item == null ? null : item);
                this.setPrefHeight(Control.USE_COMPUTED_SIZE);
            }
        };
    }

    private void loadPostsFromDatabase() {
        postList.clear();
        try (Connection conn = new DatabaseConnection().getConnection()) {
            String query = "SELECT id, user_email, title, description, reply FROM posts ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                postList.add(new Post(
                        rs.getInt("id"),
                        rs.getString("user_email"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("reply") != null ? rs.getString("reply") : ""
                ));
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
            showAlert(Alert.AlertType.WARNING, "Reply cannot be empty.");
            return;
        }

        try (Connection conn = new DatabaseConnection().getConnection()) {
            String insert = "INSERT INTO replies (post_id, doctor_email, reply_text) VALUES (?, ?, ?)";
            PreparedStatement stmt1 = conn.prepareStatement(insert);
            stmt1.setInt(1, selectedPost.getId());
            stmt1.setString(2, doctorEmail);
            stmt1.setString(3, replyText);
            stmt1.executeUpdate();

            String update = "UPDATE posts SET reply = ? WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(update);
            stmt2.setString(1, replyText);
            stmt2.setInt(2, selectedPost.getId());
            stmt2.executeUpdate();

            selectedPost.setReply(replyText);
            postsTable.refresh();
            replyField.clear();

            showAlert(Alert.AlertType.INFORMATION, "✅ Reply submitted.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error submitting reply.");
        }
    }

    @FXML
    private void editReply() {
        Post post = postsTable.getSelectionModel().getSelectedItem();
        if (post == null || post.getReply().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Select a post with a reply.");
            return;
        }

        try (Connection conn = new DatabaseConnection().getConnection()) {
            String check = "SELECT doctor_email FROM replies WHERE post_id = ?";
            PreparedStatement stmt = conn.prepareStatement(check);
            stmt.setInt(1, post.getId());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next() || !rs.getString("doctor_email").equals(doctorEmail)) {
                showAlert(Alert.AlertType.ERROR, "You can only edit your own reply.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(post.getReply());
        dialog.setTitle("Edit Reply");
        dialog.setHeaderText("Editing reply for: " + post.getTitle());
        dialog.setContentText("New Reply:");
        dialog.showAndWait().ifPresent(newReply -> {
            try (Connection conn = new DatabaseConnection().getConnection()) {
                String updateReply = "UPDATE replies SET reply_text = ? WHERE post_id = ? AND doctor_email = ?";
                PreparedStatement stmt1 = conn.prepareStatement(updateReply);
                stmt1.setString(1, newReply);
                stmt1.setInt(2, post.getId());
                stmt1.setString(3, doctorEmail);
                stmt1.executeUpdate();

                String updatePost = "UPDATE posts SET reply = ? WHERE id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(updatePost);
                stmt2.setString(1, newReply);
                stmt2.setInt(2, post.getId());
                stmt2.executeUpdate();

                post.setReply(newReply);
                postsTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "✅ Reply updated.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void deleteReply() {
        Post post = postsTable.getSelectionModel().getSelectedItem();
        if (post == null || post.getReply().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Select a post with a reply.");
            return;
        }

        try (Connection conn = new DatabaseConnection().getConnection()) {
            String check = "SELECT doctor_email FROM replies WHERE post_id = ?";
            PreparedStatement stmt = conn.prepareStatement(check);
            stmt.setInt(1, post.getId());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next() || !rs.getString("doctor_email").equals(doctorEmail)) {
                showAlert(Alert.AlertType.ERROR, "You can only delete your own reply.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this reply?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = new DatabaseConnection().getConnection()) {
                    String delete = "DELETE FROM replies WHERE post_id = ? AND doctor_email = ?";
                    PreparedStatement stmt1 = conn.prepareStatement(delete);
                    stmt1.setInt(1, post.getId());
                    stmt1.setString(2, doctorEmail);
                    stmt1.executeUpdate();

                    String update = "UPDATE posts SET reply = '' WHERE id = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(update);
                    stmt2.setInt(1, post.getId());
                    stmt2.executeUpdate();

                    post.setReply("");
                    postsTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "🗑 Reply deleted.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Reply");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
