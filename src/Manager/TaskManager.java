package Manager;
import AboutTasks.Epic;
import AboutTasks.Status;
import AboutTasks.Subtask;
import AboutTasks.Task;

import java.util.ArrayList;
import  java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private static int count = 1;

    public int getCount() {
        return count++;
    }

    //Хранения
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // a. Получение списка всех задач.
    // Можно только список через ArrayList, но я пойду по другому пути
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    //  b. Удаление всех задач.
    public void deleteTasks(){
        tasks.clear();
    }

    public void deleteSubtasks(){
        subtasks.clear();
    }

    public void deleteEpics(){
        epics.clear();
    }

    //c. Получение по идентификатору.
    public Task getTask(int id){
        return tasks.get(id);
    }

    public Subtask getSubtask(int id){
        return subtasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public void addTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
    }

    public void addEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.

    public void updateTask(Task task){
        if ((task.getId() != null) && tasks.containsKey(task.getId())){
            tasks.replace(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic){
        if((epic.getId() != null) && epics.containsKey(epic.getId())){
            ArrayList<Subtask> oldSubtasks = epics.get(epic.getId()).getSubtasks();
            epics.replace(epic.getId(), epic);
            ArrayList<Subtask> newSubtasks = epic.getSubtasks();

            if (!oldSubtasks.isEmpty()){
                for(Subtask elem: oldSubtasks){
                    subtasks.remove(elem.getId());
                }
            }
            if (!newSubtasks.isEmpty()){
                for(Subtask elem : newSubtasks){
                    subtasks.put(elem.getId(), elem);
                }
            }
        }
    }

    public void updateSubtask(Subtask subtask){
        if ((subtask.getId() != null) && subtasks.containsKey(subtask.getId())){
            subtasks.replace(subtask.getId(), subtask);
        }
        int idOfEpic = -1;
        for(Epic el1 : epics.values()){
            for(Subtask el2 : el1.getSubtasks()){
                if(Objects.equals(el2.getId(), subtask.getId())){
                    idOfEpic = el1.getId();
                    epics.get(el1.getId()).getSubtasks().remove(el2);
                    epics.get(idOfEpic).addSubtasks(subtask);
                    break;
                }
            }
        }
    }

    // f. Удаление по идентификатору.
    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    public void deleteEpicById(int id){
        for(Subtask elem : epics.get(id).getSubtasks()){
            subtasks.remove(elem.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(int id){
        int idOfEpic = -1;
        for(Epic el1 : epics.values()){
            for(Subtask el2 : el1.getSubtasks()){
                if(Objects.equals(el2.getId(), id)){
                    idOfEpic = el1.getId();
                    epics.get(idOfEpic).clearSubtaskById(id);
                    break;
                }
            }
        }
        subtasks.remove(id);
    }

    // Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){
            return epic.getSubtasks();
    }

    // Управление статусами
    public void statusControl(Epic epic){
        if(epic.getSubtasks().isEmpty()){
            epic.setStatus(Status.NEW);
        } else {
            int countNewSubtask = 0;
            int countDoneSubtask = 0;
            for (Subtask elem : epic.getSubtasks()) {
                if (elem.getStatus() == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                } else if (elem.getStatus() == Status.NEW){
                    countNewSubtask++;
                } else{
                    countDoneSubtask++;
                }
            }
            if (countDoneSubtask == epic.getSubtasks().size()){
                epic.setStatus(Status.DONE);
            } else if(countNewSubtask == epic.getSubtasks().size()){
                epic.setStatus(Status.NEW);
            }
        }
    }

}
