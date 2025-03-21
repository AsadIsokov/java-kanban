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
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
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
            List<Task> historyList = taskManager.getHistory();
            if (historyList.isEmpty()) {
                sendHasInteractions(exchange);
                return;
            }
            String response = gson.toJson(historyList);
            sendText(exchange, response, 200);
        } catch (Exception e) {
            sendRequestError(exchange);
        }
    }
}
