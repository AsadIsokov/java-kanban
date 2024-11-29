package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // a. Получение списка всех задач.
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    //  b. Удаление всех задач.
    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    //c. Получение по идентификатору.
    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // f. Удаление по идентификатору.
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    // Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);


}
