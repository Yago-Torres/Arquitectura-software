import java.io.Serializable;

public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;

    public Answer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}