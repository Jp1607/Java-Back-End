package com.example.demo.entities;
import com.example.demo.dto.StorageCenterDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "storage_center")
public class StorageCenter extends DefaultEntities{
    @Column(name = "description")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StorageCenter(StorageCenterDTO storageCenterDTO) {
        this.name = storageCenterDTO.getDescription();
        this.active = storageCenterDTO.getActive() ? 1 : 0;
        this.killed = storageCenterDTO.getKilled() ? 1 : 0;
    }
}
