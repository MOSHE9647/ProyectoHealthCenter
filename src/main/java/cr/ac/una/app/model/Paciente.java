package cr.ac.una.app.model;

import java.util.List;
import java.util.Objects;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Esta clase representa la informacion general
 * que posee cada paciente dentro del programa.
 * 
 * @author Isaac Herrera
 */

@Data									//<- Genera los Getters y Setters
@Entity									//<- Indica que es una Entidad de JPA
@Table(name = Variables.TB_PACIENTES)	//<- Asigna el Nombre de la Tabla
public class Paciente extends Usuario {
	
	// Tablas para las Listas de Alergias y Enfermedades:
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = Variables.TB_ENFERMEDADES, joinColumns = @JoinColumn(name = Variables.PACIENTE_ID))
    @Column(name = Variables.ENFERMEDAD)
    private List<String> enfermedades;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = Variables.TB_ALERGIAS, joinColumns = @JoinColumn(name = Variables.PACIENTE_ID))
    @Column(name = Variables.ALERGIA)
    private List<String> alergias;

	// Datos relacionados al Paciente:
	@Column(name = Variables.ESTADO_CIVIL, nullable = false)
	@NotBlank(message = "El campo 'Estado Civil' no puede estar vacío")
	private String estadoCivil;

	@Column(name = Variables.ESCOLARIDAD, nullable = false)
	@NotBlank(message = "El campo 'Escolaridad' no puede estar vacío")
	private String escolaridad;

	@Column(name = Variables.OCUPACION, nullable = false)
	@NotBlank(message = "El campo 'Ocupación' no puede estar vacío")
	private String ocupacion;

	@Column(name = Variables.TIPO_SANGRE)
	private String tipoSangre;

	@Column(name = Variables.PESO)
	private double peso;

	// Tipo de usuario por defecto:
	public Paciente() {
		this.tipo = Usuario.PACIENTE;
	}

	private boolean listasIguales(List<String> thisList, List<String> otherList) {
		// Verificar si las listas están vacías
		if (thisList == null && otherList.size() == 0) {
			return true;
		}
		
		// Verificar si ambas listas tienen diferente tamaño
		if (thisList.size() != otherList.size()) {
			// Si las listas tienen diferente tamaño, no pueden ser iguales
			return false;
		}
	
		// Ambas listas tienen el mismo tamaño, proceder a comparar elementos
		for (int i = 0; i < thisList.size(); i++) {
			if (!thisList.get(i).equals(otherList.get(i))) {
				// Si los elementos en la misma posición son diferentes, las listas no son iguales
				return false;
			}
		}
	
		// Si no se encontraron diferencias, las listas son iguales
		return true;
	}

	// Metodos equals y hashCode para comparacion de Pacientes:
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Paciente))
			return false;
		if (this == obj)
			return true;
		Paciente other = (Paciente) obj;
		return super.equals(obj) &&
			listasIguales(enfermedades, other.enfermedades) &&
			listasIguales(alergias, other.alergias) &&
			Objects.equals(estadoCivil, other.estadoCivil) &&
			Objects.equals(escolaridad, other.escolaridad) &&
			Objects.equals(tipoSangre, other.tipoSangre) &&
			Objects.equals(ocupacion, other.ocupacion) &&
			Double.compare(peso, other.peso) == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((enfermedades == null) ? 0 : enfermedades.hashCode());
		result = prime * result + ((alergias == null) ? 0 : alergias.hashCode());
		result = prime * result + ((estadoCivil == null) ? 0 : estadoCivil.hashCode());
		result = prime * result + ((escolaridad == null) ? 0 : escolaridad.hashCode());
		result = prime * result + ((tipoSangre == null) ? 0 : tipoSangre.hashCode());
		result = prime * result + ((ocupacion == null) ? 0 : ocupacion.hashCode());
		long temp;
		temp = Double.doubleToLongBits(peso);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

}