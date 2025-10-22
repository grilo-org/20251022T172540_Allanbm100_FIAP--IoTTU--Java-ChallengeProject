package br.com.fiap.iottu.motorcycle;

import br.com.fiap.iottu.yard.Yard;
import br.com.fiap.iottu.motorcyclestatus.MotorcycleStatus;
import br.com.fiap.iottu.tag.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "TB_MOTO")
public class Motorcycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moto")
    private Integer id;

    @NotNull(message = "{validation.motorcycle.status.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_status")
    private MotorcycleStatus status;

    @NotNull(message = "{validation.motorcycle.yard.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_patio")
    private Yard yard;

    @NotBlank(message = "{validation.motorcycle.licensePlate.notBlank}")
    @Size(min = 7, max = 7, message = "{validation.motorcycle.licensePlate.size}")
    @Column(name = "placa_moto")
    private String licensePlate;

    @NotBlank(message = "{validation.motorcycle.chassi.notBlank}")
    @Size(min = 17, max = 17, message = "{validation.motorcycle.chassi.size}")
    @Column(name = "chassi_moto")
    private String chassi;

    @NotBlank(message = "{validation.motorcycle.engineNumber.notBlank}")
    @Size(min = 5, max = 20, message = "{validation.motorcycle.engineNumber.size}")
    @Column(name = "nr_motor_moto")
    private String engineNumber;

    @NotBlank(message = "{validation.motorcycle.model.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.motorcycle.model.size}")
    @Column(name = "modelo_moto")
    private String model;

    @ManyToMany
    @JoinTable(
            name = "TB_MOTO_TAG",
            joinColumns = @JoinColumn(name = "id_moto"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private List<Tag> tags;

    @Transient
    private Integer selectedTagId;
}