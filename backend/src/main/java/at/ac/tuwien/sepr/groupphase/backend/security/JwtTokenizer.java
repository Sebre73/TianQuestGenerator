package at.ac.tuwien.sepr.groupphase.backend.security;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Creates JSON Web Tokens (JWT) for authenticated users.
 *
 * <p>Tokens are signed using HMAC-SHA512 and contain user roles as custom claims.
 */
@Component
public class JwtTokenizer {

    private static final int MIN_SECRET_LENGTH_BYTES = 64;

    private final SecurityProperties securityProperties;
    private final Clock clock;

    public JwtTokenizer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.clock = Clock.systemUTC();
    }

    /**
     * Creates a signed authentication token for the given user.
     *
     * <p>The token contains the username as subject and the granted roles as claims.
     *
     * @param username the authenticated username
     * @param roles the granted roles
     * @return the authorization token including prefix
     */
    public String getAuthToken(String username, List<String> roles) {
        SecretKey signingKey = createSigningKey();

        Instant now = clock.instant();
        Instant expiration = now.plusMillis(securityProperties.getJwtExpirationTime());

        String jwt = Jwts.builder()
            .header()
            .type(securityProperties.getJwtType())
            .and()
            .issuer(securityProperties.getJwtIssuer())
            .audience()
            .add(securityProperties.getJwtAudience())
            .and()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("roles", roles)
            .signWith(signingKey, Jwts.SIG.HS512)
            .compact();

        return securityProperties.getAuthTokenPrefix() + jwt;
    }

    /**
     * Creates the signing key from configuration.
     *
     * <p>The secret must meet the minimum length requirement for HS512.
     */
    private SecretKey createSigningKey() {
        byte[] secretBytes = securityProperties
            .getJwtSecret()
            .getBytes(StandardCharsets.UTF_8);

        if (secretBytes.length < MIN_SECRET_LENGTH_BYTES) {
            throw new IllegalStateException(
                "JWT secret is too short for HS512 (minimum 64 bytes required)"
            );
        }

        return Keys.hmacShaKeyFor(secretBytes);
    }
}
