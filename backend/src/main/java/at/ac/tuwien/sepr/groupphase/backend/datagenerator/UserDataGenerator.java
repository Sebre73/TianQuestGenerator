package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Generates initial users for development and demo purposes.
 *
 * <p>This generator creates a default admin user on application startup.
 */
@Component
public class UserDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(UserDataGenerator.class);

    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates the default admin user if it does not yet exist.
     *
     * <p>Executed once after Spring context initialization.
     */
    @PostConstruct
    public void generateAdminUser() {
        if (userRepository.findUserByEmail(ADMIN_EMAIL) != null) {
            LOGGER.info("Admin user already exists, skipping generation");
            return;
        }

        ApplicationUser admin = new ApplicationUser(
            ADMIN_EMAIL,
            passwordEncoder.encode(ADMIN_PASSWORD),
            true
        );

        userRepository.addUser(admin);

        LOGGER.info("Default admin user created: {}", ADMIN_EMAIL);
    }
}
