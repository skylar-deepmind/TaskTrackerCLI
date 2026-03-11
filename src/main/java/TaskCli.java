import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务管理命令行入口。
 *
 * 支持新增、更新、删除、状态标记与列表查询。
 */
public class TaskCli {
    /**
     * CLI 入口。示例: {@code java TaskCli add "修复登录问题"}
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        // 每次执行命令前加载现有的任务数据
        List<Task> tasks = TaskRepository.loadTasks();

        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.out.println("错误: 缺少任务描述。用法: add \"任务描述\"");
                    return;
                }
                // 自动分配 ID：找到当前最大的 ID 并 +1
                int newId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
                Task newTask = new Task(newId, args[1], "todo", LocalDateTime.now(), LocalDateTime.now());
                tasks.add(newTask);

                TaskRepository.saveTasks(tasks);
                System.out.println("成功添加任务 (ID: " + newId + ")");
                break;

            case "update":
                if (args.length < 3) {
                    System.out.println("错误: 参数不足。用法: update <id> \"新描述\"");
                    return;
                }
                try {
                    int idToUpdate = Integer.parseInt(args[1]);
                    Task taskToUpdate = findTaskById(tasks, idToUpdate);
                    if (taskToUpdate != null) {
                        taskToUpdate.setDescription(args[2]);
                        taskToUpdate.setUpdatedAt(LocalDateTime.now());
                        TaskRepository.saveTasks(tasks);
                        System.out.println("成功更新任务 (ID: " + idToUpdate + ")");
                    } else {
                        System.out.println("未找到 ID 为 " + idToUpdate + " 的任务。");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("错误: ID 必须是数字。");
                }
                break;

            case "delete":
                if (args.length < 2) {
                    System.out.println("错误: 缺少任务 ID。用法: delete <id>");
                    return;
                }
                try {
                    int idToDelete = Integer.parseInt(args[1]);
                    boolean removed = tasks.removeIf(t -> t.getId() == idToDelete);
                    if (removed) {
                        TaskRepository.saveTasks(tasks);
                        System.out.println("成功删除任务 (ID: " + idToDelete + ")");
                    } else {
                        System.out.println("未找到 ID 为 " + idToDelete + " 的任务。");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("错误: ID 必须是数字。");
                }
                break;

            case "mark-in-progress":
            case "mark-done":
                if (args.length < 2) {
                    System.out.println("错误: 缺少任务 ID。用法: " + command + " <id>");
                    return;
                }
                try {
                    int idToMark = Integer.parseInt(args[1]);
                    Task taskToMark = findTaskById(tasks, idToMark);
                    if (taskToMark != null) {
                        String newStatus = command.equals("mark-done") ? "done" : "in-progress";
                        taskToMark.setStatus(newStatus);
                        taskToMark.setUpdatedAt(LocalDateTime.now());
                        TaskRepository.saveTasks(tasks);
                        System.out.println("成功将任务 (ID: " + idToMark + ") 状态标记为 " + newStatus);
                    } else {
                        System.out.println("未找到 ID 为 " + idToMark + " 的任务。");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("错误: ID 必须是数字。");
                }
                break;

            case "list":
                String statusFilter = args.length == 2 ? args[1] : null;
                boolean found = false;
                for (Task task : tasks) {
                    if (statusFilter == null || task.getStatus().equals(statusFilter)) {
                        System.out.printf("[%d] [%s] %s (最后更新: %s)%n",
                                task.getId(),
                                task.getStatus().toUpperCase(),
                                task.getDescription(),
                                task.getUpdatedAt().format(TaskRepository.FORMATTER));
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("当前没有任务。");
                }
                break;

            default:
                System.out.println("未知命令: " + command);
                printHelp();
        }
    }

    /**
     * 根据 ID 查找任务。
     *
     * @param tasks 任务列表
     * @param id 任务 ID
     * @return 匹配的任务，若不存在返回 null
     */
    private static Task findTaskById(List<Task> tasks, int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * 输出 CLI 使用说明。
     */
    private static void printHelp() {
        System.out.println("Task Tracker 用法:");
        System.out.println("  java TaskCli add \"任务描述\"             - 添加新任务");
        System.out.println("  java TaskCli update <id> \"新描述\"       - 更新任务");
        System.out.println("  java TaskCli delete <id>                - 删除任务");
        System.out.println("  java TaskCli mark-in-progress <id>      - 标记为进行中");
        System.out.println("  java TaskCli mark-done <id>             - 标记为已完成");
        System.out.println("  java TaskCli list                       - 列出所有任务");
        System.out.println("  java TaskCli list <status>              - 按状态列出 (todo, in-progress, done)");
    }
}
