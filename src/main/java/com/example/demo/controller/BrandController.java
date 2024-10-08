package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.BrandNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.LogRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/brand")

public class BrandController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    BrandRepository repository;

    public List getBrandList(){
        List <Brand> brands = repository.findAll();
        return brands;
    }

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getBrand(@PathVariable (required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
        if (id == null){

            List<Brand> entBrands = repository.findAll();

            return ResponseEntity.status(200).body(mapper.writeValueAsString(entBrands));
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
            Brand b = repository.save(brand);
            BrandNewDTO retBrand = new BrandNewDTO(b);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.NEW);
            log.setDate(new Date());
            log.setTableName("product_brand");
            log.setTableId(brand.getId());
            logRepository.save(log);

            return ResponseEntity.status(200).body(retBrand.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putBrand(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                Brand brand = repository.findById(id).get();

                brand.setActive(brand.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(brand);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.DELETE);
                log.setDate(new Date());
                log.setTableName("product_brand");
                log.setTableId(brand.getId());
                logRepository.save(log);

                return ResponseEntity.status(200).body("Marca alterada com sucesso");
            } else {
                return ResponseEntity.status(404).body("Marca não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
