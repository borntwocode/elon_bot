package uz.pdp.elonbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Poster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private TelegramUser user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean isCompleted;

    @OneToOne(cascade = CascadeType.ALL)
    private PosterDetails posterDetails;

}
