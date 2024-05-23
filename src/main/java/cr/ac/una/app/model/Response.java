package cr.ac.una.app.model;

import lombok.Data;

/**
 * Esta clase representa la respuesta que se
 * le va a devolver al usuario despues de
 * llamar a una de las API's desde JS
 * 
 * El objeto generico se utiliza en caso de tener
 * que devolver algo ademas de solo el mensaje del
 * servidor.
 * 
 * @author Isaac Herrera
 */

@Data //<- Genera los setters, getters y demas info
public class Response<T> {
	
	// Variables para indicar el tipo de respuesta:
	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	public static final String SUCCESS = "success";

	private String titulo;	//<- Titulo de la Respuesta
	private String mensaje; //<- Mensaje a mostrar
	private String tipo;	//<- Tipo de respuesta
	private T object;		//<- Objeto generico

	/**
     * Metodo para determinar si la respuesta fue un exito.
     * @return true si la respuesta fue un exito, false de lo contrario.
     */
	public boolean isSuccess() {
		return (object instanceof Boolean) ? (boolean) object : null;
	}

}