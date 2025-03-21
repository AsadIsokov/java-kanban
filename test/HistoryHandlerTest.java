import httpServer.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);

    @Test
    void historyHandlerTest() throws IOException, InterruptedException {
        taskServer.start();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        Task task = new Task("Таск 1", "Задание 1", LocalDateTime.now(), Duration.ofMinutes(10));
        manager.addTask(task);
        Epic epic = new Epic("Epic 1", "Эпик 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Подзадачка 1", epic.getId(),
                LocalDateTime.parse("10:11:12 01.01.2023", df), Duration.ofMinutes(15));
        manager.addSubtask(subtask);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertFalse(manager.getHistory().isEmpty());
        assertEquals(manager.getHistory().size(), 3);
        taskServer.stop();
    }
}
