package uz.pdp.elonbot.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.bot.BotService;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.PosterDetails;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.ScooterType;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.repo.PhotoRepo;
import uz.pdp.elonbot.repo.PosterRepo;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class TestService {

    private final PhotoRepo photoRepo;
    private final PosterRepo posterRepo;
    private final TelegramUserService telegramUserService;

    public void createPost(TelegramUser user, BotService botService) {
        byte[] bytes = getPhoto();
        Photo photo = Photo.builder().content(bytes).build();
        photoRepo.save(photo);
        PosterDetails details = new PosterDetails(ScooterType.ELECTRIC, "model", "1", null, "1", "1", null, "1", "1", "1", "1", "1", photo);
        Poster poster = Poster.builder()
                .user(user)
                .posterDetails(details)
                .isCompleted(false)
                .isAccepted(false)
                .build();
        posterRepo.save(poster);
        telegramUserService.changeUserState(user, TgState.CHOOSING_POST_OPTIONS);
        botService.sendPoster(user, photo);
    }

    @SneakyThrows
    private byte[] getPhoto() {
        String urlString = "https://via.placeholder.com/600/92c952";
        URL url = new URL(urlString);
        try(InputStream inputStream = url.openStream()) {
            return inputStream.readAllBytes();
        }
    }

}
