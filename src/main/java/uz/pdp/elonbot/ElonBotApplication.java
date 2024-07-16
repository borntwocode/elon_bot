package uz.pdp.elonbot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class ElonBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElonBotApplication.class, args);
    }

    @Bean
    public TelegramBot telegramBot(){
        return new TelegramBot("7497849972:AAGStW6hIZ14GGSU-I8V1u4lxYgY6-U8eDY");
    }

    @Bean
    public TelegramBot adminBot(){
        return new TelegramBot("7110094577:AAGQA3BuddcSImVRSSJdOQJQuM0frqMrg3M");
    }

}
