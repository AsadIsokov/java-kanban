package manager;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import  java.util.HashMap;

public class TaskManager {
    private static int count = 1;

    private int getCount() {
        return count++;
    }

    //Хранения
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // a. Получение списка всех задач.
    // Можно только список через ArrayList, но я пойду по другому пути
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
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
        task.setId(getCount());
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask){
        subtask.setId(getCount());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtasks(subtask);
        updateEpic(epics.get(subtask.getEpicId()));
        statusControl(epics.get(subtask.getEpicId()));
    }

    public void addEpic(Epic epic){
        epic.setId(getCount());
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
            Subtask subtask2 = subtasks.get(subtask.getId());
            subtasks.replace(subtask.getId(), subtask);
            ArrayList<Subtask> subtaskArrayList = epics.get(subtask.getEpicId()).getSubtasks();
            subtaskArrayList.remove(subtask2);
            subtaskArrayList.add(subtask);
            epics.get(subtask.getEpicId()).setSubtasks(subtaskArrayList);
            updateEpic(epics.get(subtask.getEpicId()));
            statusControl(epics.get(subtask.getEpicId()));
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
        if(subtasks.containsKey(id)){
            epics.get(subtasks.get(id).getEpicId()).deleteSubtask(id);
            subtasks.remove(id);
        }
    }

    // Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){
            return epic.getSubtasks();
    }

    // Управление статусами
    private void statusControl(Epic epic){
        if(epic.getSubtasks().isEmpty()){
            epic.setStatus(TaskStatus.NEW);
        } else {
            int countNewSubtask = 0;
            int countDoneSubtask = 0;
            for (Subtask elem : epic.getSubtasks()) {
                if (elem.getStatus() == TaskStatus.IN_PROGRESS) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    break;
                } else if (elem.getStatus() == TaskStatus.NEW){
                    countNewSubtask++;
                } else{
                    countDoneSubtask++;
                }
            }
            if (countDoneSubtask == epic.getSubtasks().size()){
                epic.setStatus(TaskStatus.DONE);
            } else if(countNewSubtask == epic.getSubtasks().size()){
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

}
