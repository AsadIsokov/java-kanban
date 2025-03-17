package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> historyList = taskManager.getHistory();
            String response = gson.toJson(historyList);
            sendText(exchange, response);
        } else {
            sendRequestError(exchange);
        }
    }
}
