package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.dto.ProdutoReturnDTO;
import com.example.demo.entities.*;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private LogRepository logRepository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getProduct(HttpServletRequest request,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "barCode", required = false) Long barCode,
                                             @RequestParam(value = "brandId", required = false) Long brandId,
                                             @RequestParam(value = "groupId", required = false) Long groupId,
                                             @RequestParam(value = "typeId", required = false) Long typeId,
                                             @RequestParam(value = "muId", required = false) Long muId,
                                             @PathVariable(required = false) Long id) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            String json;
            HttpStatus status = HttpStatus.OK;

            if (id != null) {

                Product p = repository.findById(id).get();
                if (p != null) {

//                    ProdutoReturnDTO prod = new ProdutoReturnDTO(p);
//                    System.out.println("Produtos: " + p.getBrand() + prod.getBrandDesc());

                    json = mapper.writeValueAsString(p);
                } else {

                    json = "Não encontrado";
                    status = HttpStatus.NOT_FOUND;
                }
            } else {
                Product p = new Product();
                p.setId(null);
                if(name != null){
                    p.setName(name);
                }
                if(barCode != null){
                    p.setBarCode(barCode.toString());
                }
                if(brandId != null){
                    Brand b = new Brand();
                    b.setId(brandId);
                    p.setBrand(b);
                }
                if(groupId != null){
                    Group g = new Group();
                    g.setId(groupId);
                    p.setGroup(g);
                }
                if(typeId != null){
                    Type t = new Type();
                    t.setId(typeId);
                    p.setType(t);
                }
                if(muId != null){
                    MU m = new MU();
                    m.setId(muId);
                    p.setMu(m);
                }
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Product> example = Example.of(p, matcher);


                List<ProdutoReturnDTO> ps = repository.findAll(
                                example
                                //,
                              //  Sort.by("name")
                        )
                        .stream().map(ProdutoReturnDTO::new).
                        collect(Collectors.toList());
                json = mapper.writeValueAsString(ps);
                System.out.println(ps);
            }

            return ResponseEntity.status(status.value()).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("Produto não encontrado " + e.getStackTrace());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editProduct(@RequestHeader("Authorization") String token, HttpServletRequest request, @RequestBody Product product) {

        try {

            if (product.getId() == null || product.getId().compareTo(0L) == 0) {

                return ResponseEntity.status(406).body("Produto sem id para edição");
            }

            repository.save(product);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);

            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.EDIT);
            log.setDate(new Date());
            log.setTableName("product");
            log.setTableId(product.getId());
            logRepository.save(log);
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
                repository.save(p);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.DELETE);
                log.setDate(new Date());
                log.setTableName("product");
                log.setTableId(p.getId());
                logRepository.save(log);

                return ResponseEntity.status(HttpStatus.OK).body("Estado editado com sucesso" + p.toString());
            } catch (Exception e) {

                return ResponseEntity.status(500).body("Erro ao editar estado do produto" + e);
            }
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado" + e);
        }
    }

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> saveProduct(@RequestHeader("Authorization") String token, HttpServletRequest request, @RequestBody ProdutoNewDTO produtoNewDTO) {

        try {

            HttpStatus status = HttpStatus.NOT_FOUND;

            Product convertProd = new Product(produtoNewDTO);
            Product p = repository.save(convertProd);
            ProdutoNewDTO returnProd = new ProdutoNewDTO(p);

            status = HttpStatus.OK;

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.NEW);
            log.setDate(new Date());
            log.setTableName("product");
            log.setTableId(p.getId());
            logRepository.save(log);

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
}