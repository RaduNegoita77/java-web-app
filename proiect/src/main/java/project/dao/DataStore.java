package project.dao;

import project.exception.EntityNotFoundException;
import project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataStore {
    private final List<User> users = new ArrayList<>();
    private final Map<String, User> userMap = new HashMap<>();

    public DataStore() {}

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            users.add(user);
            userMap.put(user.getId(), user);
        }
    }

    public User getUserById(String id) throws EntityNotFoundException {
        User user = userMap.get(id);
        if (user == null) {
            throw new EntityNotFoundException("User with ID " + id + " not found.");
        }
        return user;
    }

    public boolean removeUserById(String id) throws EntityNotFoundException {
        if (!userMap.containsKey(id)) {
            throw new EntityNotFoundException("Cannot delete: User with ID " + id + " not found.");
        }

        userMap.remove(id);

        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.getId().equals(id)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}