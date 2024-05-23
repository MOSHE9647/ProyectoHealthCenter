package cr.ac.una.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cr.ac.una.app.model.EquipoMedico;

@Repository
public interface EquipoMedicoData extends JpaRepository<EquipoMedico, Integer> {

}