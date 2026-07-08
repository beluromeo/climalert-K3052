package ar.utn.ba.ddsi.mailing.models.dto.external.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatosActuales {
    @JsonProperty("temp_c")
    private double temperaturaCelsius;

    @JsonProperty("temp_f")
    private double temperaturaFahrenheit;

    @JsonProperty("condition")
    private Condicion condicion;

    @JsonProperty("wind_kph")
    private double velocidadVientoKmh;

    @JsonProperty("humidity")
    private int humedad;
}
