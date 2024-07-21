package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.pdp.elonbot.util.ChannelUtil;

@Configuration
public class BotConfig {

    @Value("${channel.username}")
    private String channelUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${admin-bot.token}")
    private String adminBotToken;

    @Bean
    public TelegramBot telegramBot(){
        return new TelegramBot(botToken);
    }

    @Bean
    public TelegramBot adminBot(){
        return new TelegramBot(adminBotToken);
    }

    @PostConstruct
    public void init() {
        ChannelUtil.setChannelUsername(channelUsername);
    }

}
