package cr.ac.una.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cr.ac.una.app.model.Paciente;
import cr.ac.una.app.model.Usuario;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * @author Isaac Herrera
 */

@Controller
@RequestMapping(path = "/")
public class MainController {
	
	@Autowired
    private HttpSession httpSession;

    @GetMapping({"index", ""})
	public String getIndexPage(Model model) {
		// Obtenemos la informacion del Usuario que inicio sesion:
		Usuario loggedUser = Usuario.getLoggedUser();
		model.addAttribute("usuario", loggedUser);
		return "index";
	}

	/**
     * Método para mostrar la pagina de Login
     * Utiliza los usuarios de la Base de Datos.
     * @return
     */
	@GetMapping("login")
	public String getLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        // Verificamos que el usuario no este autenticado:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Obtenemos el nombre del usuario que ingreso al login:
            String ROL = authentication.getPrincipal().toString();
            // Si el usuario no es 'anonymousUser' se redirige al index:
            if (!ROL.equals("anonymousUser")) {
                return "redirect:/index";
            }
        }

        if (error != null) {
            String errorMessage = (String) httpSession.getAttribute("errorMessage");
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
                httpSession.removeAttribute("errorMessage");
            }
        }
		return "auth/login";
	}

	/**
     * Redirige a la página de registro
     * @return
     */
    @GetMapping("register")
    public String getRegisterPage(Model model) {
        Paciente paciente = new Paciente();
        model.addAttribute("paciente", paciente);
        return "auth/register";
    }

	/**
	 * Redirige a la página de error
	 * de la aplicacion.
	 * @return
	 */
	@GetMapping("error")
	public String getErrorPage() {
		return "error";
	}

}