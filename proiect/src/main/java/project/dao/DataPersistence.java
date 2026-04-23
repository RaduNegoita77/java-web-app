package project.dao;

import project.model.User;
import project.exception.InvalidInputException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataPersistence {
    private final String url = "jdbc:postgresql://localhost:5432/proiect_facultate";
    private final String user = "postgres";
    private final String password = "hoptop";

    public DataPersistence() {
        try {
            Class.forName("org.postgresql.Driver");
            initDatabase();
        } catch (Exception e) {
            System.err.println("Driver Error: " + e.getMessage());
        }
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id TEXT PRIMARY KEY, username TEXT UNIQUE, displayname TEXT, role TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS projects (id TEXT PRIMARY KEY, userid TEXT REFERENCES users(id) ON DELETE CASCADE, title TEXT, description TEXT)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (id, username, displayname, role) VALUES ('admin-fix', 'admin', 'Administrator', 'ADMIN')");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                try {
                    users.add(new User(rs.getString("id"), rs.getString("username"), rs.getString("displayname"), rs.getString("role")));
                } catch (InvalidInputException e) { e.printStackTrace(); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    public void saveUser(User u) {
        String sql = "INSERT INTO users (id, username, displayname, role) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET displayname = EXCLUDED.displayname";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getUsername());
            pstmt.setString(3, u.getDisplayName());
            pstmt.setString(4, u.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void addProject(String userId, String title, String desc) {
        String sql = "INSERT INTO projects (id, userid, title, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, userId);
            pstmt.setString(3, title);
            pstmt.setString(4, desc);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public String loadProjectsJson() {
        StringBuilder sb = new StringBuilder("[");
        String sql = "SELECT p.*, u.displayname FROM projects p JOIN users u ON p.userid = u.id";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                sb.append(String.format("{\"title\":\"%s\",\"desc\":\"%s\",\"owner\":\"%s\"},",
                        rs.getString("title"), rs.getString("description"), rs.getString("displayname")));
            }
            if (sb.length() > 1) sb.setLength(sb.length() - 1);
        } catch (SQLException e) { e.printStackTrace(); }
        return sb.append("]").toString();
    }
}