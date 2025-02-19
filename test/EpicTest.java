import manager.InMemoryTaskManager;
import manager.Managers;
import model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static final InMemoryTaskManager manager = Managers.getDefault();

    @Test
    void shouldEpicsEqualsById() {
        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Помыть мотоцикл", "Это мото. Тут надо платная мойка!");
        epic2.setId(epic1.getId());
        assertEquals(epic1, epic2);
    }

}