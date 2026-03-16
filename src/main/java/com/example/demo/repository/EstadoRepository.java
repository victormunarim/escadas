package com.example.demo.repository;

import com.example.demo.entity.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository extends JpaRepository<EstadoEntity, String> {
    List<EstadoEntity> findDistinctByUfIsNotNullOrderByUfAsc();
}
