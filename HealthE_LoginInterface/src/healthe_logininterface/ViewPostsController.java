package healthe_logininterface;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.Stage;

public class ViewPostsController implements Initializable {

    @FXML private TableView<Post> postsTable;
    @FXML private TableColumn<Post, String> emailCol, titleCol, descCol, replyCol;

    private ObservableList<Post> postList = FXCollections.observableArrayList();
    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        replyCol.setCellValueFactory(cellData -> cellData.getValue().replyProperty());

        descCol.setCellFactory(tc -> wrapTextCell());
        replyCol.setCellFactory(tc -> wrapTextCell());

        postsTable.setFixedCellSize(-1); // enable auto row height
        postsTable.setRowFactory(tv -> {
            TableRow<Post> row = new TableRow<>() {
                @Override
                protected void updateItem(Post post, boolean empty) {
                    super.updateItem(post, empty);
                    if (post != null && !empty) {
                        setPrefHeight(Control.USE_COMPUTED_SIZE);
                    }
                }
            };
            return row;
        });

        loadPostsFromDatabase();
    }

    private TableCell<Post, String> wrapTextCell() {
        return new TableCell<>() {
            private final Label label = new Label();

            {
                label.setWrapText(true);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setStyle("-fx-padding: 5px;");
                setGraphic(label);
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.setText(empty || item == null ? null : item);
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
                    rs.getString("reply")
                ));
            }

            postsTable.setItems(postList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserData("User", userEmail);

            Stage stage = (Stage) postsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editSelectedPost() {
        Post selected = postsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a post to edit.");
            return;
        }

        if (!selected.getEmail().equals(userEmail)) {
            showAlert(Alert.AlertType.ERROR, "You can only edit your own post.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getDescription());
        dialog.setTitle("Edit Post");
        dialog.setHeaderText("Editing: " + selected.getTitle());
        dialog.setContentText("New Description:");

        dialog.showAndWait().ifPresent(newDesc -> {
            try (Connection conn = new DatabaseConnection().getConnection()) {
                String sql = "UPDATE posts SET description = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newDesc);
                stmt.setInt(2, selected.getId());
                stmt.executeUpdate();

                selected.setDescription(newDesc);
                postsTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "✅ Post updated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Failed to update.");
            }
        });
    }

    @FXML
    private void deleteSelectedPost() {
        Post selected = postsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a post to delete.");
            return;
        }

        if (!selected.getEmail().equals(userEmail)) {
            showAlert(Alert.AlertType.ERROR, "You can only delete your own post.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this post?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = new DatabaseConnection().getConnection()) {
                    String sql = "DELETE FROM posts WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();

                    postList.remove(selected);
                    showAlert(Alert.AlertType.INFORMATION, "🗑 Post deleted.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error deleting.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
