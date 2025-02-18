import manager.FileBackedTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void writeOrReadAnEmptyFile() throws IOException {
        File file = File.createTempFile("testFile", ".txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        File file1 = File.createTempFile("testFile2", ".txt");
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file1);
        assertTrue(newManager.getTasks().isEmpty() && newManager.getSubtasks().isEmpty() && newManager.getEpics().isEmpty());
        assertEquals(0, file.length());
    }

    @Test
    void readSomeTasks() {
        File file = new File("testFile3");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        assertFalse(manager.getTasks().isEmpty() && manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @Test
    void writeSomeTasks() {
        File file = new File("testFile4");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 12, 55), Duration.ofMinutes(23));
        Task helpToFriend = new Task("Помочь другу!", "Помочь чтобы его не обидеть!",
                LocalDateTime.of(2025, 2, 15, 15, 55), Duration.ofMinutes(23));
        manager.addTask(doHomeWork);
        manager.addTask(helpToFriend);

        Epic epic1 = new Epic("Помыть машину!", "Мойка - самообслуживание!");
        Epic epic2 = new Epic("Починить холодильник!", "Выключить перед выполнением!");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Найти 100 рублей", "Чтобы оплатить мойку!", epic1.getId(),
                LocalDateTime.of(2025, 2, 15, 14, 10), Duration.ofMinutes(23));
        Subtask subtask2 = new Subtask("Доехать на машине до мойки", "Чтобы помыть", epic1.getId(),
                LocalDateTime.of(2025, 2, 17, 14, 10), Duration.ofMinutes(23));
        Subtask subtask3 = new Subtask("Найти отвертку", "Искать на балконе", epic2.getId(),
                LocalDateTime.of(2025, 2, 19, 14, 10), Duration.ofMinutes(23));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        assertTrue(file.length() > 0);
    }
}