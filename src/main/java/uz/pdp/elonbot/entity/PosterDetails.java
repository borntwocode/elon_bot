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

    @OneToOne
    private Photo photo;

}
