package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.Enum.Discount;
import com.example.demo.Enum.Flow;
import com.example.demo.Enum.Payment;
import com.example.demo.dto.ReturnProdInfo;
import com.example.demo.dto.SalesItemsDTO;
import com.example.demo.entities.*;
import com.example.demo.repository.*;
import com.example.demo.service.HttpSessionService;
import com.example.demo.service.LogService;
import com.example.demo.session.HttpSessionParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping(value = "/sale")
public class SalesController {

    @Autowired
    private LogService logService;

    @Autowired
    private StorageCtrlRepository storageCtrlRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StockFlowRepository stockFlowRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SalesItemsRepository salesItemsRepository;

    @Autowired
    private HttpSessionService httpSessionService;

    private Sale sale;
    private List<SalesItems> salesItems;

    @PostMapping(value = "/start", produces = "text/plain")
    public void start(@RequestHeader("Authorization") String token) {
        Date date = new Date();
        HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
        User user = new User();
        user.setId(http.getUserDetails().getId());
        sale = new Sale();
        saleRepository.save(sale);
        sale.setDate(date);
        sale.setUser(user);
        salesItems = new ArrayList<>();

    }

    @GetMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> listStorages(@RequestParam(value = "prodId") Long prodId) {
        String body = "";
        HttpStatus status = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<StorageControl> storageControls = storageCtrlRepository.findByProdId(prodId);
            List<StorageCenter> storageCenters = new ArrayList<>();
            for (StorageControl storageControl : storageControls) {
                StorageCenter tempStorage = storageRepository.findById(storageControl.getStorageId()).get();
                storageCenters.add(tempStorage);
            }
            body = mapper.writeValueAsString(storageCenters);
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            body = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(body);
    }

    @PostMapping(value = "/add", produces = "text/plain")
    public ResponseEntity<String> addProduct(@RequestBody SalesItemsDTO salesItemsDTO) {

        HttpStatus status;
        String body;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Product product = productRepository.findById(salesItemsDTO.getProductId()).get();
            StorageCenter storageCenter = storageRepository.findById(salesItemsDTO.getStorageCenterId()).get();
            SalesItems salesItems1 = new SalesItems(salesItemsDTO, product, sale, storageCenter);
            StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(product.getId(), storageCenter.getId());
            System.out.println(salesItems);
            System.out.println(salesItems1);
            salesItems.add(salesItems1);
            if (salesItemsDTO.getQuantity() > storageControl.getQnt() && product.getNegativeStock().compareTo(0) == 0) {
                return ResponseEntity.status(200).body("Sem produtos suficientes no estoque.");
            }
            int returnQnt = 0;
            double subTotal = 0;
            double total = 0;
            for (SalesItems salesItem : salesItems) {
                returnQnt += salesItem.getQnt();
                subTotal += (salesItem.getProdValue() * salesItem.getQnt());
                total += salesItem.getSubTotal();
                sale.setTotal(sale.getTotal() + salesItem.getSubTotal());
            }
            ReturnProdInfo returnProdInfo = new ReturnProdInfo(salesItems1.getId(), returnQnt, subTotal, total);
            status = HttpStatus.OK;
            body = mapper.writeValueAsString(returnProdInfo);
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getClass().getName();
        }

        return ResponseEntity.status(status.value()).body(body);
    }

    @PostMapping(value = "/remove", produces = "text/plain")
    public ResponseEntity removeProduct(@RequestParam(value = "saleItemId") Long id) {

        HttpStatus status;
        String body;
        ObjectMapper mapper = new ObjectMapper();
        try {
            salesItems.removeIf(salesItems1 -> salesItems1.getId().compareTo(id) == 0);
            int returnQnt = 0;
            double subTotal = 0;
            double total = 0;
            for (SalesItems salesItem : salesItems) {
                returnQnt += salesItem.getQnt();
                subTotal += (salesItem.getProdValue() * salesItem.getQnt());
                total += salesItem.getSubTotal();
            }
            ReturnProdInfo returnProdInfo = new ReturnProdInfo(returnQnt, subTotal, total);
            status = HttpStatus.OK;
            body = mapper.writeValueAsString(returnProdInfo);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getMessage();
        }
        return ResponseEntity.status(status.value()).body(body);
    }

    @PostMapping(value = "clean")
    public ResponseEntity<String> cleanAllProducts() {
        HttpStatus status;
        String body;
        ObjectMapper mapper = new ObjectMapper();
        try {
            salesItems.clear();
            ReturnProdInfo returnProdInfo = new ReturnProdInfo(0, 0, 0);
            status = HttpStatus.OK;
            body = mapper.writeValueAsString(returnProdInfo);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getMessage();
        }
        return ResponseEntity.status(status.value()).body(body);
    }

    @PostMapping(value = "/sell", produces = "text/plain")
    public ResponseEntity<String> sell(@RequestHeader("Authorization") String token,
                                       @RequestParam(value = "payment") Payment payment) {

        try {
            Date date = new Date();
            sale.setPayment(payment);
            for (SalesItems salesItem : salesItems) {
                Product p = salesItem.getProd();
                StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(salesItem.getProd().getId(), salesItem.getStorageCenter().getId());
                if (p.getNegativeStock().compareTo(0) == 0 && Objects.equals(storageControl.getQnt(), salesItem.getQnt())) {
                    storageCtrlRepository.delete(storageControl);
                } else {
                    storageControl.setQnt(storageControl.getQnt() - salesItem.getQnt());
                }
                StockFlow stockFlow = new StockFlow(salesItem.getStorageCenter(), p, date, Flow.EXIT, salesItem.getQnt());
                stockFlowRepository.save(stockFlow);
                salesItemsRepository.save(salesItem);
                p.setCurrentStock(p.getCurrentStock() - salesItem.getQnt());
                productRepository.save(p);
                logService.save(token, Activity.SELL, "stock_flow", null);
            }
            saleRepository.save(sale);
            salesItems.clear();
            return ResponseEntity.status(200).body("Sucesso!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/buy", produces = "text/plain")
    public ResponseEntity<String> buy(@RequestHeader("Authorization") String token,
                                      @RequestBody SalesItemsDTO salesItemsDTO) {

        try {
            Product product = productRepository.findById(salesItemsDTO.getProductId()).get();
            StorageCenter storageCenter = storageRepository.findById(salesItemsDTO.getStorageCenterId()).get();

            Date date = new Date();
            HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
            User user = new User();
            user.setId(http.getUserDetails().getId());
            StorageControl storageCtrl = storageCtrlRepository.findByProdIdAndStorageId(salesItemsDTO.getProductId(), salesItemsDTO.getStorageCenterId());
            if (storageCtrl == null) {
                StorageControl storageControl = new StorageControl(salesItemsDTO.getProductId(), salesItemsDTO.getStorageCenterId(), salesItemsDTO.getQuantity());
                storageCtrlRepository.save(storageControl);
            } else {
                storageCtrl.setQnt(storageCtrl.getQnt() + salesItemsDTO.getQuantity());
                storageCtrlRepository.save(storageCtrl);
            }

            SalesItems salesItems = new SalesItems();
            StockFlow stockFlow = new StockFlow(salesItems.getStorageCenter(), product, date, Flow.ENTRANCE, salesItems.getQnt());
            stockFlowRepository.save(stockFlow);
            product.setCurrentStock(product.getCurrentStock() + salesItems.getQnt());
            productRepository.save(product);
            logService.save(token, Activity.BUY, "stock_flow", null);
            return ResponseEntity.status(200).body("Sucesso");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
