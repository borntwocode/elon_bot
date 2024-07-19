package uz.pdp.elonbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.elonbot.bot.BotService;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class BotController {

    private final BotService botService;

    @PostMapping("/accepted/{postId}")
    void changePostAndSendToChannel(@PathVariable UUID postId){
        botService.postAccepted(postId, true);
    }

    @PostMapping("/rejected/{postId}")
    void deletePostAndNotifyUser(@PathVariable UUID postId){
        botService.notifyUser(postId);
    }

    @PostMapping("/sold/{postId}")
    void makePostSoldAndEditMessage(@PathVariable UUID postId){
        botService.makePostSold(postId);
    }

}
