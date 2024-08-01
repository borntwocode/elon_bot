package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.elonbot.messages.BotCommands;

@Component
@RequiredArgsConstructor
public class MyBot implements CommandLineRunner {

    private final BotUpdateHandler updateHandler;
    private final TelegramBot telegramBot;
    private final TelegramBot adminBot;

    @Override
    public void run(String... args) {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(updateHandler::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        BotCommand[] commands = new BotCommand[]{
                new BotCommand(BotCommands.START, BotCommands.START),
                new BotCommand(BotCommands.SETTINGS, BotCommands.SETTINGS),
        };

        telegramBot.execute(new SetMyCommands(commands));

        adminBot.setUpdatesListener(updates -> {
            updates.forEach(updateHandler::handleAdminUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        BotCommand[] adminCommands = new BotCommand[]{
                new BotCommand(BotCommands.START, BotCommands.START)
        };

        adminBot.execute(new SetMyCommands(adminCommands));
    }

}