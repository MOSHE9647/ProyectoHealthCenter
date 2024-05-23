package cr.ac.una.app.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Esta clase representa la informacion general
 * que posee cada usuario, tanto como persona en
 * si, como para iniciar sesion en la aplicacion.
 * 
 * @author Isaac Herrera
 */

@Data											//<- Genera los Getters y Setters
@Entity											//<- Indica que es una Entidad de JPA
@Table(name = Variables.TB_USUARIOS)			//<- Asigna el Nombre de la Tabla
@Inheritance(strategy = InheritanceType.JOINED) //<- Indica que van a haber subclases
public class Usuario implements UserDetails {

	// Variables estáticas para representar el Tipo de Usuario:
	public final static String ADMIN = "Administrador";
	public final static String EMPLEADO = "Empleado";
	public final static String PACIENTE = "Paciente";

	// Generacion del ID del Usuario en la Base de Datos:
	@Id 												//<- Indica que este va a ser el ID
	@GeneratedValue(strategy = GenerationType.IDENTITY) //<- Asigna la propiedad Autoincremental al ID
	@Column(name = "ID") 								//<- Indica el Nombre de la Columna en la Tabla
	protected Integer ID;

	// Atributos que van a heredar Paciente y Empleado:
	//<- Nullable indica si el campo puede, o no, ser nulo 			        ->//
	//<- NotNull indica que, el campo (numero u objeto), no puede ser nulo  ->//
	//<- NotBlank indica que el campo no puede estar vacio ni ser nulo      ->//
	//<- Pattern verifica que el numero de telefono tenga un formato valido ->//
	@Column(name = Variables.NOMBRE, nullable = false)
	@NotBlank(message = "El campo 'Nombre' no puede estar vacío")
	protected String nombre;

	@Column(name = Variables.APELLIDO, nullable = false)
	@NotBlank(message = "El campo 'Apellido' no puede estar vacío")
	protected String apellido;

	@Column(name = Variables.CEDULA, nullable = false)
	@NotBlank(message = "El campo 'Cédula' no puede estar vacío")
	protected String cedula;
	
	@Column(name = Variables.TELEFONO, nullable = false)
	@NotBlank(message = "El campo 'Teléfono' no puede estar vacío'")
	@Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Ingrese un número de teléfono válido (Ej: +50612345678)")
	protected String telefono;

	// Ajuste de relacion entre Direccion y Usuario:
	//<- OneToOne indica una relacion 1a1 entre las entidades Direcion y Usuario en la BD 		   ->//
	//<- Cascade indica que, si se borra un Usuario, tambien se borra la direccion relacionada     ->//
	//<- ReferencedColumn indica la columna a la que va a hacer Referencia en la tabla Direcciones ->//
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = Variables.DIRECCION_ID, referencedColumnName = "ID", nullable = false)
	@NotNull(message = "El campo 'Direccion' no puede ser nulo")
	@Valid
	protected Direccion direccion;

	// Datos del usuario para iniciar sesion:
	//<- Unique indica que el campo debe ser unico 				   ->//
	//<- Email verifica que el campo se una direccion email valida ->//
	@Column(name = Variables.CORREO, unique = true, nullable = false)
	@NotBlank(message = "El campo 'Correo' no puede estar vacío")
	@Email(message = "La dirección de correo electrónico no es válida")
	protected String email;
	
	@Column(name = Variables.PASSWORD, nullable = false)
	protected String password;
	
	@Column(name = Variables.TIPO, nullable = false)
	@NotBlank(message = "El campo 'Tipo' no puede estar vacío")
	protected String tipo;

	// Tipo de usuario por defecto:
	public Usuario() {
		this.tipo = ADMIN;
	}

	// Metodos de UserDetails para uso de SpringSecurity (Login)
	@Override
	public String getUsername() { return this.getEmail(); }
	
	@Override
	public boolean isAccountNonExpired() { return true; }
	
	@Override
	public boolean isAccountNonLocked() { return true; }
	
	@Override
	public boolean isCredentialsNonExpired() { return true; }
	
	@Override
	public boolean isEnabled() { return true; }	

	// Asignacion de Roles segun el Tipo de Usuario:
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<String> permisosUsuario = new HashSet<>();
		permisosUsuario.add(this.getTipo());
		Collection<? extends GrantedAuthority> authorities = 
			permisosUsuario
			.stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
			.collect(Collectors.toSet());
		return authorities;
	}

	/**
     * Método encargado de obtener la información del
     * usuario que haya iniciado sesión en la aplicación.
     * @return {@code Usuario} con su respectiva información.
     */
    public static Usuario getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }

	// Metodos equals y hashCode para comparacion de Usuarios:
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return 
			Objects.equals(nombre, other.nombre) &&
			Objects.equals(apellido, other.apellido) &&
			Objects.equals(cedula, other.cedula) &&
			Objects.equals(telefono, other.telefono) &&
			Objects.equals(direccion, other.direccion) &&
			Objects.equals(email, other.email) &&
			Objects.equals(tipo, other.tipo)
		;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((cedula == null) ? 0 : cedula.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

}