package cr.ac.una.app.config.handlers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase actúa como un manejador global de 
 * excepciones para controladores en toda la aplicación.
 * Implementa la interfaz ErrorController para manejar 
 * errores genéricos que no se puedan manejar de otra manera.
 * @ControllerAdvice define métodos que manejan excepciones lanzadas por
 * cualquier controlador en la aplicación.
 * @ExceptionHandler maneja excepciones específicas y devuelve una vista
 * de error personalizada junto con un mensaje descriptivo.
 * La anotación @ResponseStatus especifica el código de 
 * estado HTTP que se enviará en la respuesta.
 * @author Isaac Herrera
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler implements ErrorController {
    
    /**
     * Método que maneja varios tipos de excepciones 
     * relacionados con errores 404, como 
     * NoResourceFoundException y NoHandlerFoundException.
     * Se agrega información adicional a la vista de error,
     * como la página anterior que se intentaba acceder.
     * @param ex Excepción que se va a manejar.
     * @param request Para obtener la página anterior.
     * @param model Inserta el mensaje en el HTML.
     * @return Página de error
     */
    @ExceptionHandler({
        NoResourceFoundException.class, NoHandlerFoundException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        HttpMediaTypeNotSupportedException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Exception ex, HttpServletRequest request, Model model) {
        String paginaAnterior = request.getHeader("referer");
        model.addAttribute("referer", paginaAnterior);
        model.addAttribute("error", "404: La página que buscas no existe.");
        log.error("Error 404: " + ex.getClass().getName(), ex);
        return "error";
    }

    /**
     * Este método maneja la excepción AccessDeniedException, 
     * que ocurre cuando se intenta acceder a una página sin 
     * permisos suficientes. Se agrega información adicional 
     * a la vista de error, como la página anterior a la que
     * se intentaba acceder.
     * @param ex Excepción que se va a manejar.
     * @param request Para obtener la página anterior.
     * @param model Inserta el mensaje en el HTML.
     * @return Página de error
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle403(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request, Model model) {
        String paginaAnterior = request.getHeader("referer");
        model.addAttribute("referer", paginaAnterior);
        model.addAttribute("error", "Acceso denegado: no tienes permiso para acceder a esta página.");
        log.error("Error 403: " + ex.getClass().getName(), ex);
        return "error";
    }

}
