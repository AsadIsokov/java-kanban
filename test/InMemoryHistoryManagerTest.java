import manager.*;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static final InMemoryTaskManager manager1 = Managers.getDefault();

    @Test
    void shouldAddWorkCorrect() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 12, 55), Duration.ofMinutes(23));
        Task doHomeWork1 = new Task("Сделать дз по математике!", "До дедлайна не нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 15, 55), Duration.ofMinutes(23));
        manager1.addTask(doHomeWork);
        manager1.addTask(doHomeWork1);
        manager1.getTaskById(doHomeWork.getId());
        manager1.getTaskById(doHomeWork1.getId());
        ArrayList<Task> anyArray = new ArrayList<>();
        anyArray.add(doHomeWork);
        anyArray.add(doHomeWork1);
        assertEquals(anyArray, manager1.getTasks());
    }

}