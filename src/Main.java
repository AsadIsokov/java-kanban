import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

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


        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        System.out.println(epic1);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        System.out.println(epic1);
        System.out.println(manager.getSubtasksOfEpic(epic1));



        manager.deleteTaskById(doHomeWork.getId());
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());





    }


}
