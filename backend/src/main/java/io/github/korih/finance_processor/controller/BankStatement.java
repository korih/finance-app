package io.github.korih.finance_processor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.korih.finance_processor.models.BankStatements;
import io.github.korih.finance_processor.models.Session;
import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.services.BankStatementService;
import io.github.korih.finance_processor.services.JwtService;
import io.github.korih.finance_processor.services.SessionService;
import io.github.korih.finance_processor.services.StatementParser;
import io.github.korih.finance_processor.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/bankstatement")
@RequiredArgsConstructor
public class BankStatement {
  private final StatementParser parser;
  private final BankStatementService bankStatementService;
  private final UserService userService;
  private final JwtService jwtService;
  private final SessionService sessionService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> extractBankStatementEntity(
      @CookieValue(value = "authToken", required = true) String cookie,
      @RequestParam("file") MultipartFile file) {
        System.out.println("HERE");
    User user = parseCookieSession(cookie);

    var statement = parser.extractBankStatement(user, file);
    if (statement == null) {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(statement);
  }

  @GetMapping("/statements")
  public ResponseEntity<BankStatements> getMethodName(
      @CookieValue(value = "authToken", required = true) String cookie) {
    User user = parseCookieSession(cookie);
    UUID ownerId = user.getId();

    return ResponseEntity.ok(new BankStatements(bankStatementService.getBankStatementByUser(ownerId)));
  }

  @DeleteMapping(value = "/deleteId/{id}")
  public ResponseEntity<Void> deleteBankStatementId(
      @CookieValue(value = "authToken", required = true) String cookie,
      @PathVariable Long id) {
    parseCookieSession(cookie);
    boolean deleted = bankStatementService.deleteBankStatementId(id);
    if (deleted) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  private User parseCookieSession(String cookie) {
    if (!jwtService.isTokenValid(cookie)) {
      throw new Error();
    }

    String sessionId = jwtService.extractClaim(cookie, claim -> claim
        .get("session_id")
        .toString());
    Session session = sessionService.getSessionById(sessionId);
    UUID userId = session.getUserId(); 
    User user = userService.findById(userId).get();
    if (session.getExpiration().before(new Date())) {
      throw new Error();
    }

    return user;
  } 

}
