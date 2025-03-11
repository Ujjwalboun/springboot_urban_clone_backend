package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class urban_cloneApplication {

    public static void main(String[] args) {
        try{
        SpringApplication.run(urban_cloneApplication.class, args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}