package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            case "DELETE" -> handleDelete(exchange);
            default -> sendRequestError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        String path = exchange.getRequestURI().getPath();
        if (path.split("/")[2].isEmpty()) {
            ArrayList<Subtask> subtasks = taskManager.getSubtasks();
            String response = gson.toJson(subtasks);
            sendText(exchange, response);
        } else {
            Subtask subtask = taskManager.getSubtaskById(Integer.parseInt(path.split("/")[2]));
            String response = gson.toJson(subtask);
            sendText(exchange, response);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, -1);
        String path = exchange.getRequestURI().getPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        Subtask subtask = gson.fromJson(reader, Subtask.class);
        if (path.split("/")[2].isEmpty()) {
            taskManager.addSubtask(subtask);
        } else {
            taskManager.updateSubtask(subtask);
        }
        sendText(exchange, gson.toJson(subtask));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        String path = exchange.getRequestURI().getPath();
        taskManager.deleteSubtaskById(Integer.parseInt(path.split("/")[2]));
        sendText(exchange, "Был вызван метод DELETE! Подзадача удалена!");
    }
}
