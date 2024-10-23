package com.example.demo.dto;

import com.example.demo.Enum.Discount;
import com.example.demo.entities.Product;
import com.example.demo.entities.StorageCenter;

public class SalesItemsDTO {

    private Long quantity;

    private StorageCenter storageCenter;

    private Product product;

    private Discount discountType;

    private Double discountValue;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Discount getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Discount discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public StorageCenter getStorageCenter() {
        return storageCenter;
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        this.storageCenter = storageCenter;
    }

    @Override
    public String toString() {
        return "SalesItemsDTO{" +
                "quantity=" + quantity +
                ", storageCenter=" + storageCenter +
                ", product=" + product +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                '}';
    }
}
