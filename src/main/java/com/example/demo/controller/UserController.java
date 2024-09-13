package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.session.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

                Optional<User> product = userRepository.findById(id);
                if (product.isPresent()) {

                    json = mapper.writeValueAsString(product.get());
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

return ResponseEntity.status(404).body("Produto não encontrado");
        }
    }

    @PostMapping(value = {""}, produces = "text/plain")
    public ResponseEntity<String> newUser(@RequestBody UserDTO userDTO) {

        try {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.convertValue(userDTO, User.class);
            String json;

        }
    }
}

