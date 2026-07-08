package ar.utn.ba.ddsi.mailing.schedulers;

import ar.utn.ba.ddsi.mailing.services.ServicioClima;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClimaScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ClimaScheduler.class);
    private final ServicioClima servicioClima;

    public ClimaScheduler(ServicioClima servicioClima) {
        this.servicioClima = servicioClima;
    }

    @Scheduled(fixedRate = 300000)
    public void actualizarClima() {
        servicioClima.actualizarClima()
            .doOnSuccess(v -> logger.info("Update de clima completado"))
            .doOnError(e -> logger.error("ERROR en update: {}", e.getMessage()))
            .subscribe();
    }
}
