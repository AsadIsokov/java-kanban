import manager.InMemoryTaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void shouldEpicsEqualsById() {
        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Помыть мотоцикл", "Это мото. Тут надо платная мойка!");
        epic2.setId(epic1.getId());
        assertEquals(epic1, epic2);
    }

    @Test
    void statusOfSubtasksAreNEW() {
        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 12, 54), Duration.ofMinutes(5));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 14, 15), Duration.ofMinutes(23));
        manager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void statusOfSubtasksAreDONE() {
        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 12, 54), Duration.ofMinutes(5));
        subtask1.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 14, 15), Duration.ofMinutes(23));
        subtask2.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    void statusOfSubtasksAreINPROGRESS() {
        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 12, 54), Duration.ofMinutes(5));
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 14, 15), Duration.ofMinutes(23));
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void statusOfSubtasksAreNEWandDONE() {
        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 12, 54), Duration.ofMinutes(5));
        subtask1.setStatus(TaskStatus.NEW);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId(),
                LocalDateTime.of(2025, 2, 15, 14, 15), Duration.ofMinutes(23));
        subtask2.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask2);
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

}