package uz.pdp.elonbot.bot.service;

import com.pengrad.telegrambot.model.request.ParseMode;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.*;
import uz.pdp.elonbot.messages.BotConstants;
import uz.pdp.elonbot.bot.BotUtils;
import uz.pdp.elonbot.entity.*;
import uz.pdp.elonbot.entity.enums.*;
import uz.pdp.elonbot.service.*;
import uz.pdp.elonbot.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import java.util.*;
import static uz.pdp.elonbot.messages.ValidationMessages.*;
import static uz.pdp.elonbot.messages.BotMessages.*;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService{

    private final TelegramUserService telegramUserService;
    private final MessageService messageService;
    private final TelegramUserService userService;
    private final ValidationUtil validationUtil;
    private final BotUtils botUtils;
    private final PostService postService;
    private final PhotoService photoService;
    private final TelegramBot telegramBot;
    private final TestService testService;
    private final ChannelService channelService;
    private final GroupService groupService;

    @Override
    public void showMenu(TelegramUser user) {
        postService.deletePosterIfPresent(user);
        userService.changeUserState(user, TgState.CHOOSING_MENU);
        messageService.sendWithButton(user, CHOOSE_OPTIONS, botUtils.createMenuButtons());
    }

    @Override
    public void createPosterAndAskPhoneNumber(TelegramUser user) {
        if (postService.isPendingPoster(user)) {
            messageService.sendMessage(user, WAIT_YOUR_POST_IS_IN_WORK);
            showMenu(user);
        } else {
//            testService.createPost(user, this);
            postService.createPoster(user);
            askPhoneNumber(user);
        }
    }

    private void askPhoneNumber(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
        messageService.sendWithButton(user, ENTER_PHONE_NUMBER, botUtils.createCancelButton());
    }

    @Override
    public void getPhoneAndAskScooterType(TelegramUser user, String text) {
        if (text.equals(BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (validationUtil.isValidPhoneNumber(text)) {
            postService.setPhone(user, text);
            askScooterType(user);
        } else {
            messageService.sendMessage(user, INVALID_PHONE);
            askPhoneNumber(user);
        }
    }

    private void askScooterType(TelegramUser user) {
        userService.changeUserState(user, TgState.CHOOSING_SCOOTER_TYPE);
        messageService.sendWithButton(user, CHOOSE_SCOOTER_TYPE, botUtils.createScooterTypeButtons());
    }

    @Override
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
            messageService.sendMessage(user, INVALID_SCOOTER_TYPE);
            askScooterType(user);
        }
    }

    private void askScooterModel(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_SCOOTER_MODEL);
        messageService.sendWithBackButton(user, ENTER_SCOOTER_MODEL);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_SCOOTER_MODEL);
            askScooterModel(user);
        }
    }

    private void askMaxSpeed(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_MAX_SPEED);
        messageService.sendWithBackButton(user, ENTER_MAX_SPEED);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_MAX_SPEED);
            askMaxSpeed(user);
        }
    }

    private void askEnginePower(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getEnginePowerOrHorsePower();
        userService.changeUserState(user, TgState.ENTERING_ENGINE_POWER);
        messageService.sendWithBackButton(user, text);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_ENGINE_POWER);
            askEnginePower(user);
        }
    }

    private void askReleasedYear(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_RELEASED_YEAR);
        messageService.sendWithBackButton(user, ENTER_RELEASED_YEAR);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_RELEASED_YEAR);
            askReleasedYear(user);
        }
    }

    private void askBatteryLife(TelegramUser user) {
        PosterDetails posterDetails = postService.getPosterDetails(user);
        String text = posterDetails.getFuelOrBatteryConsumption();
        userService.changeUserState(user, TgState.ENTERING_BATTERY_LIFE);
        messageService.sendWithBackButton(user, text);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_BATTERY_LIFE);
            askBatteryLife(user);
        }
    }

    private void askKmDriven(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_KM_DRIVEN);
        messageService.sendWithBackButton(user, ENTER_KM_DRIVEN);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_KM_DRIVEN);
            askKmDriven(user);
        }
    }

    private void askPrice(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_PRICE);
        messageService.sendWithBackButton(user, ENTER_SCOOTER_PRICE);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_PRICE);
            askPrice(user);
        }
    }

    private void askAddress(TelegramUser user) {
        userService.changeUserState(user, TgState.ENTERING_ADDRESS);
        messageService.sendWithBackButton(user, ENTER_ADDRESS);
    }

    @Override
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
            messageService.sendMessage(user, INVALID_ADDRESS);
            askAddress(user);
        }
    }

    private void askScooterPhoto(TelegramUser user) {
        messageService.sendWithBackButton(user, SEND_PHOTO);
        userService.changeUserState(user, TgState.SENDING_PHOTO);
    }

    @Override
    public void getPhotoAndSendPost(TelegramUser user, Message message) {
        String text = message.text();
        if (Objects.equals(text, BotConstants.BACK)) {
            postService.setAddress(user, null);
            askAddress(user);
        } else if (Objects.equals(text, BotConstants.CANCEL)) {
            handleCancel(user);
        } else if (Objects.equals(text, BotConstants.EDIT)) {

        } else if (message.photo() != null) {
            Photo photo = photoService.getPhoto(message.photo());
            postService.setPhoto(user, photo);
            sendPoster(user, photo);
        } else {
            messageService.sendMessage(user, INVALID_PHOTO);
            askScooterPhoto(user);
        }
    }

    @Override
    public void handleCancel(TelegramUser user) {
        postService.deletePosterIfPresent(user);
        messageService.sendMessage(user, CANCELLED);
        showMenu(user);
    }

    @Override
    public void sendPoster(TelegramUser user, Photo photo) {
        String posterMessage = postService.getPosterDetails(user).toString();
        SendPhoto sendPhoto = postService.createPosterHeader(user, photo, posterMessage);
        sendPhoto.replyMarkup(botUtils.createPosterButton());
        sendPhoto.parseMode(ParseMode.MarkdownV2);
        userService.changeUserState(user, TgState.CHOOSING_POST_OPTIONS);
        telegramBot.execute(sendPhoto);
    }

    @Override
    public void editPoster(TelegramUser user) {
        postService.deletePhoto(user);
        askScooterPhoto(user);
    }

    @Override
    public void resendPoster(TelegramUser user) {
        messageService.sendMessage(user, POST_OPTIONS_HINT);
        sendPoster(user, postService.getPhoto(user));
    }

    @Override
    public void sendPosterToGroup(TelegramUser user) {
        messageService.sendMessage(user, WAIT_YOUR_POST_IS_IN_WORK);
        postService.setIsCompleted(user, true);
        Photo photo = postService.getPhoto(user);
        String posterMessage = postService.getPosterDetails(user).toString();
        Poster poster = postService.getPoster(user);
        groupService.createAndSendPoster(poster, photo, posterMessage);
        showMenu(user);
    }

    @Override
    public void submitPost(UUID postId) {
        Optional<Poster> posterOpt = postService.getPoster(postId);
        if (posterOpt.isPresent() && !posterOpt.get().isAccepted()) {
            Poster poster = posterOpt.get();
            TelegramUser user = poster.getUser();
            channelService.sendToChannel(poster, true);
            messageService.sendMessage(user, POST_ACCEPTED);
            groupService.editAcceptedPostButtons(poster);
        }
    }

    @Override
    public void rejectPost(UUID postId) {
        Optional<Poster> posterOpt = postService.getPoster(postId);
        if(posterOpt.isPresent()){
            Poster poster = posterOpt.get();
            if (!poster.isAccepted()) {
                TelegramUser user = telegramUserService.getUserByPostId(postId);
                postService.deletePoster(postId);
                messageService.sendMessage(user, POST_REJECTED);
                groupService.deleteRejectedPost(poster);
            }
        }
    }

    @Override
    public void soldPost(UUID postId) {
        Optional<Poster> posterOpt = postService.getPoster(postId);
        if (posterOpt.isPresent() && posterOpt.get().isAccepted() && !posterOpt.get().isSold()) {
            Poster poster = posterOpt.get();
            channelService.editPostCaption(posterOpt.get());
            groupService.editSoldPostButtons(poster);
        }
    }

    @Override
    public void deletePost(UUID postId) {
        Optional<Poster> posterOpt = postService.getPoster(postId);
        if (posterOpt.isPresent()) {
            Poster poster = posterOpt.get();
            if (poster.isAccepted()) {
                channelService.deletePost(poster);
                groupService.deleteRejectedPost(poster);
                postService.deletePoster(postId);
            }
        }
    }

    @Override
    public boolean isMemberOfChannel(Long chatId) {
        return channelService.isUserMemberOfChannel(chatId);
    }

    @Override
    public void askToFollow(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, FOLLOW_CHANNEL);
        sendMessage.replyMarkup(botUtils.createFollowChannelButtons(chatId));
        telegramBot.execute(sendMessage);
    }

    @Override
    public void checkUserIsMember(TelegramUser user) {
        if(channelService.isUserMemberOfChannel(user.getUserId())){
            showMenu(user);
        }else{
            messageService.sendMessage(user, NOT_FOLLOWED);
        }
    }

}
