package com.example.demo.entities;

import com.example.demo.Enum.Flow;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock_flow")
public class StockFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private StorageCenter storageCenter;

    @Column(name = "quantity")
    private Long qnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", columnDefinition = "varchar(50)", length = 50, nullable = false)
    private Flow flow;

    @Column(name = "date")
    private Date date;

    public StockFlow(){}
    public StockFlow(StorageCenter storageCenter, Product product, Date date, Flow flow, Long qnt) {
        this.product = product;
        this.storageCenter = storageCenter;
        this.date = date;
        this.flow = flow;
        this.qnt = qnt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQnt() {
        return qnt;
    }

    public void setQnt(Long qnt) {
        this.qnt = qnt;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StorageCenter getStorageCenter() {
        return storageCenter;
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        this.storageCenter = storageCenter;
    }
}
