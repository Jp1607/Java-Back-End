package com.example.demo.dto;

public class ReturnProdInfo {

    private Long id;
    private int quantity;
    private double subTotal;
    private double total;

    public ReturnProdInfo(){}
    public ReturnProdInfo(int quantity, double subTotal, double total) {
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.total = total;
    }
    public ReturnProdInfo(Long id, int quantity, double subTotal, double total) {
        this.id = id;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
