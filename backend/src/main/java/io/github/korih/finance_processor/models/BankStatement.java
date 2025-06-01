package io.github.korih.finance_processor.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BankStatement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private long ownerId;

  @Column(nullable = false)
  private String statementRange;

  private String date;
  private String version;
  private BigDecimal initialAmount;
  private BigDecimal withdrawals;
  private BigDecimal deposits;
  private BigDecimal finalAmount;

  protected BankStatement() {}

  public BankStatement(BigDecimal startingAmount, BigDecimal withdrawals, BigDecimal deposits, String range) {
    LocalDate date = LocalDate.now();
    this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    this.version = "v1";
    this.initialAmount = startingAmount;
    this.withdrawals = withdrawals;
    this.deposits = deposits;
    this.statementRange = range;

    this.finalAmount = calculateEndingAmount(startingAmount, withdrawals, deposits);
  }

  @Override
  public String toString() {
    return String.format("Bank_Statement[range=%s, initialAmount=%s, withdrawals=%s, deposits=%s, finalAmount=%s]",
     statementRange, initialAmount.toString(), withdrawals.toString(), deposits.toString(), finalAmount.toString());
  }

  private BigDecimal calculateEndingAmount(BigDecimal startingAmount, BigDecimal withdrawals, BigDecimal deposits) {
    return startingAmount.add(deposits).subtract(withdrawals);
  }

  public long getId() {
    return id;
  }

  public String getDate() {
    return date;
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
