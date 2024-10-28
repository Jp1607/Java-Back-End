package com.example.demo.dto;

import com.example.demo.entities.StockFlow;

public class StockFlowDTO {

    private Long id;
    private Long prodId;
    private Long storageId;
    private Long qnt;
    private String flow;
    private String date;

    public StockFlowDTO(){}
    public StockFlowDTO(StockFlow stockFlow) {
        this.id = stockFlow.getId();
        this.prodId = stockFlow.getProduct().getId();
        this.storageId = stockFlow.getStorageCenter().getId();
        this.qnt = stockFlow.getQnt();
        this.flow = stockFlow.getFlow().getDescription();
        this.date = stockFlow.getDate().toString();
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

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }
}
