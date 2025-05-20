package io.github.korih.finance_processor.models;

import java.math.BigDecimal;
import java.util.Date;

public class RowEntry {
  private final String description;
  private final BigDecimal change;
  private final Date date;
  private final BigDecimal amount;
  private final String version;

  public RowEntry (String description, BigDecimal delta, Date date, BigDecimal amount) {
    this.description = description;
    this.change = delta;
    this.date = date;
    this.amount = amount;
    this.version = "v1";
  }

  public String getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }
  public BigDecimal getChange() {
    return change;
  }
  public Date getDate() {
    return date;
  }
  public BigDecimal getAmount() {
    return amount;
  } 
}
