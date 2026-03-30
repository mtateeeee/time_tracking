package com.example.timetracking;

import com.example.timetracking.model.Employee;
import com.example.timetracking.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class TimeTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackingApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdminUser(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            employeeRepository.findByUsername("admin").ifPresentOrElse(existing -> {
                boolean changed = false;
                if (existing.getRoles() == null || existing.getRoles().isEmpty()) {
                    existing.setRoles(Set.of("ADMIN"));
                    changed = true;
                } else if (!existing.getRoles().contains("ADMIN") && !existing.getRoles().contains("ROLE_ADMIN")) {
                    existing.getRoles().add("ADMIN");
                    changed = true;
                }

                if (existing.getPassword() == null || existing.getPassword().isBlank()) {
                    existing.setPassword(passwordEncoder.encode("admin123"));
                    changed = true;
                }

                if (existing.getFullName() == null || existing.getFullName().isBlank()) {
                    existing.setFullName("System Administrator");
                    changed = true;
                }

                if (changed) {
                    employeeRepository.save(existing);
                }
            }, () -> {
                Employee admin = new Employee();
                admin.setUsername("admin");
                admin.setFullName("System Administrator");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of("ADMIN"));
                employeeRepository.save(admin);
            });
        };
    }
}

