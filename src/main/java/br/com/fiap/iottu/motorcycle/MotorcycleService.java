package br.com.fiap.iottu.motorcycle;

import br.com.fiap.iottu.dto.MotorcycleDataDTO;
import br.com.fiap.iottu.motorcyclestatus.MotorcycleStatus;
import br.com.fiap.iottu.motorcyclestatus.MotorcycleStatusRepository;
import br.com.fiap.iottu.tag.Tag;
import br.com.fiap.iottu.tag.TagService;
import br.com.fiap.iottu.yard.Yard;
import br.com.fiap.iottu.yard.YardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleRepository repository;

    @Autowired
    private MotorcycleStatusRepository motorcycleStatusRepository;

    @Autowired
    private YardRepository yardRepository;

    @Autowired
    private TagService tagService;

    public List<Motorcycle> findAll() {
        return repository.findAll();
    }

    public Optional<Motorcycle> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Motorcycle> findByYardId(Integer yardId) {
        return repository.findByYardId(yardId);
    }

    public void save(Motorcycle motorcycle) {
        repository.save(motorcycle);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public Motorcycle saveOrUpdateWithTag(Motorcycle motorcycle, Integer selectedTagId) {
        Optional<Tag> tagOptional = tagService.findById(selectedTagId);

        if (tagOptional.isEmpty()) {
            throw new IllegalArgumentException("{service.motorcycle.error.tagNotFound}");
        }
        Tag newTag = tagOptional.get();

        if (newTag.getMotorcycles() != null && !newTag.getMotorcycles().isEmpty()) {
            boolean isAssignedToThisMotorcycle = motorcycle.getId() != null && newTag.getMotorcycles().contains(motorcycle);

            if (!isAssignedToThisMotorcycle) {
                throw new IllegalArgumentException("{service.motorcycle.error.tagAlreadyAssigned}");
            }
        }

        if (motorcycle.getId() != null) {
            Optional<Motorcycle> existingMotorcycleOptional = repository.findById(motorcycle.getId());
            if (existingMotorcycleOptional.isPresent()) {
                Motorcycle existingMotorcycle = existingMotorcycleOptional.get();

                if (existingMotorcycle.getTags() != null && !existingMotorcycle.getTags().isEmpty()) {
                    Tag oldTag = existingMotorcycle.getTags().get(0);

                    if (!oldTag.equals(newTag)) {
                        oldTag.getMotorcycles().remove(existingMotorcycle);
                        tagService.save(oldTag);
                    }
                }
            }
        }
        motorcycle.setTags(new ArrayList<>());
        motorcycle.getTags().add(newTag);
        return repository.save(motorcycle);
    }

    @Transactional
    public void processMotorcyclesData(List<MotorcycleDataDTO> motorcycleDataDTOs) {
        for (MotorcycleDataDTO dto : motorcycleDataDTOs) {
            Motorcycle motorcycle = repository.findByChassi(dto.getChassiMoto()).orElse(new Motorcycle());

            MotorcycleStatus status = motorcycleStatusRepository.findById(dto.getIdStatus())
                    .orElseThrow(() -> new IllegalArgumentException("{service.motorcycle.error.statusNotFound}" + dto.getIdStatus()));
            motorcycle.setStatus(status);

            Yard yard = yardRepository.findById(dto.getIdPatio())
                    .orElseThrow(() -> new IllegalArgumentException("{service.motorcycle.error.yardNotFound}" + dto.getIdPatio()));
            motorcycle.setYard(yard);

            motorcycle.setLicensePlate(dto.getPlacaMoto());
            motorcycle.setChassi(dto.getChassiMoto());
            motorcycle.setEngineNumber(dto.getNrMotorMoto());
            motorcycle.setModel(dto.getModeloMoto());

            if (dto.getCodigoRfidTag() != null && !dto.getCodigoRfidTag().isEmpty()) {
                Tag tag = tagService.findOrCreateTag(
                        dto.getCodigoRfidTag(),
                        dto.getSsidWifiTag(),
                        dto.getLatitude(),
                        dto.getLongitude()
                );
                if (motorcycle.getTags() == null) {
                    motorcycle.setTags(new ArrayList<>());
                }
                if (!motorcycle.getTags().contains(tag)) {
                    motorcycle.getTags().add(tag);
                }
            }
            repository.save(motorcycle);
        }
    }

    @Transactional
    public void deleteByIdWithTagUnbinding(Integer id) {
        Motorcycle motorcycleToDelete = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("{service.motorcycle.error.notFoundById}" + id));

        if (motorcycleToDelete.getTags() != null  && !motorcycleToDelete.getTags().isEmpty()) {
            Tag associatedTag = motorcycleToDelete.getTags().get(0);
            associatedTag.getMotorcycles().remove(motorcycleToDelete);
            tagService.save(associatedTag);
        }
        repository.deleteById(id);
    }
}