package com.example.demo.repository;

import com.example.demo.entity.BairroEntity;
import com.example.demo.entity.LocalidadeIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BairroRepository extends JpaRepository<BairroEntity, LocalidadeIdEntity> {
    List<BairroEntity> findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(String uf);
}
