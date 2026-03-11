import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 任务实体，包含任务的核心字段与序列化能力。
 */
public class Task {
    private int id;
    private String description;
    /**
     * 状态枚举值: "todo", "in-progress", "done"
     */
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 构造任务对象。
     *
     * @param id 任务 ID
     * @param description 任务描述
     * @param status 任务状态
     * @param createdAt 创建时间
     * @param updatedAt 更新时间
     */
    public Task(int id, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * @return 任务 ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id 任务 ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return 任务描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 任务描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 任务状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status 任务状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 序列化为 JSON 格式字符串。
     *
     * @return JSON 字符串
     */
    public String toJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return "{\n" +
                "    \"id\": " + id + ",\n" +
                "    \"description\": \"" + description.replace("\"", "\\\"") + "\",\n" +
                "    \"status\": \"" + status + "\",\n" +
                "    \"createdAt\": \"" + createdAt.format(formatter) + "\",\n" +
                "    \"updatedAt\": \"" + updatedAt.format(formatter) + "\"\n" +
                "  }";
    }
}
