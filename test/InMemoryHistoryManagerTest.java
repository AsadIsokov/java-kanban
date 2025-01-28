import manager.*;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static final InMemoryTaskManager manager1 = Managers.getDefault();

    @Test
    void shouldAddWorkCorrect() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        Task doHomeWork1 = new Task("Сделать дз по математике!", "До дедлайна не нужно сдать!");
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