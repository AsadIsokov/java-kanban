import AboutTasks.Epic;
import AboutTasks.Status;
import AboutTasks.Subtask;
import AboutTasks.Task;
import Manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task doHomeWork = new Task(manager.getCount(), "Сделать дз!", "До дедлайна нужно сдать!");
        Task helpToFriend = new Task(manager.getCount(), "Помочь другу!", "Помочь чтобы его не обидеть!", Status.NEW);

        manager.addTask(doHomeWork);
        manager.addTask(helpToFriend);

        Epic epic1 = new Epic(manager.getCount(), "Помыть машину!", "Мойка - самообслуживание!");
        Epic epic2 = new Epic(manager.getCount(), "Починить холодильник!", "Выключить перед выполнением!");

        Subtask subtask1 = new Subtask(manager.getCount(), "Найти 100 рублей", "Чтобы оплатить мойку!", Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask(manager.getCount(), "Доехать на машине до мойки", "Чтобы помыть", Status.DONE);
        epic1.addSubtasks(subtask1);
        epic1.addSubtasks(subtask2);
        Subtask subtask3 = new Subtask(manager.getCount(), "Найти отвертку", "Искать на балконе", Status.NEW);
        epic2.addSubtasks(subtask3);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println(helpToFriend.getStatus());
        System.out.println(subtask1.getStatus());
        System.out.println(subtask2.getStatus());
        System.out.println(subtask3.getStatus());
        manager.statusControl(epic1);
        System.out.println(epic1.getStatus());
        manager.getSubtask(subtask1.getId()).setStatus(Status.DONE);
        manager.statusControl(epic1);
        System.out.println(epic1.getStatus());

        manager.deleteTaskById(doHomeWork.getId());
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());





    }


}
