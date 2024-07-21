package uz.pdp.elonbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.elonbot.entity.enums.ScooterType;
import java.util.UUID;
import static uz.pdp.elonbot.messages.BotMessages.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PosterDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private ScooterType scooterType;

    private String model;

    private String maxSpeed;

    private String horsePower;

    private String enginePower;

    private String releasedYear;

    private String fuelTo100km;

    private String batteryLifeToKm;

    private String kmDriven;

    private String price;

    private String address;

    private String phoneNumber;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Photo photo;

    public String getEnginePowerOrHorsePower() {
        return scooterType.equals(ScooterType.ELECTRIC) ? ENTER_ENGINE_POWER : ENTER_HORSE_POWER;
    }

    public String getFuelOrBatteryConsumption() {
        return scooterType.equals(ScooterType.ELECTRIC) ? ENTER_BATTERY_LIFE : ENTER_FUEL_TO_100_KM;
    }

    @Override
    public String toString() {
        return "🚀 **Skuter Tafsilotlari** 🚀\n\n" +
               "🔹 **Turi**: " + scooterType.getDisplayName() + "\n" +
               "🔹 **Modeli**: " + model + "\n" +
               "🔹 **Maksimal Tezlik**: " + maxSpeed + " 🚀\n" +
               "🔹 **Chiqarilgan Yili**: " + releasedYear + " 📅\n" +
               "🔹 **Bosib o'tgan Yo'li**: " + kmDriven + " 🌍\n" +
               (scooterType.equals(ScooterType.GASOLINE) ?
                       "🔹 **100 km ga Benzin**: " + fuelTo100km + " ⛽\n" +
                       "🔹 **Ot Kuchi**: " + horsePower + " 🐎\n" :
                       "🔹 **Zaryad Hayoti**: " + batteryLifeToKm + " 🔋\n" +
                       "🔹 **Dvigatel Quvvati**: " + enginePower + " ⚡\n") +
               "🔹 **Narxi**: " + price + " 💰\n" +
               "🔹 **Manzil**: " + address + " 🏠\n" +
               "🔹 **Telefon Raqami**: " + phoneNumber + " 📞";
    }

}
