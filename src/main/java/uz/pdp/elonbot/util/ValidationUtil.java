package uz.pdp.elonbot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.elonbot.entity.enums.ScooterType;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class ValidationUtil {

    public boolean isValidPhoneNumber(String text) {
        return text.matches("^\\d{9}$");
    }

    public boolean isValidScooterType(String text) {
        return text.equals(ScooterType.ELECTRIC.getDisplayName()) || text.equals(ScooterType.GASOLINE.getDisplayName());
    }

    public boolean isValidModel(String text) {
        return !text.isEmpty();
    }

    public boolean isValidMaxSpeed(String text) {
        if (text == null || !text.matches("-?\\d+")) {
            return false;
        }
        int maxSpeed = Integer.parseInt(text);
        return maxSpeed > 20 && maxSpeed < 300;
    }

    public boolean isValidEnginePower(String text) {
        return true;
    }

    public boolean isValidReleasedYear(String text) {
        if (text.matches("^\\d{4}$")) {
            int year = Integer.parseInt(text);
            int currentYear = Year.now().getValue();
            return year >= 2000 && year <= currentYear;
        }
        return false;
    }

    public boolean isValidBatteryLife(String text) {
        return true;
    }

    public boolean isValidKmDriven(String text) {
        return true;
    }

    public boolean isValidPrice(String text) {
        return true;
    }

    public boolean isValidAddress(String text) {
        return true;
    }

}
