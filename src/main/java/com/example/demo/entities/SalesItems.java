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
    public SalesItems(SalesItemsDTO salesItemsDTO, Product product, Sale sale, StorageCenter storageCenter) {
        this.product = product;
        this.qnt = salesItemsDTO.getQuantity();
        this.sale = sale;
        this.storageCenter = storageCenter;
        this.discount = Discount.valueOf(salesItemsDTO.getDiscountType());
        this.prodValue = product.getPrice();
        this.subTotal = calcSubTotal(discount, product.getPrice(), salesItemsDTO.getDiscountValue(), salesItemsDTO.getQuantity());

    }


    private Double calcSubTotal(Discount discount, Double price, Double value, Long quantity) {
        double subTotal = 0.0;
        if(discount.name().equals("DECIMAL")) {
            subTotal = (price - value) * quantity;
        } else if (discount.name().equals("PERCENTAGE")) {
            subTotal = (price - (price/100 * value)) * quantity;
        }
        return subTotal;
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
