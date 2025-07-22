package healthe_logininterface;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ViewPostsController implements Initializable {

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

    private ObservableList<Post> postList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        replyCol.setCellValueFactory(cellData -> cellData.getValue().replyProperty());

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

                postList.add(new Post(id, email, title, description, reply));
            }

            postsTable.setItems(postList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) postsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
