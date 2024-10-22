package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.Enum.Flow;
import com.example.demo.Enum.Payment;
import com.example.demo.dto.SalesItemsDTO;
import com.example.demo.entities.*;
import com.example.demo.repository.*;
import com.example.demo.service.HttpSessionService;
import com.example.demo.service.LogService;
import com.example.demo.session.HttpSessionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping(value = "/sale")
public class SalesController {

    @Autowired
    private LogService logService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockFlowRepository stockFlowRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SalesItemsRepository salesItemsRepository;

    @Autowired
    private HttpSessionService httpSessionService;

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> sell(@RequestHeader("Authorization") String token,
                                      @RequestBody SalesItemsDTO salesItemsDTO,
                                      @RequestParam(value = "payment")Payment payment) {

        try {
            if (salesItemsDTO.getProduct().getCurrentStock() < salesItemsDTO.getQuantity() && salesItemsDTO.getProduct().getNegativeStock().compareTo(0) == 0) {
                return ResponseEntity.status(200).body("Sem produtos suficientes no estoque");
            } else {
                Product product = salesItemsDTO.getProduct();
                Date date = new Date();
                HttpSessionParam http = httpSessionService.getHttpSessionParam(token.split(" ")[1]);
                User user = new User();
                user.setId(http.getUserDetails().getId());
                SalesItems salesItems = new SalesItems(salesItemsDTO);
                Sale sale = new Sale(date, payment, user, salesItems.getSubTotal());
                StockFlow stockFlow = new StockFlow(salesItems.getStorageCenter(), product, date, Flow.EXIT, salesItems.getQnt());
                salesItems.setSales(sale);
                stockFlowRepository.save(stockFlow);
                salesItemsRepository.save(salesItems);
                saleRepository.save(sale);
                System.out.println(product.getCurrentStock());
                product.setCurrentStock(product.getCurrentStock() - salesItems.getQnt());
                System.out.println(product.getCurrentStock());
                productRepository.save(product);
                logService.save(token, Activity.SELL, "stock_flow", null);
                return ResponseEntity.status(200).body("Sucesso");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
