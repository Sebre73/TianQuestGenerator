package at.ac.tuwien.sepr.groupphase.backend.basetest;

import java.util.List;

public interface TestData {

    String BASE_URI = "/api/v1";

    String ADMIN_USER = "admin@email.com";
    String DEFAULT_USER = "admin@email.com";

    List<String> ADMIN_ROLES = List.of(
        "ROLE_ADMIN",
        "ROLE_USER"
    );

    List<String> USER_ROLES = List.of(
        "ROLE_USER"
    );
}
