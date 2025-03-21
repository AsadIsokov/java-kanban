package httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> sendRequestError(exchange);
            }
        } catch (IOException e) {
            sendRequestError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathArr = path.split("/");
            String response;
            if (pathArr.length < 3) {
                ArrayList<Epic> epics = taskManager.getEpics();
                response = gson.toJson(epics);
                sendText(exchange, response, 200);
            } else if (pathArr.length == 3) {
                int epicId = Integer.parseInt(pathArr[2]);
                Epic epic = taskManager.getEpicById(epicId);
                response = gson.toJson(epic);
            } else {
                int epicId = Integer.parseInt(pathArr[2]);
                Epic epic = taskManager.getEpicById(epicId);
                ArrayList<Subtask> subtaskArrayList = taskManager.getSubtasksOfEpic(epic);
                response = gson.toJson(subtaskArrayList);
            }
            sendText(exchange, response, 200);
        } catch (NullPointerException | NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            Epic epic = gson.fromJson(reader, Epic.class);
            taskManager.addEpic(epic);
            sendText(exchange, "Эпик успешно добавлен!", 201);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathArr = path.split("/");
        try {
            if (pathArr.length != 3 || pathArr[2].isEmpty()) {
                sendRequestError(exchange);
                return;
            }
            int epicId = Integer.parseInt(pathArr[2]);
            taskManager.deleteEpicById(epicId);
            sendText(exchange, "Был вызван метод DELETE! Эпик удален!", 200);
        } catch (NullPointerException | NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
