package uz.pdp.elonbot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.PosterDetails;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.ScooterType;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.service.MessageService;
import uz.pdp.elonbot.service.PostService;
import uz.pdp.elonbot.service.TelegramUserService;
import uz.pdp.elonbot.util.ValidationUtil;

@Service
@RequiredArgsConstructor
public class BotService {

    private final MessageService messageService;
    private final TelegramUserService userService;
    private final ValidationUtil validationUtil;
    private final BotUtils botUtils;
    private final PostService postService;

    public void showMenu(TelegramUser user) {
        messageService.sendWithButton(user, "Tanlang", botUtils.createMenuButtons());
        userService.changeUserState(user, TgState.CHOOSING_MENU);
    }

    public void createPosterAndAskPhoneNumber(TelegramUser user) {
        postService.createPoster(user);
        askPhoneNumber(user);
    }

    private void askPhoneNumber(TelegramUser user) {
        messageService.sendWithButton(user, "Telefon raqamingizni kiriting. (901234567)", botUtils.createCancelButton());
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
    }

    public void getPhoneAndAskScooterType(TelegramUser user, String text) {
        if (text.equals(BotConstants.CANCEL)) {
            postService.removePoster(user);
            messageService.sendMessage(user, "Bekor qilindi");
            showMenu(user);
        } else if (validationUtil.isValidPhoneNumber(text)) {
            postService.setPhone(user, text);
            askScooterType(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askPhoneNumber(user);
        }
    }

    private void askScooterType(TelegramUser user) {
        messageService.sendWithButton(user, "Skuter turini tanlang", botUtils.createScooterTypeButtons());
        userService.changeUserState(user, TgState.CHOOSING_SCOOTER_TYPE);
    }

    public void getScooterTypeAndAskModel(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            askPhoneNumber(user);
        } else if (validationUtil.isValidScooterType(text)) {
            postService.setScooterType(user, text);
            askScooterModel(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askScooterType(user);
        }
    }

    private void askScooterModel(TelegramUser user) {
        messageService.sendWithButton(user, "Skuter modelini kiriting", botUtils.createBackButton());
        userService.changeUserState(user, TgState.ENTERING_SCOOTER_MODEL);
    }

    public void getModelAndAskMaxSpeed(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            askScooterType(user);
        } else if (validationUtil.isValidModel(text)) {
            postService.setModel(user, text);
            askMaxSpeed(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askScooterModel(user);
        }
    }

    private void askMaxSpeed(TelegramUser user) {
        messageService.sendWithBackButton(user, "Maksimal tezligini kiriting km da (100)");
        userService.changeUserState(user, TgState.ENTERING_MAX_SPEED);
    }

    public void getMaxSpeedAndAskEnginePower(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            askScooterModel(user);
        } else if (validationUtil.isValidMaxSpeed(text)) {
            postService.setMaxSpeed(user, text);
            askEnginePower(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askMaxSpeed(user);
        }
    }

    private void askEnginePower(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getScooterType().equals(ScooterType.ELECTRIC) ? "Motor quvvatini" : "Motor ot kuchini";
        messageService.sendWithBackButton(user, text + " kiriting");
        userService.changeUserState(user, TgState.ENTERING_ENGINE_POWER);
    }

    public void getEnginePowerAndAskYear(TelegramUser user, String text) {


    }

}
