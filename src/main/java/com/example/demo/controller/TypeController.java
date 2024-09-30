package com.example.demo.controller;
import com.example.demo.dto.TypeNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Type;
import com.example.demo.repository.TypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/type")

public class TypeController {

    @Autowired
    TypeRepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getType(@PathVariable (required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null){

                List<Type> entType = repository.findAll();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entType));
            } else {
                Type type = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(type));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewType(@RequestBody TypeNewDTO typeDTO) {

        try {

            Type type = new Type(typeDTO);
            Type t = repository.save(type);
            TypeNewDTO retType = new TypeNewDTO(t);

            return ResponseEntity.status(200).body(retType.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putType(@PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                Type type = repository.findById(id).get();

                type.setActive(type.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(type);
                return ResponseEntity.status(200).body("Tipo alterado com sucesso");
            } else {
                return ResponseEntity.status(404).body("Tipo não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
