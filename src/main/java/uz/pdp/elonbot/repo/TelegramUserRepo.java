package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.elonbot.entity.TelegramUser;

public interface TelegramUserRepo extends JpaRepository<TelegramUser, Long> {



}