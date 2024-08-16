package com.example.demo.controller;

import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    ProductRepository repository;

    @GetMapping(value = {"/product", "/product/{id}"}, produces = "application/json")
    public ResponseEntity<String> getProduct(HttpServletRequest request, @PathVariable(required = false) Long id) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json;
            HttpStatus status = HttpStatus.OK;
            if (id != null) {
                Optional<Product> p = repository.findById(id);
                if(p.isPresent()){
                    json = mapper.writeValueAsString(p.get());
                }else{
                    json = "Não encontrado";
                    status = HttpStatus.NOT_FOUND;
                }
            } else {
                List<Product> ps = repository.findAll();
                json = mapper.writeValueAsString(ps);
            }
            return ResponseEntity.status(status.value()).body(json);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @PutMapping(value = "/product", produces = "text/plain")
    public ResponseEntity<String> editProduct(HttpServletRequest request, @RequestBody Product product) {
        try {
            repository.save(product);
            return ResponseEntity.ok("Produto editado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @PostMapping(value = "/product", produces = "text/plain")
    public ResponseEntity<String> saveProduct(HttpServletRequest request, @RequestBody ProdutoNewDTO produtoNewDTO) {
        try {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ObjectMapper mapper = new ObjectMapper();

            Product product = mapper.convertValue(produtoNewDTO, Product.class);

            Product p = repository.save(product);

            status = HttpStatus.OK;
            return ResponseEntity.status(status.value()).body(p.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @DeleteMapping(value =  "/product/{id}", produces = "text/plain")
    public ResponseEntity<String> deleteProduct(HttpServletRequest request, @PathVariable(required = true) Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.ok().body("Produto deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }
}