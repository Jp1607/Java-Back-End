package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.GroupNewDTO;
import com.example.demo.entities.Group;
import com.example.demo.entities.Product;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/group")

public class GroupController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogService logService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository repository;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getGroup(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) Boolean active,
                                           @RequestParam(value = "id", required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();
        String body;

        try {
            //can be empty
            Group g = new Group();
            g.setId(null);

            if (id == null) {

                if (name != null) {
                    g.setDescription(name);
                }
                if (active == null || !active) {
                    g.setActive(1);
                } else {
                    g.setActive(0);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Group> example = Example.of(g, matcher);
                List<Group> entGroups = repository.findAll(example).stream().filter(group -> group.getKilled().compareTo(0) == 0).collect(Collectors.toList());
                return ResponseEntity.status(200).body(mapper.writeValueAsString(entGroups));

            } else {
                //can be empty
                try {
                    g = repository.findById(id).
                            filter(group -> group.getKilled().compareTo(0) == 0 && group.getActive().compareTo(active ? 0 : 1) == 0).
                            orElseThrow(() -> new NoSuchElementException("Nenhum grupo encontrado."));
                    body = mapper.writeValueAsString(g);
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
    public ResponseEntity<String> postNewGroup(@RequestHeader("Authorization") String token, @RequestBody GroupNewDTO groupDTO) {

        try {

            Group group = new Group(groupDTO);
            group.setDescription(group.getDescription().toUpperCase());
            Group g = repository.save(group);
            GroupNewDTO retGroup = new GroupNewDTO(g);

            logService.save(token, Activity.NEW, "product_group", group.getId());

            return ResponseEntity.status(200).body(retGroup.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> stateGroup(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        String body;

        try {

            List<Product> prods = productRepository.findAll().stream().
                    filter(product -> Objects.equals(product.getGroupId(), id) && product.getKilled().compareTo(0) == 0).
                    collect(Collectors.toList());

            if (prods.isEmpty()) {

                Group g = repository.findById(id).
                        orElseThrow(() -> new NoSuchElementException("Grupo não encontrado"));
                g.setActive(g.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(g);
                logService.save(token, Activity.EDIT_STATE, "product_group", g.getId());
                body = "Grupo alterado com sucesso!";
            } else {
                body = "Produto está em uso e não pode ser desativado.";
            }
            return ResponseEntity.status(200).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody Group group) {
        try {

            group.setDescription(group.getDescription().toUpperCase());
            repository.save(group);

            logService.save(token, Activity.EDIT, "product_group", group.getId());

            return ResponseEntity.ok("Grupo editado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
