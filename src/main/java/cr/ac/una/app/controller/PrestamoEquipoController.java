package cr.ac.una.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.una.app.model.PrestamoEquipo;
import cr.ac.una.app.model.Response;
import cr.ac.una.app.service.PrestamoEquipoService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(path = "/api/v1/prestamos")
public class PrestamoEquipoController {
    
    @Autowired
    PrestamoEquipoService prestamoEquipoService;

    @PostMapping("/agregar")
    public Response<Boolean> guardarPrestamo(@Valid @ModelAttribute PrestamoEquipo prestamo) {
        
        return null;
    }
    
    @PostMapping("/eliminar/{id}")
    public Response<Boolean> eliminarPrestamo(@PathVariable int id) {

        return null;
    }

}