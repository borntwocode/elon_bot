package uz.pdp.elonbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.PosterDetails;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.ScooterType;
import uz.pdp.elonbot.entity.enums.TgState;
import uz.pdp.elonbot.repo.PosterDetailsRepo;
import uz.pdp.elonbot.repo.PosterRepo;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PosterRepo posterRepo;
    private final PosterDetailsRepo posterDetailsRepo;
    private final TelegramUserService telegramUserService;

    public void createPoster(TelegramUser user) {
        Poster poster = Poster.builder()
                .user(user)
                .posterDetails(new PosterDetails())
                .build();
        posterRepo.save(poster);
    }

    public void setPhone(TelegramUser user, String phoneNumber) {
        updatePosterDetails(user, details -> details.setPhoneNumber(phoneNumber));
    }

    public void setScooterType(TelegramUser user, String scooterType) {
        updatePosterDetails(user, details ->
                details.setScooterType(scooterType == null ? null : ScooterType.fromDisplayName(scooterType))
        );
    }

    public void setModel(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setModel(text));
    }

    public void setMaxSpeed(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setMaxSpeed(text));
    }

    public void setEnginePower(TelegramUser user, String text) {
        updatePosterDetails(user, details -> {
            if (details.getScooterType().equals(ScooterType.ELECTRIC)) {
                details.setEnginePower(text);
            } else if (details.getScooterType().equals(ScooterType.GASOLINE)) {
                details.setHorsePower(text);
            }
        });
    }

    public void setReleasedYear(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setReleasedYear(text));
    }

    public void setBatteryLife(TelegramUser user, String text) {
        updatePosterDetails(user, details -> {
            if (details.getScooterType().equals(ScooterType.ELECTRIC)) {
                details.setBatteryLifeToKm(text);
            } else if (details.getScooterType().equals(ScooterType.GASOLINE)) {
                details.setFuelTo100km(text);
            }
        });
    }

    public void setKmDriven(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setKmDriven(text));
    }

    public void setPrice(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setPrice(text));
    }

    public void setAddress(TelegramUser user, String text) {
        updatePosterDetails(user, details -> details.setAddress(text));
    }

    public void setPhoto(TelegramUser user, Photo photo) {
        updatePosterDetails(user, details -> details.setPhoto(photo));
    }

    public PosterDetails getPosterDetails(TelegramUser user) {
        return getPoster(user).getPosterDetails();
    }

    public Photo getPhoto(TelegramUser user) {
        return getPosterDetails(user).getPhoto();
    }

    public void deletePosterIfPresent(TelegramUser user) {
        List<Poster> posters = posterRepo.findAllByIsCompleted(false);
        posterRepo.deleteAll(posters);
        telegramUserService.changeUserState(user, TgState.START);
    }

    public String getPosterMessage(TelegramUser user) {
        PosterDetails details = getPosterDetails(user);
        StringBuilder sb = new StringBuilder();
        sb.append("Turi: ").append(details.getScooterType().getDisplayName()).append("\n")
                .append("Modeli: ").append(details.getModel()).append("\n")
                .append("Tezlik chegarasi: ").append(details.getMaxSpeed()).append("\n")
                .append("Chiqarilgan yili: ").append(details.getReleasedYear()).append("\n")
                .append("Bosib o'tgan yo'li: ").append(details.getKmDriven()).append("\n");

        if (details.getScooterType().equals(ScooterType.GASOLINE)) {
            sb.append("100 km ga benzin: ").append(details.getFuelTo100km()).append("\n")
                    .append("Motor ot kuchi: ").append(details.getHorsePower()).append("\n");
        } else {
            sb.append("Zaryadka km ga yetadi: ").append(details.getBatteryLifeToKm()).append("\n")
                    .append("Motor kuchi: ").append(details.getEnginePower()).append("\n");
        }
        return sb.toString();
    }

    private Poster getPoster(TelegramUser user) {
        return posterRepo.findByUserId(user.getId());
    }

    private void updatePosterDetails(TelegramUser user, java.util.function.Consumer<PosterDetails> updater) {
        PosterDetails posterDetails = getPosterDetails(user);
        updater.accept(posterDetails);
        posterDetailsRepo.save(posterDetails);
    }

}
