package com.example.demo.tarefas.repository;

import com.example.demo.tarefas.model.TipoTarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoTarefaRepository extends JpaRepository<TipoTarefaEntity, Long> {

    Optional<TipoTarefaEntity> findByNomeIgnoreCase(String nome);
}
