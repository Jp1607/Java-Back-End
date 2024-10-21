package com.example.demo.controller;

import com.example.demo.entities.SalesItems;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stock_flow")
public class StockFlowController {

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> sale(@RequestHeader("Authorization")
                                       @RequestBody SalesItems salesItems){

    }
}
