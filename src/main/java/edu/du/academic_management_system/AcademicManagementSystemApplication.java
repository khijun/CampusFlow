package edu.du.academic_management_system;

import edu.du.academic_management_system.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcademicManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicManagementSystemApplication.class, args);
    }

}
