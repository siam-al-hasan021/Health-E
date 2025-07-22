package healthe_logininterface;

import javafx.beans.property.*;

public class Post {

    private int id;
    private final StringProperty email, title, description, reply;

    public Post(int id, String email, String title, String description, String reply) {
        this.id = id;
        this.email = new SimpleStringProperty(email);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.reply = new SimpleStringProperty(reply);
    }

    public int getId() {
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

    public void setReply(String reply) {
        this.reply.set(reply);
    }
}
