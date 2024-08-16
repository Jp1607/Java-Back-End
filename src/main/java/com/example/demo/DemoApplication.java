package com.example.demo;

import com.example.demo.entities.Roles;
import com.example.demo.entities.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception 	{
		User u = new User();
		u.setId(1L);
		u.setName("admin");
		u.setPassword("$2a$10$WMwGGHmwDwBDlm6592NmPuZpLKTmX/l.7iV8jwyiuViC80Ho3JbIq");
		Set<Roles> r = new HashSet();
		Roles r1 = new Roles();
		r1.setId(1L);
		r1.setName("ADMIN");
		roleRepository.save(r1);

		r.add(r1);
		u.setRoles(r);

		userRepository.save(u);
	}
}
