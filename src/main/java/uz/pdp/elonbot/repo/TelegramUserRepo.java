package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.elonbot.entity.TelegramUser;
import java.util.UUID;

public interface TelegramUserRepo extends JpaRepository<TelegramUser, Long> {

    @Query(value = "select t.* from telegram_user t join poster p on t.id = p.user_id limit 1", nativeQuery = true)
    TelegramUser findByPostId(UUID postId);

}