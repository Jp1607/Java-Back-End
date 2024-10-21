package com.example.demo.dto;

import com.example.demo.entities.StorageCenter;

public class StorageCenterDTO extends DefaultDTO{

    public StorageCenterDTO(StorageCenter storageCenter) {
        this.description = storageCenter.getName();
        this.active = storageCenter.getActive().compareTo(1) == 0;
        this.killed = storageCenter.getKilled().compareTo(1) == 0;
    }
}
