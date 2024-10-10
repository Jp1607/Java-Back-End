package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.GroupNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Group;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.LogRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/group")

public class GroupController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

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
            Group g = repository.save(group);
            GroupNewDTO retGroup = new GroupNewDTO(g);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.NEW);
            log.setDate(new Date());
            log.setTableName("product_group");
            log.setTableId(g.getId());
            logRepository.save(log);

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
                repository.save(group);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.DELETE);
                log.setDate(new Date());
                log.setTableName("product_group");
                log.setTableId(group.getId());
                logRepository.save(log);

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

            repository.save(group);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);

            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.EDIT);
            log.setDate(new Date());
            log.setTableName("product_group");
            log.setTableId(group.getId());
            logRepository.save(log);
            return ResponseEntity.ok("Grupo editado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
