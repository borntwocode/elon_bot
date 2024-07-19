package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.model.request.ParseMode;
import org.springframework.beans.factory.annotation.Value;
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

    private final TelegramUserService telegramUserService;
    @Value("${channel.username}")
    private String channelUsername;

    private final MessageService messageService;
    private final TelegramUserService userService;
    private final ValidationUtil validationUtil;
    private final BotUtils botUtils;
    private final PostService postService;
    private final PhotoService photoService;
    private final TelegramBot telegramBot;
    private final AdminService adminService;
    private final TestService testService;

    public void showMenu(TelegramUser user) {
        postService.deletePosterIfPresent(user);
        userService.changeUserState(user, TgState.CHOOSING_MENU);
        messageService.sendWithButton(user, "Tanlang", botUtils.createMenuButtons());
    }

    public void createPosterAndAskPhoneNumber(TelegramUser user) {
        if (postService.isPendingPoster(user)) {
            messageService.sendMessage(user, "E'lon berib bo'lgansiz");
            showMenu(user);
        } else {
            testService.createPost(user, this);
//            postService.createPoster(user);
//            askPhoneNumber(user);
        }
    }

    private void askPhoneNumber(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
        messageService.sendWithButton(user, "Telefon raqamingizni kiriting. (901234567)", botUtils.createCancelButton());
    }

    public void getPhoneAndAskScooterType(TelegramUser user, String text) {
        if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidPhoneNumber(text)) {
            postService.setPhone(user, text);
            askScooterType(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askPhoneNumber(user);
        }
    }

    private void askScooterType(TelegramUser user) {
        userService.changeUserState(user, TgState.CHOOSING_SCOOTER_TYPE);
        messageService.sendWithButton(user, "Skuter turini tanlang", botUtils.createScooterTypeButtons());
    }

    public void getScooterTypeAndAskModel(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setPhone(user, null);
            askPhoneNumber(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidScooterType(text)) {
            postService.setScooterType(user, text);
            askScooterModel(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askScooterType(user);
        }
    }

    private void askScooterModel(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_SCOOTER_MODEL);
        messageService.sendWithBackButton(user, "Skuter modelini kiriting");
    }

    public void getModelAndAskMaxSpeed(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setScooterType(user, null);
            askScooterType(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidModel(text)) {
            postService.setModel(user, text);
            askMaxSpeed(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askScooterModel(user);
        }
    }

    private void askMaxSpeed(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_MAX_SPEED);
        messageService.sendWithBackButton(user, "Maksimal tezligini kiriting km da (100)");
    }

    public void getMaxSpeedAndAskEnginePower(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setModel(user, null);
            askScooterModel(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
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
        userService.changeUserState(user, TgState.ENTERING_ENGINE_POWER);
        messageService.sendWithBackButton(user, text + " kiriting");
    }

    public void getEnginePowerAndAskYear(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setMaxSpeed(user, null);
            askMaxSpeed(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidEnginePower(text)) {
            postService.setEnginePower(user, text);
            askReleasedYear(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askEnginePower(user);
        }
    }

    private void askReleasedYear(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_RELEASED_YEAR);
        messageService.sendWithBackButton(user, "Skuter chiqarilgan yilini kiriting");
    }

    public void getReleasedYearAndAskBatteryLife(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setEnginePower(user, null);
            askEnginePower(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidReleasedYear(text)) {
            postService.setReleasedYear(user, text);
            askBatteryLife(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askReleasedYear(user);
        }
    }

    private void askBatteryLife(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getScooterType().equals(ScooterType.ELECTRIC) ? "Akkumulyator quvvatini" : "100km ga nechi litr benzin sarfalshini";
        userService.changeUserState(user, TgState.ENTERING_BATTERY_LIFE);
        messageService.sendWithBackButton(user, text + " kiriting");
    }

    public void getBatteryLifeAndAskKmDriven(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setReleasedYear(user, null);
            askReleasedYear(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidBatteryLife(text)) {
            postService.setBatteryLife(user, text);
            askKmDriven(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askBatteryLife(user);
        }
    }

    private void askKmDriven(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_KM_DRIVEN);
        messageService.sendWithBackButton(user, "Skuter bosib o'tgan yo'lni kiriting");
    }

    public void getKmDrivenAndAskPrice(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setBatteryLife(user, null);
            askBatteryLife(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidKmDriven(text)) {
            postService.setKmDriven(user, text);
            askPrice(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askKmDriven(user);
        }
    }

    private void askPrice(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PRICE);
        messageService.sendWithBackButton(user, "Skuter narxini kiriting");
    }

    public void getPriceAndAskAddress(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setKmDriven(user, null);
            askKmDriven(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidPrice(text)) {
            postService.setPrice(user, text);
            askAddress(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askPrice(user);
        }
    }

    private void askAddress(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_ADDRESS);
        messageService.sendWithBackButton(user, "Adresingizni kiriting (Viloyat, Shahar)");
    }

    public void getAddressAndAskPhoto(TelegramUser user, String text) {
        if (text.equals(BotConstants.BACK)) {
            postService.setPrice(user, null);
            askPrice(user);
        } else if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidAddress(text)) {
            postService.setAddress(user, text);
            askScooterPhoto(user);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askAddress(user);
        }
    }

    private void askScooterPhoto(TelegramUser user) {
        userService.changeUserState(user, TgState.SENDING_PHOTO);
        messageService.sendWithBackButton(user, "Skuter rasm(lar)ini jo'nating");
    }

    public void getPhotosAndSendPost(TelegramUser user, Message message) {
        if (Objects.equals(message.text(), BotConstants.BACK)) {
            postService.setAddress(user, null);
            askAddress(user);
        } else if (Objects.equals(message.text(), BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (message.photo() != null) {
            Photo photo = photoService.getPhoto(message.photo());
            postService.setPhoto(user, photo);
            sendPoster(user, photo);
        } else {
            messageService.sendMessage(user, "Noto'g'ri format");
            askScooterPhoto(user);
        }
    }

    private void handleCancel(TelegramUser user) {
        postService.deletePosterIfPresent(user);
        messageService.sendMessage(user, "Bekor qilindi");
        showMenu(user);
    }

    public void sendPoster(TelegramUser user, Photo photo) {
        String posterMessage = postService.getPosterDetails(user).toString();
        SendPhoto sendPhoto = postService.createPosterHeader(user, photo, posterMessage);
        sendPhoto.replyMarkup(botUtils.createPosterButton());
        sendPhoto.parseMode(ParseMode.MarkdownV2);
        userService.changeUserState(user, TgState.CHOOSING_POST_OPTIONS);
        telegramBot.execute(sendPhoto);
    }

    public void handlePostOptions(TelegramUser user, String text) {
        switch (text) {
            case BotConstants.CANCEL -> handleCancel(user);
            case BotConstants.EDIT -> editPoster(user);
            case BotConstants.POST -> sendPosterToAdmin(user);
            default -> resendPoster(user);
        }
    }

    private void editPoster(TelegramUser user) {
        postService.deletePhoto(user);
        askScooterPhoto(user);
    }

    private void resendPoster(TelegramUser user) {
        messageService.sendMessage(user, "Noto'g'ri format");
        sendPoster(user, postService.getPhoto(user));
    }

    private void sendPosterToAdmin(TelegramUser user) {
        postService.setIsCompleted(user, true);
        userService.changeUserState(user, TgState.PENDING);
        Photo photo = postService.getPhoto(user);
        String posterMessage = postService.getPosterDetails(user).toString();
        Poster poster = postService.getPoster(user);
        adminService.createAndSendPoster(poster, photo, posterMessage);
    }

    public void sendToChannel(UUID postId, boolean isAccepted) {
        PosterDetails posterDetails = postService.setIsAccepted(postId, isAccepted);
        SendPhoto sendPhoto = new SendPhoto(channelUsername, posterDetails.getPhoto().getContent());
        sendPhoto.caption(posterDetails.toString()).parseMode(ParseMode.MarkdownV2);
        telegramBot.execute(sendPhoto);
    }

    public void notifyUser(UUID postId) {
        TelegramUser user = telegramUserService.getUserByPostId(postId);
        postService.deletePoster(postId);
        messageService.sendMessage(user, "Siznig eloningiz admin tomonidan rad etildi");
    }

}
