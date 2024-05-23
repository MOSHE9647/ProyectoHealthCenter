package cr.ac.una.app.model;

import java.time.LocalDate;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = Variables.TB_EQUIPOS_MEDICOS)
public class EquipoMedico {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="Tipo", nullable=false, length=100)
	private String tipo;

	@Column(name="Cantidad", nullable=false, length=10)
	private int cantidad;

	@Column(name="Disponible", nullable=false, length=1)
	private boolean disponible;

	@Column(name="Marca", nullable=false, length=100)
	private String marca;

	@Temporal(TemporalType.DATE)
	@Column(name = "FechaFabricacion")
	@PastOrPresent(message = "La fecha de fabricación no puede ser futura.")
	private LocalDate fechaFabricacion;

	@Column(name="InstruccionesDeUso", nullable=false, length=100)
	private String instruccionesUso;

}