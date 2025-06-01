package io.github.korih.finance_processor.models.repositories;

import org.springframework.data.repository.CrudRepository;

import io.github.korih.finance_processor.models.BankStatement;
import java.util.List;


public interface BankStatementRepository extends CrudRepository<BankStatement, Long> {

  List<BankStatement> findByVersion(String version);

  boolean existsByStatementRange(String statementRange);
  
}
