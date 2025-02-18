import manager.InMemoryTaskManager;
import manager.Managers;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final InMemoryTaskManager manager = Managers.getDefault();

    @Test
    void shouldTasksEqualById() {
        Task cleanHome1 = new Task("Убрать комнату / дом!", "Нужно убраться на выходных!",
                LocalDateTime.of(2025, 2, 15, 12, 55), Duration.ofMinutes(23));
        manager.addTask(cleanHome1);
        int cleanHome1Id = cleanHome1.getId();
        Task cleanHome2 = new Task("Убрать балкон!", "Пустая трата времени!",
                LocalDateTime.of(2025, 2, 15, 15, 55), Duration.ofMinutes(23));
        cleanHome2.setId(cleanHome1Id);
        assertEquals(cleanHome1, cleanHome2);
    }


}