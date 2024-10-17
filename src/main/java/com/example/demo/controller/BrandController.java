package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.BrandNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Product;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/brand")

public class BrandController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    BrandRepository repository;

    @Autowired
    private LogService logService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getBrand(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null) {
                Brand b = new Brand();
                b.setId(null);
                if (name != null) {
                    b.setDescription(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Brand> example = Example.of(b, matcher);
                List<Brand> entBrands = repository.findAll(example);
                if (active) {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entBrands));
                } else {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entBrands.stream().filter(brand -> brand.getActive().compareTo(1) == 0).collect(Collectors.toList())));
                }
            } else {
                Brand brand;
                if (active == null || !active) {
                    brand = repository.findById(id).filter(brand1 -> brand1.getKilled().compareTo(0) == 0 && brand1.getActive().compareTo(1) == 0).get();
                } else {
                    brand = repository.findById(id).filter(brand1 -> brand1.getKilled().compareTo(0) == 0).get();
                }
                return ResponseEntity.status(200).body(mapper.writeValueAsString(brand));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewBrand(@RequestHeader("Authorization") String token, @RequestBody BrandNewDTO brandDTO) {

        try {
            ObjectMapper mapper = new ObjectMapper();
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

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> stateBrand(@RequestHeader("Authorization") String token,
                                             @PathVariable Long id) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Brand b = new Brand();
            b.setId(id);
            List<Product> ps = productRepository.findAll().stream().filter(product -> Objects.equals(product.getBrandId(), id) && product.getKilled().compareTo(0) == 0).collect(Collectors.toList());

            if (!ps.isEmpty()) {
                return ResponseEntity.status(400).body("Marca está em uso!");
            } else {

                if (repository.findById(id).isPresent()) {
                    Brand brand = repository.findById(id).get();

                    brand.setActive(brand.getActive().compareTo(1) == 0 ? 0 : 1);
                    brand.setDescription(brand.getDescription().toUpperCase());
                    repository.save(brand);

                    logService.save(token, Activity.EDIT_STATE, "product_brand", brand.getId());

                    return ResponseEntity.status(200).body("Marca alterada com sucesso");
                } else {
                    return ResponseEntity.status(404).body("Marca não encontrada");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editBrand(@RequestHeader("Authorization") String token,
                                            @RequestBody Brand brand) {
        try {

            brand.setDescription(brand.getDescription().toUpperCase());
            repository.save(brand);

            logService.save(token, Activity.EDIT, "product_brand", brand.getId());

            return ResponseEntity.ok("Marca editada com sucesso");
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
                logService.save(token, Activity.DELETE, "product_table", b.getId());
                return ResponseEntity.status(200).body("Marca excluída com sucesso");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}