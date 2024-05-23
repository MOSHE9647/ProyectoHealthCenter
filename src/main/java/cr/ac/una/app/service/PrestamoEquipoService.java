package cr.ac.una.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.PrestamoEquipoData;
import cr.ac.una.app.model.PrestamoEquipo;

@Service
@Primary
public class PrestamoEquipoService {
 
    @Autowired
    private PrestamoEquipoData prestamoEquipoData;

    public Page<PrestamoEquipo> obtenerPagePrestamoEquipo(Pageable pageable) {
        return prestamoEquipoData.findAll(pageable);
    }

}