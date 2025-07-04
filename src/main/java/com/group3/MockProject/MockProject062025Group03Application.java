package com.group3.MockProject;

import com.group3.MockProject.entity.Role;
import com.group3.MockProject.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MockProject062025Group03Application {

	public static void main(String[] args) {
		SpringApplication.run(MockProject062025Group03Application.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
//			if (roleRepository.count() == 0) {
//				roleRepository.save(new Role("ADMIN", "System Administrator", false, null, null));
//				roleRepository.save(new Role("OFFICER", "Investigation Officer", false, null, null));
//				roleRepository.save(new Role("USER", "Regular User", false, null, null));
//				System.out.println("Role data initialized.");
//			}
		};
	}
}
