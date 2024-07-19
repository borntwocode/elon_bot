package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.elonbot.entity.Poster;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosterRepo extends JpaRepository<Poster, UUID> {

    @Query(value = "select p.* from poster p where p.user_id = :userId limit 1", nativeQuery = true)
    Poster findByUserId(Long userId);

    List<Poster> findAllByIsCompletedAndUserId(boolean completed, Long userId);

    Optional<Poster> findByIsAcceptedAndUserId(boolean isAccepted, Long userId);
}
