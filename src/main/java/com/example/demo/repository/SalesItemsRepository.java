package com.example.demo.repository;

import com.example.demo.entities.SalesItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemsRepository extends JpaRepository<SalesItems, Long> {}
