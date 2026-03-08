package com.example.demo.pedidos.localidade.repository;

import com.example.demo.pedidos.localidade.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioEstado extends JpaRepository<Estado, String> {
    List<Estado> findDistinctByUfIsNotNullOrderByUfAsc();
}
