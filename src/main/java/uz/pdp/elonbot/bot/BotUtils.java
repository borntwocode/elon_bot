package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.enums.ScooterType;

@Service
@RequiredArgsConstructor
public class BotUtils {

    private static final KeyboardButton BACK_BUTTON = new KeyboardButton(BotConstants.BACK);
    private static final KeyboardButton POST_BUTTON = new KeyboardButton(BotConstants.POST);
    private static final KeyboardButton CANCEL_BUTTON = new KeyboardButton(BotConstants.CANCEL);

    public Keyboard createScooterTypeButtons() {
        var electric = new KeyboardButton(ScooterType.ELECTRIC.getDisplayName());
        var gasoline = new KeyboardButton(ScooterType.GASOLINE.getDisplayName());
        var keyboardMarkup = new ReplyKeyboardMarkup(electric, gasoline);
        return keyboardMarkup.addRow(BACK_BUTTON).oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createMenuButtons() {
        var keyboardMarkup = new ReplyKeyboardMarkup(POST_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createCancelButton() {
        var keyboardMarkup = new ReplyKeyboardMarkup(CANCEL_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createBackButton() {
        var keyboardMarkup = new ReplyKeyboardMarkup(BACK_BUTTON);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

}
