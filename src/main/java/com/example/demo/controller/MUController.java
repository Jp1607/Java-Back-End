package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.dto.muNewDTO;
import com.example.demo.entities.Brand;
import com.example.demo.entities.Log;
import com.example.demo.entities.MU;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.MURepository;
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
@RequestMapping(value = "/mu")

public class MUController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    MURepository repository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getMU(@RequestParam (value = "name", required = false) String name,
                                        @RequestParam (value = "active", required = false) boolean active,
                                        @PathVariable (required = false) Long id) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (id == null){
                MU m = new MU();
                m.setId(null);
                if (name != null) {
                    m.setDescription(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<MU> example = Example.of(m, matcher);
                List<MU> entMUs = repository.findAll(example);
                if (active) {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entMUs));
                } else {
                    return ResponseEntity.status(200).body(mapper.writeValueAsString(entMUs.stream().filter(mu -> mu.getActive().compareTo(1) == 0).collect(Collectors.toList())));
                }
            } else {
                MU mu = repository.findById(id).get();
                return ResponseEntity.status(200).body(mapper.writeValueAsString(mu));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> postNewMU(@RequestHeader("Authorization") String token, @RequestBody muNewDTO muDTO) {

        try {

            MU mu = new MU(muDTO);
            MU m = repository.save(mu);
            muNewDTO retMU = new muNewDTO(m);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.NEW);
            log.setDate(new Date());
            log.setTableName("product_mu");
            log.setTableId(m.getId());
            logRepository.save(log);

            return ResponseEntity.status(200).body(retMU.toString());
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> putMU(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        try {

            if (repository.findById(id).isPresent()) {
                MU mu= repository.findById(id).get();

                mu.setActive(mu.getActive().compareTo(1) == 0 ? 0 : 1);
                repository.save(mu);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.DELETE);
                log.setDate(new Date());
                log.setTableName("product_mu");
                log.setTableId(mu.getId());
                logRepository.save(log);

                return ResponseEntity.status(200).body("Unidade de medida alterada com sucesso");
            } else {
                return ResponseEntity.status(404).body("Unidade de medida não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping(value = "/edit", produces = "text/plain")
    public ResponseEntity<String> editMu(@RequestHeader("Authorization") String token,
                                            @RequestBody MU mu) {
        try {

            repository.save(mu);

            String t = token.split(" ")[1];
            HttpSessionParam http = httpSessionService.getHttpSessionParam(t);

            User u = new User();
            u.setId(http.getUserDetails().getId());
            Log log = new Log();
            log.setUser(u);
            log.setActivity(Activity.EDIT);
            log.setDate(new Date());
            log.setTableName("product_mu");
            log.setTableId(mu.getId());
            logRepository.save(log);
            return ResponseEntity.ok("U.M. editada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
