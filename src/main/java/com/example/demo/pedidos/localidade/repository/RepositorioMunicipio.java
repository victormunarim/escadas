package com.example.demo.pedidos.localidade.repository;

import com.example.demo.pedidos.localidade.model.LocalidadeId;
import com.example.demo.pedidos.localidade.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioMunicipio extends JpaRepository<Municipio, LocalidadeId> {
    List<Municipio> findDistinctByIdUfIgnoreCaseOrderByIdNomeAsc(String uf);
}
