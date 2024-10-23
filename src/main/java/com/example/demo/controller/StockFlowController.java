package com.example.demo.controller;

import com.example.demo.Enum.Flow;
import com.example.demo.entities.StockFlow;
import com.example.demo.entities.StorageCenter;
import com.example.demo.repository.StockFlowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/stock_flow")
public class StockFlowController {

    @Autowired
    StockFlowRepository stockFlowRepository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> listFlow(@RequestParam(value = "flow", required = false) Flow flow,
                                           @RequestParam(value = "initialDate", required = false) Date initialDate,
                                           @RequestParam(value = "finalDate", required = false) Date finalDate,
                                           @RequestParam(value = "storage", required = false) Long storageCenterId) {

        try {
            StockFlow stockFlow = new StockFlow();
            ObjectMapper mapper = new ObjectMapper();
            if (flow != null) {
                stockFlow.setFlow(flow);
            }
            if (storageCenterId != null) {
                StorageCenter storageCenter = new StorageCenter();
                storageCenter.setId(storageCenterId);
                stockFlow.setStorageCenter(storageCenter);
            }
            ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<StockFlow> example = Example.of(stockFlow, matcher);
            String body = mapper.writeValueAsString(stockFlowRepository.findAll(example).stream().filter(stockFlow1 -> ));
            System.out.println("body" + mapper.writeValueAsString(stockFlowRepository.findAll(example)));
            return ResponseEntity.status(200).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
