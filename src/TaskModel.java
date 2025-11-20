import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskModel {
    private int id;
    private String description;
    private State status;
    private String createdAt;
    private String updatedAt;

    public TaskModel(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = State.TODO;
        this.createdAt = getCurrentTime();
        this.updatedAt = getCurrentTime();
    }

    public TaskModel(int id, String description, State status, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public State getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = getCurrentTime();
    }

    public void setStatus(State status) {
        this.status = status;
        this.updatedAt = getCurrentTime();
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String toJson() {
        return String.format(
                "{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                id, description, status.toString(), createdAt, updatedAt
        );
    }
}
