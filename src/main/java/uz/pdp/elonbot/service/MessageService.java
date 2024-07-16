package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.bot.BotUtils;
import uz.pdp.elonbot.entity.TelegramUser;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final TelegramBot telegramBot;
    private final BotUtils botUtils;
//    private final MessageDeletingService deletingService;

    public void sendWithButton(TelegramUser user, String text, Keyboard buttons) {
        SendMessage message = new SendMessage(user.getId(), text);
        message.replyMarkup(buttons);
        Integer messageId = telegramBot.execute(message).message().messageId();
//        deletingService.addToDeleting(messageId, user);
    }

    public void sendWithBackButton(TelegramUser user, String text) {
        SendMessage message = new SendMessage(user.getId(), text);
        message.replyMarkup(botUtils.createBackAndCancelButtons());
        Integer messageId = telegramBot.execute(message).message().messageId();
    }

    public void sendAndAddToDeleting(TelegramUser user, String text) {
        SendMessage message = new SendMessage(user.getId(), text);
        Integer messageId = telegramBot.execute(message).message().messageId();
//        deletingService.addToDeleting(messageId, user);
    }

    public void sendMessage(TelegramUser user, String text) {
        SendMessage message = new SendMessage(user.getId(), text);
        telegramBot.execute(message);
    }

}
