package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.BackendApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Minimal security integration tests.
 *
 * <p>
 * These tests intentionally cover only existing endpoints.
 * The goal is to verify the basic security setup (health + authentication)
 * without introducing artificial protected endpoints.
 * </p>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = BackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Health endpoint must always be publicly accessible.
     */
    @Test
    public void healthEndpointIsPublic() throws Exception {
        MvcResult result = mockMvc.perform(get("/health")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Login with valid credentials must succeed.
     */
    @Test
    public void loginWithValidCredentialsReturns200() throws Exception {
        String body = """
            {
              "email": "admin@email.com",
              "password": "password"
            }
            """;

        MvcResult result = mockMvc.perform(
            post("/api/v1/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Login with invalid credentials must be rejected.
     *
     * <p>
     * Spring Security responds with 403 Forbidden for failed authentication
     * attempts to avoid leaking information about valid users.
     * </p>
     */
    @Test
    public void loginWithInvalidCredentialsReturns403() throws Exception {
        String body = """
            {
              "email": "admin@email.com",
              "password": "wrong"
            }
            """;

        MvcResult result = mockMvc.perform(
            post("/api/v1/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

    /**
     * Login without request body must fail validation.
     */
    @Test
    public void loginWithoutBodyReturns400() throws Exception {
        MvcResult result = mockMvc.perform(
            post("/api/v1/authentication")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }
}

