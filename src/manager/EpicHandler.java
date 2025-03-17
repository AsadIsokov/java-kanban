package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        String[] pathArray = path.split("/");
        if (pathArray.length > 3 && pathArray[3].equals("subtask")) {
            ArrayList<Subtask> subtasksOfEpic = taskManager.getSubtasksOfEpic(taskManager.getEpics()
                    .get(Integer.parseInt(pathArray[2])));
            String response = gson.toJson(subtasksOfEpic);
            sendText(exchange, response);
        } else if (pathArray.length == 3) {
            Epic epic = taskManager.getEpicById(Integer.parseInt(pathArray[2]));
            String response = gson.toJson(epic);
            sendText(exchange, response);
        } else {
            ArrayList<Epic> epics = taskManager.getEpics();
            String response = gson.toJson(epics);
            sendText(exchange, response);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, -1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        Epic epic = gson.fromJson(reader, Epic.class);
        taskManager.addEpic(epic);
        sendText(exchange, gson.toJson(epic));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        String path = exchange.getRequestURI().getPath();
        String[] pathArray = path.split("/");
        taskManager.deleteEpicById(Integer.parseInt(pathArray[2]));
        sendText(exchange, "Был вызван метод DELETE! Эпик удален!");
    }
}
