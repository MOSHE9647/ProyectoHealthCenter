package cr.ac.una.app.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.PacienteData;
import cr.ac.una.app.data.UsuarioData;
import cr.ac.una.app.model.Paciente;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa la logica
 * relacionada a la entidad Paciente
 * 
 * @author Isaac Herrera
 */

@Slf4j
@Service
public class PacienteService {

	@Autowired
	PacienteData pacienteData;

	@Autowired
	UsuarioData usuarioData;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Devuelve una lista con la info principal de todos
	 * los Pacientes que hayan registrados en la BD.
	 * @return
	 */
	public List<Paciente> obtenerPacientes() {
		return pacienteData.findAll();
	}

	/**
	 * Obtiene un Paciente desde la Base de Datos
	 * segun su correo electronico.
	 * @param email Correo electronico del paciente
	 * @return Objeto 'Optional' que puede, o no,
	 * contener un objeto de tipo 'Paciente'.
	 */
	public Optional<Paciente> obtenerPacientePorEmail(String email) {
		return pacienteData.findByEmail(email);
	}

	/**
	 * Crea un Paciente en la Base de Datos.
	 * @param paciente que se va a guardar.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> guardarPaciente(Paciente paciente) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(paciente);
			if (!response.getObject()) {
				log.error("Los datos del paciente '{}' no son validos.", paciente.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el correo no este en uso:
			Optional<Usuario> usuarioEnBD = usuarioData.findByEmail(paciente.getEmail());
			if (usuarioEnBD.isPresent()) {
				// Se devuelve un error en caso de que el correo ya este en uso:
				log.error("No se puede guardar el paciente. El correo '{}' ya está en uso por otro paciente.", paciente.getEmail());
				response.setMensaje("El correo ingresado ya está en uso");
				return response;
			}

			// Continua en caso de no haber errores:
			// <- Se encripta la contraseña ->
			String encodedPassword = passwordEncoder.encode(paciente.getPassword());
			paciente.setPassword(encodedPassword);

			//<- Se crea el paciente en la BD ->
			pacienteData.save(paciente);
			log.info("El paciente '{}' fue guardado correctamente.", paciente.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Éxito");
			response.setMensaje("El usuario fue creado satisfactoriamente.");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrio un error inesperado al guardar el paciente.", ex);
			//<- Se retorna un mensaje de error ->
			Response<Boolean> errorResponse = new Response<>();
			errorResponse.setTitulo("Error");
			errorResponse.setMensaje("Error inesperado al crear el usuario.");
			errorResponse.setTipo(Response.ERROR);
			errorResponse.setObject(false);
			return errorResponse;
		}
	}

	/**
	 * Verifica y actualiza la informacion de un
	 * paciente en la Base de Datos del proyecto.
	 * @param paciente {@code Paciente} con los
	 * cambios realizados por el mismo desde la
	 * vista html.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> actualizarPaciente(Paciente paciente) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(paciente);
			if (!response.getObject()) {
				log.error("Los datos del paciente '{}' no son válidos.", paciente.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el paciente exista en la BD:
			Optional<Paciente> pacienteEnBD = pacienteData.findById(paciente.getID());
			if (!pacienteEnBD.isPresent()) {
				log.error("No se puede actualizar el paciente. No se encontró el paciente con el ID '{}'.", paciente.getID());
				response.setMensaje("No se encontró el usuario a actualizar.");
				return response;
			}

			// Verificar que hayan cambios para actualizar:
			Paciente pacienteActualizado = verificarCambios(pacienteEnBD.get(), paciente);
			if (pacienteActualizado == null) {
				// Si no se encontraron cambios:
				log.error("No se encontraron cambios para realizarle al paciente.");
				response.setTitulo("Usuario no actualizado");
				response.setMensaje("No hay cambios para realizar.");
				return response;
			}

			// Verificar si se le hicieron cambios al correo:
			if (!pacienteActualizado.getEmail().equalsIgnoreCase(pacienteEnBD.get().getEmail())) {
				Optional<Paciente> pacienteByEmail = pacienteData.findByEmail(pacienteActualizado.getEmail());
				if (pacienteByEmail.isPresent()) {
					// Se devuelve un error en caso de que el correo ya este en uso:
					log.error("No se puede actualizar el correo electrónico. El correo '{}' ya está en uso por otro paciente.", pacienteActualizado.getEmail());
					response.setMensaje("El correo ingresado ya está en uso");
					return response;
				}
			}

			// Continua en caso de no haber errores:
			//<- Se actualiza el usuario en la BD ->
			pacienteData.save(pacienteActualizado);
			log.info("El paciente con correo '{}' fue actualizado correctamente.", pacienteActualizado.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Éxito");
			response.setMensaje("El usuario fue actualizado correctamente.");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al actualizar el paciente.", ex);
			//<- Se retorna un mensaje de error ->
			Response<Boolean> errorResponse = new Response<>();
			errorResponse.setTitulo("Error");
			errorResponse.setMensaje("Error inesperado al actualizar el paciente.");
			errorResponse.setTipo(Response.ERROR);
			errorResponse.setObject(false);
			return errorResponse;
		}
	}

	/**
	 * Elimina a un Paciente de la Base de Datos
	 * según su Correo Electrónico.
	 * @param email Correo del paciente a eliminar.
	 * @return {@code true} si se realizo la eliminacion.
	 */
	@Transactional
	public Response<Boolean> eliminarPaciente(String email) {
		Response<Boolean> response = new Response<>();
		try {
			// Se intenta eliminar al paciente:
			pacienteData.deleteByEmail(email);
			log.info("El paciente '{}' fue eliminado correctamente.", email);

			// Si se elimino, se manda un mensaje de exito:
			response.setTitulo("Éxito");
			response.setMensaje("Usuario eliminado correctamente");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);

			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al eliminar el paciente '{}'.", email, ex);
			
			//<- Se retorna un mensaje de error ->
			response.setTitulo("Error");
			response.setMensaje("Ocurrió un error al eliminar el usuario.");
			response.setTipo(Response.ERROR);
			response.setObject(false);

			return response;
		}
	}

	/**
	 * Verifica si existen cambios para
	 * actualizar el paciente en la BD.
	 * @param pacienteEnBD paciente original.
	 * @param pacienteEnviado paciente recibido
	 * desde la vista html.
	 * @return Un {@code Paciente} si el paciente
	 * recibio cambios, y {@code null} si no.
	 */
	private Paciente verificarCambios(Paciente pacienteEnBD, Paciente pacienteEnviado) {
		if (StringUtils.isNotBlank(pacienteEnviado.getPassword())) {
			// Verificar si la contraseña del usuario enviado no es nula ni está en blanco:
			// <- Si es valida, se procede a encriptarla ->
			String encodedPassword = passwordEncoder.encode(pacienteEnviado.getPassword());
			pacienteEnviado.setPassword(encodedPassword);
		} else {
			// Si la contraseña del usuario enviado es nula o está en blanco:
			//<- Se establece la contraseña del usuario enviado como la contraseña almacenada en la base de datos ->
			pacienteEnviado.setPassword(pacienteEnBD.getPassword());
		}

		// Verifica que la informacion de ambos no sea la misma:
		if (pacienteEnviado.equals(pacienteEnBD)) {
			// Si es la misma info, se devuelve null:
			return null;
		} else {
			// Si no es la misma, se obtienen los cambios realizados:
			//<- Se obtiene el ID para actualizar ->
			Paciente pacienteActualizado = pacienteEnviado;
			pacienteActualizado.setID(pacienteEnBD.getID());
			
			//<- Se devuelve el paciente actualizado ->
			return pacienteActualizado;
		}
	}

	/**
	 * Verifica que los datos del paciente sean validos
	 * @param paciente que necesita verificacion
	 * @return {@code true} si los datos son validos
	 */
	private Response<Boolean> validarDatos(Paciente paciente) {
		Response<Boolean> resultado = new Response<>();
		resultado.setTitulo("Error");
		resultado.setTipo(Response.ERROR);
		resultado.setObject(false);

		if (StringUtils.isBlank(paciente.getNombre())) {
			resultado.setMensaje("El campo 'Nombre' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getApellido())) {
			resultado.setMensaje("El campo 'Apellido' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getCedula())) {
			resultado.setMensaje("El campo 'Cedula' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getTelefono())) {
			resultado.setMensaje("El campo 'Telefono' no puede estar vacío");
		} else if (paciente.getDireccion() == null) {
			resultado.setMensaje("Debe indicar la información de su dirección");
		} else if (StringUtils.isBlank(paciente.getEmail())) {
			resultado.setMensaje("El campo 'Correo' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getTipo())) {
			resultado.setMensaje("El campo 'Tipo' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getEstadoCivil())) {
			resultado.setMensaje("El campo 'Estado Civil' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getEscolaridad())) {
			resultado.setMensaje("El campo 'Escolaridad' no puede estar vacío");
		} else if (StringUtils.isBlank(paciente.getOcupacion())) {
			resultado.setMensaje("El campo 'Ocupación' no puede estar vacío");
		} else {
			resultado.setObject(true);
			resultado.setMensaje(null);
		}

		return resultado;
	}

}