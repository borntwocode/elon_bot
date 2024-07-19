package uz.pdp.elonbot.service;

import com.pengrad.telegrambot.request.SendPhoto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Photo;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.PosterDetails;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.ScooterType;
import uz.pdp.elonbot.repo.PhotoRepo;
import uz.pdp.elonbot.repo.PosterDetailsRepo;
import uz.pdp.elonbot.repo.PosterRepo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PosterRepo posterRepo;
    private final PosterDetailsRepo posterDetailsRepo;
    private final PhotoRepo photoRepo;

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

    @Transactional
    public void deletePosterIfPresent(TelegramUser user) {
        List<Poster> posters = posterRepo.findAllByIsCompletedAndUserId(false, user.getId());
        posterRepo.deleteAll(posters);
    }

    @Transactional
    public void deletePhoto(TelegramUser user) {
        PosterDetails posterDetails = getPosterDetails(user);
        Photo photo = posterDetails.getPhoto();
        posterDetails.setPhoto(null);
        posterDetailsRepo.save(posterDetails);
        photoRepo.deleteById(photo.getId());
    }

    public void setIsCompleted(TelegramUser user, boolean isCompleted) {
        Poster poster = getPoster(user);
        poster.setCompleted(isCompleted);
        posterRepo.save(poster);
    }

    public SendPhoto createPosterHeader(TelegramUser user, Photo photo, String text) {
        SendPhoto sendPhoto = new SendPhoto(user.getId(), photo.getContent());
        sendPhoto.caption(text);
        return sendPhoto;
    }

    public boolean isPendingPoster(TelegramUser user) {
        Optional<Poster> posterOpt = posterRepo.findByIsAcceptedAndUserId(false, user.getId());
        return posterOpt.isPresent();
    }

    public Poster getPoster(TelegramUser user) {
        return posterRepo.findByUserId(user.getId());
    }

    protected void updatePosterDetails(TelegramUser user, Consumer<PosterDetails> updater) {
        PosterDetails posterDetails = getPosterDetails(user);
        updater.accept(posterDetails);
        posterDetailsRepo.save(posterDetails);
    }

    public PosterDetails setIsAccepted(UUID postId, boolean isAccepted) {
        Poster poster = posterRepo.findById(postId).orElseThrow();
        poster.setAccepted(isAccepted);
        posterRepo.save(poster);
        return poster.getPosterDetails();
    }

    public void deletePoster(UUID postId) {
        posterRepo.deleteById(postId);
    }

}
