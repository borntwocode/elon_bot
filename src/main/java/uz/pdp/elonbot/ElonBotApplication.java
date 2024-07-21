package uz.pdp.elonbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ElonBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElonBotApplication.class, args);
    }

}
