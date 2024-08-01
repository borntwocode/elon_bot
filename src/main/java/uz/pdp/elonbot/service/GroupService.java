package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.bot.BotUtils;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.Poster;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final PostService postService;
    @Value("${admin.group.chat.id}")
    private String groupChatId;

    private final BotUtils botUtils;
    private final TelegramBot telegramBot;

    public void createAndSendPoster(Poster poster, Photo photo, String posterMessage) {
        SendPhoto sendPhoto = new SendPhoto(groupChatId, photo.getContent());
        sendPhoto.caption(posterMessage);
        sendPhoto.replyMarkup(botUtils.createPosterAdminButtons(poster));
        sendPhoto.parseMode(ParseMode.MarkdownV2);
        SendResponse execute = telegramBot.execute(sendPhoto);
        Integer messageId = execute.message().messageId();
        postService.setAdminMessageId(poster, messageId);
    }

    public void editAcceptedPostButtons(Poster poster) {
        var editMessageReplyMarkup = new EditMessageReplyMarkup(groupChatId, poster.getAdminMessageId());
        editMessageReplyMarkup.replyMarkup(botUtils.createAcceptedPosterButtons(poster));
        telegramBot.execute(editMessageReplyMarkup);
    }

    public void editSoldPostButtons(Poster poster) {
        var editMessageReplyMarkup = new EditMessageReplyMarkup(groupChatId, poster.getAdminMessageId());
        editMessageReplyMarkup.replyMarkup(botUtils.createSoldPostButton(poster));
        telegramBot.execute(editMessageReplyMarkup);
    }

    public void deleteRejectedPost(Poster poster) {
        DeleteMessage deleteMessage = new DeleteMessage(groupChatId, poster.getAdminMessageId());
        telegramBot.execute(deleteMessage);
    }

}
