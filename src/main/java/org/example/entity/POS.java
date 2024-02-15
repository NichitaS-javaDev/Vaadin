package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class POS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 15)
    private String name;
    @Pattern(regexp = "^\\+[0-9]{1,3}$")
    private String countryCode;
    @Pattern(regexp = "^[0-9]+$")
    private String telephone;
    @Size(min = 5, max = 25)
    private String address;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private City city;
    @Size(min = 3, max = 15)
    private String model;
    @Size(min = 3, max = 15)
    private String brand;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private ConnectionType connType;
    @NotNull
    @Temporal(TemporalType.TIME)
    private LocalTime opening;
    @NotNull
    @Temporal(TemporalType.TIME)
    private LocalTime closing;

}
