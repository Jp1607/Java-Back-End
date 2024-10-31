package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.Enum.Discount;
import com.example.demo.Enum.Flow;
import com.example.demo.Enum.Payment;
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

    @GetMapping(value = "sell", produces = "text/plain")
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
        }
        return ResponseEntity.status(status).body(body);
    }

    @PostMapping(value = "/sell", produces = "text/plain")
    public ResponseEntity<String> sell(@RequestHeader("Authorization") String token,
                                       @RequestBody ArrayList<SalesItemsDTO> salesItemsListDTO,
                                       @RequestParam(value = "discount") Discount discount,
                                       @RequestParam(value = "discountValue") Double discountValue,
                                       @RequestParam(value = "payment") Payment payment) {

        try {
            System.out.println("Lista: " + salesItemsListDTO);
            Date date = new Date();
            HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
            User user = new User();
            user.setId(http.getUserDetails().getId());

            Sale sale = new Sale(date, payment, user);
            double saleTotal = 0;
            sale = saleRepository.save(sale);
            for (SalesItemsDTO salesItemsDTO : salesItemsListDTO) {
                Product p = productRepository.findById(salesItemsDTO.getProductId()).get();
                StorageCenter s = storageRepository.findById(salesItemsDTO.getStorageCenterId()).get();
                if (p.getCurrentStock().compareTo(salesItemsDTO.getQuantity()) < 0 && p.getNegativeStock().compareTo(0) == 0) {
                    return ResponseEntity.status(200).body("Sem produtos suficientes no estoque");
                } else {
                    StorageControl storageControl = storageCtrlRepository.findByProdIdAndStorageId(salesItemsDTO.getProductId(), salesItemsDTO.getStorageCenterId());
                    if(p.getNegativeStock().compareTo(0) == 0 && storageControl.getQnt() <= salesItemsDTO.getQuantity()) {
                        storageCtrlRepository.delete(storageControl);
                    } else {
                        storageControl.setQnt(storageControl.getQnt() - salesItemsDTO.getQuantity());
                    }
                    SalesItems salesItems = new SalesItems(salesItemsDTO, p, s, discount, discountValue);
                    StockFlow stockFlow = new StockFlow(salesItems.getStorageCenter(), p, date, Flow.EXIT, salesItems.getQnt());
                    saleTotal += salesItems.getSubTotal();
                    salesItems.setSales(sale);
                    stockFlowRepository.save(stockFlow);
                    salesItems.setDiscount(discount);
                    salesItemsRepository.save(salesItems);
                    p.setCurrentStock(p.getCurrentStock() - salesItems.getQnt());
                    productRepository.save(p);
                    logService.save(token, Activity.SELL, "stock_flow", null);
                }
            }
            sale.setTotal(saleTotal);
            saleRepository.save(sale);
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
if(storageCtrl == null){
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
