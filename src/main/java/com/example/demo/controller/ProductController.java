package com.example.demo.controller;
import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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

                System.out.println(json);
            return ResponseEntity.status(status.value()).body(json);
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editProduct(HttpServletRequest request, @RequestBody Product product) {

        try {

            if(product.getId() == null || product.getId().compareTo(0L) == 0){

                return ResponseEntity.status(406).body("Produto sem id para edição");
            }

            repository.save(product);
            return ResponseEntity.ok("Produto editado com sucesso");
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @PutMapping(value = "/edit/{id}", produces = "text/plain")
    public ResponseEntity<String> activateProduct(@PathVariable Long id) {

        try {

            Product p = repository.findById(id).get();

            try {

                p.setActive(p.getActive().compareTo(1) == 0 ? 0: 1);
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
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.convertValue(produtoNewDTO, Product.class);
            Product p = repository.save(product);
            status = HttpStatus.OK;

            return ResponseEntity.status(status.value()).body(p.getId().toString());
        } catch (Exception e) {

            String error = "Problema ao salvar o produto";
            if(e instanceof DataIntegrityViolationException){

                error = e.getMessage();
            }
            return ResponseEntity.status(404).body(error);
        }
    }

    @DeleteMapping(value =  "/{id}", produces = "text/plain")
    public ResponseEntity<String> deleteProduct(HttpServletRequest request, @PathVariable(required = true) Long id) {

        try {

            repository.deleteById(id);
            return ResponseEntity.ok().body("Produto deletado com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }
}