package cr.ac.una.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.PrestamoEquipoData;
import cr.ac.una.app.model.PrestamoEquipo;
import cr.ac.una.app.model.Response;
import jakarta.transaction.Transactional;

@Service
@Primary
public class PrestamoEquipoService {
 
    @Autowired
    private PrestamoEquipoData prestamoEquipoData;

    public List<PrestamoEquipo> todosLosPrestamos() {
        return prestamoEquipoData.findAll();
    }

    public Page<PrestamoEquipo> obtenerPagePrestamoEquipo(Pageable pageable) {
        return prestamoEquipoData.findAll(pageable);
    }

    public Response<Boolean> guardarPrestamo(PrestamoEquipo prestamo) {
        Response<Boolean> respuesta = new Response<>();
        try {
            // Validar fechas aquí, si es necesario
            if (prestamo.getFechaEntrega().isAfter(LocalDate.now())) {
                respuesta.setTitulo("Error de validación");
                respuesta.setMensaje("La fecha de entrega no puede ser después de hoy!!");
                respuesta.setTipo(Response.ERROR);
                respuesta.setObject(Boolean.FALSE);
                return respuesta;
            }

            prestamoEquipoData.save(prestamo);
            respuesta.setTitulo("Éxito");
            respuesta.setMensaje("Prestamo guardado exitosamente");
            respuesta.setTipo(Response.SUCCESS);
            respuesta.setObject(Boolean.TRUE);
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setTitulo("Error en el servidor");
            respuesta.setMensaje("Ocurrió un error interno");
            respuesta.setTipo(Response.ERROR);
            respuesta.setObject(Boolean.FALSE);
            return respuesta;
        }
    }

    @Transactional
    public Response<Boolean> eliminarPrestamo(int id) {
        Response<Boolean> respuesta = new Response<>();
        try {
            prestamoEquipoData.deleteById(id);
            respuesta.setTitulo("Éxito");
            respuesta.setMensaje("Prestamo eliminado exitosamente");
            respuesta.setTipo(Response.SUCCESS);
            respuesta.setObject(Boolean.TRUE);
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setTitulo("Error en el servidor");
            respuesta.setMensaje("Ocurrió un error interno");
            respuesta.setTipo(Response.ERROR);
            respuesta.setObject(Boolean.FALSE);
            return respuesta;
        }
    }

    public PrestamoEquipo buscarPorId(int id) {
        Optional<PrestamoEquipo> prestamo = prestamoEquipoData.findById(id);
        return prestamo.orElse(null);
    }

}