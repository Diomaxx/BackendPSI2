package com.psi2.donaciones;

import com.psi2.donaciones.config.service.EmailService;
import com.psi2.donaciones.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class EmsApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/La_Paz"));

        // ✅ Guarda el contexto de la aplicación
        var context = SpringApplication.run(EmsApplication.class, args);

        // ✅ Usa Spring para obtener el EmailService con inyección hecha
        EmailService emailService = context.getBean(EmailService.class);
        emailService.enviarCorreo("diogofrancomontenegro06@gmail.com", "Asunto", "Cuerpo del correo");
    }
}
