package cr.ac.una.app.utils;

/**
 * Clase para el almacenamiento de las
 * variables que van a ser utilizadas
 * dentro del Proyecto
 * @author Isaac Herrera
 */
public class Variables {
    
    // VARIABLES DE ACCESO A LA BASE DE DATOS //
    public final static String DB_NAME = "sql5691151";
    public final static String DB_USER = "sql5691151";
    public final static String DB_PASS = "9lQgPp5tRd";

    // VARIABLES DE CONEXIÓN A LA BASE DE DATOS //
    public final static String HOST = "sql5.freesqldatabase.com";
    public final static int PORT = 3306;
    public final static String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;

    // VARIABLES PARA LAS TABLAS DE LA BD //
    public static final String TB_PRESTAMOS_EQUIPOS = "tbprestamosmedicos";
    public static final String TB_EQUIPOS_MEDICOS = "tbequiposmedicos";
    public static final String TB_ENFERMEDADES = "tbenfermedades";
    public static final String TB_DIRECCIONES = "tbdirecciones";
    public static final String TB_EMPLEADOS = "tbempleados";
    public static final String TB_PACIENTES = "tbpacientes";
    public static final String TB_ALERGIAS = "tbalergias";
    public static final String TB_USUARIOS = "tbusuarios";

    // VARIABLES PARA AUTENTICACION //
    public static final String TIPO = "Tipo";
    public static final String CORREO = "Email";
    public static final String PASSWORD = "Password";
    
    // VARIABLES DE LA CLASE USUARIO //
    public static final String CEDULA = "Cedula";
    public static final String NOMBRE = "Nombre";
    public static final String APELLIDO = "Apellido";
    public static final String TELEFONO = "Telefono";
    public static final String DIRECCION = "Direccion";

    // VARIABLES DE LA CLASE EMPLEADO //
    public static final String CUENTA_BANCARIA = "CuentaBancaria";
    public static final String AREA_TRABAJO = "AreaTrabajo";
    public static final String PROFESION = "Profesion";
    public static final String ESTADO = "Estado";

    // VARIABLES DE LA CLASE PACIENTE //
    public static final String ESTADO_CIVIL = "EstadoCivil";
    public static final String ESCOLARIDAD = "Escolaridad";
    public static final String TIPO_SANGRE = "TipoSangre";
    public static final String PACIENTE_ID = "PacienteID";
    public static final String ENFERMEDAD = "Enfermedad";
    public static final String OCUPACION = "Ocupacion";
    public static final String ALERGIA = "Alergia";
    public static final String PESO = "Peso";

    // VARIABLES DE LA CLASE DIRECCION //
    public static final String DIRECCION_ID = "DireccionID";
    public static final String PROVINCIA = "Provincia";
    public static final String CANTON = "Canton";
    public static final String DISTRITO = "Distrito";
    public static final String BARRIO = "Barrio";
    public static final String INFO = "InformacionAdicional";

}