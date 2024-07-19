package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.bot.BotUtils;
import uz.pdp.elonbot.client.AdminClient;
import uz.pdp.elonbot.dto.AdminDTO;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.Poster;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TelegramBot adminBot;
    private final AdminClient adminClient;
    private final BotUtils botUtils;

    public void createAndSendPoster(Poster poster, Photo photo, String posterMessage) {
        List<AdminDTO> admins = adminClient.getAdmins();
        for (AdminDTO admin : admins) {
            SendPhoto sendPhoto = new SendPhoto(admin.getChatId(), photo.getContent());
            sendPhoto.caption(posterMessage);
            sendPhoto.replyMarkup(botUtils.createPosterAdminButtons(poster));
            sendPhoto.parseMode(ParseMode.MarkdownV2);
            adminBot.execute(sendPhoto);
        }
    }

}
