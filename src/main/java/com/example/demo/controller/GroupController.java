package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.GroupNewDTO;
import com.example.demo.entities.Group;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.LogRepository;
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
@RequestMapping(value = "/group")

public class GroupController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogService logService;

    @Autowired
    private GroupRepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getGroup(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "active", required = false) boolean active,
                                           @PathVariable(required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null) {
                Group g = new Group();
                g.setId(null);
                if (name != null) {
                    g.setDescription(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<Group> example = Example.of(g, matcher);
                List<Group> entGroups = repository.findAll(example);
                if (active) {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entGroups));
                } else {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entGroups.stream().filter(group -> group.getActive().compareTo(1) == 0).collect(Collectors.toList())));
                }
            } else {
                Group group = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(group));
            }
        } catch (Exception e) {
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
    public ResponseEntity<String> putGroup(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                Group group = repository.findById(id).get();

                group.setActive(group.getActive().compareTo(1) == 0 ? 0 : 1);
                group.setDescription(group.getDescription().toUpperCase());
                repository.save(group);

                logService.save(token, Activity.DELETE, "product_group", group.getId());

                return ResponseEntity.status(200).body("Grupo alterado com sucesso");
            } else {
                return ResponseEntity.status(404).body("Grupo não encontrado");
            }
        } catch (Exception e) {
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
