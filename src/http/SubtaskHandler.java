package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
            if (pathArr.length < 3 || pathArr[2].isEmpty()) {
                ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                response = gson.toJson(subtasks);
            } else {
                int subId = Integer.parseInt(pathArr[2]);
                Subtask subtask = taskManager.getSubtaskById(subId);
                if (!taskManager.getSubtasks().contains(subtask)) {
                    sendNotFound(exchange);
                    return;
                }
                response = gson.toJson(subtask);
            }
            sendText(exchange, response, 200);
        } catch (NumberFormatException | NullPointerException e) {
            sendRequestError(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathArr = path.split("/");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            Subtask subtask = gson.fromJson(reader, Subtask.class);
            if (pathArr.length < 3 || pathArr[2].isEmpty()) {
                if (taskManager.getSubtasks().contains(subtask)) {
                    sendHasInteractions(exchange);
                    return;
                }
                taskManager.addSubtask(subtask);
                sendText(exchange, gson.toJson(subtask), 201);
            } else if (pathArr.length < 4) {
                taskManager.updateSubtask(subtask);
                sendText(exchange, gson.toJson(subtask), 201);
            }
        } catch (IOException e) {
            sendRequestError(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathArr = path.split("/");
        if (pathArr[2].isEmpty()) {
            sendRequestError(exchange);
            return;
        }
        try {
            int subtaskId = Integer.parseInt(pathArr[2]);
            taskManager.deleteSubtaskById(subtaskId);
            sendText(exchange, "Был вызван метод DELETE! Подзадача удалена!", 200);
        } catch (NumberFormatException e) {
            sendRequestError(exchange);
        }
    }
}
