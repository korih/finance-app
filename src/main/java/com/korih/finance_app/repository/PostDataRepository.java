package com.korih.finance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.korih.finance_app.models.reddit.entity.PostEntity;

public interface PostDataRepository extends JpaRepository<PostEntity, String> {
    
}
