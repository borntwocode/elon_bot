package uz.pdp.elonbot.entity.enums;

import lombok.Getter;
import uz.pdp.elonbot.bot.BotConstants;

@Getter
public enum ScooterType {

    ELECTRIC(BotConstants.ELECTOR),
    GASOLINE(BotConstants.GASOLINE);

    private final String displayName;

    ScooterType(String displayName) {
        this.displayName = displayName;
    }

    public static ScooterType fromDisplayName(String displayName) {
        for (ScooterType type : ScooterType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid scooter type: " + displayName);
    }

}
