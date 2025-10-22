package br.com.fiap.iottu.motorcycle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Integer> {

    List<Motorcycle> findByYardId(Integer yardId);
    Optional<Motorcycle> findByChassi(String chassi);

}
