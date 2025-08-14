package com.korih.finance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.korih.finance_app.models.reddit.entity.CommentEntity;

public interface CommentDataRepository extends JpaRepository<CommentEntity, String> {
  
}
