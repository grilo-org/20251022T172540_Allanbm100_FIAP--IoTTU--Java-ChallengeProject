package br.com.fiap.iottu.motorcyclestatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_STATUS_MOTO")
public class MotorcycleStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer id;

    @NotBlank(message = "{validation.motorcyclestatus.description.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.motorcyclestatus.description.size}")
    @Column(name = "descricao_status")
    private String description;
}