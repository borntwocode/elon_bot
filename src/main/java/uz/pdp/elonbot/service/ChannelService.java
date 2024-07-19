package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageCaption;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.bot.BotConstants;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.PosterDetails;

@Service
@RequiredArgsConstructor
public class ChannelService {

    @Value("${channel.username}")
    private String channelUsername;

    private final TelegramBot telegramBot;
    private final PostService postService;

    public void sendToChannel(Poster poster, boolean isAccepted) {
        PosterDetails posterDetails = postService.setIsAccepted(poster.getId(), isAccepted);
        SendPhoto sendPhoto = new SendPhoto(channelUsername, posterDetails.getPhoto().getContent());
        sendPhoto.caption(posterDetails.toString()).parseMode(ParseMode.MarkdownV2);
        SendResponse execute = telegramBot.execute(sendPhoto);
        Integer messageId = execute.message().messageId();
        postService.setChannelMessageId(poster.getId(), messageId);
    }

    public void editPostCaption(Poster poster) {
        postService.setIsSold(poster.getId());
        Integer messageId = poster.getChannelMessageId();
        String text = poster.getPosterDetails().toString();
        var editCaption = new EditMessageCaption(channelUsername, messageId);
        editCaption.caption(text + "\n" + BotConstants.SOLD_POST).parseMode(ParseMode.MarkdownV2);
        telegramBot.execute(editCaption);
    }

}
