package com.devflows.barberflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "BarberFlow API",
                version = "v1",
                description = "API REST para gerenciamento de clientes, barbeiros, horarios e agendamentos."
        )
)
public class BarberFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarberFlowApplication.class, args);
    }

}


