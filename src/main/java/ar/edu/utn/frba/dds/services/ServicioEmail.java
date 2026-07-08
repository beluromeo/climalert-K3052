package ar.utn.ba.ddsi.mailing.services;

import ar.utn.ba.ddsi.mailing.models.entities.Email;
import java.util.List;

public interface ServicioEmail {
    Email crearEmail(Email email);
    List<Email> obtenerEmails(Boolean pendiente);
    void procesarPendientes();
    void loguearEmailsPendientes();
}
