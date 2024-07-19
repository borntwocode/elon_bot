package uz.pdp.elonbot.bot;

import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.*;
import uz.pdp.elonbot.entity.*;
import uz.pdp.elonbot.entity.enums.*;
import uz.pdp.elonbot.service.*;
import uz.pdp.elonbot.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BotService {

    private final MessageService messageService;
    private final TelegramUserService userService;
    private final ValidationUtil validationUtil;
    private final BotUtils botUtils;
    private final PostService postService;
    private final PhotoService photoService;
    private final TelegramBot telegramBot;

    public void showMenu(TelegramUser user) {
        userService.changeUserState(user, TgState.CHOOSING_MENU);
        messageService.sendWithButton(user, BotValidation.CHOOSE, botUtils.createMenuButtons());
    }

    public void createPosterAndAskPhoneNumber(TelegramUser user) {
        postService.createPoster(user);
        askPhoneNumber(user);
    }

    private void askPhoneNumber(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
        messageService.sendWithButton(user, BotValidation.ENTER_PHONE, botUtils.createCancelButton());
    }

    public void getPhoneAndAskScooterType(TelegramUser user, String text) {
        if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidPhoneNumber(text)) {
            postService.setPhone(user, text);
            askScooterType(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askPhoneNumber(user);
        }
    }

    private void askScooterType(TelegramUser user) {
        userService.changeUserState(user, TgState.CHOOSING_SCOOTER_TYPE);
        messageService.sendWithButton(user, BotValidation.CHOOSE_SCOOTER, botUtils.createScooterTypeButtons());
    }

    public void getScooterTypeAndAskModel(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setPhone(user, null);
            askPhoneNumber(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidScooterType(text)) {
            postService.setScooterType(user, text);
            askScooterModel(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askScooterType(user);
        }
    }

    private void askScooterModel(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_SCOOTER_MODEL);
        messageService.sendWithBackButton(user, BotValidation.ENTER_SCOOTER_MODEL);
    }

    public void getModelAndAskMaxSpeed(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setScooterType(user, null);
            askScooterType(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidModel(text)) {
            postService.setModel(user, text);
            askMaxSpeed(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askScooterModel(user);
        }
    }

    private void askMaxSpeed(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_MAX_SPEED);
        messageService.sendWithBackButton(user, BotValidation.ENTER_MAXIMUM_SPEED);
    }

    public void getMaxSpeedAndAskEnginePower(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setModel(user, null);
            askScooterModel(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidMaxSpeed(text)) {
            postService.setMaxSpeed(user, text);
            askEnginePower(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askMaxSpeed(user);
        }
    }

    private void askEnginePower(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getScooterType().equals(ScooterType.ELECTRIC) ? BotValidation.ENTER_ENGINE_POWER :BotValidation.ENTER_GASOLINE_POWER;
        userService.changeUserState(user, TgState.ENTERING_ENGINE_POWER);
        messageService.sendWithBackButton(user, text + BotValidation.ENTER_MESSAGE);
    }

    public void getEnginePowerAndAskYear(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setMaxSpeed(user, null);
            askMaxSpeed(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidEnginePower(text)) {
            postService.setEnginePower(user, text);
            askReleasedYear(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askEnginePower(user);
        }
    }

    private void askReleasedYear(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_RELEASED_YEAR);
        messageService.sendWithBackButton(user, BotValidation.CREATE_SCOOTER_DATE);
    }

    public void getReleasedYearAndAskBatteryLife(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setEnginePower(user, null);
            askEnginePower(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidReleasedYear(text)) {
            postService.setReleasedYear(user, text);
            askBatteryLife(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askReleasedYear(user);
        }
    }

    private void askBatteryLife(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getScooterType().equals(ScooterType.ELECTRIC) ? BotValidation.ENTER_CHARGE: BotValidation.ENTER_GASOLINE_FOR_100KM;
        userService.changeUserState(user, TgState.ENTERING_BATTERY_LIFE);
        messageService.sendWithBackButton(user, text + BotValidation.ENTER_MESSAGE);
    }

    public void getBatteryLifeAndAskKmDriven(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setReleasedYear(user, null);
            askReleasedYear(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidBatteryLife(text)) {
            postService.setBatteryLife(user, text);
            askKmDriven(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askBatteryLife(user);
        }
    }

    private void askKmDriven(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_KM_DRIVEN);
        messageService.sendWithBackButton(user, BotValidation.ENTER_SCOOTER_KM);
    }

    public void getKmDrivenAndAskPrice(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setBatteryLife(user, null);
            askBatteryLife(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidKmDriven(text)) {
            postService.setKmDriven(user, text);
            askPrice(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askKmDriven(user);
        }
    }

    private void askPrice(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PRICE);
        messageService.sendWithBackButton(user, BotValidation.ENTER_PRICE);
    }

    public void getPriceAndAskAddress(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setKmDriven(user, null);
            askKmDriven(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidPrice(text)) {
            postService.setPrice(user, text);
            askAddress(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askPrice(user);
        }
    }

    private void askAddress(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_ADDRESS);
        messageService.sendWithBackButton(user, BotValidation.ADDRESS);
    }

    public void getAddressAndAskPhoto(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setPrice(user, null);
            askPrice(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (validationUtil.isValidAddress(text)) {
            postService.setAddress(user, text);
            askScooterPhoto(user);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askAddress(user);
        }
    }

    private void askScooterPhoto(TelegramUser user) {
        userService.changeUserState(user, TgState.SENDING_PHOTO);
        messageService.sendWithBackButton(user, BotValidation.IMAGES);
    }

    public void getPhotosAndSendPost(TelegramUser user, Message message) {
        if (Objects.equals(message.text(), BotConstants.BACK)) {
            postService.setAddress(user, null);
            askAddress(user);
        } else if (Objects.equals(message.text(), BotConstants.CANCEL)) {
            postService.deletePosterIfPresent(user);
            messageService.sendMessage(user, BotValidation.CANCEL);
            showMenu(user);
        } else if (message.photo() != null) {
            Photo photo = photoService.getPhoto(message.photo());
            postService.setPhoto(user, photo);
            String text = postService.getPosterMessage(user);
            sendPoster(user, text, photo);
        } else {
            messageService.sendMessage(user, BotValidation.INVALID_FORMAT);
            askScooterPhoto(user);
        }
    }

    private void sendPoster(TelegramUser user, String text, Photo photo) {
        SendPhoto sendPhoto = new SendPhoto(user.getId(), photo.getContent());
        sendPhoto.caption(text);
        sendPhoto.replyMarkup(botUtils.createPosterButton());
        userService.changeUserState(user, TgState.CHOOSING_POST_OPTIONS);
        telegramBot.execute(sendPhoto);
    }

    public void processPostOptions(TelegramUser user, String text) {

    }

}
