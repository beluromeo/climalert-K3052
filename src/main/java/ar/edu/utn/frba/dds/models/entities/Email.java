package ar.utn.ba.ddsi.mailing.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Email {
    private Long id;
    private String destinatario;
    private String remitente;
    private String asunto;
    private String contenido;
    private boolean enviado;

    public Email(String destinatario, String remitente, String asunto, String contenido) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.asunto = asunto;
        this.contenido = contenido;
        this.enviado = false;
    }

    public void enviar() {
        System.out.println("belenariadnaromeo Enviando email a: "+ destinatario + "Asunto: " + asunto);
    }
} //me parecio buena idea agregar un print para mostrar en consola un mensaje de como se veria con el destinantirio real