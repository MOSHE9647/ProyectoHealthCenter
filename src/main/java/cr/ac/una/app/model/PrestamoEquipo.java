package cr.ac.una.app.model;

import java.time.LocalDate;

import cr.ac.una.app.utils.Variables;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = Variables.TB_PRESTAMOS_EQUIPOS)
public class PrestamoEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "EquipoID", referencedColumnName = "id", nullable = false)
    private EquipoMedico equipo;

    @OneToOne()
    @JoinColumn(name = "PacienteID", referencedColumnName = "ID", nullable = false)
    private Paciente usuario;

    @Column(name = "Motivo", nullable = false, length = 200)
    private String motivo;

    @Column(name = "FechaEntrega", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaEntrega;

    @Column(name = "FechaFinal", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;

}
