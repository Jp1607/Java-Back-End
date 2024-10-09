package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserLoginDTO;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getUser(@RequestParam(value = "name", required = false) String name,
                                          @PathVariable(required = false) Long id) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            String json = "";
            HttpStatus status = HttpStatus.OK;

            if (id != null) {

                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {

                    json = mapper.writeValueAsString(user.get());
                } else {

                    json = "Não encontrado";
                    status = HttpStatus.NOT_FOUND;
                }
            } else {

                User u = new User();
                if (name != null) {
                    u.setName(name);
                }
                ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                Example<User> example = Example.of(u, matcher);


                List<UserDTO> users = userRepository.findAll(example).stream().map(UserDTO::new).collect(Collectors.toList());
                json = mapper.writeValueAsString(users);
            }
            return ResponseEntity.status(status).body(json);

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(404).body("Usuários não encontrado");
        }
    }

//    @PostMapping(value = "", produces = "text/plain")
//    public ResponseEntity<String> newUser(@RequestBody UserDTO userDTO) {
//
//        try {
//
//            HttpStatus status = HttpStatus.NOT_FOUND;
//            ObjectMapper mapper = new ObjectMapper();
//            User convertedUser = mapper.convertValue(userDTO, User.class);
//            User u = userRepository.save(convertedUser);
//            status = HttpStatus.OK;
//            return ResponseEntity.status(status.value()).body(u.getId().toString());
//
//        } catch (Exception e) {
//
//            String error = "Erro ao salvar o usuário";
//
//            if (e instanceof DataIntegrityViolationException) {
//
//                error = (e.getMessage());
//            }
//            return ResponseEntity.status(404).body(error);
//        }
//    }

    @PostMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> saveUser(@RequestHeader("Authorization") String token, @RequestBody User user) {

        try {

            if (userRepository.findByName(user.getName()).isEmpty()) {

                ObjectMapper mapper = new ObjectMapper();

                user.setPassword(encoder.encode(user.getPassword()));
                user.setActive(1);

                User u = userRepository.save(user);
                UserDTO convertedUser = new UserDTO(u);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.NEW);
                log.setDate(new Date());
                log.setTableName("user");
                log.setTableId(u.getId());
                logRepository.save(log);

                return ResponseEntity.status(200).body("Sucesso ao salvar o usuário " + convertedUser.toString());
            } else {

                return ResponseEntity.status(500).body("O usuário " + user.getName() + " já existe");
            }

        } catch (Exception e) {

            return ResponseEntity.status(400).body("Falha ao salvar o usuário " + e);
        }
    }

    @PutMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> editUser(@RequestHeader("Authorization") String token, @PathVariable(required = true) long id) {

        try {

            User user;
            Optional<User> toValidate = userRepository.findById(id);

            if (toValidate.isPresent()) {

                user = toValidate.get();


                user.setActive(0);
                userRepository.save(user);

                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                user.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(user);
                log.setActivity(Activity.NEW);
                log.setDate(new Date());
                log.setTableName("user");
                log.setTableId(user.getId());
                logRepository.save(log);

                return ResponseEntity.status(202).body("Usuário desativado com sucesso");

//                else {
//
//                    user.setActive(1);
//                    userRepository.save(user);
//
//                    String t = token.split(" ")[1];
//                    HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
//                    u.setId(http.getUserDetails().getId());
//                    Log log = new Log();
//                    log.setUser(u);
//                    log.setActivity(Activity.NEW);
//                    log.setDate(new Date());
//                    log.setTableName("user");
//                    log.setTableId(u.getId());
//                    logRepository.save(log);
//
//                    return ResponseEntity.status(202).body("Usuário ativado com sucesso");
//                }
            } else {

                return ResponseEntity.status(404).body("Usuário com ID: " + id + " não encontrado! ");
            }
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Erro ao editar o usuário");
        }
    }

//    @DeleteMapping(value = "/{id}", produces = "text/plain")
//    public ResponseEntity<String> deleteUser(@PathVariable(required = true) long id) {
//
//        try {
//            userRepository.deleteById(id);
//            return ResponseEntity.status(202).body("Usuário deletado com sucesso");
//        } catch (Exception e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }
}











































































































































































































































































































































































































































































































































































































































































































































































































































































































































































