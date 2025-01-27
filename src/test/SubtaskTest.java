package test;

import manager.InMemoryTaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static final InMemoryTaskManager manager = Managers.getDefault();

    @Test
    void shouldSubtasksEqualById() {
        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        Epic epic2 = new Epic("Починить холодильник!", "Выключить перед выполнением!");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic1.getId());
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic1.getId());
        Subtask subtask3 = new Subtask("Найти отвертку", "Искать на балконе", epic2.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        subtask2.setId(subtask1.getId());
        subtask3.setId(subtask1.getId());
        assertEquals(subtask1, subtask2);
        assertEquals(subtask2, subtask3);
    }

}