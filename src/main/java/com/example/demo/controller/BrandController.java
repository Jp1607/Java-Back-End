package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.BrandNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.LogRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/brand")

public class BrandController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    BrandRepository repository;

    @Autowired
    private LogService logService;

    public List getBrandList() {
        List<Brand> brands = repository.findAll();
        return brands;
    }

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getBrand(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) boolean active,
                                           @PathVariable(required = false) Long id) {

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
                Brand brand = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(brand));
            }

        } catch (Exception e) {
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

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> stateBrand(@RequestHeader("Authorization") String token,
                                             @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                Brand brand = repository.findById(id).get();

                brand.setActive(brand.getActive().compareTo(1) == 0 ? 0 : 1);
                brand.setDescription(brand.getDescription().toUpperCase());
                repository.save(brand);

                logService.save(token, Activity.DELETE, "product_brand", brand.getId());

                return ResponseEntity.status(200).body("Marca alterada com sucesso");
            } else {
                return ResponseEntity.status(404).body("Marca não encontrada");
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
}