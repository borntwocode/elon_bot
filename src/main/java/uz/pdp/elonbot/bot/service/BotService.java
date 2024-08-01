package uz.pdp.elonbot.bot.service;

import com.pengrad.telegrambot.model.Message;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.TelegramUser;
import java.util.UUID;

public interface BotService {

    void showMenu(TelegramUser user);

    void createPosterAndAskPhoneNumber(TelegramUser user);

    void getPhoneAndAskScooterType(TelegramUser user, String text);

    void getScooterTypeAndAskModel(TelegramUser user, String text);

    void getModelAndAskMaxSpeed(TelegramUser user, String text);

    void getMaxSpeedAndAskEnginePower(TelegramUser user, String text);

    void getEnginePowerAndAskYear(TelegramUser user, String text);

    void getReleasedYearAndAskBatteryLife(TelegramUser user, String text);

    void getBatteryLifeAndAskKmDriven(TelegramUser user, String text);

    void getKmDrivenAndAskPrice(TelegramUser user, String text);

    void getPriceAndAskAddress(TelegramUser user, String text);

    void getAddressAndAskPhoto(TelegramUser user, String text);

    void getPhotoAndSendPost(TelegramUser user, Message message);

    void handleCancel(TelegramUser user);

    void sendPoster(TelegramUser user, Photo photo);

    void editPoster(TelegramUser user);

    void resendPoster(TelegramUser user);

    void sendPosterToGroup(TelegramUser user);

    void submitPost(UUID postId);

    void rejectPost(UUID postId);

    void soldPost(UUID postId);

    void deletePost(UUID postId);

    boolean isMemberOfChannel(Long chatId);

    void askToFollow(Long chatId);

    void checkUserIsMember(TelegramUser user);

}
