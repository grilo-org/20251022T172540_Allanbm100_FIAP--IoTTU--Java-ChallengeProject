package br.com.fiap.iottu.antenna;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AntennaRepository extends JpaRepository<Antenna, Integer> {

    List<Antenna> findByYardId(Integer yardId);
    Optional<Antenna> findByCode(String code);

}
