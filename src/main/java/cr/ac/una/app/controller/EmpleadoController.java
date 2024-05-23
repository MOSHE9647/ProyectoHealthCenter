package cr.ac.una.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.una.app.model.Empleado;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import cr.ac.una.app.service.EmpleadoService;
import jakarta.validation.Valid;

/**
 * Controlador encargado del manejo de las
 * solicitudes relacionadas con el CRUD
 * de la clase Empleado
 * 
 * @author Isaac Herrera
 */

@RestController
@RequestMapping(path = "/api/v1/empleado")
public class EmpleadoController {
	
	@Autowired
	EmpleadoService empleadoService;

	@Secured("ROLE_" + Usuario.ADMIN)
	@PostMapping("/create")
	public Response<Boolean> crearPaciente(@Valid @ModelAttribute Empleado paciente) {
		Response<Boolean> respuesta = empleadoService.guardarEmpleado(paciente);
		return respuesta;
	}

	@Secured("ROLE_" + Usuario.ADMIN + ", ROLE_" + Usuario.EMPLEADO)
	@PostMapping("/update")
	public Response<Boolean> actualizarPaciente(@Valid @ModelAttribute Empleado paciente) {
		Response<Boolean> respuesta = empleadoService.actualizarEmpleado(paciente);
		return respuesta;
	}

	@Secured("ROLE_" + Usuario.ADMIN)
	@DeleteMapping("/delete")
	public Response<Boolean> eliminarPaciente(@RequestParam String email) {
		Response<Boolean> respuesta = empleadoService.eliminarEmpleado(email);
		return respuesta;
	}

}