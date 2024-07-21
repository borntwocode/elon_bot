package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.service.TelegramUserService;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotService botService;
    private final TelegramUserService userService;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            CallbackQuery callbackQuery = update.callbackQuery();
        }

    }

    private void handleMessage(Message message) {
        String text = message.text();
        Long chatId = message.chat().id();
        String firstName = message.from().firstName();
        TelegramUser user = userService.findUser(chatId, firstName);

        if (!user.getState().equals(TgState.SENDING_PHOTO) && text != null) {
            if (text.equals(BotCommands.START)) {
                botService.showMenu(user);
            } else {
                switch (user.getState()) {
                    case CHOOSING_MENU -> {
                        if (text.equals(BotConstants.POST)) {
                            botService.createPosterAndAskPhoneNumber(user);
                        }
                    }
                    case ENTERING_PHONE_NUMBER -> botService.getPhoneAndAskScooterType(user, text);
                    case CHOOSING_SCOOTER_TYPE -> botService.getScooterTypeAndAskModel(user, text);
                    case ENTERING_SCOOTER_MODEL -> botService.getModelAndAskMaxSpeed(user, text);
                    case ENTERING_MAX_SPEED -> botService.getMaxSpeedAndAskEnginePower(user, text);
                    case ENTERING_ENGINE_POWER -> botService.getEnginePowerAndAskYear(user, text);
                    case ENTERING_RELEASED_YEAR -> botService.getReleasedYearAndAskBatteryLife(user, text);
                    case ENTERING_BATTERY_LIFE -> botService.getBatteryLifeAndAskKmDriven(user, text);
                    case ENTERING_KM_DRIVEN -> botService.getKmDrivenAndAskPrice(user, text);
                    case ENTERING_PRICE -> botService.getPriceAndAskAddress(user, text);
                    case ENTERING_ADDRESS -> botService.getAddressAndAskPhoto(user, text);
                    case CHOOSING_POST_OPTIONS -> handlePostOptions(user, text);
                }
            }
        } else if (user.getState().equals(TgState.SENDING_PHOTO)) {
            botService.getPhotoAndSendPost(user, message);
        }
    }

    public void handlePostOptions(TelegramUser user, String text) {
        switch (text) {
            case BotConstants.CANCEL -> botService.handleCancel(user);
            case BotConstants.EDIT -> botService.editPoster(user);
            case BotConstants.POST -> botService.sendPosterToAdmin(user);
            default -> botService.resendPoster(user);
        }
    }

}
