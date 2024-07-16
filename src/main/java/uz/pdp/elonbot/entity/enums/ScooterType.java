package uz.pdp.elonbot.entity.enums;

import lombok.Getter;

@Getter
public enum ScooterType {

    ELECTRIC("Elektr"),
    GASOLINE("Benzin");

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
