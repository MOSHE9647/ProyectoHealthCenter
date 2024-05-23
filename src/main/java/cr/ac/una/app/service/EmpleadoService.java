package cr.ac.una.app.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.EmpleadoData;
import cr.ac.una.app.data.UsuarioData;
import cr.ac.una.app.model.Empleado;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa la logica
 * relacionada a la entidad Empleado
 * 
 * @author Isaac Herrera
 */

@Slf4j
@Service
public class EmpleadoService {
	
	@Autowired
	EmpleadoData empleadoData;

	@Autowired
	UsuarioData usuarioData;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Devuelve una lista con la info principal de todos
	 * los Empleados que hayan registrados en la BD.
	 * @return
	 */
	public List<Empleado> obtenerEmpleados() {
		return empleadoData.findAll();
	}

	/**
	 * Obtiene un Empleado desde la Base de Datos
	 * segun su correo electronico.
	 * @param email Correo electronico del empleado
	 * @return Objeto 'Optional' que puede, o no,
	 * contener un objeto de tipo 'Empleado'.
	 */
	public Optional<Empleado> obtenerEmpleadoPorEmail(String email) {
		return empleadoData.findByEmail(email);
	}

	/**
	 * Crea un Empleado en la Base de Datos.
	 * @param empleado que se va a guardar.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> guardarEmpleado(Empleado empleado) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(empleado);
			if (!response.getObject()) {
				log.error("Los datos del empleado '{}' no son validos.", empleado.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el correo no este en uso:
			Optional<Usuario> usuarioEnBD = usuarioData.findByEmail(empleado.getEmail());
			if (usuarioEnBD.isPresent()) {
				// Se devuelve un error en caso de que el correo ya este en uso:
				log.error("No se puede guardar el empleado. El correo '{}' ya está en uso por otro empleado.", empleado.getEmail());
				response.setMensaje("El correo ingresado ya está en uso");
				return response;
			}

			// Continua en caso de no haber errores:
			// <- Se encripta la contraseña ->
			String encodedPassword = passwordEncoder.encode(empleado.getPassword());
			empleado.setPassword(encodedPassword);

			//<- Se crea el empleado en la BD ->
			empleadoData.save(empleado);
			log.info("El empleado '{}' fue guardado correctamente.", empleado.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Éxito");
			response.setMensaje("El empleado fue creado satisfactoriamente.");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrio un error inesperado al guardar el empleado.", ex);
			//<- Se retorna un mensaje de error ->
			Response<Boolean> errorResponse = new Response<>();
			errorResponse.setTitulo("Error");
			errorResponse.setMensaje("Error inesperado al crear el empleado.");
			errorResponse.setTipo(Response.ERROR);
			errorResponse.setObject(false);
			return errorResponse;
		}
	}

	/**
	 * Verifica y actualiza la informacion de un
	 * empleado en la Base de Datos del proyecto.
	 * @param empleado {@code Empleado} con los
	 * cambios realizados por el mismo desde la
	 * vista html.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> actualizarEmpleado(Empleado empleado) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(empleado);
			if (!response.getObject()) {
				log.error("Los datos del empleado '{}' no son válidos.", empleado.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el empleado exista en la BD:
			Optional<Empleado> empleadoEnBD = empleadoData.findById(empleado.getID());
			if (!empleadoEnBD.isPresent()) {
				log.error("No se puede actualizar el empleado. No se encontró el empleado con el ID '{}'.", empleado.getID());
				response.setMensaje("No se encontró el empleado a actualizar.");
				return response;
			}

			// Verificar que hayan cambios para actualizar:
			Empleado empleadoActualizado = verificarCambios(empleadoEnBD.get(), empleado);
			if (empleadoActualizado == null) {
				// Si no se encontraron cambios:
				log.error("No se encontraron cambios para realizarle al empleado.");
				response.setTitulo("Empleado no actualizado");
				response.setMensaje("No hay cambios para realizar.");
				return response;
			}

			// Verificar si se le hicieron cambios al correo:
			if (!empleadoActualizado.getEmail().equalsIgnoreCase(empleadoEnBD.get().getEmail())) {
				Optional<Empleado> pacienteByEmail = empleadoData.findByEmail(empleadoActualizado.getEmail());
				if (pacienteByEmail.isPresent()) {
					// Se devuelve un error en caso de que el correo ya este en uso:
					log.error("No se puede actualizar el correo electrónico. El correo '{}' ya está en uso por otro empleado.", empleadoActualizado.getEmail());
					response.setMensaje("El correo ingresado ya está en uso");
					return response;
				}
			}

			// Continua en caso de no haber errores:
			//<- Se actualiza el usuario en la BD ->
			empleadoData.save(empleadoActualizado);
			log.info("El empleado con correo '{}' fue actualizado correctamente.", empleadoActualizado.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Éxito");
			response.setMensaje("El empleado fue actualizado correctamente.");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al actualizar el empleado.", ex);
			//<- Se retorna un mensaje de error ->
			Response<Boolean> errorResponse = new Response<>();
			errorResponse.setTitulo("Error");
			errorResponse.setMensaje("Error inesperado al actualizar el empleado.");
			errorResponse.setTipo(Response.ERROR);
			errorResponse.setObject(false);
			return errorResponse;
		}
	}

	/**
	 * Elimina a un Empleado de la Base de Datos
	 * según su Correo Electrónico.
	 * @param email Correo del empleado a eliminar.
	 * @return {@code true} si se realizo la eliminacion.
	 */
	@Transactional
	public Response<Boolean> eliminarEmpleado(String email) {
		Response<Boolean> response = new Response<>();
		try {
			// Se intenta eliminar al empleado:
			empleadoData.deleteByEmail(email);
			log.info("El empleado '{}' fue eliminado correctamente.", email);

			// Si se elimino, se manda un mensaje de exito:
			response.setTitulo("Éxito");
			response.setMensaje("Empleado eliminado correctamente");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);

			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al eliminar el empleado '{}'.", email, ex);
			
			//<- Se retorna un mensaje de error ->
			response.setTitulo("Error");
			response.setMensaje("Ocurrió un error al eliminar el empleado.");
			response.setTipo(Response.ERROR);
			response.setObject(false);

			return response;
		}
	}

	/**
	 * Verifica si existen cambios para
	 * actualizar el empleado en la BD.
	 * @param empleadoEnBD empleado original.
	 * @param empleadoEnviado empleado recibido
	 * desde la vista html.
	 * @return Un {@code Empleado} si el empleado
	 * recibio cambios, y {@code null} si no.
	 */
	private Empleado verificarCambios(Empleado empleadoEnBD, Empleado empleadoEnviado) {
		if (StringUtils.isNotBlank(empleadoEnviado.getPassword())) {
			// Verificar si la contraseña del usuario enviado no es nula ni está en blanco:
			// <- Si es valida, se procede a encriptarla ->
			String encodedPassword = passwordEncoder.encode(empleadoEnviado.getPassword());
			empleadoEnviado.setPassword(encodedPassword);
		} else {
			// Si la contraseña del usuario enviado es nula o está en blanco:
			//<- Se establece la contraseña del usuario enviado como la contraseña almacenada en la base de datos ->
			empleadoEnviado.setPassword(empleadoEnBD.getPassword());
		}

		// Verifica que la informacion de ambos no sea la misma:
		if (empleadoEnviado.equals(empleadoEnBD)) {
			// Si es la misma info, se devuelve null:
			return null;
		} else {
			// Si no es la misma, se obtienen los cambios realizados:
			//<- Se obtiene el ID para actualizar ->
			Empleado empleadoActualizado = empleadoEnviado;
			empleadoActualizado.setID(empleadoEnBD.getID());

			//<- Se devuelve el empleado actualizado ->
			return empleadoActualizado;
		}
	}

	/**
	 * Verifica que los datos del empleado sean validos
	 * @param empleado que necesita verificacion
	 * @return {@code true} si los datos son validos
	 */
	private Response<Boolean> validarDatos(Empleado empleado) {
		Response<Boolean> resultado = new Response<>();
		resultado.setTitulo("Error");
		resultado.setTipo(Response.ERROR);
		resultado.setObject(false);

		if (StringUtils.isBlank(empleado.getNombre())) {
			resultado.setMensaje("El campo 'Nombre' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getApellido())) {
			resultado.setMensaje("El campo 'Apellido' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getCedula())) {
			resultado.setMensaje("El campo 'Cedula' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getTelefono())) {
			resultado.setMensaje("El campo 'Telefono' no puede estar vacío");
		} else if (empleado.getDireccion() == null) {
			resultado.setMensaje("Debe indicar la información de su dirección");
		} else if (StringUtils.isBlank(empleado.getEmail())) {
			resultado.setMensaje("El campo 'Correo' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getTipo())) {
			resultado.setMensaje("El campo 'Tipo' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getCuentaBancaria())) {
			resultado.setMensaje("El campo 'Cuenta Bancaria' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getAreaTrabajo())) {
			resultado.setMensaje("El campo 'Área de Trabajo' no puede estar vacío");
		} else if (StringUtils.isBlank(empleado.getProfesion())) {
			resultado.setMensaje("El campo 'Profesión' no puede estar vacío");
		} else if (empleado.getEstado() != null) {
			resultado.setMensaje("El campo 'Estado' no puede ser nulo");
		} else {
			resultado.setObject(true);
			resultado.setMensaje(null);
		}

		return resultado;
	}

}