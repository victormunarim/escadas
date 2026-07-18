package com.example.demo.localidades.repository;

import com.example.demo.localidades.model.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {
    List<EstadoEntity> findAllByOrderByNomeAsc();
    Optional<EstadoEntity> findBySiglaIgnoreCase(String sigla);
}