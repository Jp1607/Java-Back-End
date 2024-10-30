package com.example.demo.entities;

import com.example.demo.Enum.Discount;
import com.example.demo.dto.SalesItemsDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class SalesItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sales_id", referencedColumnName = "id")
    private Sale sale;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private StorageCenter storageCenter;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", columnDefinition = "varchar(50)", length = 50, nullable = true)
    private Discount discount;

    @Column(name = "quantity")
    private Long qnt;

    @Column(name = "product_value")
    private Double prodValue;

    @Column(name = "sub_total")
    private Double subTotal;

    public SalesItems() {
    }

    public SalesItems(SalesItemsDTO salesItemsDTO, Product product, StorageCenter storageCenter, Discount discount, Double discountValue) {
        this.product = product;
        this.subTotal = Objects.equals(discount, Discount.DECIMAL.name()) ? ((product.getPrice() - discountValue) * salesItemsDTO.getQuantity()) : ((product.getPrice() - product.getPrice() / 100 * discountValue) * salesItemsDTO.getQuantity());
        this.discount = Discount.valueOf(discount.name());
        this.prodValue = product.getPrice();
        this.qnt = salesItemsDTO.getQuantity();
        this.storageCenter = storageCenter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSales() {
        return sale;
    }

    public void setSales(Sale sales) {
        this.sale = sales;
    }

    public Product getProd() {
        return product;
    }

    public void setProd(Product prod) {
        this.product = prod;
    }

    public StorageCenter getStorageCenter() {
        return storageCenter;
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        this.storageCenter = storageCenter;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Long getQnt() {
        return qnt;
    }

    public void setQnt(Long qnt) {
        this.qnt = qnt;
    }

    public Double getProdValue() {
        return prodValue;
    }

    public void setProdValue(Double prodValue) {
        this.prodValue = prodValue;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
