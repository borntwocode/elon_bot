package uz.pdp.elonbot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.elonbot.dto.AdminDTO;
import java.util.List;

@Component
@FeignClient(value = "AdminClient", url = "http://localhost:8080/api/admin")
public interface AdminClient {

    @GetMapping
    List<AdminDTO> getAdmins();

}
