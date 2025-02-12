package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File memoryFile;

    public FileBackedTaskManager(String fileName) {
        this.memoryFile = new File(fileName);
        setCountOne();
    }

    public FileBackedTaskManager(File file) {
        this.memoryFile = file;
        setCountOne();
    }

    protected void setCountOne() {
        InMemoryTaskManager.count = 1;
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

    private void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(memoryFile, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                fw.write(CSVFormat.toString(task));
            }
            for (Epic epic : getEpics()) {
                fw.write(CSVFormat.toString(epic));
            }
            for (Subtask subtask : getSubtasks()) {
                fw.write(CSVFormat.toString(subtask));
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
                    fbt.addTask(CSVFormat.fromString(str));
                } else if (arr[1].equals("EPIC")) {
                    fbt.addEpic((Epic) CSVFormat.fromString(str));
                } else {
                    fbt.addSubtask((Subtask) CSVFormat.fromString(str));
                }
            }

            return fbt;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

}
