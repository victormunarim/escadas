package com.example.demo.tarefas.repository;

import com.example.demo.tarefas.model.TarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface TarefaRepository
        extends JpaRepository<TarefaEntity, Long>,
        JpaSpecificationExecutor<TarefaEntity> {

    List<TarefaEntity> findByExtChaveAndExtIdOrderByDataCadastroDesc(String extChave, Long extId);
}
