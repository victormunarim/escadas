package com.example.demo.pedidos.localidade.repository;

import com.example.demo.pedidos.localidade.model.Bairro;
import com.example.demo.pedidos.localidade.model.LocalidadeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioBairro extends JpaRepository<Bairro, LocalidadeId> {
    List<Bairro> findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(String uf);
}
