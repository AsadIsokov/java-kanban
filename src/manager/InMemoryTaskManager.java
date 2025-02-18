package manager;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected static int count = 1;

    private int getCount() {
        return count++;
    }

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        if (task.getStartTime() == null) {
            throw new IllegalArgumentException("Задача должна иметь время начала.");
        }

        if (checkIntersection(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующими задачами.");
        }
        task.setId(getCount());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            if (subtask.getStartTime() == null) {
                throw new ManagerSaveException("Задача должна иметь время начала.");
            }
            if (checkIntersection(subtask)) {
                throw new ManagerSaveException("Задача пересекается по времени с существующими задачами.");
            }
            subtask.setId(getCount());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtasks(subtask);
            updateEpic(epics.get(subtask.getEpicId()));
            epics.get(subtask.getEpicId()).startTimeOfEpic();
            epics.get(subtask.getEpicId()).durationOfEpic();
            statusControl(epics.get(subtask.getEpicId()));
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка добавления подзадачи: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Ошибка: Epic не найден. " + e.getMessage());
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
        tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .forEach(prioritizedTasks::add);
        subtasks.values().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .forEach(prioritizedTasks::add);
        return prioritizedTasks;
    }

    private boolean checkIntersection(Task task) {
        return getPrioritizedTasks().stream()
                .anyMatch(existingTask -> checkIntersectionTwoTasks(existingTask, task));
    }

    private boolean checkIntersectionTwoTasks(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task1.getEndTime().isAfter(task2.getStartTime());
    }
}
