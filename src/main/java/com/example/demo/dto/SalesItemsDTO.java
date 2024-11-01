package com.example.demo.dto;

public class SalesItemsDTO {

    private Long quantity;

    private Long storageCenterId;

    private Long productId;

    private String discountType;

    private Double discountValue;

    @Override
    public String toString() {
        return "SalesItemsDTO{" +
                "quantity=" + quantity +
                ", storageCenterId=" + storageCenterId +
                ", productId=" + productId +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                '}';
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStorageCenterId() {
        return storageCenterId;
    }

    public void setStorageCenterId(Long storageCenterId) {
        this.storageCenterId = storageCenterId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }
}
