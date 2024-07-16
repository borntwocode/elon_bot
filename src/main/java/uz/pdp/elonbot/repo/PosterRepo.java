package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.elonbot.entity.Poster;

public interface PosterRepo extends JpaRepository<Poster, Long> {

    @Query(value = "select p.* from poster p where user_id = :userId", nativeQuery = true)
    Poster findByUserId(Long userId);

}
