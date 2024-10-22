package com.example.demo.entities;
import com.example.demo.dto.StorageCenterDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "storage_center")
public class StorageCenter extends DefaultEntities{
    @Column(name = "description")
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String name) {
        this.description = name;
    }

    public StorageCenter(){}
    public StorageCenter(StorageCenterDTO storageCenterDTO) {
        this.description = storageCenterDTO.getDescription();
        this.active = storageCenterDTO.getActive() ? 1 : 0;
        this.killed = storageCenterDTO.getKilled() ? 1 : 0;
    }
}
