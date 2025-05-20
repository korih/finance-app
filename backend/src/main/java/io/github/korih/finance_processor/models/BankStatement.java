package io.github.korih.finance_processor.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.micrometer.common.lang.Nullable;

public class BankStatement {
  private final String date;
  private final List<RowEntry> rows;
  private final String version;
  private final BigDecimal initialAmount;
  private final BigDecimal withdrawals;
  private final BigDecimal deposits;
  private final BigDecimal finalAmount;
  private final String statementRange;


  public BankStatement(BigDecimal startingAmount, BigDecimal withdrawals, BigDecimal deposits, String range, @Nullable List<RowEntry> entries) {
    LocalDate date = LocalDate.now();
    this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    this.version = "v1";
    this.initialAmount = startingAmount;
    this.withdrawals = withdrawals;
    this.deposits = deposits;
    this.statementRange = range;

    this.rows = entries;
    this.finalAmount = calculateEndingAmount(startingAmount, withdrawals, deposits);
  }

  private BigDecimal calculateEndingAmount(BigDecimal startingAmount, BigDecimal withdrawals, BigDecimal deposits) {
    return startingAmount.add(deposits).subtract(withdrawals);
  }

  public String getDate() {
    return date;
  }

  public List<RowEntry> getRows() {
    return rows;
  }

  public String getVersion() {
    return version;
  }

  public BigDecimal getInitialAmount() {
    return initialAmount;
  }

  public BigDecimal getWithdrawals() {
    return withdrawals;
  }

  public BigDecimal getDeposits() {
    return deposits;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public String getStatementRange() {
    return statementRange;
  }

}
