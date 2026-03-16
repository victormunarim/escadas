package com.example.demo.repository;

import com.example.demo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmailIgnoreCase(String email);

    Optional<UsuarioEntity> findByNomeIgnoreCase(String nome);
}
