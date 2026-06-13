package com.example.demo.localidades.repository;
import com.example.demo.localidades.model.EstadoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {
    List<EstadoEntity> findAllByOrderByNomeAsc();
    java.util.Optional<EstadoEntity> findBySiglaIgnoreCase(String sigla);
}