package cr.ac.una.app.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cr.ac.una.app.model.Empleado;

/**
 * Esta clase es la encargada de comunicarse con la
 * Base de Datos. Los métodos con, sus respectivos
 * Querys, ya los trae generados por si misma.
 * 
 * @author Isaac Herrera
 */

@Repository
public interface EmpleadoData extends JpaRepository<Empleado, Integer> {
    
    /**
	 * Encuentra a un empleado en la BD
	 * según su correo electrónico. Este metodo
	 * devuelve un objeto que ayuda a verificar
	 * si se encontró o no el empleado mediante el
	 * metodo 'isPresent()'. De haberse encontrado
	 * el empleado, se puede obtener mediante el
	 * metodo get() del mismo.
	 * @param email correo del empleado a buscar.
	 * @return Objeto 'Optional' que puede, o no,
	 * contener un objeto de tipo 'Paciente'.
	 */
	Optional<Empleado> findByEmail(String email);

	/**
	 * Elimina a un empleado de la BD mediante
	 * el uso de su correo electrónico.
	 * @param email correo del empleado a eliminar.
	 * @return {@code true} si se elimino el empleado.
	 */
	void deleteByEmail(String email);

}
