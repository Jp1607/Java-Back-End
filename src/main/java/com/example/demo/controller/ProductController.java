package com.example.demo.controller;

import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.dto.ProdutoReturnDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Product p = new Product();
//List<Brand> brands = bc.getBrandList();
//            brands.stream().map(brand -> {
//        if (Objects.equals(brand.getDescription(), produtoReturnDTO.getBrandDesc())) {
//        p.setBrand(brand);
//                }
//                        return null;
//                        }).collect(Collectors.toList());
//
//        repository.save(p);

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductRepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getProduct(HttpServletRequest request, @PathVariable(required = false) Long id) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            String json;
            HttpStatus status = HttpStatus.OK;

            if (id != null) {

                Product p = repository.findById(id).get();
                if (p != null) {

                    ProdutoReturnDTO prod = new ProdutoReturnDTO(p);
                    json = mapper.writeValueAsString(prod);
                } else {

                    json = "Não encontrado";
                    status = HttpStatus.NOT_FOUND;
                }
            } else {

                List<ProdutoReturnDTO> ps = repository.findAll().
                        stream().map(ProdutoReturnDTO::new).
                        collect(Collectors.toList());
                json = mapper.writeValueAsString(ps);
            }
            return ResponseEntity.status(status.value()).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("Produto não encontrado " + e.getStackTrace());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editProduct(HttpServletRequest request, @RequestBody Product product) {

        try {

            if (product.getId() == null || product.getId().compareTo(0L) == 0) {

                return ResponseEntity.status(406).body("Produto sem id para edição");
            }


            repository.save(product);
            return ResponseEntity.ok("Produto editado com sucesso");
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado " + e);
        }
    }

    @PutMapping(value = "/edit/{id}", produces = "text/plain")
    public ResponseEntity<String> activateProduct(@PathVariable Long id) {

        try {

            Product p = repository.findById(id).get();

            try {

                p.setActive(p.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(p);

                return ResponseEntity.status(HttpStatus.OK).body("Estado editado com sucesso" + p.toString());
            } catch (Exception e) {

                return ResponseEntity.status(500).body("Erro ao editar estado do produto" + e);
            }
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado" + e);
        }
    }

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> saveProduct(HttpServletRequest request, @RequestBody ProdutoNewDTO produtoNewDTO) {

        try {

            HttpStatus status = HttpStatus.NOT_FOUND;

            Product convertProd = new Product(produtoNewDTO);
            Product p = repository.save(convertProd);
            ProdutoNewDTO returnProd = new ProdutoNewDTO(p);

            status = HttpStatus.OK;

            return ResponseEntity.status(status.value()).body(returnProd.toString());
        } catch (Exception e) {

            String error = "Problema ao salvar o produto";
            if (e instanceof DataIntegrityViolationException) {
                error = e.getMessage();
                ConstraintViolationException constraintEx = (ConstraintViolationException) e.getCause();
                if (constraintEx.getConstraintName().contains("UK")) {
                    error = "Este código de barras já foi cadastrado no sistema.";
                }
            }
            return ResponseEntity.status(404).body(error);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> deleteProduct(HttpServletRequest request, @PathVariable(required = true) Long id) {

        try {

            repository.deleteById(id);
            return ResponseEntity.ok().body("Produto deletado com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }
}