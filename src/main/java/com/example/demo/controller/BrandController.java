package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.BrandNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Group;
import com.example.demo.entities.Product;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/brand")

public class BrandController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    BrandRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private LogService logService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getBrand(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active) {

        ObjectMapper mapper = new ObjectMapper();
        String body;
        try {
            Brand b = new Brand();
            b.setId(null);
            if (id == null) {

                if (name != null) {
                    b.setDescription(name);
                }
                if (active == null || !active) {
                    b.setActive(1);
                } else {
                    b.setActive(0);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Brand> example = Example.of(b, matcher);
                List<Brand> entBrands = repository.findAll(example).stream().filter(group -> group.getKilled().compareTo(0) == 0).collect(Collectors.toList());
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entBrands));
            } else {
                try {
                    b = repository.findById(id).
                            filter(group -> group.getKilled().compareTo(0) == 0 && group.getActive().compareTo(active ? 0 : 1) == 0).
                            orElseThrow(() -> new NoSuchElementException("Nenhuma marca encontrada."));
                    body = mapper.writeValueAsString(b);
                } catch (Exception e) {
                    body = e.getMessage();
                }
                return ResponseEntity.status(200).body(body);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewBrand(@RequestHeader("Authorization") String token, @RequestBody BrandNewDTO brandDTO) {

        try {
            Brand brand = new Brand(brandDTO);
            brand.setDescription(brand.getDescription().toUpperCase());
            Brand b = repository.save(brand);
            BrandNewDTO retBrand = new BrandNewDTO(b);
            logService.save(token, Activity.NEW, "product_brand", brand.getId());
            return ResponseEntity.status(200).body(retBrand.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editBrand(@RequestHeader("Authorization") String token,
                                            @RequestBody Brand brand) {
        try {
            HttpStatus status;
            String body;
            Brand tempB = repository.findById(brand.getId()).orElseThrow(() -> new NoSuchElementException("Marca não encontrada"));
            brand.setDescription(brand.getDescription().toUpperCase());
            if (tempB.getActive().compareTo(1) == 0 && brand.getActive().compareTo(0) == 0) {
                brand.setActive(0);
                logService.save(token, Activity.EDIT_STATE, "product_brand", brand.getId());
                body = "Marca editada e desativada com sucesso!";
            } else if (tempB.getActive().compareTo(0) == 0 && brand.getActive().compareTo(1) == 0) {
                brand.setActive(1);
                logService.save(token, Activity.EDIT_STATE, "product_brand", brand.getId());
                body = "Marca editada e ativada com sucesso!";
            } else {
                body = "Marca editada com sucesso!";
            }
            repository.save(brand);
            logService.save(token, Activity.EDIT, "product_brand", brand.getId());
            status = HttpStatus.OK;
            return ResponseEntity.status(status.value()).body(body);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/kill/{id}", produces = "text/plain")
    public ResponseEntity<String> killBrand(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id) {
        try {
            Brand b = new Brand();
            b.setId(id);
            List<Product> ps = productRepository.findAll().stream().filter(product -> Objects.equals(product.getBrandId(), id)).collect(Collectors.toList());
            if (!ps.isEmpty()) {
                return ResponseEntity.status(400).body("Marca está em uso!");
            } else {
                b.setKilled(1);
                repository.save(b);
                logService.save(token, Activity.DELETE, "product_brand", b.getId());
                return ResponseEntity.status(200).body("Marca excluída com sucesso");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}
