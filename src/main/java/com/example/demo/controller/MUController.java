package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.muNewDTO;
import com.example.demo.entities.MU;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.MURepository;
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
@RequestMapping(value = "/mu")
public class MUController {

    @Autowired
    private LogService logService;

    @Autowired
    MURepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getMU(@RequestParam (value = "name", required = false) String name,
                                        @RequestParam (value = "active", required = false) boolean active,
                                        @PathVariable (required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null){
                MU m = new MU();
                m.setId(null);
                if (name != null) {
                    m.setDescription(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<MU> example = Example.of(m, matcher);
                List<MU> entMUs = repository.findAll(example);
                if (active) {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entMUs));
                } else {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entMUs.stream().filter(mu -> mu.getActive().compareTo(1) == 0).collect(Collectors.toList())));
                }
            } else {
                MU mu = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(mu));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewMU(@RequestHeader("Authorization") String token, @RequestBody muNewDTO muDTO) {

        try {

            MU mu = new MU(muDTO);
            mu.setDescription(mu.getDescription().toUpperCase());
            MU m = repository.save(mu);
            muNewDTO retMU = new muNewDTO(m);

            logService.save(token, Activity.NEW, "product_mu", mu.getId());

            return ResponseEntity.status(200).body(retMU.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putMU(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                MU mu= repository.findById(id).get();

                mu.setActive(mu.getActive().compareTo(1) == 0 ? 0 : 1);
                mu.setDescription(mu.getDescription().toUpperCase());
                repository.save(mu);

                logService.save(token, Activity.DELETE, "product_mu", mu.getId());

                return ResponseEntity.status(200).body("Unidade de medida alterada com sucesso");
            } else {
                return ResponseEntity.status(404).body("Unidade de medida não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editMu(@RequestHeader("Authorization") String token,
                                            @RequestBody MU mu) {
        try {

            mu.setDescription(mu.getDescription().toUpperCase());
            repository.save(mu);

            logService.save(token, Activity.EDIT, "product_mu", mu.getId());

            return ResponseEntity.ok("U.M. editada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
