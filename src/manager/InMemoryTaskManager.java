package manager;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import  java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int count = 1;

    private int getCount() {
        return count++;
    }

    //Хранения
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public HistoryManager historyManager = Managers.getDefaultHistory();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // a. Получение списка всех задач.
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    //  b. Удаление всех задач.
    @Override
    public void deleteTasks(){
        tasks.clear();
    }

    @Override
    public void deleteSubtasks(){
        subtasks.clear();
    }

    @Override
    public void deleteEpics(){
        epics.clear();
    }

    //c. Получение по идентификатору.
    @Override
    public Task getTask(int id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id){
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id){
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addTask(Task task){
        task.setId(getCount());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask){
        subtask.setId(getCount());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtasks(subtask);
        updateEpic(epics.get(subtask.getEpicId()));
        statusControl(epics.get(subtask.getEpicId()));
    }

    @Override
    public void addEpic(Epic epic){
        epic.setId(getCount());
        epics.put(epic.getId(), epic);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.

    @Override
    public void updateTask(Task task){
        if ((task.getId() != null) && tasks.containsKey(task.getId())){
            tasks.replace(task.getId(), task);
        }
    }

    @Override
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

    @Override
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
    @Override
    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id){
        for(Subtask elem : epics.get(id).getSubtasks()){
            subtasks.remove(elem.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id){
        if(subtasks.containsKey(id)){
            epics.get(subtasks.get(id).getEpicId()).deleteSubtask(id);
            subtasks.remove(id);
        }
    }

    // Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    @Override
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

    @Override
    public HistoryManager getHistoryList(){
        return historyManager;
    }


}
