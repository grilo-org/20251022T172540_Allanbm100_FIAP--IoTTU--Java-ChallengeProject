package br.com.fiap.iottu.yard;

import br.com.fiap.iottu.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_PATIO")
public class Yard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patio")
    private Integer id;

    @NotNull(message = "{validation.yard.user.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    @NotBlank(message = "{validation.yard.zipCode.notBlank}")
    @Size(min = 8, max = 8, message = "{validation.yard.zipCode.size}")
    @Column(name = "cep_patio")
    private String zipCode;

    @NotBlank(message = "{validation.yard.number.notBlank}")
    @Size(min = 1, max = 10, message = "{validation.yard.number.size}")
    @Column(name = "numero_patio")
    private String number;

    @NotBlank(message = "{validation.yard.city.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.yard.city.size}")
    @Column(name = "cidade_patio")
    private String city;

    @NotBlank(message = "{validation.yard.state.notBlank}")
    @Size(min = 2, max = 2, message = "{validation.yard.state.size}")
    @Column(name = "estado_patio")
    private String state;

    @NotNull(message = "{validation.yard.capacity.notNull}")
    @Min(value = 0, message = "{validation.yard.capacity.min}")
    @Column(name = "capacidade_patio")
    private Integer capacity;
}