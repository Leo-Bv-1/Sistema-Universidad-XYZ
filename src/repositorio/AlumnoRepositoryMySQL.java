package src.repositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import src.modelo.*;
/**
 * Implementación del repositorio usando MySQL
 * Esta clase demuestra cómo extender el sistema para usar base de datos
 * 
 * REQUISITOS:
 * - MySQL Connector/J (JDBC Driver)
 * - Base de datos MySQL configurada
 * 
 * CONFIGURACIÓN:
 * 1. Agregar mysql-connector-java al classpath
 * 2. Crear la base de datos y tablas (ver script SQL al final)
 * 3. Ajustar credenciales de conexión
 */
public class AlumnoRepositoryMySQL implements IAlumnoRepository {
    
    // Configuración de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/sideral_carreon";
    private static final String USER = "root";
    private static final String PASSWORD = "tu_password";
    
    // Instancia única (Singleton)
    private static AlumnoRepositoryMySQL instancia;
    
    private AlumnoRepositoryMySQL() {
        // Verificar conexión al inicializar
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            verificarConexion();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado");
            System.err.println("Agregue mysql-connector-java al classpath");
        }
    }
    
    public static synchronized AlumnoRepositoryMySQL getInstancia() {
        if (instancia == null) {
            instancia = new AlumnoRepositoryMySQL();
        }
        return instancia;
    }
    
    // Método para obtener conexión
    private Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Verificar que la conexión funcione
    private void verificarConexion() {
        try (Connection conn = getConexion()) {
            System.out.println("✓ Conexión a base de datos establecida");
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos");
            System.err.println("Mensaje: " + e.getMessage());
        }
    }
    
    @Override
    public boolean guardar(Alumno alumno) {
        if (alumno == null || !alumno.validarDatos()) {
            return false;
        }
        
        String sql = "INSERT INTO alumnos (codigo_alumno, nombre, apellido, dni, " +
                    "fecha_nacimiento, direccion, telefono, email, carrera, ciclo, " +
                    "promedio, estado, fecha_ingreso, creditos_acumulados) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, alumno.getCodigoAlumno());
            stmt.setString(2, alumno.getNombre());
            stmt.setString(3, alumno.getApellido());
            stmt.setString(4, alumno.getDni());
            stmt.setString(5, alumno.getFechaNacimiento());
            stmt.setString(6, alumno.getDireccion());
            stmt.setString(7, alumno.getTelefono());
            stmt.setString(8, alumno.getEmail());
            stmt.setString(9, alumno.getCarrera());
            stmt.setInt(10, alumno.getCiclo());
            stmt.setDouble(11, alumno.getPromedio());
            stmt.setString(12, alumno.getEstado());
            stmt.setString(13, alumno.getFechaIngreso());
            stmt.setInt(14, alumno.getCreditosAcumulados());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizar(Alumno alumno) {
        if (alumno == null || !alumno.validarDatos()) {
            return false;
        }
        
        String sql = "UPDATE alumnos SET nombre = ?, apellido = ?, dni = ?, " +
                    "fecha_nacimiento = ?, direccion = ?, telefono = ?, email = ?, " +
                    "carrera = ?, ciclo = ?, promedio = ?, estado = ?, " +
                    "fecha_ingreso = ?, creditos_acumulados = ? " +
                    "WHERE codigo_alumno = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido());
            stmt.setString(3, alumno.getDni());
            stmt.setString(4, alumno.getFechaNacimiento());
            stmt.setString(5, alumno.getDireccion());
            stmt.setString(6, alumno.getTelefono());
            stmt.setString(7, alumno.getEmail());
            stmt.setString(8, alumno.getCarrera());
            stmt.setInt(9, alumno.getCiclo());
            stmt.setDouble(10, alumno.getPromedio());
            stmt.setString(11, alumno.getEstado());
            stmt.setString(12, alumno.getFechaIngreso());
            stmt.setInt(13, alumno.getCreditosAcumulados());
            stmt.setString(14, alumno.getCodigoAlumno());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar alumno: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminar(String codigoAlumno) {
        String sql = "DELETE FROM alumnos WHERE codigo_alumno = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoAlumno);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar alumno: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Alumno> buscarPorCodigo(String codigoAlumno) {
        String sql = "SELECT * FROM alumnos WHERE codigo_alumno = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoAlumno);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Alumno> obtenerTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY apellido, nombre";
        
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public List<Alumno> buscarPorNombre(String termino) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE " +
                    "LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? OR " +
                    "LOWER(CONCAT(nombre, ' ', apellido)) LIKE ? " +
                    "ORDER BY apellido, nombre";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + termino.toLowerCase() + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            stmt.setString(3, patron);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public Optional<Alumno> buscarPorDNI(String dni) {
        String sql = "SELECT * FROM alumnos WHERE dni = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por DNI: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Alumno> buscarPorCarrera(String carrera) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE LOWER(carrera) = ? " +
                    "ORDER BY apellido, nombre";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, carrera.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por carrera: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public List<Alumno> buscarPorEstado(String estado) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE LOWER(estado) = ? " +
                    "ORDER BY apellido, nombre";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por estado: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public List<Alumno> buscarPorCiclo(int ciclo) {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE ciclo = ? " +
                    "ORDER BY apellido, nombre";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ciclo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por ciclo: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) as total FROM alumnos";
        
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar alumnos: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public int contarPorEstado(String estado) {
        String sql = "SELECT COUNT(*) as total FROM alumnos WHERE LOWER(estado) = ?";
        
        try (Connection conn = getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar por estado: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public List<Alumno> obtenerEnRiesgoAcademico() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE promedio < 11.0 AND estado = 'Activo' " +
                    "ORDER BY promedio ASC";
        
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                alumnos.add(construirAlumno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos en riesgo: " + e.getMessage());
        }
        
        return alumnos;
    }
    
    @Override
    public boolean existe(String codigoAlumno) {
        return buscarPorCodigo(codigoAlumno).isPresent();
    }
    
    @Override
    public boolean existePorDNI(String dni) {
        return buscarPorDNI(dni).isPresent();
    }
    
    @Override
    public void limpiar() {
        String sql = "DELETE FROM alumnos";
        
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("✓ Base de datos limpiada");
            
        } catch (SQLException e) {
            System.err.println("Error al limpiar base de datos: " + e.getMessage());
        }
    }
    
    @Override
    public String obtenerFuenteDatos() {
        return "MySQL Database (" + URL + ")";
    }
    
    // Método auxiliar para construir objeto Alumno desde ResultSet
    private Alumno construirAlumno(ResultSet rs) throws SQLException {
        return new Alumno(
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("dni"),
            rs.getString("fecha_nacimiento"),
            rs.getString("direccion"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getString("codigo_alumno"),
            rs.getString("carrera"),
            rs.getInt("ciclo"),
            rs.getDouble("promedio"),
            rs.getString("estado"),
            rs.getString("fecha_ingreso"),
            rs.getInt("creditos_acumulados")
        );
    }
}

/*
 * ============================================================================
 * SCRIPT SQL PARA CREAR LA BASE DE DATOS
 * ============================================================================
 * 
 * Ejecuta este script en MySQL para crear la base de datos y la tabla:
 * 
 * -- Crear la base de datos
 * CREATE DATABASE IF NOT EXISTS sideral_carreon
 * CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
 * 
 * USE sideral_carreon;
 * 
 * -- Crear la tabla de alumnos
 * CREATE TABLE IF NOT EXISTS alumnos (
 *     id INT AUTO_INCREMENT PRIMARY KEY,
 *     codigo_alumno VARCHAR(10) NOT NULL UNIQUE,
 *     nombre VARCHAR(100) NOT NULL,
 *     apellido VARCHAR(100) NOT NULL,
 *     dni VARCHAR(8) NOT NULL UNIQUE,
 *     fecha_nacimiento DATE,
 *     direccion VARCHAR(255),
 *     telefono VARCHAR(9),
 *     email VARCHAR(100) NOT NULL,
 *     carrera VARCHAR(100) NOT NULL,
 *     ciclo INT NOT NULL CHECK (ciclo BETWEEN 1 AND 12),
 *     promedio DECIMAL(4,2) NOT NULL CHECK (promedio BETWEEN 0 AND 20),
 *     estado ENUM('Activo', 'Inactivo', 'Egresado', 'Retirado') DEFAULT 'Activo',
 *     fecha_ingreso DATE NOT NULL,
 *     creditos_acumulados INT DEFAULT 0,
 *     fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *     fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 *     INDEX idx_codigo (codigo_alumno),
 *     INDEX idx_dni (dni),
 *     INDEX idx_carrera (carrera),
 *     INDEX idx_estado (estado),
 *     INDEX idx_nombre (nombre, apellido)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 * 
 * -- Insertar datos de prueba
 * INSERT INTO alumnos (codigo_alumno, nombre, apellido, dni, fecha_nacimiento, 
 *                     direccion, telefono, email, carrera, ciclo, promedio, 
 *                     estado, fecha_ingreso, creditos_acumulados) VALUES
 * ('2020001', 'Juan Carlos', 'Pérez García', '12345678', '2000-05-15',
 *  'Av. Principal 123, Lima', '987654321', 'juan.perez@sideral.edu.pe',
 *  'Ingeniería de Sistemas', 5, 15.5, 'Activo', '2020-03-01', 120),
 * 
 * ('2021002', 'María Fernanda', 'García López', '87654321', '2001-08-20',
 *  'Calle Secundaria 456, Lima', '912345678', 'maria.garcia@sideral.edu.pe',
 *  'Administración de Empresas', 3, 16.2, 'Activo', '2021-03-01', 75);
 * 
 * ============================================================================
 */