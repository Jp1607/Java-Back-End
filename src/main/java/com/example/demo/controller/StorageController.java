package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.StorageCenterDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.StorageCenter;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StorageRepository;
import com.example.demo.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/storage")
public class StorageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private StorageRepository storageRepository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getStorage(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active) {

        String body;
        HttpStatus status;
        try {
            StorageCenter storageCenter = new StorageCenter();
            storageCenter.setId(null);
            ObjectMapper mapper = new ObjectMapper();
            if (id != null) {
                body = mapper.writeValueAsString(storageRepository.
                        findById(id).stream().
                        filter(sC -> sC.getKilled().compareTo(0) == 0 && storageCenter.getActive().compareTo(active ? 0 : 1) == 0).
                        collect(Collectors.toList()));
            } else {
                if (name != null) {
                    storageCenter.setDescription(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<StorageCenter> example = Example.of(storageCenter, matcher);
                body = mapper.writeValueAsString(storageRepository.
                        findAll(example).stream().
                        filter(sC -> sC.getKilled().compareTo(0) == 0 && sC.getActive().compareTo(active ? 0 : 1) == 0).
                        collect(Collectors.toList()));
            }
            status = HttpStatus.OK;
            return ResponseEntity.status(status).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = e.getMessage();
            return ResponseEntity.status(status).body(body);
        }
    }

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> newStorage(@RequestHeader("Authorization") String token, @RequestBody StorageCenterDTO storageCenterDTO) {

        try {
            StorageCenter storageCenter = new StorageCenter(storageCenterDTO);
            storageCenter.setDescription(storageCenter.getDescription().toUpperCase());
            storageRepository.save(storageCenter);
            logService.save(token, Activity.NEW, "storage_center", storageCenter.getId());
            return ResponseEntity.status(200).body("Centro de armazenamento salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity editStorage(@RequestHeader ("Authorization") String token, @RequestBody StorageCenter storageCenter) {

        try{
            storageRepository.save(storageCenter);
            logService.save(token, Activity.EDIT, "storage_center", storageCenter.getId());
            return ResponseEntity.status(200).body("Centro de armazenamento editado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

//    @PutMapping(value = "/exclude/{id}", produces = "text/plain")
//    public ResponseEntity excludeStorage(@RequestHeader ("Authorization") String token,
//                                         @PathVariable Long id){
//
//        try{
//            List<Product> tempProds = productRepository.findAll().stream().filter(product -> product.getKilled().compareTo(0) == 0 && product.get).collect(Collectors.toList());
//        }
   }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//