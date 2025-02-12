package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class CSVFormat {
    public static String toString(Task task) {
        if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s%n", task.getId(), TypeOfTask.EPIC, task.getName(), task.getStatus(), task.getDescription());
        } else if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%d%n", task.getId(), TypeOfTask.SUBTASK, task.getName(), task.getStatus(), task.getDescription(), ((Subtask) task).getEpicId());
        }
        return String.format("%d,%s,%s,%s,%s%n", task.getId(), TypeOfTask.TASK, task.getName(), task.getStatus(), task.getDescription());
    }

    public static Task fromString(String value) {
        String[] arr = value.split(",");
        if (arr[1].equals("EPIC")) {
            Epic epic = new Epic(arr[2], arr[4]);
            epic.setId(Integer.parseInt(arr[0]));
            epic.setStatus(TaskStatus.valueOf(arr[3]));
            return epic;
        } else if (arr[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(arr[2], arr[4], Integer.parseInt(arr[5]));
            subtask.setId(Integer.parseInt(arr[0]));
            subtask.setStatus(TaskStatus.valueOf(arr[3]));
            return subtask;
        }
        Task task = new Task(arr[2], arr[4]);
        task.setId(Integer.parseInt(arr[0]));
        task.setStatus(TaskStatus.valueOf(arr[3]));
        return task;
    }
}
