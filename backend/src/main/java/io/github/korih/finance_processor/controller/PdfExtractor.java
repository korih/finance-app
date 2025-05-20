package io.github.korih.finance_processor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.korih.finance_processor.services.PdfParser;

import javax.print.attribute.standard.MediaTray;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/extract")
public class PdfExtractor {
  PdfParser parser;

  public PdfExtractor(PdfParser parser) {
    this.parser = parser;
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

}
