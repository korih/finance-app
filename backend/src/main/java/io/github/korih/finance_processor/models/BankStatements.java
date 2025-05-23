package io.github.korih.finance_processor.models;

import java.util.List;

public class BankStatements {
  List<BankStatement> bankStatements;

  public BankStatements(List<BankStatement> bankStatements) {
    this.bankStatements = bankStatements;
  }

  public List<BankStatement> getBankStatements() {
    return bankStatements;
  }

}
