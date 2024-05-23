-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-05-2024 a las 03:20:29
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bdproyectoprograiv`
--

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `tbdirecciones`
--

INSERT INTO `tbdirecciones` (`ID`, `Barrio`, `Canton`, `Distrito`, `InformacionAdicional`, `Provincia`) VALUES
(1, 'Urb. Miraflores', 'Sarapiquí', 'Horquetas', NULL, 'Heredia'),
(2, 'San Pedro', 'Montes de Oca', 'San Pedro', 'Cerca de la Universidad de Costa Rica', 'San José'),
(3, 'Escazú Centro', 'Escazú', 'Escazú', 'Zona comercial', 'San José'),
(4, 'San Rafael', 'Heredia', 'San Rafael', 'Residencial tranquilo', 'Heredia'),
(5, 'Santa Ana Centro', 'Santa Ana', 'Santa Ana', 'Área de rápido crecimiento', 'San José'),
(6, 'San Isidro', 'Vázquez de Coronado', 'San Isidro', 'Cerca del Parque Nacional Braulio Carrillo', 'San José'),
(7, 'Liberia', 'Liberia', 'Liberia', 'Capital de la provincia de Guanacaste', 'Guanacaste'),
(8, 'San Vicente', 'Moravia', 'San Vicente', 'Zona residencial', 'San José'),
(9, 'San Ramón', 'San Ramón', 'San Ramón', 'Conocido por su clima fresco', 'Alajuela'),
(10, 'Tamarindo', 'Santa Cruz', 'Tamarindo', 'Destino turístico popular', 'Guanacaste'),
(11, 'San Rafael', 'Alajuela', 'San Rafael', 'Área con desarrollo comercial', 'Alajuela'),
(12, 'Tres Ríos', 'La Unión', 'Tres Ríos', 'Cerca del centro comercial Terramall', 'Cartago'),
(13, 'Prueba', 'Prueba1', 'Prueba', '', 'Heredia'),
(14, 'Prueba', 'Prueba1', 'Prueba', '', 'Heredia');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `tbequiposmedicos`
--

INSERT INTO `tbequiposmedicos` (`id`, `Cantidad`, `Disponible`, `FechaFabricacion`, `InstruccionesDeUso`, `Marca`, `Tipo`) VALUES
(1, 45, b'1', '2021-08-15', 'Instrucción 1', 'Marca 1', 'Tipo 1'),
(2, 78, b'0', '2022-02-10', 'Instrucción 2', 'Marca 2', 'Tipo 2'),
(3, 23, b'1', '2020-11-25', 'Instrucción 3', 'Marca 3', 'Tipo 3'),
(4, 90, b'0', '2019-06-30', 'Instrucción 4', 'Marca 4', 'Tipo 4'),
(5, 67, b'1', '2023-01-12', 'Instrucción 5', 'Marca 5', 'Tipo 5'),
(6, 15, b'0', '2022-08-23', 'Instrucción 6', 'Marca 6', 'Tipo 6'),
(7, 39, b'1', '2021-05-14', 'Instrucción 7', 'Marca 7', 'Tipo 7'),
(8, 85, b'0', '2020-12-01', 'Instrucción 8', 'Marca 8', 'Tipo 8'),
(9, 50, b'1', '2019-09-19', 'Instrucción 9', 'Marca 9', 'Tipo 9'),
(10, 72, b'0', '2023-04-20', 'Instrucción 10', 'Marca 10', 'Tipo 10');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `tbpacientes`
--

INSERT INTO `tbpacientes` (`Escolaridad`, `EstadoCivil`, `Ocupacion`, `Peso`, `TipoSangre`, `ID`) VALUES
('UNIVERSIDAD-INCOMPLETA', 'SOLTERO', 'Estudiante', 0, 'N/A', 14);

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `tbusuarios`
--

INSERT INTO `tbusuarios` (`ID`, `Apellido`, `Cedula`, `Email`, `Nombre`, `Password`, `Telefono`, `Tipo`, `DireccionID`) VALUES
(2, 'Herrera', '1 1802 0841', 'isaacmhp2001@gmail.com', 'Isaac', '$2a$10$hjGpTol2Ka6hm1sid14ak.TdYtcqKZGEXhkP7/mzCSL65tBrJkYeO', '+506 6421 2950', 'Administrador', 1),
(3, 'González', '987654321', 'maria.gonzalez@example.com', 'Maria', 'password456', '88884321', 'Empleado', 3),
(4, 'Martínez', '192837465', 'carlos.martinez@example.com', 'Carlos', 'password789', '88885678', 'Paciente', 4),
(5, 'Fernández', '564738291', 'ana.fernandez@example.com', 'Ana', 'password012', '88886789', 'Paciente', 5),
(6, 'López', '847362915', 'pedro.lopez@example.com', 'Pedro', 'password345', '88887890', 'Empleado', 6),
(7, 'Ramírez', '748392615', 'lucia.ramirez@example.com', 'Lucia', 'password678', '88888901', 'Paciente', 7),
(8, 'Hernández', '839475162', 'diego.hernandez@example.com', 'Diego', 'password901', '88889012', 'Empleado', 8),
(9, 'García', '921837465', 'laura.garcia@example.com', 'Laura', 'password234', '88880123', 'Paciente', 9),
(10, 'Sánchez', '138496275', 'jose.sanchez@example.com', 'Jose', 'password567', '88881234', 'Empleado', 10),
(11, 'Vargas', '728394651', 'marta.vargas@example.com', 'Marta', 'password890', '88882345', 'Paciente', 11),
(12, 'Jiménez', '839475261', 'sergio.jimenez@example.com', 'Sergio', 'password123', '88883456', 'Empleado', 12),
(13, 'Rodríguez', '123456789', 'juan.rodriguez@example.com', 'Juan', 'password123', '88881234', 'Paciente', 2),
(14, 'Prueba', '8 7654 3210', 'prueba@prueba.com', 'Prueba', '$2a$10$PZO.phmZbCyN8DffzpIDbeVR3OcBlQYH2mU.oqpz3TRqFfgLa6cc6', '+506 1234 5678', 'Paciente', 14);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
