package project.server;

import project.dao.DataPersistence;
import project.model.User;
import project.exception.InvalidInputException;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataPersistence db = new DataPersistence();

    public ClientHandler(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(out, true)) {

            String line = in.readLine();
            if (line == null || line.isEmpty()) return;
            String path = line.split(" ")[1];

            if (path.startsWith("/api/add-user")) {
                handleUserAdd(path);
                pw.println("HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\n\r\n{\"status\":\"ok\"}");
            } else if (path.startsWith("/api/add-project")) {
                handleProjectAdd(path);
                pw.println("HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\n\r\n{\"status\":\"ok\"}");
            } else if (path.equals("/api/users")) {
                pw.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nAccess-Control-Allow-Origin: *\r\n\r\n" + convertUsersToJson());
            } else if (path.equals("/api/projects")) {
                pw.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nAccess-Control-Allow-Origin: *\r\n\r\n" + db.loadProjectsJson());
            } else {
                serveFile(out, path);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleUserAdd(String path) {
        try {
            String query = path.split("\\?")[1];
            String name = "", user = "";
            for (String p : query.split("&")) {
                String[] pair = p.split("=");
                if (pair.length < 2) continue;
                String val = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                if (pair[0].equals("name")) name = val;
                if (pair[0].equals("user")) user = val;
            }
            db.saveUser(new User(UUID.randomUUID().toString(), user, name, "USER"));
        } catch (InvalidInputException e) { e.printStackTrace();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleProjectAdd(String path) {
        try {
            String query = path.split("\\?")[1];
            String title = "", desc = "", uid = "";
            for (String p : query.split("&")) {
                String[] pair = p.split("=");
                if (pair.length < 2) continue;
                String val = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                if (pair[0].equals("title")) title = val;
                if (pair[0].equals("desc")) desc = val;
                if (pair[0].equals("uid")) uid = val;
            }
            db.addProject(uid, title, desc);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void serveFile(OutputStream out, String path) throws IOException {
        if (path.equals("/")) path = "/index.html";
        File f = new File("src/main/resources/web" + path);
        if (!f.exists()) return;
        byte[] content = Files.readAllBytes(f.toPath());
        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
        out.write(content);
    }

    private String convertUsersToJson() {
        StringBuilder sb = new StringBuilder("[");
        List<User> list = db.loadUsers();
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            sb.append(String.format("{\"id\":\"%s\",\"username\":\"%s\",\"displayName\":\"%s\"}", u.getId(), u.getUsername(), u.getDisplayName()));
            if (i < list.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }
}