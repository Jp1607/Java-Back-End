package com.example.demo.repository;
import com.example.demo.entities.StorageCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<StorageCenter, Long> {}
