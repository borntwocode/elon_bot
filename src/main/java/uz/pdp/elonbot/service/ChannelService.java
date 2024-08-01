package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.messages.BotConstants;
import uz.pdp.elonbot.entity.*;
import lombok.*;

@Service
@Getter
@RequiredArgsConstructor
public class ChannelService {

    @Value("${channel1.chat.id}")
    private String channel1ChatId;
    @Value("${channel2.chat.id}")
    private String channel2ChatId;
    @Value("${channel.username}")
    private String channelUsername;
    @Value("${youtube.url}")
    private String youTubeChannel;
    @Value("${instagram.url}")
    private String instagramChannel;

    private final TelegramBot telegramBot;
    private final PostService postService;

    public void sendToChannel(Poster poster, boolean isAccepted) {
        PosterDetails posterDetails = postService.setIsAccepted(poster.getId(), isAccepted);
        SendPhoto sendPhoto = new SendPhoto(channel1ChatId, posterDetails.getPhoto().getContent());
        String text = addUrlsToText(posterDetails.toString());
        sendPhoto.caption(text).parseMode(ParseMode.MarkdownV2);
        SendResponse execute = telegramBot.execute(sendPhoto);
        Integer messageId = execute.message().messageId();
        postService.setChannelMessageId(poster.getId(), messageId);
    }

    public void editPostCaption(Poster poster) {
        postService.setIsSold(poster.getId());
        Integer messageId = poster.getChannelMessageId();
        String text = poster.getPosterDetails().toString();
        var editCaption = new EditMessageCaption(channel1ChatId, messageId);
        editCaption.caption(BotConstants.SOLD_POST + "✅✅✅" + "\n\n\n" + text + "\n\n\n" + BotConstants.SOLD_POST + "✅✅✅").parseMode(ParseMode.MarkdownV2);
        telegramBot.execute(editCaption);
    }

    public void deletePost(Poster poster) {
        DeleteMessage deleteMessage = new DeleteMessage(channel1ChatId, poster.getChannelMessageId());
        telegramBot.execute(deleteMessage);
    }

    public boolean isUserMemberOfChannel(Long userId) {
        GetChatMember getChatMember1 = new GetChatMember(channel1ChatId, userId);
        GetChatMemberResponse response1 = telegramBot.execute(getChatMember1);
        GetChatMember getChatMember2 = new GetChatMember(channel2ChatId, userId);
        GetChatMemberResponse response2 = telegramBot.execute(getChatMember2);
        boolean isMemberOfChannel1 = false;
        boolean isMemberOfChannel2 = false;
        if (response1.isOk()) {
            ChatMember chatMember1 = response1.chatMember();
            ChatMember.Status status1 = chatMember1.status();
            isMemberOfChannel1 = (status1 == ChatMember.Status.member || status1 == ChatMember.Status.administrator || status1 == ChatMember.Status.creator);
        }
        if (response2.isOk()) {
            ChatMember chatMember2 = response2.chatMember();
            ChatMember.Status status2 = chatMember2.status();
            isMemberOfChannel2 = (status2 == ChatMember.Status.member || status2 == ChatMember.Status.administrator || status2 == ChatMember.Status.creator);
        }
        return isMemberOfChannel1 && isMemberOfChannel2;
    }

    private String addUrlsToText(String string) {
        return string + "\n\n\n⚡ **Skuter E'lonlar kanali**: " + escapeMarkdownV2(channelUsername) +
               "\n⚡ **YouTube kanal**: [YouTube](https://www.youtube.com/@armada_motors_official)" +
               "\n⚡ **Instagram kanal**: [Instagram](https://www.instagram.com/armada_motors_official?igsh=bWdtOTV6M3hqamlp)";
    }

    private String escapeMarkdownV2(String text) {
        return text.replaceAll("([_\\*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }

}
