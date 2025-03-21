import com.google.gson.*;
import httpServer.DurationAdapter;
import httpServer.HttpTaskServer;
import httpServer.LocalDateTimeAdapter;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void setUp() {
        manager.deleteEpics();
        manager.deleteSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    void subtasksGetMethod() throws IOException, InterruptedException {
        Epic epic = new Epic("Сделать домашнее задание!", "Успеть до понедельника!");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Сделать домашнее задание!", "Параграф 4, страница 121!", epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(25));
        epic.addSubtasks(subtask);
        manager.addSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void subtasksPostMethod() throws IOException, InterruptedException {
        Epic epic = new Epic("Сделать домашнее задание!", "Успеть до понедельника!");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Сделать домашнее задание!", "Параграф 4, страница 121!", epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(25));
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertFalse(manager.getSubtasks().isEmpty());
    }

    @Test
    void subtasksDeleteMethod() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Сделать домашнее задание!", "Параграф 4, страница 121!", 3,
                LocalDateTime.now(), Duration.ofMinutes(25));
        manager.addSubtask(subtask);
        int subtaskId = subtask.getId();
        assertFalse(manager.getSubtasks().isEmpty());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks" + "/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.getSubtasks().isEmpty());
    }
}


