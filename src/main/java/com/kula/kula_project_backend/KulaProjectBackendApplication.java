package com.kula.kula_project_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableCaching
public class KulaProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KulaProjectBackendApplication.class, args);
    }

}
