package test;

import manager.InMemoryTaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Subtask subtask = new Subtask("Починить холодильник!", "Выключить перед выполнением!", epic.getId());
        subtask.setId(subtask.getEpicId());
        manager.addSubtask(subtask);
        assertNotEquals(subtask.getId(), subtask.getEpicId());
    }

    @Test
    void shouldAddAllTypesTasksAndFindThemById() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        manager.addTask(doHomeWork);

        Epic epic = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic.getId());
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertNotNull(manager.getTasks());
        assertNotNull(manager.getEpics());
        assertNotNull(manager.getSubtasks());

        assertNotNull(manager.getTask(doHomeWork.getId()));
        assertNotNull(manager.getEpic(epic.getId()));
        assertNotNull(manager.getSubtask(subtask2.getId()));
    }

    @Test
    void shouldStabilityTask() {
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!");
        String nameTask = doHomeWork.getName();
        String descriptionTask = doHomeWork.getDescription();
        manager.addTask(doHomeWork);
        assertEquals(nameTask, manager.getTask(doHomeWork.getId()).getName());
        assertEquals(descriptionTask, "До дедлайна нужно сдать!");
    }
}