package com.workastra.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.workastra")
public class WorkastraConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkastraConsoleApplication.class, args);
    }
}
