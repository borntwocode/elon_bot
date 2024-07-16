package uz.pdp.elonbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.elonbot.entity.Photo;
import java.util.UUID;

public interface PhotoRepo extends JpaRepository<Photo, UUID> {
}