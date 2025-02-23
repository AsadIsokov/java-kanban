package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {
    public Epic(String name, String description) {
        super(name, description);
    }

    ArrayList<Subtask> subtasks = new ArrayList<>();

    public void startTimeOfEpic() {
        List<Subtask> sortedListByStartTime = subtasks.stream()
                .sorted(Comparator.comparing(Subtask::getStartTime))
                .toList();
        this.setStartTime(sortedListByStartTime.get(0).getStartTime());
    }

    public void durationOfEpic() {
        long duration1 = subtasks.stream()
                .mapToLong(subtask -> subtask.getDuration().toMinutes())
                .sum();
        this.setDuration(Duration.ofMinutes(duration1));
    }

    @Override
    public LocalDateTime getEndTime() {
        List<Subtask> sortedListByEndTime = subtasks.stream()
                .sorted(Comparator.comparing(Subtask::getEndTime).reversed())
                .toList();
        return sortedListByEndTime.get(0).getEndTime();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public void deleteSubtask(int id) {
        if (subtasks.contains(subtasks.get(id))) {
            subtasks.remove(id);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration.toMinutes() +
                ", startTime=" + startTime.format(TEXT_FORMAT) +
                '}';
    }
}
