package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.muNewDTO;
import com.example.demo.entities.MU;
import com.example.demo.entities.Product;
import com.example.demo.repository.MURepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.LogService;
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
@RequestMapping(value = "/mu")
public class MUController {

    @Autowired
    private LogService logService;

    @Autowired
    MURepository repository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getMU(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active) {

        ObjectMapper mapper = new ObjectMapper();
        String body;
        try {
            MU mu = new MU();
            mu.setId(null);
            if (id == null) {
                if (name != null) {
                    mu.setDescription(name);
                }
                if (active == null || !active) {
                    mu.setActive(1);
                } else {
                    mu.setActive(0);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<MU> example = Example.of(mu, matcher);
                List<MU> entBrands = repository.findAll(example).stream().filter(measurementUnit -> measurementUnit.getKilled().compareTo(0) == 0).collect(Collectors.toList());
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entBrands));
            } else {
                try {
                    mu = repository.findById(id).
                            filter(group -> group.getKilled().compareTo(0) == 0 && group.getActive().compareTo(active ? 0 : 1) == 0).
                            orElseThrow(() -> new NoSuchElementException("Nenhuma uni. de medida encontrada."));
                    body = mapper.writeValueAsString(mu);
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
    public ResponseEntity<String> postNewMU(@RequestHeader("Authorization") String token, @RequestBody muNewDTO muDTO) {

        try {
            MU mu = new MU(muDTO);
            mu.setDescription(mu.getDescription().toUpperCase());
            MU Mu = repository.save(mu);
            muNewDTO retMu = new muNewDTO(Mu);
            logService.save(token, Activity.NEW, "product_mu", mu.getId());
            return ResponseEntity.status(200).body(retMu.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editMu(@RequestHeader("Authorization") String token,
                                            @RequestBody MU mu) {
        try {
            HttpStatus status;
            String body;
            MU tempMu = repository.findById(mu.getId()).orElseThrow(() -> new NoSuchElementException("Uni. de medida não encontrada"));
            mu.setDescription(mu.getDescription().toUpperCase());
            if (tempMu.getActive().compareTo(1) == 0 && mu.getActive().compareTo(0) == 0) {
                mu.setActive(0);
                logService.save(token, Activity.EDIT_STATE, "product_mu", mu.getId());
                body = "Unidade editada e desativada com sucesso!";
            } else if (tempMu.getActive().compareTo(0) == 0 && mu.getActive().compareTo(1) == 0) {
                mu.setActive(1);
                logService.save(token, Activity.EDIT_STATE, "product_mu", mu.getId());
                body = "Unidade editada e ativada com sucesso!";
            } else {
                body = "Unidade editada com sucesso!";
            }
            repository.save(mu);
            logService.save(token, Activity.EDIT, "product_mu", mu.getId());
            status = HttpStatus.OK;
            return ResponseEntity.status(status.value()).body(body);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/kill/{id}", produces = "text/plain")
    public ResponseEntity<String> killMu(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id) {
        try {
            MU mu = new MU();
            mu.setId(id);
            List<Product> ps = productRepository.findAll().stream().filter(product -> Objects.equals(product.getBrandId(), id)).collect(Collectors.toList());
            if (!ps.isEmpty()) {
                return ResponseEntity.status(400).body("Marca está em uso!");
            } else {
                mu.setKilled(1);
                repository.save(mu);
                logService.save(token, Activity.DELETE, "product_mu", mu.getId());
                return ResponseEntity.status(200).body("Unidade excluída com sucesso");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

