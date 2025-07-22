package healthe_logininterface;

import javafx.beans.property.*;

public class Post {

    private final IntegerProperty id;
    private final StringProperty email;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty reply;

    // Constructor with reply
    public Post(int id, String email, String title, String description, String reply) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.reply = new SimpleStringProperty(reply);
    }

    // Constructor without reply (if needed)
    public Post(String email, String title, String description) {
        this.id = new SimpleIntegerProperty(0);
        this.email = new SimpleStringProperty(email);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.reply = new SimpleStringProperty("");
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getReply() {
        return reply.get();
    }

    // Property methods for TableView bindings
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty replyProperty() {
        return reply;
    }

    // Setters (only for reply)
    public void setReply(String newReply) {
        this.reply.set(newReply);
    }
}
