package uz.pdp.elonbot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class ElonBotApplication {

    @Value("${bot.token}")
    private String botToken;

    @Value("${admin-bot.token}")
    private String adminBotToken;

    public static void main(String[] args) {
        SpringApplication.run(ElonBotApplication.class, args);
    }

    @Bean
    public TelegramBot telegramBot(){
        return new TelegramBot(botToken);
    }

    @Bean
    public TelegramBot adminBot(){
        return new TelegramBot(adminBotToken);
    }

}
