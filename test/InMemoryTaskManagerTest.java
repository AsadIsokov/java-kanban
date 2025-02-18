import manager.InMemoryTaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void shouldEpicNotAddEpicLikeSubtask() {
        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Починить холодильник!", "Выключить перед выполнением!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 10, 55), Duration.ofMinutes(23));
        subtask.setId(subtask.getEpicId());
        manager.addSubtask(subtask);
        assertNotEquals(subtask.getId(), subtask.getEpicId());
    }

    @Test
    void shouldAddAllTypesTasksAndFindThemById() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 21, 55), Duration.ofMinutes(23));
        manager.addTask(doHomeWork);

        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 12, 55), Duration.ofMinutes(23));
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 15, 55), Duration.ofMinutes(23));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertNotNull(manager.getTasks());
        assertNotNull(manager.getEpics());
        assertNotNull(manager.getSubtasks());
    }

    @Test
    void shouldStabilityTask() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 12, 55), Duration.ofMinutes(23));
        String nameTask = doHomeWork.getName();
        String descriptionTask = doHomeWork.getDescription();
        manager.addTask(doHomeWork);
        assertEquals(nameTask, manager.getTaskById(doHomeWork.getId()).getName());
        assertEquals(descriptionTask, "До дедлайна нужно сдать!");
    }
}