package httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
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
            TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            if (prioritizedTasks.isEmpty()) {
                sendHasInteractions(exchange);
                return;
            }
            String response = gson.toJson(prioritizedTasks);
            sendText(exchange, response, 200);

        } catch (Exception e) {
            sendRequestError(exchange);
        }
    }
}
