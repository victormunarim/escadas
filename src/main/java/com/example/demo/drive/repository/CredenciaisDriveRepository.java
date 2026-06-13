package com.example.demo.drive.repository;
import com.example.demo.drive.model.CredenciaisDriveEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredenciaisDriveRepository extends JpaRepository<CredenciaisDriveEntity, String> {
}