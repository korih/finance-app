package io.github.korih.finance_processor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.korih.finance_processor.models.BankStatements;
import io.github.korih.finance_processor.services.BankStatementService;
import io.github.korih.finance_processor.services.JwtService;
import io.github.korih.finance_processor.services.StatementParser;
import lombok.RequiredArgsConstructor;

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
  private final JwtService jwtService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> extractBankStatementEntity(
    @CookieValue(value = "", required = true) String cookie,
    @RequestParam("file") MultipartFile file
    ) {
    var statement = parser.extractBankStatement(file);
    if (statement == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(statement);
  }

  @GetMapping("/statements")
  public ResponseEntity<BankStatements> getMethodName(
    @CookieValue(value = "authToken", required = true) String authToken
    ) {
      if (!jwtService.isTokenValid(authToken)) {
        return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok(new BankStatements(bankStatementService.getBankStatementByVersion("v1")));
  }

  @DeleteMapping(value = "/deleteId/{id}")
  public ResponseEntity<Void> deleteBankStatementId(
    @CookieValue(value = "authToken", required = true) String cookie,
    @PathVariable Long id
    ) {
    boolean deleted = bankStatementService.deleteBankStatementId(id);
    if (deleted) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
