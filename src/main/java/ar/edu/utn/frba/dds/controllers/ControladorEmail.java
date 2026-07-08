package ar.utn.ba.ddsi.mailing.controllers;

import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.services.ServicioEmail;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class ControladorEmail {
    private final ServicioEmail servicioEmail;

    public ControladorEmail(ServicioEmail servicioEmail) {
        this.servicioEmail = servicioEmail;
    }

    @PostMapping
    public Email crearEmail(@RequestBody Email email) {
        return servicioEmail.crearEmail(email);
    }

    @GetMapping
    public List<Email> obtenerEmails(@RequestParam(required = false) Boolean pendiente) {
        return servicioEmail.obtenerEmails(pendiente);
    }
}
