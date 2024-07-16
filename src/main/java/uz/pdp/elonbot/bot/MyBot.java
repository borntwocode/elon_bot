package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyBot implements CommandLineRunner {

    private final BotUpdateHandler updateHandler;
    private final TelegramBot telegramBot;

    @Override
    public void run(String... args) {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(updateHandler::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        BotCommand[] commands = {
                new BotCommand(BotCommands.START, "Boshlash"),
                new BotCommand(BotCommands.SETTINGS, "Sozlamalar"),
        };

        telegramBot.execute(new SetMyCommands(commands));
    }

}