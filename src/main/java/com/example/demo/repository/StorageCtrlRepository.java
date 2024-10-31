package com.example.demo.repository;

import com.example.demo.entities.StorageControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageCtrlRepository extends JpaRepository<StorageControl, Long> {

    StorageControl findByProdIdAndStorageId(Long prodId, Long StorageId);
    List<StorageControl> findByProdId(Long prodId);
    List<StorageControl> findByStorageId(Long storageId);
}
