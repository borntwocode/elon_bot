package uz.pdp.elonbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.elonbot.entity.enums.ScooterType;
import java.util.UUID;

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

    public PosterDetails(ScooterType scooterType, String model, String maxSpeed, String horsePower, String enginePower, String releasedYear, String fuelTo100km, String batteryLifeToKm, String kmDriven, String price, String address, String phoneNumber, Photo photo) {
        this.scooterType = scooterType;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.horsePower = horsePower;
        this.enginePower = enginePower;
        this.releasedYear = releasedYear;
        this.fuelTo100km = fuelTo100km;
        this.batteryLifeToKm = batteryLifeToKm;
        this.kmDriven = kmDriven;
        this.price = price;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("🚀 **Skuter Tafsilotlari** 🚀\n\n")
                .append("**Turi**: ").append(this.scooterType.getDisplayName()).append("\n")
                .append("**Modeli**: ").append(this.model).append("\n")
                .append("**Maksimal Tezlik**: ").append(this.maxSpeed).append(" km/soat\n")
                .append("**Chiqarilgan Yili**: ").append(this.releasedYear).append("\n")
                .append("**Bosib o'tgan Yo'li**: ").append(this.kmDriven).append(" km\n");

        if (this.scooterType.equals(ScooterType.GASOLINE)) {
            sb.append("**100 km ga Benzin**: ").append(this.fuelTo100km).append(" L/100 km\n")
                    .append("**Ot Kuchi**: ").append(this.horsePower).append(" HP\n");
        } else {
            sb.append("**Zaryad Hayoti**: ").append(this.batteryLifeToKm).append(" km\n")
                    .append("**Dvigatel Quvvati**: ").append(this.enginePower).append(" kW\n");
        }

        sb.append("**Narxi**: ").append(this.price).append(" USD\n")
                .append("**Manzil**: ").append(this.address).append("\n")
                .append("**Telefon Raqami**: ").append(this.phoneNumber).append("\n");
        return sb.toString();
    }

}
