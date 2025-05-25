package io.github.korih.finance_processor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.korih.finance_processor.models.BankStatements;
import io.github.korih.finance_processor.services.BankStatementService;
import io.github.korih.finance_processor.services.PdfParser;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/extract")
public class PdfUpload {
  PdfParser parser;
  BankStatementService bankStatementService;

  public PdfUpload(PdfParser parser, BankStatementService bankStatementService) {
    this.parser = parser;
    this.bankStatementService = bankStatementService;
  }

  @PostMapping(value = "/bankstatement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> extractBankStatementEntity(@RequestParam("file") MultipartFile file) {
    var statement = parser.extractBankStatement(file);
    if (statement == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(statement);
  }

  @PostMapping(value = "/creditstatement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> extractCreditStatementEntity(@RequestParam("file") MultipartFile file) {
    var statement = parser.extractCreditCardStatement(file);
    if (statement == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(statement);
  }

  @GetMapping("/statements")
  public ResponseEntity<BankStatements> getMethodName() {
      return ResponseEntity.ok(new BankStatements(bankStatementService.getBankStatementByVersion("v1")));
  }

  @DeleteMapping(value = "/deleteId/{id}")
  public ResponseEntity<Void> deleteBankStatementId(@PathVariable Long id) {
    boolean deleted = bankStatementService.deleteBankStatementId(id);
    if (deleted) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
