package cr.ac.una.app.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.UsuarioData;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.model.Usuario;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa la logica
 * relacionada a la entidad Usuario
 * 
 * @author Isaac Herrera
 */

@Slf4j
@Service
public class UsuarioService implements UserDetailsService {
	
	@Autowired
	UsuarioData usuarioData;

	// Clase encargada de encriptar las contraseñas.
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public long contarUsuarios() {
		return usuarioData.count();
	}

	/**
	 * Devuelve una lista con la info principal de todos
	 * los Usuarios que hayan registrados en la BD.
	 * @return
	 */
	public List<Usuario> obtenerListaUsuarios() {
		return usuarioData.findAll();
	}

	public Page<Usuario> obtenerPageUsuarios(Pageable pageable) {
		return usuarioData.findAll(pageable);
	}

	/**
	 * Obtiene un Usuario desde la Base de Datos
	 * segun su correo electronico. Esta funcion
	 * tambien se utiliza para verificar las
	 * credenciales del Usuario en el login.
	 * @param email Correo electronico del usuario
	 * @return Objeto 'Optional' que puede, o no,
	 * contener un objeto de tipo 'Usuario'.
	 */
	public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
		return usuarioData.findByEmail(email);
	}

	/**
	 * Crea un Usuario en la Base de Datos.
	 * @param usuario que se va a guardar.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> guardarUsuario(Usuario usuario) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(usuario);
			if (!response.getObject()) {
				log.error("Los datos del usuario '{}' no son válidos.", usuario.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el correo no este en uso:
			Optional<Usuario> usuarioEnBD = usuarioData.findByEmail(usuario.getEmail());
			if (usuarioEnBD.isPresent()) {
				// Se devuelve un error en caso de que el correo ya este en uso:
				log.error("No se puede guardar el usuario. El correo '{}' ya está en uso por otro usuario.", usuario.getEmail());
				response.setMensaje("El correo ingresado ya está en uso");
				return response;
			}

			// Continua en caso de no haber errores:
			// <- Se encripta la contraseña ->
			String encodedPassword = passwordEncoder.encode(usuario.getPassword());
			usuario.setPassword(encodedPassword);

			//<- Se crea el usuario en la BD ->
			usuarioData.save(usuario);
			log.info("El usuario '{}' fue guardado correctamente.", usuario.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Éxito");
			response.setMensaje("El usuario fue creado satisfactoriamente.");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error inesperado al guardar el usuario.", ex);
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
	 * usuario en la Base de Datos del proyecto.
	 * @param usuario {@code Usuario} con los
	 * cambios realizados por el mismo desde la
	 * vista html.
	 * @return {@code Response<Boolean>} con
	 * un mensaje de error o de exito y su
	 * respectivo valor Booleano.
	 */
	public Response<Boolean> actualizarUsuario(Usuario usuario) {
		try {
			// Verificar que los datos recibidos sean validos:
			Response<Boolean> response = validarDatos(usuario);
			if (!response.isSuccess()) {
				log.error("Los datos del usuario '{}' no son válidos.", usuario.getEmail());
				// Se devuelve el mensaje con el error:
				return response;
			}

			// Verificar que el usuario exista en la BD:
			Optional<Usuario> usuarioEnBD = usuarioData.findById(usuario.getID());
			if (!usuarioEnBD.isPresent()) {
				log.error("No se puede actualizar el usuario. No se encontró un usuario con el ID '{}'.", usuario.getID());
				response.setMensaje("No existen usuarios con el correo ingresado.");
				return response;
			}

			// Verificar que hayan cambios para actualizar:
			Usuario usuarioActualizado = verificarCambios(usuarioEnBD.get(), usuario);
			if (usuarioActualizado == null) {
				// Si no se encontraron cambios:
				log.error("No se encontraron cambios para realizarle al usuario.");
				response.setTitulo("Usuario no actualizado");
				response.setMensaje("No hay cambios para realizar.");
				return response;
			}

			// Verificar si se le hicieron cambios al correo:
			if (!usuarioActualizado.getEmail().equalsIgnoreCase(usuarioEnBD.get().getEmail())) {
				Optional<Usuario> usuarioByEmail = usuarioData.findByEmail(usuarioActualizado.getEmail());
				if (usuarioByEmail.isPresent()) {
					// Se devuelve un error en caso de que el correo ya este en uso:
					log.error("No se puede actualizar el correo electrónico. El correo '{}' ya está en uso por otro usuario.", usuarioActualizado.getEmail());
					response.setMensaje("El correo ingresado ya está en uso");
					return response;
				}
			}

			// Continua en caso de no haber errores:
			//<- Se actualiza el usuario en la BD ->
			usuarioData.save(usuarioActualizado);
			log.info("El usuario con correo '{}' fue actualizado correctamente.", usuarioActualizado.getEmail());

			//<- Se retorna un mensaje de exito ->
			response.setTitulo("Usuario actualizado correctamente");
			response.setMensaje("Vuelva a iniciar sesión para aplicar los cambios");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);
			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al actualizar el usuario.", ex);
			//<- Se retorna un mensaje de error ->
			Response<Boolean> errorResponse = new Response<>();
			errorResponse.setTitulo("Error");
			errorResponse.setMensaje("Error inesperado al actualizar el usuario.");
			errorResponse.setTipo(Response.ERROR);
			errorResponse.setObject(false);
			return errorResponse;
		}
	}

	/**
	 * Elimina a un Usuario de la Base de Datos
	 * según su Correo Electrónico.
	 * @param email Correo del usuario a eliminar.
	 * @return {@code true} si se realizo la eliminacion.
	 */
	@Transactional
	public Response<Boolean> eliminarUsuario(String email) {
		Response<Boolean> response = new Response<>();
		try {
			// Se intenta eliminar el usuario:
			usuarioData.deleteByEmail(email);
			log.info("El usuario '{}' fue eliminado correctamente.", email);

			// Si se elimino, se manda un mensaje de exito:
			response.setTitulo("Éxito");
			response.setMensaje("Usuario eliminado correctamente");
			response.setTipo(Response.SUCCESS);
			response.setObject(true);

			return response;
		} catch (Exception ex) {
			log.error("Ocurrió un error al eliminar el usuario '{}'.", email, ex);
			
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
	 * actualizar el usuario en la BD.
	 * @param usuarioEnBD usuario original.
	 * @param usuarioEnviado usuario recibido
	 * desde la vista html.
	 * @return Un {@code Usuario} si el usuario
	 * recibio cambios, y {@code null} si no.
	 */
	private Usuario verificarCambios(Usuario usuarioEnBD, Usuario usuarioEnviado) {
		if (StringUtils.isNotBlank(usuarioEnviado.getPassword())) {
			// Verificar si la contraseña del usuario enviado no es nula ni está en blanco:
			// <- Si es valida, se procede a encriptarla ->
			String encodedPassword = passwordEncoder.encode(usuarioEnviado.getPassword());
			usuarioEnviado.setPassword(encodedPassword);
		} else {
			// Si la contraseña del usuario enviado es nula o está en blanco:
			//<- Se establece la contraseña del usuario enviado como la contraseña almacenada en la base de datos ->
			usuarioEnviado.setPassword(usuarioEnBD.getPassword());
		}
		
		// Verifica que la informacion de ambos usuarios no sea la misma:
		if (usuarioEnviado.equals(usuarioEnBD)) {
			// Si es la misma info, se devuelve null:
			return null;
		} else {
			// Si no es la misma, se obtienen los cambios realizados:
			//<- Se obtiene el ID para actualizar ->
			Usuario usuarioActualizado = usuarioEnviado;
			usuarioActualizado.setID(usuarioEnBD.getID());

			//<- Se devuelve el usuario actualizado ->
			return usuarioActualizado;
		}
	}

	/**
	 * Verifica que los datos del usuario sean validos
	 * @param usuario que necesita verificacion
	 * @return {@code true} si los datos son validos
	 */
	private Response<Boolean> validarDatos(Usuario usuario) {
		Response<Boolean> resultado = new Response<>();
		resultado.setTitulo("Error");
		resultado.setTipo(Response.ERROR);
		resultado.setObject(false);

		if (StringUtils.isBlank(usuario.getNombre())) {
			resultado.setMensaje("El campo 'Nombre' no puede estar vacío");
		} else if (StringUtils.isBlank(usuario.getApellido())) {
			resultado.setMensaje("El campo 'Apellido' no puede estar vacío");
		} else if (StringUtils.isBlank(usuario.getCedula())) {
			resultado.setMensaje("El campo 'Cedula' no puede estar vacío");
		} else if (StringUtils.isBlank(usuario.getTelefono())) {
			resultado.setMensaje("El campo 'Telefono' no puede estar vacío");
		} else if (usuario.getDireccion() == null) {
			resultado.setMensaje("El campo 'Direccion' no puede ser nulo");
		} else if (StringUtils.isBlank(usuario.getEmail())) {
			resultado.setMensaje("El campo 'Correo' no puede estar vacío");
		} else if (StringUtils.isBlank(usuario.getTipo())) {
			resultado.setMensaje("El campo 'Tipo' no puede estar vacío");
		} else {
			resultado.setObject(true);
			resultado.setMensaje(null);
		}

		return resultado;
	}

	/**
	 * Obtiene un Usuario desde la Base de Datos
	 * para autenticarse con SpringSecurity dentro
	 * dentro del sistema.
	 * @param username Correo del Usuario.
	 * @return {@code Usuario} con la informacion
	 * del usuario encontrado en la BD.
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioEnBD = obtenerUsuarioPorEmail(username);
		if (usuarioEnBD.isEmpty()) {
			throw new UsernameNotFoundException("El Usuario '" + username +  "' no existe.");
		}
		return usuarioEnBD.get();
	}

}