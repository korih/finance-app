package io.github.korih.finance_processor.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_statements")
@NoArgsConstructor
public class BankStatement {

  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID ownerId;

  @Column(nullable = false)
  private String statementRange;

  private Date date;
  private String version;
  private BigDecimal initialAmount;
  private BigDecimal withdrawals;
  private BigDecimal deposits;
  private BigDecimal finalAmount;

  public BankStatement(UUID ownerId, BigDecimal startingAmount, BigDecimal withdrawals, BigDecimal deposits, String range) {
    id = UUID.randomUUID();
    this.date = new Date();
    this.version = "v1";
    this.initialAmount = startingAmount;
    this.withdrawals = withdrawals;
    this.deposits = deposits;
    this.statementRange = range;
    this.ownerId = ownerId;

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

  public UUID getId() {
    return id;
  }

  public Date getDate() {
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
