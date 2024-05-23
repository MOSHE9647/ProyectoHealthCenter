package cr.ac.una.app.model;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Esta clase representa la informacion relacionada
 * a los datos especificos de la direccion en la que
 * vive un usuario.
 * 
 * @author Isaac Herrera
 */

@Data										//<- Genera los Getters y Setters
@Entity										//<- Indica que es una Entidad de JPA
@Table(name = Variables.TB_DIRECCIONES)		//<- Asigna el Nombre de la Tabla
public class Direccion {
	
	// Generacion del ID de la Direccion en la Base de Datos:
	@Id 												//<- Indica que este va a ser el ID
	@GeneratedValue(strategy = GenerationType.IDENTITY) //<- Asigna la propiedad Autoincremental al ID
	@Column(name = "ID") 								//<- Indica el Nombre de la Columna en la Tabla
	private Long ID;

	// Atributos relacionados a la direccion del usuario:
	//<- NotBlank verifica que el campo no este vacio ni sea nulo  ->//
	//<- Size asigna una cantidad maxima de caracteres al atributo ->//
	@Column(name = Variables.PROVINCIA, nullable = false)
	@NotBlank(message = "El campo 'Provincia' no puede estar vacio")
    private String provincia;

	@Column(name = Variables.CANTON, nullable = false)
	@NotBlank(message = "El campo 'Canton' no puede estar vacio")
    private String canton;

	@Column(name = Variables.DISTRITO, nullable = false)
	@NotBlank(message = "El campo 'Distrito' no puede estar vacio")
    private String distrito;

	@Column(name = Variables.BARRIO, nullable = false)
	@NotBlank(message = "El campo 'Barrio' no puede estar vacio")
	private String barrio;

	@Column(name = Variables.INFO)
	@Size(max = 255, message = "El campo 'Información Adicional' debe tener máximo {max} caracteres")
    private String informacionAdicional;

	// Metodos equals y hashCode para comparacion de atributos:
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Direccion other = (Direccion) obj;
		return
			Objects.equals(this.barrio, other.barrio)
			&& Objects.equals(this.distrito, other.distrito)
			&& Objects.equals(this.canton, other.canton)
			&& Objects.equals(this.provincia, other.provincia)
			&& Objects.equals(this.informacionAdicional, other.informacionAdicional)
		;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			barrio, distrito, canton, provincia, informacionAdicional
		);
	}

	// Metodo toString para mostrar la informacion
	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		if (StringUtils.isNotBlank(informacionAdicional)) {
			info.append(informacionAdicional + ", ");
		}
		info.append(barrio + ", ");
		info.append(canton + ", ");
		info.append(distrito + ", ");
		info.append(provincia);

		return info.toString();
	}

}