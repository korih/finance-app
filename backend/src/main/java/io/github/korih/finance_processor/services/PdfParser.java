package io.github.korih.finance_processor.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.korih.finance_processor.models.BankStatement;

@Service
public class PdfParser {
  private static final Pattern MONEY_PATTERN = Pattern.compile(
      "\\b(?:\\d{1,3}(?:,\\d{3})+|\\d+)(?:\\.\\d{2})\\b");
  private static final String STUDENT_PATTERN = 
          "[A-Z]{3} \\d{1,2}/\\d{2} - [A-Z]{3} \\d{1,2}/\\d{2}\\s*\\n";

  public BankStatement extractBankStatement(MultipartFile file) {
    try (PDDocument document = PDDocument.load(file.getInputStream())) {
      PDFTextStripper stripper = new PDFTextStripper();
      String text = stripper.getText(document);
      var initAmount = findStartingBalanceOfStatement(text);
      var changes = findWithdrawalsAndDeposits(text);
      var withdrawals = changes[0];
      var deposits = changes[1];
      var range = getBankStatementRange(text);

      var bankstatement = new BankStatement(initAmount, withdrawals, deposits, range, null);

      return bankstatement;
    } catch (IOException e) {
      return null;
    }

  }

  private String getBankStatementRange(String text) {
    var pattern = Pattern.compile(STUDENT_PATTERN);
    var matcher = pattern.matcher(text);

    if (matcher.find()) {
      return matcher.group().strip();
    }
    return null;
  }

  private BigDecimal findStartingBalanceOfStatement(String text) {
    var startingIndex = text.indexOf("STARTING BALANCE");
    var endingIndex = text.indexOf('\n', startingIndex);
    var startingBalance = text.substring(startingIndex, endingIndex);
    var matcher = MONEY_PATTERN.matcher(startingBalance);

    matcher.find();
    var stringBalance = matcher.group();

    return stringToDouble(stringBalance);
  }

  private BigDecimal[] findWithdrawalsAndDeposits(String text) {
    String[] lines = text.strip().split("\\R");
    String lastLine = lines[lines.length - 1];
    var matcher = MONEY_PATTERN.matcher(lastLine);

    if (matcher.find()) {
      var withdrawals = stringToDouble(matcher.group());
      if (matcher.find()) {
        var deposits = stringToDouble(matcher.group());
        return new BigDecimal[] { withdrawals, deposits };
      }
    }

    return null;
  }

  private BigDecimal stringToDouble(String string) {
    var processedString = string.replace(",", "");
    BigDecimal amount = new BigDecimal(processedString);
    return amount;
  }

  public Object extractCreditCardStatement(MultipartFile file) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'extractCreditCardStatement'");
  }

}
