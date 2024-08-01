package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.service.TelegramUserService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotService botService;
    private final TelegramUserService userService;
    private final TelegramBot telegramBot;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.data();
        if (data.contains("/")) {
            User from = callbackQuery.from();
            String[] split = data.split("/");
            String command = split[0];
            Long chatId = Long.parseLong(split[1]);
            TelegramUser user = userService.findUser(chatId, from.id(), from.username());
            if (command.equals(BotConstants.CHECK)) {
                botService.checkUserIsMember(user);
            }
        } else if (data.contains("_")) {
            String[] s = data.split("_");
            String command = s[0];
            UUID postId = UUID.fromString(s[1]);
            switch (command) {
                case BotConstants.SUBMIT_POST -> botService.submitPost(postId);
                case BotConstants.REJECT_POST -> botService.rejectPost(postId);
                case BotConstants.SOLD_POST -> botService.soldPost(postId);
                case BotConstants.DELETE_POST -> botService.deletePost(postId);
            }
        }

    }

    private void handleMessage(Message message) {
        Long userId = message.from().id();
        if (botService.isMemberOfChannel(userId)) {
            continueMessageHandling(message);
        } else {
            botService.askToFollow(userId);
        }
    }

    private void continueMessageHandling(Message message) {
        String text = message.text();
        Long chatId = message.chat().id();
        Long userId = message.from().id();
        String firstName = message.from().firstName();
        TelegramUser user = userService.findUser(chatId, userId, firstName);

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

    private void handlePostOptions(TelegramUser user, String text) {
        switch (text) {
            case BotConstants.CANCEL -> botService.handleCancel(user);
            case BotConstants.EDIT -> botService.editPoster(user);
            case BotConstants.POST -> botService.sendPosterToGroup(user);
            default -> botService.resendPoster(user);
        }
    }

    public void handleAdminUpdate(Update update) {
        if (update.channelPost() != null) {
            Message message = update.channelPost();
            Long chatId = message.chat().id();
            Integer messageId = message.messageId();
            Chat chat = message.senderChat();
            if (Objects.equals(chat.username(), "armada_motors")) {
                List<TelegramUser> users = userService.getAllFollowers();
                for (TelegramUser user : users) {
                    if (chatId != null && messageId != null) {
                        ForwardMessage forwardMessage = new ForwardMessage(user.getId(), chatId, messageId);
                        telegramBot.execute(forwardMessage);
                    }
                }
            }
        }
    }

}
