package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.dto.ProdutoReturnDTO;
import com.example.demo.entities.*;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    ProductRepository repository;

    @Autowired
    private LogService logService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getProducts(HttpServletRequest request,
                                              @RequestParam(value = "id", required = false) Long id,
                                              @RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "description", required = false) String description,
                                              @RequestParam(value = "barCode", required = false) String barCode,
                                              @RequestParam(value = "brandId", required = false) Long brandId,
                                              @RequestParam(value = "groupId", required = false) Long groupId,
                                              @RequestParam(value = "typeId", required = false) Long typeId,
                                              @RequestParam(value = "muId", required = false) Long muId,
                                              @RequestParam(value = "active", required = false) Boolean active) {

        String json;
        HttpStatus status = HttpStatus.OK;

        try {

            ObjectMapper mapper = new ObjectMapper();
            Product p = new Product();
            p.setId(null);

            if (id != null) {
                System.out.println("Entrou aqui");
                Product prod;
                if (active == null || !active) {
                    System.out.println("TEm que entrar" + repository.findById(id));
                    prod = repository.findById(id).filter(product -> product.getKilled().compareTo(0) == 0 && product.getActive().compareTo(1) == 0).get();
                    System.out.println(prod);
                } else {
                    System.out.println("Não tem que etnra" + id);
                    prod = repository.findById(id).filter(product -> product.getKilled().compareTo(0) == 0).get();
                }
                return ResponseEntity.status(200).body(mapper.writeValueAsString(prod));

            } else {
                if (name != null) {
                    p.setName(name);
                }
                if (description != null) {
                    p.setDescription(description);
                }
                if (barCode != null) {
                    p.setBarCode(barCode);
                }
                if (brandId != null) {
                    Brand b = new Brand();
                    b.setId(brandId);
                    p.setBrand(b);
                }
                if (groupId != null) {
                    Group g = new Group();
                    g.setId(groupId);
                    p.setGroup(g);
                }
                if (typeId != null) {
                    Type t = new Type();
                    t.setId(typeId);
                    p.setType(t);
                }
                if (muId != null) {
                    MU m = new MU();
                    m.setId(muId);
                    p.setMu(m);
                }
                if (active == null || !active) {
                    p.setActive(1);
                } else {
                    p.setActive(0);
                }
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Product> example = Example.of(p, matcher);
                List<ProdutoReturnDTO> ps = repository.findAll(example)
                        .stream().map(ProdutoReturnDTO::new).
                        collect(Collectors.toList());
                System.out.println(mapper.writeValueAsString(p));
                json = mapper.writeValueAsString(ps.stream().filter(produtoReturnDTO -> !produtoReturnDTO.getKilled()).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("Produto não encontrado ou desativado");
        }
        return ResponseEntity.status(status.value()).body(json);
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editProduct(@RequestHeader("Authorization") String token, HttpServletRequest request, @RequestBody Product product) {

        try {
            if (product.getBarCode().length() < 13 || product.getBarCode().length() > 14) {
                return ResponseEntity.status(400).body("código de barras inválido");
            }
            if (product.getId() == null || product.getId().compareTo(0L) == 0) {
                return ResponseEntity.status(406).body("Produto sem id para edição");
            }
            product.setName(product.getName().toUpperCase());
            product.setDescription(product.getDescription().toUpperCase());
            repository.save(product);

            logService.save(token, Activity.EDIT, "product", product.getId());

            return ResponseEntity.ok("Produto editado com sucesso");
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado " + e);
        }
    }

    @PutMapping(value = "/edit/{id}", produces = "text/plain")
    public ResponseEntity<String> activateProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            Product p = repository.findById(id).get();

            try {

                p.setActive(p.getActive().compareTo(1) == 0 ? 0 : 1);
                p.setName(p.getName().toUpperCase());
                p.setDescription(p.getDescription().toUpperCase());
                repository.save(p);

                logService.save(token, Activity.EDIT_STATE, "product", p.getId());

                return ResponseEntity.status(HttpStatus.OK).body("Estado editado com sucesso" + p.toString());
            } catch (Exception e) {

                return ResponseEntity.status(500).body("Erro ao editar estado do produto" + e);
            }
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado" + e);
        }
    }

    @PutMapping(value = "/kill/{id}", produces = "text/plain")
    public ResponseEntity<String> killProduct(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {

        try {

            Product p = repository.findById(id).get();

            try {
                p.setKilled(1);
                repository.save(p);
                logService.save(token, Activity.DELETE, "product", p.getId());

                return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso");
            } catch (Exception e) {

                return ResponseEntity.status(500).body("Erro ao remover produto" + e);
            }
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado" + e);
        }
    }

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> saveProduct(@RequestHeader("Authorization") String token, HttpServletRequest request,
                                              @RequestBody ProdutoNewDTO produtoNewDTO) {

        try {
            if (produtoNewDTO.getBarCode().length() < 13 || produtoNewDTO.getBarCode().length() > 14) {
                return ResponseEntity.status(400).body("código de barras inválido");
            }
            HttpStatus status = HttpStatus.NOT_FOUND;
            Product convertProd = new Product(produtoNewDTO);
            convertProd.setName(convertProd.getName().toUpperCase());
            convertProd.setDescription(convertProd.getDescription().toUpperCase());
            Product p = repository.save(convertProd);
            ProdutoNewDTO returnProd = new ProdutoNewDTO(p);
            status = HttpStatus.OK;
            logService.save(token, Activity.NEW, "product", p.getId());

            return ResponseEntity.status(status.value()).body("Produto: " + returnProd.toString() + " salvo com sucesso!");
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
}