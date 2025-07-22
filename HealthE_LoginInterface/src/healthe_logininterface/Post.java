package healthe_logininterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Post {
    private final StringProperty email;
    private final StringProperty title;
    private final StringProperty description;

    public Post(String email, String title, String description) {
        this.email = new SimpleStringProperty(email);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
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
}
