package project.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Project implements Serializable {
    private UUID id = UUID.randomUUID();
    private String title;
    private Set<String> requiredSkills = new HashSet<>();

    public Project() {}

    public Project(String title) {
        this.title = title;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public Set<String> getRequiredSkills() { return requiredSkills; }

    @Override
    public String toString() {
        return title + " requires " + requiredSkills;
    }
}
