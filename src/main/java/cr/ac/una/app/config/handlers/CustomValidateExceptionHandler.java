package cr.ac.una.app.config.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cr.ac.una.app.model.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase encargada del manejo y respuesta hacia errores
 * que puedan ser generados como excepciones por la clase
 * 'MethodArgumentNotValidException' de la dependecia
 * Validation de SpringBoot.
 * 
 * @author Isaac Herrera
 */

@Slf4j
@RestControllerAdvice
public class CustomValidateExceptionHandler {
	
	@ResponseStatus(code = HttpStatus.OK)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response<Boolean> handleValidateExceptions(MethodArgumentNotValidException ex) {
		// Creamos el objeto que va a contener el mensaje de error:
		Response<Boolean> errorResponse = new Response<>();

		// Se imprime el mensaje completo con todos los errores:
		log.error("Errores de validación: ");
		ex.getBindingResult().getAllErrors().forEach(error -> {
			log.error(error.getDefaultMessage());
		});

		// Obtenemos el primer mensaje error de validacion que encuentre:
		FieldError error = (FieldError) ex.getBindingResult().getAllErrors().get(0);
		String message = error.getDefaultMessage();

		// Armamos el objeto con el mensaje de error:
		errorResponse.setTitulo("Error");
		errorResponse.setTipo(Response.ERROR);
		errorResponse.setMensaje(message);
		errorResponse.setObject(false);

		// Retornamos el mensaje:
		return errorResponse;
	}

}