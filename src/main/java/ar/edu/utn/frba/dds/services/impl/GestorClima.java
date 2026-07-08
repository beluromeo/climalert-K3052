package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.models.entities.Clima;
import ar.utn.ba.ddsi.mailing.models.repositories.RepositorioClima;
import ar.utn.ba.ddsi.mailing.models.dto.external.weatherapi.RespuestaClima;
import ar.utn.ba.ddsi.mailing.services.ServicioClima;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GestorClima implements ServicioClima {
    private static final Logger logger = LoggerFactory.getLogger(GestorClima.class);

    private final RepositorioClima repositorioClima;
    private final WebClient webClient;
    private final String apiKey;
    private final String ciudad;

    public GestorClima(
            RepositorioClima repositorioClima,
            @Value("${weather.api.key}") String apiKey,
            @Value("${weather.api.base-url}") String baseUrl,
            @Value("${weather.api.ciudad}") String ciudad) {
        this.repositorioClima = repositorioClima;
        this.apiKey = apiKey;
        this.ciudad = ciudad;
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public Mono<Void> actualizarClima() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", ciudad)
                .queryParam("aqi", "no")
                .build())
            .retrieve()
            .bodyToMono(RespuestaClima.class)
            .map(respuesta -> {
                Clima clima = new Clima();
                clima.setCiudad(ciudad);
                clima.setRegion(respuesta.getUbicacion().getRegion());
                clima.setPais(respuesta.getUbicacion().getPais());
                clima.setTemperaturaCelsius(respuesta.getDatosActuales().getTemperaturaCelsius());
                clima.setTemperaturaFahrenheit(respuesta.getDatosActuales().getTemperaturaFahrenheit());
                clima.setCondicion(respuesta.getDatosActuales().getCondicion().getTexto());
                clima.setVelocidadVientoKmh(respuesta.getDatosActuales().getVelocidadVientoKmh());
                clima.setHumedad(respuesta.getDatosActuales().getHumedad());
                return clima;
            })
            .flatMap(clima -> {
                repositorioClima.save(clima);
                logger.info("Clima updateado para: {}", ciudad);
                return Mono.empty();
            })
            .onErrorResume(e -> {
                logger.error("Error al updatear el clima: {}", e.getMessage());
                return Mono.empty();
            })
            .then();
    }
}
