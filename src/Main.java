import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.getDefault();
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        Task helpToFriend = new Task("Помочь другу!", "Помочь чтобы его не обидеть!");
        manager.addTask(doHomeWork);
        manager.addTask(helpToFriend);

        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        Epic epic2 = new Epic("Починить холодильник!", "Выключить перед выполнением!");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic1.getId());
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic1.getId());
        Subtask subtask3 = new Subtask("Найти отвертку", "Искать на балконе", epic2.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.getTaskById(2);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getEpicById(4);
        manager.getEpicById(3);
        manager.getSubtaskById(7);
        manager.getEpicById(3);
        printAllTasks(manager);


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
