package cr.ac.una.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.una.app.model.Paciente;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import cr.ac.una.app.service.MailSenderService;
import cr.ac.una.app.service.PacienteService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador encargado del manejo de las
 * solicitudes relacionadas con el CRUD
 * de la clase Paciente
 * 
 * @author Isaac Herrera
 */

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/paciente")
public class PacienteController {
	
	@Autowired
	PacienteService pacienteService;

	@Autowired
	MailSenderService mailSender;
	
	@PostMapping("/register")
	public Response<Boolean> registrarPaciente(@Valid @ModelAttribute Paciente paciente) {
		// Intentamos guardar el paciente en la Base de Datos:
		String password = paciente.getPassword();
		Response<Boolean> respuesta = pacienteService.guardarPaciente(paciente);
		// Mandamos un email en caso de haberse creado el paciente:
		if (respuesta.isSuccess()) {
			// Modificamos el mensaje de respuesta:
			respuesta.setTitulo("Registro completado");
			respuesta.setMensaje("Redirigiendo al login...");

			// Obtenemos los datos del paciente:
			String nombreUsuario = paciente.getNombre() + " " + paciente.getApellido();
			String destinatario = paciente.getEmail();
			String asunto = "Registro completado con éxito!!";
			String html = "register-confirmation.html";

			// Intentamos enviar el correo de confirmacion:
			try {
				// Hacemos una lista con los datos que se van a enviar en el correo:
				List<String> datosHTML = new ArrayList<>();
				datosHTML.add("${userName}, " + nombreUsuario);
                datosHTML.add("${userEmail}, " + destinatario);
                datosHTML.add("${userPassword}, " + password);

				// Enviamos la confirmacion al correo del usuario:
				mailSender.sendHtmlEmail(datosHTML, html, asunto, destinatario);
				log.info("Correo de confirmacion enviado con exito al usuario '{}'", destinatario);

			} catch (MessagingException | IOException e) {
				// Se muestra un mensaje de error en la consola:
				log.error("Ocurrio un error al enviar el correo electronico: " + e.getMessage());
			}
		}
		return respuesta;
	}
	
	@Secured("ROLE_" + Usuario.ADMIN)
	@PostMapping("/create")
	public Response<Boolean> crearPaciente(@Valid @ModelAttribute Paciente paciente) {
		Response<Boolean> respuesta = pacienteService.guardarPaciente(paciente);
		return respuesta;
	}
	
	@Secured("ROLE_" + Usuario.ADMIN + ", ROLE_" + Usuario.PACIENTE)
	@PostMapping("/update")
	public Response<Boolean> actualizarPaciente(@Valid @ModelAttribute Paciente paciente) {
		Response<Boolean> respuesta = pacienteService.actualizarPaciente(paciente);
		return respuesta;
	}

	@Secured("ROLE_" + Usuario.ADMIN)
	@DeleteMapping("/delete")
	public Response<Boolean> eliminarPaciente(@RequestParam String email) {
		Response<Boolean> respuesta = pacienteService.eliminarPaciente(email);
		return respuesta;
	}

}