package cr.ac.una.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cr.ac.una.app.model.PrestamoEquipo;

@Repository
public interface PrestamoEquipoData extends JpaRepository<PrestamoEquipo, Integer> {
    
}