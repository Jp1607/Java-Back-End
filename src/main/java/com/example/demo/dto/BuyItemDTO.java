package com.example.demo.dto;

public class BuyItemDTO {

    private Long quantity;

    private Long storageCenterId;

    private Long productId;

    @Override
    public String toString() {
        return "SalesItemsDTO{" +
                "quantity=" + quantity +
                ", storageCenterId=" + storageCenterId +
                ", productId=" + productId +
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
}
