package cr.ac.una.app.controller;

import org.springframework.web.bind.annotation.RestController;

import cr.ac.una.app.model.EquipoMedico;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.service.EquipoMedicoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "/api/v1/equipos")
public class EquipoMedicoController {
    
    @Autowired
    private EquipoMedicoService equipoMServices;

    @PostMapping("/agregar")
    public Response<Boolean> agregarEquipo(@ModelAttribute @Valid EquipoMedico equipo) {
        Response<Boolean> respuesta = equipoMServices.guardar(equipo);
        if (respuesta.isSuccess()) {
            System.out.println("Exito");
        }
        return respuesta;
    }

    @PostMapping("/eliminar/{id}")
    public Response<Boolean> eliminarEquipo(@PathVariable int id) {
        Response<Boolean> respuesta = equipoMServices.eliminar(id);
        return respuesta;
    }

}
