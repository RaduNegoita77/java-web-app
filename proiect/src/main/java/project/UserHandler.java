package project;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserHandler implements HttpHandler {

    private static final List<User> userList = new ArrayList<>();


    private String extractValueFromJson(String jsonBody, String key) {
        String searchKey = "\"" + key + "\":\"";
        int start = jsonBody.indexOf(searchKey);
        if (start == -1) return null;

        start += searchKey.length();
        int end = jsonBody.indexOf("\"", start);
        if (end == -1) return null;

        return jsonBody.substring(start, end);
    }

    private String userToJson(User user) {
        return String.format(
                "{\"id\": \"%s\", \"username\": \"%s\", \"displayName\": \"%s\"}",
                user.getId(), user.getUsername(), user.getDisplayName()
        );
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if ("GET".equals(requestMethod)) {
            handleGet(exchange);
        } else if ("POST".equals(requestMethod)) {
            handlePost(exchange);
        } else if ("DELETE".equals(requestMethod)) {
            handleDelete(exchange);
        } else {
            writeResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String userJsonList = userList.stream()
                .map(this::userToJson)
                .collect(Collectors.joining(","));

        String finalResponse = "[" + userJsonList + "]";

        writeResponse(exchange, 200, finalResponse);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            requestBody = br.lines().collect(Collectors.joining("\n"));
        }

        String username = extractValueFromJson(requestBody, "username");
        String displayName = extractValueFromJson(requestBody, "displayName");

        if (username == null || displayName == null || username.trim().isEmpty() || displayName.trim().isEmpty()) {
            writeResponse(exchange, 400, "{\"error\": \"Username and Display Name are required.\"}");
            return;
        }

        String finalUsername = username.trim();
        boolean usernameExists = userList.stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(finalUsername));

        if (usernameExists) {
            writeResponse(exchange, 409, "{\"error\": \"Username already exists.\"}");
            return;
        }

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setUsername(finalUsername);
        newUser.setDisplayName(displayName.trim());

        userList.add(newUser);

        writeResponse(exchange, 201, userToJson(newUser));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length < 4) {
            writeResponse(exchange, 400, "{\"error\": \"Missing User ID in path.\"}");
            return;
        }

        String userIdToDelete = parts[3];

        boolean removed = userList.removeIf(user -> user.getId().equals(userIdToDelete));

        if (removed) {
            exchange.sendResponseHeaders(204, -1);
        } else {
            writeResponse(exchange, 404, "{\"error\": \"User not found.\"}");
        }
    }
}