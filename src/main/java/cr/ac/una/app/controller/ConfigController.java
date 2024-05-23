package cr.ac.una.app.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cr.ac.una.app.model.EquipoMedico;
import cr.ac.una.app.model.PrestamoEquipo;
import cr.ac.una.app.model.Usuario;
import cr.ac.una.app.service.EquipoMedicoService;
import cr.ac.una.app.service.PrestamoEquipoService;
import cr.ac.una.app.service.UsuarioService;


/**
 * 
 * @author Isaac Herrera
 */

@Controller
@RequestMapping(path = "/config")
@PreAuthorize("hasRole('ROLE_" + Usuario.ADMIN + "')")
public class ConfigController {
	
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	EquipoMedicoService equipoMedicoService;

	@Autowired
	PrestamoEquipoService prestamoEquipoService;

	private 
	List<Integer> pageSizeOptions = Arrays.asList(5, 10, 20);

	/**
	 * Devuelve la pagina de configuracion de la App
	 * @param model
	 * @return 
	 */
	@GetMapping
	public String getConfigPage(@PageableDefault(size = 5, page = 0) Pageable pageable, Model model) {
		Usuario loggedUser = Usuario.getLoggedUser();
		model.addAttribute("usuario", loggedUser);
		
		// Obtiene la página de las entidades utilizando el servicio de cada una.
		// La paginación se controla mediante el objeto Pageable proporcionado por Spring.
		Page<Usuario> usersPage = usuarioService.obtenerPageUsuarios(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		Page<EquipoMedico> equiposPage = equipoMedicoService.obtenerPageEquipoMedico(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		Page<PrestamoEquipo> prestamosPage = prestamoEquipoService.obtenerPagePrestamoEquipo(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		// Page<Cita> citasPage = citaService.obtenerPageCitas(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		// Verifica si la página tiene algún registro.
		// Si la página no está vacía, la añade al modelo para que pueda ser utilizada en la vista.
		if (!usersPage.isEmpty()) { model.addAttribute("usersPage", usersPage); }
		if (!equiposPage.isEmpty()) { model.addAttribute("equiposPage", equiposPage); }
		if (!prestamosPage.isEmpty()) { model.addAttribute("prestamosPage", prestamosPage); }
		// if (!citasPage.isEmpty()) { model.addAttribute("citasPage", citasPage); }

		// Define las opciones de tamaño de página disponibles para la paginación.
		// Estas opciones se utilizan en la vista para permitir al usuario seleccionar el número de registros por página.
		model.addAttribute("pageSizeOptions", pageSizeOptions);

		// Devuelve la vista que muestra la información de los usuarios.
		return "view/config";
	}

	@GetMapping("/users")
	public String getUsersTable(@PageableDefault(size = 5, page = 0, sort = "cedula") Pageable pageable, Model model) {
		Usuario loggedUser = Usuario.getLoggedUser();
		model.addAttribute("usuario", loggedUser);

		Page<Usuario> page = usuarioService.obtenerPageUsuarios(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		if (!page.isEmpty()) {
			model.addAttribute("usersPage", page);
		}

		model.addAttribute("pageSizeOptions", pageSizeOptions);
		return "tables/usersTable";
	}

	@GetMapping("/equipos")
	public String getEquiposTable(@PageableDefault(size = 5, page = 0, sort = "marca") Pageable pageable, Model model) {
		Page<EquipoMedico> page = equipoMedicoService.obtenerPageEquipoMedico(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		if (!page.isEmpty()) {
			model.addAttribute("equiposPage", page);
		}

		model.addAttribute("pageSizeOptions", pageSizeOptions);
		return "tables/equiposTable";
	}

	@GetMapping("/prestamos")
	public String getPrestamosPage(@PageableDefault(size = 5, page = 0, sort = "paciente.cedula") Pageable pageable, Model model) {
		Page<PrestamoEquipo> prestamosPage = prestamoEquipoService.obtenerPagePrestamoEquipo(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		if (!prestamosPage.isEmpty()) { 
			model.addAttribute("prestamosPage", prestamosPage);
		}

		model.addAttribute("pageSizeOptions", pageSizeOptions);
		return "tables/prestamosTable";
	}

	@GetMapping("/citas")
	public String getCitasTable(@PageableDefault(size = 5, page = 0) Pageable pageable, Model model) {
		// Obtiene la página de citas utilizando el servicio de citas.
		// Page<Cita> citasPage = citaService.obtenerPageCitas(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		// Verifica si la página tiene algún registro.
		// Si la página no está vacía, la añade al modelo para que pueda ser utilizada en la vista.
		// if (!citasPage.isEmpty()) { model.addAttribute("citasPage", citasPage); }

		// Define las opciones de tamaño de página disponibles para la paginación.
		// Estas opciones se utilizan en la vista para permitir al usuario seleccionar el número de registros por página.
		model.addAttribute("pageSizeOptions", pageSizeOptions);

		// Devuelve la vista que muestra la información de los usuarios.
		return "tables/citasTable";
	}

}