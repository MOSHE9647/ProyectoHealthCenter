package cr.ac.una.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cr.ac.una.app.data.EquipoMedicoData;
import cr.ac.una.app.model.EquipoMedico;
import cr.ac.una.app.model.Response;

@Service
@Primary
public class EquipoMedicoService {
    
    @Autowired
    private EquipoMedicoData equipoMedicoData;

    public Page<EquipoMedico> obtenerPageEquipoMedico(Pageable pageable) {
		return equipoMedicoData.findAll(pageable);
	}

    public Response<Boolean> guardar(EquipoMedico equipo) {
        Response<Boolean> respuesta = new Response<>();
        try {
            if (equipo.getFechaFabricacion().isAfter(LocalDate.now())) {
                respuesta.setTitulo("Error de validacion");
                respuesta.setMensaje("La fecha de fabricación no puede ser después de hoy!!");
                respuesta.setTipo(Response.ERROR);
                respuesta.setObject(Boolean.FALSE);
                return respuesta;
            }
            equipoMedicoData.save(equipo);
            respuesta.setTitulo("Exito");
            respuesta.setMensaje("Equipo Guardado exitosamente");
            respuesta.setTipo(Response.SUCCESS);
            respuesta.setObject(Boolean.TRUE);
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setTitulo("Error en el Servidor");
            respuesta.setMensaje("Ocurrio un mensaje interno");
            respuesta.setTipo(Response.ERROR);
            respuesta.setObject(Boolean.FALSE);
            return respuesta;
        }
    }

    public List<EquipoMedico> getEquiposM() {
        return equipoMedicoData.findAll();
    }

    public Response<Boolean> eliminar(int id) {
        Response<Boolean> respuesta = new Response<>();
        try {
            equipoMedicoData.deleteById(id);
            respuesta.setTitulo("Exito");
            respuesta.setMensaje("Equipo Guardado exitosamente");
            respuesta.setTipo(Response.SUCCESS);
            respuesta.setObject(Boolean.TRUE);
            return respuesta;

        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setTitulo("Error en el Servidor");
            respuesta.setMensaje("Ocurrio un mensaje interno");
            respuesta.setTipo(Response.ERROR);
            respuesta.setObject(Boolean.FALSE);
            return respuesta;
        }
    }

    public EquipoMedico buscarPorId(int id) {
        Optional<EquipoMedico> equipo = equipoMedicoData.findById(id);
        return equipo.orElse(null); // Retorna el equipo si existe, de lo contrario retorna null
    }

}