package httpServer;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text, Integer statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, -1);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, -1);
        h.close();
    }

    protected void sendRequestError(HttpExchange h) throws IOException {
        h.sendResponseHeaders(500, -1);
        h.close();
    }

}
