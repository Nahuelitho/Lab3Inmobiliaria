-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-11-2024 a las 17:02:47
-- Versión del servidor: 10.4.14-MariaDB
-- Versión de PHP: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `apiinmobiliaria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contrato`
--

CREATE TABLE `contrato` (
  `Id` int(11) NOT NULL,
  `Precio` double NOT NULL,
  `FechaInicio` datetime NOT NULL,
  `FechaFin` datetime NOT NULL,
  `Id_Inquilino` int(11) NOT NULL,
  `Id_Inmueble` int(11) NOT NULL,
  `FechaTerminacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `contrato`
--

INSERT INTO `contrato` (`Id`, `Precio`, `FechaInicio`, `FechaFin`, `Id_Inquilino`, `Id_Inmueble`, `FechaTerminacion`) VALUES
(5, 180000, '2024-11-11 03:47:40', '2024-11-18 03:47:40', 1, 13, NULL),
(6, 180000, '2024-11-10 03:48:24', '2024-11-30 03:48:24', 2, 14, NULL),
(7, 1200, '2024-11-11 03:50:08', '2024-11-30 03:50:08', 2, 13, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inmueble`
--

CREATE TABLE `inmueble` (
  `Id` int(11) NOT NULL,
  `Direccion` varchar(255) NOT NULL,
  `Ambientes` int(11) NOT NULL,
  `Tipo` varchar(100) NOT NULL,
  `Uso` varchar(100) NOT NULL,
  `Precio` int(11) NOT NULL,
  `Disponible` tinyint(4) NOT NULL,
  `Id_Propietario` int(11) DEFAULT NULL,
  `Foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `inmueble`
--

INSERT INTO `inmueble` (`Id`, `Direccion`, `Ambientes`, `Tipo`, `Uso`, `Precio`, `Disponible`, `Id_Propietario`, `Foto`) VALUES
(13, 'La Punta 4/02', 4, 'Casa', 'Vivienda', 150000, 1, 19, '/avatars/034c3fde-a5a7-4210-aa98-965c351aff8f_avatar7256203457210638773.jpg'),
(14, 'San Luis Pringles 182', 5, 'Departamento', 'Alquiler', 500000, 0, 19, '/avatars/354b7635-36e4-4f62-b217-fffe70a1af66_avatar4261716736787433345.jpg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inquilino`
--

CREATE TABLE `inquilino` (
  `Id` int(11) NOT NULL,
  `Dni` int(11) NOT NULL,
  `Apellido` varchar(100) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Direccion` varchar(255) NOT NULL,
  `Telefono` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `inquilino`
--

INSERT INTO `inquilino` (`Id`, `Dni`, `Apellido`, `Nombre`, `Direccion`, `Telefono`) VALUES
(1, 45612312, 'Juan', 'Gomez', 'San Luis', 266416225),
(2, 41272923, 'Vargas', 'Nahuel', 'La Punta 4/02', 162251);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `Id` int(11) NOT NULL,
  `NumeroPago` int(11) NOT NULL,
  `Id_Contrato` int(11) NOT NULL,
  `FechaPago` datetime NOT NULL,
  `Importe` double DEFAULT NULL,
  `Detalle` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `pago`
--

INSERT INTO `pago` (`Id`, `NumeroPago`, `Id_Contrato`, `FechaPago`, `Importe`, `Detalle`) VALUES
(4, 1, 5, '2024-11-11 03:49:09', 12545, 'Se Pago el mes de Octubre');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `propietario`
--

CREATE TABLE `propietario` (
  `Id` int(11) NOT NULL,
  `Dni` varchar(20) NOT NULL,
  `Apellido` varchar(100) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Telefono` varchar(30) NOT NULL,
  `Mail` varchar(320) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Avatar` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `propietario`
--

INSERT INTO `propietario` (`Id`, `Dni`, `Apellido`, `Nombre`, `Telefono`, `Mail`, `Password`, `Avatar`) VALUES
(19, '41272923', 'Vargas', 'Jose', '2664162251', 'nahuel11906@gmail.com', '3ljBA/9if5s6o+KwjkludaWUh4OSEUB0EXrhZUhRcrg=', '/avatarPerfil/4bb621de-75d7-4bf9-8971-7b2c98a2888a_avatarPerfil5798289073478662936.jpg');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `contrato`
--
ALTER TABLE `contrato`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Id_Inquilino` (`Id_Inquilino`),
  ADD KEY `Id_Inmueble` (`Id_Inmueble`);

--
-- Indices de la tabla `inmueble`
--
ALTER TABLE `inmueble`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Id_Propietario` (`Id_Propietario`);

--
-- Indices de la tabla `inquilino`
--
ALTER TABLE `inquilino`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `pago`
--
ALTER TABLE `pago`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Id_Contrato` (`Id_Contrato`);

--
-- Indices de la tabla `propietario`
--
ALTER TABLE `propietario`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `contrato`
--
ALTER TABLE `contrato`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `inmueble`
--
ALTER TABLE `inmueble`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `inquilino`
--
ALTER TABLE `inquilino`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pago`
--
ALTER TABLE `pago`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `propietario`
--
ALTER TABLE `propietario`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `contrato`
--
ALTER TABLE `contrato`
  ADD CONSTRAINT `contrato_ibfk_1` FOREIGN KEY (`Id_Inquilino`) REFERENCES `inquilino` (`Id`) ON DELETE CASCADE,
  ADD CONSTRAINT `contrato_ibfk_2` FOREIGN KEY (`Id_Inmueble`) REFERENCES `inmueble` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `inmueble`
--
ALTER TABLE `inmueble`
  ADD CONSTRAINT `inmueble_ibfk_1` FOREIGN KEY (`Id_Propietario`) REFERENCES `propietario` (`Id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `pago`
--
ALTER TABLE `pago`
  ADD CONSTRAINT `pago_ibfk_1` FOREIGN KEY (`Id_Contrato`) REFERENCES `contrato` (`Id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
