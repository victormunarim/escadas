package com.example.demo.repository;

import com.example.demo.entity.LocalidadeIdEntity;
import com.example.demo.entity.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<MunicipioEntity, LocalidadeIdEntity> {
    List<MunicipioEntity> findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(String uf);
}
