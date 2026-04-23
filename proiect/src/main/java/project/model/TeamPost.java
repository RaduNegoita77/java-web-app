package project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamPost implements Serializable {
    private UUID id = UUID.randomUUID();
    private String author;
    private String description;
    private List<String> rolesNeeded = new ArrayList<>();

    public TeamPost() {}

    public TeamPost(String author, String description) {
        this.author = author;
        this.description = description;
    }

    public UUID getId() { return id; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public List<String> getRolesNeeded() { return rolesNeeded; }

    @Override
    public String toString() {
        return author + ": " + description + " -> roles " + rolesNeeded;
    }
}
