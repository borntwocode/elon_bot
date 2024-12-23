package uz.pdp.elonbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import uz.pdp.elonbot.entity.enums.TgState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TelegramUser {

    @Id
    private Long id;

    private Long userId;

    private String phoneNumber;

    private String firstName;

    private boolean isFollowed;

    @Enumerated(value = EnumType.STRING)
    private TgState state;

}
