package com.example.demo.orcamentos.repository;

import com.example.demo.orcamentos.model.OrcamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrcamentoRepository
        extends JpaRepository<OrcamentoEntity, Long>,
        JpaSpecificationExecutor<OrcamentoEntity> {
    
    List<OrcamentoEntity> findByFlagOcultoFalseAndFlagEncerradoFalseOrderByIdDesc();
}
