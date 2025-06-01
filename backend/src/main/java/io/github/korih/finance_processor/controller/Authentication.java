package io.github.korih.finance_processor.controller;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.models.dtos.LoginDto;
import io.github.korih.finance_processor.models.dtos.LoginResponse;
import io.github.korih.finance_processor.models.dtos.RegisterClassDto;
import io.github.korih.finance_processor.services.AuthenticationService;
import io.github.korih.finance_processor.services.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class Authentication {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody RegisterClassDto entity) {
        User user = authenticationService.signUp(entity);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody LoginDto entity) {
        User user = authenticationService.authenticate(entity);
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("authToken", token)
        .httpOnly(false)
        .secure(false)
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

}
