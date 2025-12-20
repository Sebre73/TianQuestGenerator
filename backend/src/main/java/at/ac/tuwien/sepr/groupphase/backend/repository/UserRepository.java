package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory user repository.
 *
 * <p>This is a simplified repository implementation without persistence.
 * Users are stored in memory for the lifetime of the application.
 */
@Repository
public class UserRepository {

    private final Map<String, ApplicationUser> usersByEmail = new ConcurrentHashMap<>();

    /**
     * Finds a user by email address.
     *
     * @param email the email address
     * @return the user or {@code null} if not found
     */
    public ApplicationUser findUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Stores a new user in memory.
     *
     * @param user the user to store
     */
    public void addUser(ApplicationUser user) {
        usersByEmail.put(user.getEmail(), user);
    }
}
