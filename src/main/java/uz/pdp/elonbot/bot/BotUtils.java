package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.enums.ScooterType;

@Service
@RequiredArgsConstructor
public class BotUtils {

    private static final KeyboardButton BACK_BUTTON = new KeyboardButton(BotConstants.BACK);
    private static final KeyboardButton POST_BUTTON = new KeyboardButton(BotConstants.POST);
    private static final KeyboardButton CANCEL_BUTTON = new KeyboardButton(BotConstants.CANCEL);
    private static final KeyboardButton EDIT_BUTTON = new KeyboardButton(BotConstants.EDIT);

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
                new InlineKeyboardButton(BotConstants.REJECT_POST).callbackData(BotConstants.REJECT_POST + "_" + poster.getId()),
                new InlineKeyboardButton(BotConstants.SUBMIT_POST).callbackData(BotConstants.SUBMIT_POST + "_" + poster.getId())
        );
        return keyboardMarkup;
    }

}
