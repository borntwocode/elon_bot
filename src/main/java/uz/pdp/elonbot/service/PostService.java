package uz.pdp.elonbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.Poster;
import uz.pdp.elonbot.entity.PosterDetails;
import uz.pdp.elonbot.entity.TelegramUser;
import uz.pdp.elonbot.entity.enums.ScooterType;
import uz.pdp.elonbot.repo.PosterDetailsRepo;
import uz.pdp.elonbot.repo.PosterRepo;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PosterRepo posterRepo;
    private final PosterDetailsRepo posterDetailsRepo;

    public void createPoster(TelegramUser user){
        Poster poster = Poster.builder()
                .user(user)
                .posterDetails(new PosterDetails())
                .build();
        posterRepo.save(poster);
    }

    public void removePoster(TelegramUser user) {
        Poster poster = getPoster(user);
        posterRepo.delete(poster);
    }

    public void setPhone(TelegramUser user, String phoneNumber) {
        PosterDetails posterDetails = getPosterDetails(user);
        posterDetails.setPhoneNumber(phoneNumber);
        posterDetailsRepo.save(posterDetails);
    }

    public void setScooterType(TelegramUser user, String scooterType) {
        PosterDetails posterDetails = getPosterDetails(user);
        posterDetails.setScooterType(ScooterType.fromDisplayName(scooterType));
        posterDetailsRepo.save(posterDetails);
    }

    public void setModel(TelegramUser user, String text) {
        PosterDetails posterDetails = getPosterDetails(user);
        posterDetails.setModel(text);
        posterDetailsRepo.save(posterDetails);
    }

    public void setMaxSpeed(TelegramUser user, String text) {
        PosterDetails posterDetails = getPosterDetails(user);
        posterDetails.setMaxSpeed(text);
        posterDetailsRepo.save(posterDetails);
    }

    public void setReleasedYear(TelegramUser user, String text) {
        PosterDetails posterDetails = getPosterDetails(user);
        posterDetails.setReleasedYear(text);
        posterDetailsRepo.save(posterDetails);
    }

    public PosterDetails getPosterDetails(TelegramUser user) {
        return posterRepo.findByUserId(user.getId()).getPosterDetails();
    }

    private Poster getPoster(TelegramUser user) {
        return posterRepo.findByUserId(user.getId());
    }

}
