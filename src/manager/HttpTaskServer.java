package manager;

import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager){
        this.taskManager = taskManager;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
            httpServer.createContext("/epics", new EpicHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        } catch (IOException e){
            System.out.println("Ошибка во время создании сервера!");
        }
    }

    public void start(){
        httpServer.start();
        System.out.println("Сервер запущен! ПОРТ: " + PORT);
    }

    public void stop(){
        httpServer.stop(0);
        System.out.println("Сервер остановлен!");
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task doHomeWork = new Task("Сделать дз!", "До дедлайна нужно сдать!",
                LocalDateTime.of(2025, 2, 15, 10, 10), Duration.ofMinutes(5));
        Task helpToFriend = new Task("Помочь другу!", "Помочь чтобы его не обидеть!",
                LocalDateTime.of(2025, 2, 15, 10, 30), Duration.ofMinutes(10));
        manager.addTask(doHomeWork);
        manager.addTask(helpToFriend);
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }
}
