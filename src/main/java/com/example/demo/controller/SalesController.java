package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.Enum.Discount;
import com.example.demo.Enum.Flow;
import com.example.demo.Enum.Payment;
import com.example.demo.dto.BuyItemDTO;
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

    public SalesController() {
        System.out.println(" fui criado ");
    }

    //    private Sale sale;
//    private List<SalesItems> salesItems;
//
//    @PostMapping(value = "/start", produces = "text/plain")
//    public void start(@RequestHeader("Authorization") String token) {
//        Date date = new Date();
//        HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
//        User user = new User();
//        user.setId(http.getUserDetails().getId());
//        sale = new Sale();
//        saleRepository.save(sale);
//        sale.setDate(date);
//        sale.setUser(user);
//        salesItems = new ArrayList<>();
//
//    }
//
    @GetMapping(value = "", produces = "application/json")
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
//
//    @PostMapping(value = "/add", produces = "text/plain")
//    public ResponseEntity<String> addProduct(@RequestBody SalesItemsDTO salesItemsDTO) {
//
//        HttpStatus status;
//        String body;
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Product product = productRepository.findById(salesItemsDTO.getProductId()).get();
//            StorageCenter storageCenter = storageRepository.findById(salesItemsDTO.getStorageCenterId()).get();
//            SalesItems salesItems1 = new SalesItems(salesItemsDTO, product, sale, storageCenter);
//            StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(product.getId(), storageCenter.getId());
//            salesItems.add(salesItems1);
//            if (salesItemsDTO.getQuantity() > storageControl.getQnt() && product.getNegativeStock().compareTo(0) == 0) {
//                return ResponseEntity.status(200).body("Sem produtos suficientes no estoque.");
//            }
//            int returnQnt = 0;
//            double subTotal = 0;
//            double total = 0;
//            for (SalesItems salesItem : salesItems) {
//                returnQnt += salesItem.getQnt();
//                subTotal += (salesItem.getProdValue() * salesItem.getQnt());
//                total += salesItem.getSubTotal();
//                sale.setTotal(sale.getTotal() + salesItem.getSubTotal());
//            }
//            ReturnProdInfo returnProdInfo = new ReturnProdInfo(salesItems1.getId(), returnQnt, subTotal, total);
//            status = HttpStatus.OK;
//            body = mapper.writeValueAsString(returnProdInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            body = e.getClass().getName();
//        }
//
//        return ResponseEntity.status(status.value()).body(body);
//    }
//
//    @PostMapping(value = "/remove", produces = "text/plain")
//    public ResponseEntity removeProduct(@RequestParam(value = "saleItemId") Long id) {
//
//        HttpStatus status;
//        String body;
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            salesItems.removeIf(salesItems1 -> salesItems1.getId().compareTo(id) == 0);
//            int returnQnt = 0;
//            double subTotal = 0;
//            double total = 0;
//            for (SalesItems salesItem : salesItems) {
//                returnQnt += salesItem.getQnt();
//                subTotal += (salesItem.getProdValue() * salesItem.getQnt());
//                total += salesItem.getSubTotal();
//            }
//            ReturnProdInfo returnProdInfo = new ReturnProdInfo(returnQnt, subTotal, total);
//            status = HttpStatus.OK;
//            body = mapper.writeValueAsString(returnProdInfo);
//        } catch (Exception e) {
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            body = e.getMessage();
//        }
//        return ResponseEntity.status(status.value()).body(body);
//    }
//
//    @PostMapping(value = "clean")
//    public ResponseEntity<String> cleanAllProducts() {
//        HttpStatus status;
//        String body;
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            salesItems.clear();
//            ReturnProdInfo returnProdInfo = new ReturnProdInfo(0, 0, 0);
//            status = HttpStatus.OK;
//            body = mapper.writeValueAsString(returnProdInfo);
//        } catch (Exception e) {
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            body = e.getMessage();
//        }
//        return ResponseEntity.status(status.value()).body(body);
//    }

    @PostMapping(value = "/sell", produces = "text/plain")
    public ResponseEntity<String> sell(@RequestHeader("Authorization") String token,
                                       @RequestBody ArrayList<SalesItemsDTO> salesItemsListDTO,
                                       @RequestParam(value = "payment") Payment payment) {
        HttpStatus status;
        String body;
        try {
            Date date = new Date();
            HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
            User user = new User();
            user.setId(http.getUserDetails().getId());
            Sale sale = new Sale();
            sale.setDate(date);
            sale.setUser(user);
            sale.setPayment(payment);
            sale = saleRepository.save(sale);
            Double total = 0.0;
            for (SalesItemsDTO salesItemDTO : salesItemsListDTO) {
                Product p = productRepository.findById(salesItemDTO.getProductId()).get();
                StorageCenter s = storageRepository.findById(salesItemDTO.getStorageCenterId()).get();
                SalesItems salesItems = new SalesItems(salesItemDTO, p, sale, s);
                StockFlow stockFlow = new StockFlow(s, p, date, Flow.EXIT, salesItemDTO.getQuantity());
                stockFlowRepository.save(stockFlow);
                salesItemsRepository.save(salesItems);
                p.setCurrentStock(p.getCurrentStock() - salesItems.getQnt());
                productRepository.save(p);
                total += (salesItems.getSubTotal());
                StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(p.getId(), s.getId());
                if (p.getNegativeStock().compareTo(0) == 0 && Objects.equals(storageControl.getQnt(), salesItemDTO.getQuantity())) {
                    storageCtrlRepository.delete(storageControl);
                } else {
                    storageControl.setQnt(storageControl.getQnt() - salesItemDTO.getQuantity());
                }
            }
            sale.setTotal(total);
            saleRepository.save(sale);
            logService.save(token, Activity.SELL, "sale", sale.getId());
            status = HttpStatus.OK;
            body = "sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getMessage();
        }
        return ResponseEntity.status(status.value()).body(body);
    }

    @PostMapping(value = "/buy", produces = "text/plain")
    public ResponseEntity<String> buy(@RequestHeader("Authorization") String token,
                                      @RequestBody ArrayList<BuyItemDTO> buyItemDTOS) {
        HttpStatus status;
        String body;
        try {
            Date date = new Date();
            HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
            User user = new User();
            user.setId(http.getUserDetails().getId());
            for (BuyItemDTO saleItemDTO : buyItemDTOS) {
                Product p = productRepository.findById(saleItemDTO.getProductId()).get();
                StorageCenter storageCenter = storageRepository.findById(saleItemDTO.getStorageCenterId()).get();
                p.setCurrentStock(p.getCurrentStock() + saleItemDTO.getQuantity());
                productRepository.save(p);
                StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(p.getId(), saleItemDTO.getStorageCenterId());
                if (storageControl == null) {
                   StorageControl storageControl1 = new StorageControl(p.getId(), saleItemDTO.getStorageCenterId(), saleItemDTO.getQuantity());
                storageCtrlRepository.save(storageControl1);
                } else {
                    storageControl.setQnt(storageControl.getQnt() + saleItemDTO.getQuantity());
                    storageCtrlRepository.save(storageControl);
                }
                StockFlow stockFlow = new StockFlow(storageCenter, p, date, Flow.ENTRANCE, saleItemDTO.getQuantity());
                stockFlowRepository.save(stockFlow);
            }
            status = HttpStatus.OK;
            body = "sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getMessage();
        }
        return ResponseEntity.status(status.value()).body(body);
    }
}
