package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File memoryFile;

    public FileBackedTaskManager(String fileName) {
        setCount(1);
        this.memoryFile = new File(fileName);
    }

    public FileBackedTaskManager(File file) {
        setCount(1);
        this.memoryFile = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    private String toString(Task task) {
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

    public void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(memoryFile, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                fw.write(toString(task));
            }
            for (Epic epic : getEpics()) {
                fw.write(toString(epic));
            }
            for (Subtask subtask : getSubtasks()) {
                fw.write(toString(subtask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            FileBackedTaskManager fbt = new FileBackedTaskManager(file);
            br.readLine();
            while (br.ready()) {
                String str = br.readLine();
                String[] arr = str.split(",");
                if (arr[1].equals("TASK")) {
                    fbt.addTask(fromString(str));
                } else if (arr[1].equals("EPIC")) {
                    fbt.addEpic((Epic) fromString(str));
                } else {
                    fbt.addSubtask((Subtask) fromString(str));
                }
            }

            return fbt;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

}
