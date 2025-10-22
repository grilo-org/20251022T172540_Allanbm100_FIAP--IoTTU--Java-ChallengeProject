package br.com.fiap.iottu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AntenasPayloadDTO {
    @JsonProperty("antenas")
    private List<AntenaDataDTO> antenas;

    // Construtor padrão
    public AntenasPayloadDTO() {}

    // Getters e Setters
    public List<AntenaDataDTO> getAntenas() {
        return antenas;
    }

    public void setAntenas(List<AntenaDataDTO> antenas) {
        this.antenas = antenas;
    }
}
