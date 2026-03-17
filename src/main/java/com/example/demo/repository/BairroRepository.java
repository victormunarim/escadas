package com.example.demo.repository;

import com.example.demo.entity.BairroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BairroRepository extends JpaRepository<BairroEntity, Long> {
    List<BairroEntity> findDistinctByUfIgnoreCaseOrderByNomeAsc(String uf);
}
