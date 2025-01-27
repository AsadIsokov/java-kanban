package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void returnCheck() {
        InMemoryTaskManager manager1 = new InMemoryTaskManager();
        InMemoryHistoryManager manager2 = new InMemoryHistoryManager();
        InMemoryTaskManager manager3 = Managers.getDefault();
        InMemoryHistoryManager manager4 = Managers.getDefaultHistory();
        assertEquals(manager1.getClass(), manager3.getClass());
        assertEquals(manager2.getClass(), manager4.getClass());
    }
}