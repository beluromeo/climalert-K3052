package ar.utn.ba.ddsi.mailing.models.dto.external.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condicion {
    @JsonProperty("text")
    private String texto;
}
