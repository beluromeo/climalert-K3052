package ar.utn.ba.ddsi.mailing.models.dto.external.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
    @JsonProperty("name")
    private String nombre;

    private String region;

    @JsonProperty("country")
    private String pais;
}
