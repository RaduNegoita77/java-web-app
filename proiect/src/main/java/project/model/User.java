package project.model;

import project.exception.InvalidInputException;

public class User {
    private String id;
    private String username;
    private String displayName;
    private String role;

    public User() {} // Constructor gol necesar pentru UserHandler

    public User(String id, String username, String displayName, String role) throws InvalidInputException {
        if (username == null || username.length() < 3) throw new InvalidInputException("Username prea scurt");
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.role = role;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}