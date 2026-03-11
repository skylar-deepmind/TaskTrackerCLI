import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务数据的本地持久化仓库。
 * <p>
 * 采用 JSON 文件保存任务列表，不依赖外部 JSON 库。
 */
public class TaskRepository {
    /**
     * 任务文件路径（相对执行目录）。
     */
    private static final String FILE_PATH = "task-tracker.json";
    /**
     * 日期时间格式，与序列化保持一致。
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * 读取并解析 JSON 文件。
     *
     * @return 任务列表
     */
    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        // 如果文件不存在，直接返回空列表
        if (!Files.exists(path)) {
            return tasks;
        }

        try {
            String content = new String(Files.readAllBytes(path));

            // 使用正则表达式匹配 JSON 中的各个字段 (未引入外部库)
            String regex = "\\{\\s*\"id\":\\s*(\\d+),\\s*\"description\":\\s*\"(.*?)\",\\s*\"status\":\\s*\"(.*?)\",\\s*\"createdAt\":\\s*\"(.*?)\",\\s*\"updatedAt\":\\s*\"(.*?)\"\\s*\\}";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                // 还原被转义的双引号
                String description = matcher.group(2).replace("\\\"", "\"");
                String status = matcher.group(3);
                LocalDateTime createdAt = LocalDateTime.parse(matcher.group(4), FORMATTER);
                LocalDateTime updatedAt = LocalDateTime.parse(matcher.group(5), FORMATTER);

                tasks.add(new Task(id, description, status, createdAt, updatedAt));
            }
        } catch (IOException e) {
            System.out.println("读取任务文件失败: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * 将任务列表序列化为 JSON 并保存到文件。
     *
     * @param tasks 任务列表
     */
    public static void saveTasks(List<Task> tasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {
            // 将 Task 对象的 toJson 结果缩进，使其更美观
            String taskJson = tasks.get(i).toJson().replace("\n", "\n  ");
            jsonBuilder.append("  ").append(taskJson);

            // 如果不是最后一个元素，添加逗号
            if (i < tasks.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }
        jsonBuilder.append("]");

        try {
            Files.write(Paths.get(FILE_PATH), jsonBuilder.toString().getBytes());
        } catch (IOException e) {
            System.out.println("保存任务文件失败: " + e.getMessage());
        }
    }
}
