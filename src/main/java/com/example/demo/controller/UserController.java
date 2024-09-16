package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.session.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"", "/{id}"}, produces = "application/json")
    public ResponseEntity<String> getUser(@PathVariable(required = false) Long id) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            String json;
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

                List<User> users = userRepository.findAll();
                json = mapper.writeValueAsString(users);
            }

            return ResponseEntity.status(status).body(json);
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Usuário não encontrado");
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
    public ResponseEntity<String> saveUser(String name, CharSequence password) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            User user = new User();

            user.setName(name);
            user.setPassword(encoder.encode(password));
            user.setActive(1);

            User u = userRepository.save(user);
            UserDTO convertedUser = mapper.convertValue(u, UserDTO.class);
            return ResponseEntity.status(200).body(convertedUser.toString());
        } catch (Exception e) {

            return ResponseEntity.status(404).body("Falha ao salvar o usuário" + e);
        }

    }

    @PutMapping(value = "", produces = "text/plain")
    public ResponseEntity<String> editUser(@RequestBody User user) {

        try {
            if (user.getActive() == 1) {

                user.setActive(0);
            userRepository.save(user);
            return ResponseEntity.status(202).body("Usuário desativado com sucesso");
            } else {

                user.setActive(1);
            userRepository.save(user);
            return ResponseEntity.status(202).body("Usuário ativado com sucesso");
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











































































































































































































































































































































































































































































































































































































































































































































































































































































































































































