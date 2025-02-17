package manager;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected static int count = 1;

    private int getCount() {
        return count++;
    }

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public HistoryManager historyManager = Managers.getDefaultHistory();

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

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void addTask(Task task) {
        if (checkInterSectionTask(task)) {
            task.setId(getCount());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkIntersectionSubtask(subtask)) {
            subtask.setId(getCount());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtasks(subtask);
            updateEpic(epics.get(subtask.getEpicId()));
            epics.get(subtask.getEpicId()).startTimeOfEpic();
            epics.get(subtask.getEpicId()).durationOfEpic();
            statusControl(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getCount());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if ((task.getId() != null) && tasks.containsKey(task.getId())) {
            tasks.replace(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if ((epic.getId() != null) && epics.containsKey(epic.getId())) {
            ArrayList<Subtask> oldSubtasks = epics.get(epic.getId()).getSubtasks();
            epics.replace(epic.getId(), epic);
            ArrayList<Subtask> newSubtasks = epic.getSubtasks();

            if (!oldSubtasks.isEmpty()) {
                for (Subtask elem : oldSubtasks) {
                    subtasks.remove(elem.getId());
                }
            }
            if (!newSubtasks.isEmpty()) {
                for (Subtask elem : newSubtasks) {
                    subtasks.put(elem.getId(), elem);
                }
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if ((subtask.getId() != null) && subtasks.containsKey(subtask.getId())) {
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

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (Subtask elem : epics.get(id).getSubtasks()) {
            subtasks.remove(elem.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).deleteSubtask(id);
            subtasks.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

    private void statusControl(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
        } else {
            int countNewSubtask = 0;
            int countDoneSubtask = 0;
            for (Subtask elem : epic.getSubtasks()) {
                if (elem.getStatus() == TaskStatus.IN_PROGRESS) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    break;
                } else if (elem.getStatus() == TaskStatus.NEW) {
                    countNewSubtask++;
                } else {
                    countDoneSubtask++;
                }
            }
            if (countDoneSubtask == epic.getSubtasks().size()) {
                epic.setStatus(TaskStatus.DONE);
            } else if (countNewSubtask == epic.getSubtasks().size()) {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        for (Task elem : tasks.values()) {
            if (elem.getStartTime() != null) {
                sortedTasks.add(elem);
            }
        }
        return sortedTasks;
    }

    public TreeSet<Subtask> getPrioritizedSubtasks() {
        TreeSet<Subtask> sortedSubtasks = new TreeSet<>(Comparator.comparing(Subtask::getStartTime));
        for (Subtask elem : subtasks.values()) {
            if (elem.getStartTime() != null) {
                sortedSubtasks.add(elem);
            }
        }
        return sortedSubtasks;
    }

    public boolean checkInterSectionTask(Task task) {
        for (Task value : getPrioritizedTasks()) {
            if (task.getEndTime().isAfter(value.getStartTime()) &&
                    task.getStartTime().isBefore(value.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIntersectionSubtask(Subtask subtask) {
        for (Subtask value : getPrioritizedSubtasks()) {
            if (subtask.getEndTime().isAfter(value.getStartTime()) &&
                    subtask.getStartTime().isBefore(value.getStartTime())) {
                return false;
            }
        }
        return true;
    }

}
