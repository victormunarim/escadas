package com.example.demo.localidades.repository;
import com.example.demo.localidades.model.MunicipioEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<MunicipioEntity, Integer> {
    List<MunicipioEntity> findDistinctByEstadoIdOrderByNomeAsc(Integer estadoId);
    java.util.Optional<MunicipioEntity> findByNomeIgnoreCaseAndEstadoId(String nome, Integer estadoId);
}