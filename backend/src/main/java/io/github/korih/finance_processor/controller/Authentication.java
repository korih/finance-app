package io.github.korih.finance_processor.controller;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.korih.finance_processor.models.Session;
import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.models.auth.LoginDto;
import io.github.korih.finance_processor.models.auth.LoginResponse;
import io.github.korih.finance_processor.models.auth.RegisterClassDto;
import io.github.korih.finance_processor.models.repositories.SessionRepository;
import io.github.korih.finance_processor.services.AuthenticationService;
import io.github.korih.finance_processor.services.JwtService;
import io.github.korih.finance_processor.services.SessionService;
import io.github.korih.finance_processor.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class Authentication {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final SessionService sessionService;
    private final UserService userService;

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody RegisterClassDto entity) {
        User user = authenticationService.signUp(entity);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody LoginDto entity) {
        User user = authenticationService.authenticate(entity);
        String sessionId = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        Map<String, Object> claims = Map.of(
                "session_id", sessionId,
                "refresh_token", refreshToken);

        String token = jwtService.generateToken(claims, user);
        sessionService.createSession(UUID.fromString(sessionId), user);

        ResponseCookie cookie = ResponseCookie
                .from("authToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(900)
                .sameSite(SameSiteCookies.LAX.toString())
                .build();

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiration(jwtService.getExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "authToken", required = true) String cookie) {
        String oldRefreshToken = jwtService.extractClaim(cookie, claim -> claim
                .get("refresh_token")
                .toString());
        Session session = sessionService.getSessionByRefreshToken(oldRefreshToken);
        if (Objects.isNull(session)) {
            return ResponseEntity.badRequest().build();
        }

        UUID userId = session.getUserId();
        User user = userService.findById(userId).get();
        String sessionId = UUID.randomUUID().toString();

        String refreshToken = UUID.randomUUID().toString();
        Map<String, Object> claims = Map.of(
                "session_id", sessionId,
                "refresh_token", refreshToken);

        String token = jwtService.generateToken(claims, user);
        sessionService.createSession(UUID.fromString(sessionId), user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token.toString())
                .build();
    }

}
