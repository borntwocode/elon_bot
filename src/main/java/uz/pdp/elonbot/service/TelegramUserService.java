package uz.pdp.elonbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.repo.TelegramUserRepo;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepo telegramUserRepo;

    public TelegramUser findUser(Long chatId, String firstName) {
        return telegramUserRepo.findById(chatId)
                .orElseGet(() -> createAndSaveUser(firstName, chatId));
    }

    private TelegramUser createAndSaveUser(String firstName, Long chatId) {
        TelegramUser user = TelegramUser.builder()
                .id(chatId)
                .firstName(firstName)
                .state(TgState.START)
                .build();
        return telegramUserRepo.save(user);
    }

    public void changeUserState(TelegramUser user, TgState state) {
        user.setState(state);
        telegramUserRepo.save(user);
    }

}
