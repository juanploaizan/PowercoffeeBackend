package com.powercoffee;

import com.powercoffee.model.Role;
import com.powercoffee.model.enums.ERole;
import com.powercoffee.repository.RoleRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Transacciones Bancarias API")
                        .version("1.0")
                        .description("Transacciones Bancarias API, desarrollada con Spring Boot 3.1.4 y Springdoc-openapi-ui 2.2.0")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org"))
                        .termsOfService("https://swagger.io/terms/")
                );
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(RoleRepository roleRepository) {
        return args -> {
            roleRepository.save(Role.builder().name(ERole.ROLE_CUSTOMER).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_EMPLOYEE).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_SUPER_ADMIN).build());
        };
    }

}
