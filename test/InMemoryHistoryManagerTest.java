import manager.*;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static final InMemoryTaskManager manager = Managers.getDefault();

    @Test
    void shouldAddWorkCorrect() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        Task doHomeWork1 = new Task("Сделать дз по математике!", "До дедлайна не нужно сдать!");
        manager.addTask(doHomeWork);
        manager.addTask(doHomeWork1);
        manager.getTask(doHomeWork.getId());
        manager.getTask(doHomeWork1.getId());
        ArrayList<Task> anyArray = new ArrayList<>();
        anyArray.add(doHomeWork);
        anyArray.add(doHomeWork1);
        assertEquals(anyArray, manager.getTasks());
    }

}