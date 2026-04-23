package project.dao;

import project.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:final.db";

    public DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS users (id TEXT PRIMARY KEY, username TEXT, displayName TEXT, role TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void insertUser(User u) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES(?,?,?,?)")) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getUsername());
            pstmt.setString(3, u.getDisplayName());
            pstmt.setString(4, u.getRole());
            pstmt.executeUpdate();
        }
    }

    public List<User> getAll() throws Exception {
        List<User> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                list.add(new User(rs.getString("id"), rs.getString("username"), rs.getString("displayName"), rs.getString("role")));
            }
        }
        return list;
    }
}