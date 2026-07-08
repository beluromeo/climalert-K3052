package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.models.entities.Clima;
import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.models.repositories.RepositorioClima;
import ar.utn.ba.ddsi.mailing.services.ServicioAlertas;
import ar.utn.ba.ddsi.mailing.services.ServicioEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;

@Service
public class GestorAlertas implements ServicioAlertas {
    private static final Logger logger = LoggerFactory.getLogger(GestorAlertas.class);
    private static final double TEMPERATURA_ALERTA = 35.0;
    private static final int HUMEDAD_ALERTA = 60;

    private final RepositorioClima repositorioClima;
    private final ServicioEmail servicioEmail;
    private final String remitente;
    private final List<String> destinatarios;

    public GestorAlertas(
            RepositorioClima repositorioClima,
            ServicioEmail servicioEmail,
            @Value("${email.alertas.remitente}") String remitente,
            @Value("${email.alertas.destinatarios}") String destinatarios) {
        this.repositorioClima = repositorioClima;
        this.servicioEmail = servicioEmail;
        this.remitente = remitente;
        this.destinatarios = Arrays.asList(destinatarios.split(","));
    }

    @Override
    public Mono<Void> generarAlertasYAvisar() {
        return Mono.fromCallable(() -> repositorioClima.findByProcesado(false))
            .flatMap(climas -> {
                logger.info("Hay {} registros nuevos para revisar", climas.size());
                return Mono.just(climas);
            })
            .flatMap(climas -> {
                climas.stream()
                    .filter(this::cumpleCondicionesAlerta)
                    .forEach(this::generarYEnviarEmail);

                climas.forEach(clima -> {
                    clima.setProcesado(true);
                    repositorioClima.save(clima);
                });

                return Mono.empty();
            })
            .onErrorResume(e -> {
                logger.error("EErro al revisar las alertas: {}", e.getMessage());
                return Mono.empty();
            })
            .then();
    }

    private boolean cumpleCondicionesAlerta(Clima clima) {
        return clima.getTemperaturaCelsius() > TEMPERATURA_ALERTA &&
               clima.getHumedad() > HUMEDAD_ALERTA;
    }

    private void generarYEnviarEmail(Clima clima) {
        String asunto = "Alerta climática en " + clima.getCiudad();
        String mensaje = String.format(
            "Se detectaron condiciones climáticas de riesgo en %s.\n\n" +
            "Temperatura: %.1f°C\n" +
            "Humedad: %d%%\n" +
            "Estado del tiempo: %s\n" +
            "Viento: %.1f km/h\n\n" +
            "Se recomienda tomar las medidas correspondientes.",
            clima.getCiudad(),
            clima.getTemperaturaCelsius(),
            clima.getHumedad(),
            clima.getCondicion(),
            clima.getVelocidadVientoKmh()
        );

        for (String destinatario : destinatarios) {
            servicioEmail.crearEmail(new Email(destinatario, remitente, asunto, mensaje));
        }

        logger.info("Se notificó a {} destinatarios por la situación en {}", destinatarios.size(), clima.getCiudad());
    }
}
