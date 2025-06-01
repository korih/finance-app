package io.github.korih.finance_processor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.korih.finance_processor.models.BankStatement;
import io.github.korih.finance_processor.models.repositories.BankStatementRepository;

@Service
public class BankStatementService {

  @Autowired
  private BankStatementRepository bankStatementRepository;

  public BankStatement createBankStatement(BankStatement bankStatement) {
    if (bankStatementRepository.existsByStatementRange(bankStatement.getStatementRange())) {
      return bankStatement;
    }
    return bankStatementRepository.save(bankStatement);
  }

  public List<BankStatement> getBankStatementByVersion(String version) {
    return bankStatementRepository.findByVersion(version);
  }

  public boolean deleteBankStatementId(Long id) {
    bankStatementRepository.deleteById(id);
    if (bankStatementRepository.existsById(id)) {
      return false;
    }
    return true;
  }
  
}
