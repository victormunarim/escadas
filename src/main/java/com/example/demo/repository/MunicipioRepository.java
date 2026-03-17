package com.example.demo.repository;

import com.example.demo.entity.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<MunicipioEntity, Long> {
    List<MunicipioEntity> findDistinctByUfIgnoreCaseOrderByNomeAsc(String uf);
}
