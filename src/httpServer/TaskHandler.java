package httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
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
                ArrayList<Task> tasks = taskManager.getTasks();
                response = gson.toJson(tasks);
            } else {
                Task task = taskManager.getTaskById(Integer.parseInt(path.split("/")[2]));
                response = gson.toJson(task);
            }
            sendText(exchange, response, 200);
        } catch (IOException e) {
            sendNotFound(exchange);
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathArr = path.split("/");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            Task task = gson.fromJson(reader, Task.class);
            if (pathArr.length < 3 || pathArr[2].isEmpty()) {
                if (taskManager.getTasks().contains(task)) {
                    sendHasInteractions(exchange);
                    return;
                }
                taskManager.addTask(task);
                sendText(exchange, gson.toJson(task), 201);
            } else {
                taskManager.updateTask(task);
                sendText(exchange, gson.toJson(task), 201);
            }
        } catch (IOException e) {
            sendRequestError(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathArr = path.split("/");
        if (pathArr.length < 3 || pathArr[2].isEmpty()) {
            sendRequestError(exchange);
            return;
        }
        try {
            int tasksId = Integer.parseInt(pathArr[2]);
            taskManager.deleteTaskById(tasksId);
            sendText(exchange, "Был вызван метод DELETE! Задача удалена!", 200);
        } catch (NumberFormatException e) {
            sendRequestError(exchange);
        }
    }
}
