package com.example.demo.repository;

import com.example.demo.dto.PedidoResumoDTO;
import com.example.demo.entity.PedidoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PedidoRepositoryCustom {
    List<PedidoResumoDTO> buscarResumo(Specification<PedidoEntity> spec, Pageable pageable);
}
