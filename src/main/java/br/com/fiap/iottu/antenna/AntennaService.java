package br.com.fiap.iottu.antenna;

import br.com.fiap.iottu.dto.AntenaDataDTO;
import br.com.fiap.iottu.yard.Yard;
import br.com.fiap.iottu.yard.YardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AntennaService {

    private static final Logger log = LoggerFactory.getLogger(AntennaService.class);

    @Autowired
    private AntennaRepository repository;

    @Autowired
    private YardService yardService;

    public List<Antenna> findAll() {
        return repository.findAll();
    }

    public Optional<Antenna> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Antenna> findByYardId(Integer yardId) {
        return repository.findByYardId(yardId);
    }

    public void save(Antenna antenna) {
        repository.save(antenna);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public void processAntennasData(List<AntenaDataDTO> antenasData) {
        for (AntenaDataDTO dto : antenasData) {
            try {
                Optional<Yard> yardOptional = yardService.findById(dto.getIdPatio());
                if (yardOptional.isEmpty()) {
                    log.warn("{service.antenna.warn.yardNotFound}", dto.getIdPatio(), dto.getCodigoAntena());
                    continue;
                }
                Yard yard = yardOptional.get();

                Antenna antenna = repository.findByCode(dto.getCodigoAntena())
                        .orElse(new Antenna());

                antenna.setYard(yard);
                antenna.setCode(dto.getCodigoAntena());
                antenna.setLatitude(BigDecimal.valueOf(dto.getLatitudeAntena()));
                antenna.setLongitude(BigDecimal.valueOf(dto.getLongitudeAntena()));

                repository.save(antenna);
                log.info("{service.antenna.info.saved}", dto.getCodigoAntena());
            } catch (Exception e) {
                log.error("{service.antenna.error.processing}", dto.getCodigoAntena(), e.getMessage(), e);
            }
        }
    }
}