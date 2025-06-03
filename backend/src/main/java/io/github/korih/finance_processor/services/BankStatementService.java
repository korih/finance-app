package io.github.korih.finance_processor.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.korih.finance_processor.models.BankStatement;
import io.github.korih.finance_processor.models.User;
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

  public List<BankStatement> getBankStatementByUser(UUID id) {
    return bankStatementRepository.findByOwnerId(id);
  }

  public boolean deleteBankStatementId(UUID id) {
    bankStatementRepository.deleteById(id);
    if (bankStatementRepository.existsById(id)) {
      return false;
    }
    return true;
  }
  
}
