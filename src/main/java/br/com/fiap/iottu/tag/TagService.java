package br.com.fiap.iottu.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Adicionado
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public Optional<Tag> findById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Tag tag) {
        repository.save(tag);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Tag> findAvailableTags() {
        return repository.findAvailableTags();
    }

    @Transactional
    public Tag findOrCreateTag(String rfidCode, String wifiSsid, Double latitude, Double longitude) {
        Optional<Tag> existingTag = repository.findByRfidCode(rfidCode);
        Tag tag;

        if (existingTag.isPresent()) {
            tag = existingTag.get();
        } else {
            tag = new Tag();
            tag.setRfidCode(rfidCode);
        }
        tag.setWifiSsid(wifiSsid);
        if (latitude != null) {
            tag.setLatitude(BigDecimal.valueOf(latitude));
        }
        if (longitude != null) {
            tag.setLongitude(BigDecimal.valueOf(longitude));
        }
        return repository.save(tag);
    }
}
