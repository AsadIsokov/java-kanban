import com.google.gson.*;
import httpServer.DurationAdapter;
import httpServer.HttpTaskServer;
import httpServer.LocalDateTimeAdapter;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
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

class EpicHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Epic epic;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void setUp() {
        epic = new Epic("Сделать домашнее задание!", "Успеть до понедельника!");
        epic.setStartTime(LocalDateTime.now());
        epic.setDuration(Duration.ofMinutes(15));
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
        manager.deleteEpics();
    }

    @Test
    void epicGetMethod() throws IOException, InterruptedException {
        manager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertFalse(manager.getEpics().isEmpty());
    }

    @Test
    void epicsPostMethod() throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertFalse(manager.getEpics().isEmpty());
    }

    @Test
    void epicsDeleteMethod() throws IOException, InterruptedException {
        manager.addEpic(epic);
        int epicId = epic.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics" + "/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.getEpics().isEmpty());
    }
}


