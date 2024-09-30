package com.example.demo.controller;
import com.example.demo.dto.muNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.MU;
import com.example.demo.repository.MURepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/mu")

public class MUController {

    @Autowired
    MURepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getMU(@PathVariable (required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null){

                List<MU> entMU = repository.findAll();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entMU));
            } else {
                MU mu = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(mu));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewMU(@RequestBody muNewDTO muDTO) {

        try {

            MU mu = new MU(muDTO);
            MU m = repository.save(mu);
            muNewDTO retMU = new muNewDTO(m);

            return ResponseEntity.status(200).body(retMU.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putMU(@PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                MU mu= repository.findById(id).get();

                mu.setActive(mu.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(mu);
                return ResponseEntity.status(200).body("Unidade de medida alterada com sucesso");
            } else {
                return ResponseEntity.status(404).body("Unidade de medida não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
