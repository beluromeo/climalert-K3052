package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.models.repositories.RepositorioEmail;
import ar.utn.ba.ddsi.mailing.services.ServicioEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GestorEmail implements ServicioEmail {
    private static final Logger logger = LoggerFactory.getLogger(GestorEmail.class);
    private final RepositorioEmail repositorioEmail;

    public GestorEmail(RepositorioEmail repositorioEmail) {
        this.repositorioEmail = repositorioEmail;
    }

    @Override
    public Email crearEmail(Email email) {
        return repositorioEmail.save(email);
    }

    @Override
    public List<Email> obtenerEmails(Boolean pendiente) {
        if (pendiente != null) {
            return repositorioEmail.findByEnviado(!pendiente);
        }
        return repositorioEmail.findAll();
    }

    @Override
    public void procesarPendientes() {
        List<Email> pendientes = repositorioEmail.findByEnviado(false);
        for (Email email : pendientes) {
            email.enviar();
            email.setEnviado(true);
            repositorioEmail.save(email);
        }
    }

    @Override
    public void loguearEmailsPendientes() {
        List<Email> pendientes = obtenerEmails(true);
        logger.info("Emails pendientes de envio: {}", pendientes.size());
        pendientes.forEach(email ->
            logger.info("Email pendiente ID: {}, Destinatario: {}, Asunto: {}",
                email.getId(),
                email.getDestinatario(),
                email.getAsunto())
        );
    }
}
