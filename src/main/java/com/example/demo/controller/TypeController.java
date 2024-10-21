package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.TypeNewDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.Type;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.TypeRepository;
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
@RequestMapping(value = "/type")

public class TypeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LogService logService;

    @Autowired
    TypeRepository repository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getType(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active) {

        ObjectMapper mapper = new ObjectMapper();
        String body;
        try {
            Type t = new Type();
            t.setId(null);
            if (id == null) {
                if (name != null) {
                    t.setDescription(name);
                }
                if (active == null || !active) {
                    t.setActive(1);
                } else {
                    t.setActive(0);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Type> example = Example.of(t, matcher);
                List<Type> entTypes = repository.findAll(example).stream().filter(type -> type.getKilled().compareTo(0) == 0).collect(Collectors.toList());
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entTypes));
            } else {
                try {
                    t = repository.findById(id).
                            filter(type -> type.getKilled().compareTo(0) == 0 && type.getActive().compareTo(active ? 0 : 1) == 0).
                            orElseThrow(() -> new NoSuchElementException("Nenhum tipo encontrado."));
                    body = mapper.writeValueAsString(t);
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
    public ResponseEntity<String> postNewType(@RequestHeader("Authorization") String token, @RequestBody TypeNewDTO typeDTO) {

        try {
            Type type = new Type(typeDTO);
            type.setDescription(type.getDescription().toUpperCase());
            Type t = repository.save(type);
            TypeNewDTO retGroup = new TypeNewDTO(t);
            logService.save(token, Activity.NEW, "product_type", type.getId());
            return ResponseEntity.status(200).body(retGroup.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editType(@RequestHeader("Authorization") String token,
                                            @RequestBody Type type) {
        try {
            HttpStatus status;
            String body;
            Type tempT = repository.findById(type.getId()).orElseThrow(() -> new NoSuchElementException("Tipo não encontrado"));
            type.setDescription(type.getDescription().toUpperCase());
            if (tempT.getActive().compareTo(1) == 0 && type.getActive().compareTo(0) == 0) {
                type.setActive(0);
                logService.save(token, Activity.EDIT_STATE, "product_type", type.getId());
                body = "Tipo editado e desativado com sucesso!";
            } else if (tempT.getActive().compareTo(0) == 0 && type.getActive().compareTo(1) == 0) {
                type.setActive(1);
                logService.save(token, Activity.EDIT_STATE, "product_type", type.getId());
                body = "Tipo editado e ativado com sucesso!";
            } else {
                body = "Tipo editado com sucesso!";
            }
            repository.save(type);
            logService.save(token, Activity.EDIT, "product_type", type.getId());
            status = HttpStatus.OK;
            return ResponseEntity.status(status.value()).body(body);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/kill/{id}", produces = "text/plain")
    public ResponseEntity<String> killType(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id) {
        try {
            Type t = new Type();
            t.setId(id);
            List<Product> ps = productRepository.findAll().stream().filter(product -> Objects.equals(product.getTypeId(), id)).collect(Collectors.toList());
            if (!ps.isEmpty()) {
                return ResponseEntity.status(400).body("Tipo está em uso!");
            } else {
                t.setKilled(1);
                repository.save(t);
                logService.save(token, Activity.DELETE, "product_group", t.getId());
                return ResponseEntity.status(200).body("Tipo excluído com sucesso");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
