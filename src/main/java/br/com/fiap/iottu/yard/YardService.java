package br.com.fiap.iottu.yard;

import br.com.fiap.iottu.antenna.Antenna;
import br.com.fiap.iottu.antenna.AntennaService;
import br.com.fiap.iottu.motorcycle.Motorcycle;
import br.com.fiap.iottu.motorcycle.MotorcycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YardService {

    @Autowired
    private YardRepository repository;

    @Lazy
    @Autowired
    private AntennaService antennaService;

    @Autowired
    private MotorcycleService motorcycleService;

    @Autowired
    private YardMapService yardMapService;

    public List<Yard> findAll() {
        return repository.findAll();
    }

    public Optional<Yard> findById(Integer id) {
        return repository.findById(id);
    }

    public void save(Yard yard) {
        repository.save(yard);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public YardMapDTO prepareYardMapData(Integer yardId) {
        Yard yard= findById(yardId)
                .orElseThrow(() -> new IllegalArgumentException("{service.yard.error.invalidId}" + yardId));

        List<Antenna> antennas = antennaService.findByYardId(yardId);
        List<Motorcycle> motorcycles = motorcycleService.findByYardId(yardId);

        return yardMapService.createMap(antennas, motorcycles);
    }
}