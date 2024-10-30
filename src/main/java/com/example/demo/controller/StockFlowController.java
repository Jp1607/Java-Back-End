package com.example.demo.controller;

import com.example.demo.Enum.Flow;
import com.example.demo.dto.StockFlowDTO;
import com.example.demo.entities.StockFlow;
import com.example.demo.entities.StorageCenter;
import com.example.demo.repository.StockFlowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/stock_flow")
public class StockFlowController {

    @Autowired
    StockFlowRepository stockFlowRepository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> listFlow(@RequestParam(value = "flow", required = false) Flow flow,
                                           @RequestParam(value = "storage", required = false) Long storageCenterId,
                                           @RequestParam(value = "initialDate", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") Date initialDate,
                                           @RequestParam(value = "finalDate", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") Date finalDate) {

        try {
            System.out.println("data 1" + finalDate);
            System.out.println(initialDate);
            StockFlow stockFlow = new StockFlow();
            stockFlow.setId(null);
            ObjectMapper mapper = new ObjectMapper();
            if (flow != null) {
                stockFlow.setFlow(flow);
            }
            if (storageCenterId != null) {
                StorageCenter storageCenter = new StorageCenter();
                storageCenter.setId(storageCenterId);
                stockFlow.setStorageCenter(storageCenter);
            }
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            Example<StockFlow> example = Example.of(stockFlow, matcher);

            List<StockFlowDTO> listFlow =  stockFlowRepository.findAll(example).stream().filter(stockFlow1 -> {
                if(initialDate == null && finalDate == null) {
                    return true;
                } else if (initialDate != null && finalDate != null) {
                  if (initialDate.compareTo(finalDate) < 0){
                      return (stockFlow1.getDate().after(initialDate) && stockFlow1.getDate().before(finalDate));
                  } else if (initialDate.compareTo(finalDate) == 0) {
                      return stockFlow1.getDate().compareTo(initialDate) == 0;
                  }
                } else if (initialDate != null) {
                    return stockFlow1.getDate().after(initialDate);
                } else {
                    return stockFlow1.getDate().before(finalDate);
                }
                return false;
            }).map(StockFlowDTO::new).collect(Collectors.toList());
            String body = mapper.writeValueAsString(listFlow);

            return ResponseEntity.status(200).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
