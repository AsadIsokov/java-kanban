import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager2 = new FileBackedTaskManager("tasksManagerHistory.txt");
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        Task helpToFriend = new Task("Помочь другу!", "Помочь чтобы его не обидеть!");
        manager2.addTask(doHomeWork);
        manager2.addTask(helpToFriend);

        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        Epic epic2 = new Epic("Починить холодильник!", "Выключить перед выполнением!");
        manager2.addEpic(epic1);
        manager2.addEpic(epic2);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic1.getId());
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic1.getId());
        Subtask subtask3 = new Subtask("Найти отвертку", "Искать на балконе", epic2.getId());
        manager2.addSubtask(subtask1);
        manager2.addSubtask(subtask2);
        manager2.addSubtask(subtask3);


        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(new File("tasks.txt"));
        System.out.println(manager1.getTasks());
        System.out.println(manager1.getEpics());
        System.out.println(manager1.getSubtasks());
    }

    private static void printAllTasks(InMemoryTaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
            for (Subtask subtask : manager.getSubtasksOfEpic(epic)) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }


}
