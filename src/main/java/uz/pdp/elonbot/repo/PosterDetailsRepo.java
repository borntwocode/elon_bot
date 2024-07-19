package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.elonbot.entity.PosterDetails;
import java.util.UUID;

public interface PosterDetailsRepo extends JpaRepository<PosterDetails, UUID> {

}