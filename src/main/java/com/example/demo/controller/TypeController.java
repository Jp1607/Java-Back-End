package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.TypeNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Log;
import com.example.demo.entities.Type;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/type")

public class TypeController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

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
    public ResponseEntity<String> postNewType(@RequestHeader("Authorization") String token, @RequestBody TypeNewDTO typeDTO) {

        try {

            Type type = new Type(typeDTO);
            Type T = repository.save(type);
            TypeNewDTO retType = new TypeNewDTO(T);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.NEW);
            log.setDate(new Date());
            log.setTableName("product_type");
            log.setTableId(T.getId());
            logRepository.save(log);

            return ResponseEntity.status(200).body(retType.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putType(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                Type type = repository.findById(id).get();

                type.setActive(type.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(type);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.DELETE);
                log.setDate(new Date());
                log.setTableName("product_type");
                log.setTableId(type.getId());
                logRepository.save(log);

                return ResponseEntity.status(200).body("Tipo alterado com sucesso");
            } else {
                return ResponseEntity.status(404).body("Tipo não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
