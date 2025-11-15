package com.workastra.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.workastra.worker", "com.workastra.common"})
public class WorkastraWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkastraWorkerApplication.class, args);
    }
}
