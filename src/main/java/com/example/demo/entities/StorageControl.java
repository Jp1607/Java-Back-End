package com.example.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "storage_control")
public class StorageControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @Column(name = "product_id")
    private Long prodId;

    @Column(name = "storage_id")
    private Long storageId;

    @Column(name = "quantity")
    private Long qnt;

    public StorageControl(){}
    public StorageControl(Long prodId, Long storageId, Long qnt) {
        this.prodId = prodId;
        this.storageId = storageId;
        this.qnt = qnt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getQnt() {
        return qnt;
    }

    public void setQnt(Long qnt) {
        this.qnt = qnt;
    }
}
