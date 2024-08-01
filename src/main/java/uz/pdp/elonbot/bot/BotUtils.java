package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.enums.ScooterType;
import static uz.pdp.elonbot.messages.BotConstants.*;

@Service
@RequiredArgsConstructor
public class BotUtils {

    @Value("${channel1.url}")
    private String channel1Url;
    @Value("${channel2.url}")
    private String channel2Url;

    private static final KeyboardButton BACK_BUTTON = new KeyboardButton(BACK);
    private static final KeyboardButton POST_BUTTON = new KeyboardButton(POST);
    private static final KeyboardButton CANCEL_BUTTON = new KeyboardButton(CANCEL);
    private static final KeyboardButton EDIT_BUTTON = new KeyboardButton(EDIT);

    public Keyboard createScooterTypeButtons() {
        var electric = new KeyboardButton(ScooterType.ELECTRIC.getDisplayName());
        var gasoline = new KeyboardButton(ScooterType.GASOLINE.getDisplayName());
        var keyboardMarkup = new ReplyKeyboardMarkup(electric, gasoline);
        return keyboardMarkup.addRow(CANCEL_BUTTON, BACK_BUTTON).oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createMenuButtons() {
        var keyboardMarkup = new ReplyKeyboardMarkup(POST_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createCancelButton() {
        var keyboardMarkup = new ReplyKeyboardMarkup(CANCEL_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createPosterButton() {
        var keyboardMarkup = new ReplyKeyboardMarkup(EDIT_BUTTON, POST_BUTTON).addRow(CANCEL_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createBackAndCancelButtons() {
        var keyboardMarkup = new ReplyKeyboardMarkup(CANCEL_BUTTON, BACK_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createBackButton() {
        var keyboardMarkup = new ReplyKeyboardMarkup(BACK_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createPosterAdminButtons(Poster poster) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(
                new InlineKeyboardButton(REJECT_POST).callbackData(REJECT_POST + "_" + poster.getId()),
                new InlineKeyboardButton(SUBMIT_POST).callbackData(SUBMIT_POST + "_" + poster.getId())
        );
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createAcceptedPosterButtons(Poster poster) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(
                new InlineKeyboardButton(DELETE_POST).callbackData(DELETE_POST + "_" + poster.getId()),
                new InlineKeyboardButton(SOLD_POST).callbackData(SOLD_POST + "_" + poster.getId())
        );
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createSoldPostButton(Poster poster) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(DELETE_POST).callbackData(DELETE_POST + "_" + poster.getId())
        );
    }

    public Keyboard createFollowChannelButtons(Long chatId) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(FOLLOW).url(channel1Url),
                new InlineKeyboardButton(FOLLOW).url(channel2Url)
        ).addRow(new InlineKeyboardButton(CHECK).callbackData(CHECK + "/" + chatId));
    }

}
