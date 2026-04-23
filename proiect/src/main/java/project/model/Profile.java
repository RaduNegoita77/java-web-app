package project.model;

import project.iface.Matchable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Profile implements Serializable, Matchable<Profile> {
    private UUID id = UUID.randomUUID();
    private UUID userId;
    private Set<String> skills = new HashSet<>();

    public Profile() {}

    public Profile(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public Set<String> getSkills() { return skills; }

    @Override
    public double scoreMatch(Profile other) {
        Set<String> common = new HashSet<>(skills);
        common.retainAll(other.skills);
        return common.size();
    }

    @Override
    public String toString() {
        return "Profile(" + skills + ")";
    }
}
