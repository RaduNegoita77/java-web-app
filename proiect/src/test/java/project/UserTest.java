package project;

import org.junit.jupiter.api.Test;
import project.model.User;
import project.exception.InvalidInputException;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserConstructorValid() throws InvalidInputException {
        User u = new User("1", "admin", "Administrator", "ADMIN");
        assertEquals("admin", u.getUsername());
        assertNotNull(u.getId());
    }

    @Test
    public void testUserConstructorInvalid() {
        assertThrows(InvalidInputException.class, () -> {
            new User("2", "", "Nume", "USER");
        });
    }

    @Test
    public void testRoleAssignment() throws InvalidInputException {
        User u = new User("3", "radu", "Radu", "USER");
        assertEquals("USER", u.getRole());
    }
}