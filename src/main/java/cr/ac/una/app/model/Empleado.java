package cr.ac.una.app.model;

import java.util.Objects;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Esta clase representa la informacion general
 * que posee cada empleado dentro del programa.
 * 
 * @author Isaac Herrera
 */

@Data									//<- Genera los Getters y Setters
@Entity									//<- Indica que es una Entidad de JPA
@Table(name = Variables.TB_EMPLEADOS)	//<- Asigna el Nombre de la Tabla
public class Empleado extends Usuario {
	
	// Datos relacionados al Empleado:
	@Column(name = Variables.CUENTA_BANCARIA, nullable = false)
	@NotBlank(message = "El campo 'Cuenta Bancaria' no puede estar vacío")
	private String cuentaBancaria;

	@Column(name = Variables.AREA_TRABAJO, nullable = false)
	@NotBlank(message = "El campo 'Área de Trabajo' no puede estar vacío")
    private String areaTrabajo;

	@Column(name = Variables.PROFESION, nullable = false)
	@NotBlank(message = "El campo 'Profesión' no puede estar vacío")
    private String profesion;
	
	@Column(name = Variables.ESTADO, nullable = false)
	@NotNull(message = "El campo 'Estado' no puede ser nulo")
    private Boolean estado;

	// Tipo de usuario y estado por defecto:
	public Empleado() {
		this.tipo = Usuario.EMPLEADO;
		this.estado = true;
	}

	// Verificacion de SpringSecurity para Inicio de Sesion:
	//<- Si isEnabled() es false, el empleado no puede iniciar sesion ->
	@Override
	public boolean isEnabled() { return this.estado; }

	// Metodos equals y hashCode para comparacion de Empleados:
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Empleado))
			return false;
		if (this == obj)
			return true;
		Empleado other = (Empleado) obj;
		return super.equals(obj) &&
			Objects.equals(cuentaBancaria, other.cuentaBancaria) &&
			Objects.equals(areaTrabajo, other.areaTrabajo) &&
			Objects.equals(profesion, other.profesion) &&
			Objects.equals(estado, other.estado);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cuentaBancaria == null) ? 0 : cuentaBancaria.hashCode());
		result = prime * result + ((areaTrabajo == null) ? 0 : areaTrabajo.hashCode());
		result = prime * result + ((profesion == null) ? 0 : profesion.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		return result;
	}

}