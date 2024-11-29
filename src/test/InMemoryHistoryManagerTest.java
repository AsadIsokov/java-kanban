package test;

import manager.InMemoryHistoryManager;
import manager.Managers;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static final InMemoryHistoryManager manager = Managers.getDefaultHistory();
    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void shouldAddWithoutChanges(){
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        String nameTask = doHomeWork.getName();
        String descriptionTask = doHomeWork.getDescription();
        doHomeWork.setId(1);
        manager.add(doHomeWork);
        assertEquals(nameTask, doHomeWork.getName());
        assertEquals(1, doHomeWork.getId());
        assertEquals(descriptionTask, doHomeWork.getDescription());
    }

}