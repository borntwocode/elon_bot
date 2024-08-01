package uz.pdp.elonbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.repo.TelegramUserRepo;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepo telegramUserRepo;

    public TelegramUser findUser(Long chatId, Long userId, String username) {
        return telegramUserRepo.findById(chatId)
                .orElseGet(() -> createAndSaveUser(userId, chatId, username));
    }

    private TelegramUser createAndSaveUser(Long chatId, Long userId, String username) {
        TelegramUser user = TelegramUser.builder()
                .id(chatId)
                .userId(userId)
                .firstName(username)
                .state(TgState.START)
                .build();
        return telegramUserRepo.save(user);
    }

    public void changeUserState(TelegramUser user, TgState state) {
        user.setState(state);
        telegramUserRepo.save(user);
    }

    public TelegramUser getUserByPostId(UUID postId) {
        return telegramUserRepo.findByPostId(postId);
    }

    public List<TelegramUser> getAllFollowers() {
        return telegramUserRepo.findAll();
    }

}
