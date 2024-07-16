package uz.pdp.elonbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.client.AdminClient;
import uz.pdp.elonbot.dto.AdminDTO;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.service.TelegramUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final AdminClient adminClient;
    private final TelegramBot adminBot;
    private final BotService botService;
    private final TelegramUserService userService;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            Message message = update.message();
            String text = message.text();
            Long chatId = message.chat().id();
            String firstName = message.from().firstName();
            TelegramUser user = userService.findUser(chatId, firstName);

            if (text.equals(BotCommands.START)) {
                botService.showMenu(user);
            } else if (user.getState().equals(TgState.CHOOSING_MENU)) {
                switch (text) {
                    case BotConstants.POST -> botService.createPosterAndAskPhoneNumber(user);
                }
            } else if (user.getState().equals(TgState.ENTERING_PHONE_NUMBER)) {
                botService.getPhoneAndAskScooterType(user, text);
            } else if (user.getState().equals(TgState.CHOOSING_SCOOTER_TYPE)) {
                botService.getScooterTypeAndAskModel(user, text);
            } else if (user.getState().equals(TgState.ENTERING_SCOOTER_MODEL)) {
                botService.getModelAndAskMaxSpeed(user, text);
            } else if (user.getState().equals(TgState.ENTERING_MAX_SPEED)) {
                botService.getMaxSpeedAndAskEnginePower(user, text);
            } else if(user.getState().equals(TgState.ENTERING_ENGINE_POWER)) {
                botService.getEnginePowerAndAskYear(user, text);
            }

        } else if (update.callbackQuery() != null) {
            CallbackQuery callbackQuery = update.callbackQuery();
            // Handle callback query if needed
        }
    }

    private void sendMessageToAnotherBot(SendMessage message, AdminDTO adminDTO) {
        List<AdminDTO> admins = adminClient.getAdmins();
        for (AdminDTO admin : admins) {
            SendMessage request = new SendMessage(admin.getChatId(), "Hello from one bot to another!");
            adminBot.execute(request);
        }
    }

}
