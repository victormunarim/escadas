package com.example.demo.shared.arquivos.repository;

import com.example.demo.shared.arquivos.model.ArquivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArquivoRepository extends JpaRepository<ArquivoEntity, Long> {

    List<ArquivoEntity> findByExtChaveAndExtIdOrderByNomeAsc(String extChave, Long extId);
}
