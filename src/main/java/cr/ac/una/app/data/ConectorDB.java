package cr.ac.una.app.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cr.ac.una.app.utils.Variables;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Isaac Herrera
 */
@Slf4j
public class ConectorDB extends Variables {
	
	/* Variable para la Conexión a la BD */
    private static Connection conexion;

    public static Connection crearConexion() {
        String DriverClassName = "com.mysql.cj.jdbc.Driver";
        try {
            /* Verifica que exista el controlador de MySQL */
            Class.forName(DriverClassName);

            /* Verifica la conexión a la Base de Datos */
            try {
                conexion = DriverManager.getConnection(URL, DB_USER, DB_PASS);
            } catch (SQLException e) {
                log.error(
                    "Error al establecer la conexión con la Base de Datos:\n" + 
                    "\nCódigo de Estado SQL: " + e.getSQLState() +
                    "\nCódigo de Error: " + e.getErrorCode() +
                    "\nMensaje: " + e.getMessage())
                ;
            }
        } catch (ClassNotFoundException e) {
            log.error("Class for name '" + DriverClassName + "'" + "not found:\n" + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion(Connection cn, String nombreClase) {
        try {
            if (cn != null) { cn.close(); }
        } catch (SQLException e) {
            log.error(
                "\n\n[" + nombreClase + "@Data] " +
                "\nError al intentar cerrar la conexion: " + e.getMessage()
            );
        }
    }

}