package ar.utn.ba.ddsi.mailing.schedulers;

import ar.utn.ba.ddsi.mailing.services.ServicioEmail;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    private final ServicioEmail servicioEmail;

    public EmailScheduler(ServicioEmail servicioEmail) {
        this.servicioEmail = servicioEmail;
    }

    @Scheduled(cron = "${cron.expression}")
    public void procesarEmailsPendientes() {
        servicioEmail.loguearEmailsPendientes();
        servicioEmail.procesarPendientes();
    }
}
