package com.example.demo.repository;

import com.example.demo.entity.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<MunicipioEntity, Integer> {
    List<MunicipioEntity> findDistinctByEstadoIdOrderByNomeAsc(Integer estadoId);
    java.util.Optional<MunicipioEntity> findByNomeIgnoreCaseAndEstadoId(String nome, Integer estadoId);
}
