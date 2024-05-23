package cr.ac.una.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import cr.ac.una.app.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * Controlador encargado del manejo de las
 * solicitudes relacionadas con el CRUD
 * de la clase Usuario (Admin)
 * 
 * @author Isaac Herrera
 */

@RestController
@RequestMapping(path = "/api/v1/usuario")
@PreAuthorize("hasRole('ROLE_" + Usuario.ADMIN + "')")
public class AdminController {
	
	@Autowired
	UsuarioService usuarioService;

	@PostMapping("/create")
	public Response<Boolean> crearUsuario(@Valid @ModelAttribute Usuario usuario) {
		Response<Boolean> respuesta = usuarioService.guardarUsuario(usuario);
		return respuesta;
	}

	@PostMapping("/update")
	public Response<Boolean> actualizarUsuario(@Valid @ModelAttribute Usuario usuario) {
		Response<Boolean> respuesta = usuarioService.actualizarUsuario(usuario);
		SecurityContextHolder.clearContext();
		return respuesta;
	}

	@DeleteMapping("/delete")
	public Response<Boolean> eliminarUsuario(@RequestParam String email) {
		Response<Boolean> respuesta = usuarioService.eliminarUsuario(email);
		return respuesta;
	}

}