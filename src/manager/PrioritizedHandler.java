package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    public PrioritizedHandler(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            TreeSet<Task> prioritized = taskManager.getPrioritizedTasks();
            String response = gson.toJson(prioritized);
            sendText(exchange, response);
        } else{
            sendRequestError(exchange);
        }
    }
}
