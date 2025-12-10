package src.controlador;

import java.util.List;
import java.util.Optional;
import src.repositorio.*;
import src.modelo.*;;

/**
 * Controlador principal para la gestión de Alumnos
 * Maneja toda la lógica de negocio y validaciones
 * Actúa como intermediario entre la vista y el repositorio
 */
public class AlumnoController {
    
    private IAlumnoRepository repositorio;
    
    // Constructor que permite inyección de dependencias
    public AlumnoController(IAlumnoRepository repositorio) {
        this.repositorio = repositorio;
    }
    
    // Constructor por defecto (usa implementación en memoria)
    public AlumnoController() {
        this.repositorio = AlumnoRepositoryMemoria.getInstancia();
    }
    
    // ==================== OPERACIONES CRUD ====================
    
    /**
     * Crear un nuevo alumno
     * Valida todos los datos antes de guardar
     */
    public RespuestaOperacion crearAlumno(String codigoAlumno, String nombre, String apellido, 
                                          String dni, String fechaNacimiento, String direccion,
                                          String telefono, String email, String carrera, 
                                          int ciclo, double promedio, String fechaIngreso) {
        try {
            // Validaciones de negocio
            if (codigoAlumno == null || codigoAlumno.trim().isEmpty()) {
                return new RespuestaOperacion(false, "El código de alumno es obligatorio");
            }
            
            if (nombre == null || nombre.trim().isEmpty()) {
                return new RespuestaOperacion(false, "El nombre es obligatorio");
            }
            
            if (apellido == null || apellido.trim().isEmpty()) {
                return new RespuestaOperacion(false, "El apellido es obligatorio");
            }
            
            if (dni == null || !dni.matches("\\d{8}")) {
                return new RespuestaOperacion(false, "El DNI debe tener 8 dígitos");
            }
            
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return new RespuestaOperacion(false, "El email no tiene un formato válido");
            }
            
            if (carrera == null || carrera.trim().isEmpty()) {
                return new RespuestaOperacion(false, "La carrera es obligatoria");
            }
            
            if (ciclo < 1 || ciclo > 12) {
                return new RespuestaOperacion(false, "El ciclo debe estar entre 1 y 12");
            }
            
            if (promedio < 0 || promedio > 20) {
                return new RespuestaOperacion(false, "El promedio debe estar entre 0 y 20");
            }
            
            // Verificar duplicados
            if (repositorio.existe(codigoAlumno)) {
                return new RespuestaOperacion(false, 
                    "Ya existe un alumno con el código: " + codigoAlumno);
            }
            
            if (repositorio.existePorDNI(dni)) {
                return new RespuestaOperacion(false, 
                    "Ya existe un alumno con el DNI: " + dni);
            }
            
            // Crear el alumno
            Alumno nuevoAlumno = new Alumno(
                nombre.trim(), apellido.trim(), dni.trim(), fechaNacimiento,
                direccion, telefono, email.trim().toLowerCase(),
                codigoAlumno.trim().toUpperCase(), carrera.trim(), ciclo, 
                promedio, "Activo", fechaIngreso, 0
            );
            
            // Guardar en el repositorio
            if (repositorio.guardar(nuevoAlumno)) {
                return new RespuestaOperacion(true, 
                    "Alumno creado exitosamente: " + nuevoAlumno.getNombreCompleto(),
                    nuevoAlumno);
            } else {
                return new RespuestaOperacion(false, 
                    "No se pudo guardar el alumno. Verifique los datos.");
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error inesperado al crear alumno: " + e.getMessage());
        }
    }
    
    /**
     * Modificar un alumno existente
     */
    public RespuestaOperacion modificarAlumno(String codigoAlumno, String nombre, String apellido,
                                              String dni, String fechaNacimiento, String direccion,
                                              String telefono, String email, String carrera,
                                              int ciclo, double promedio, String estado, 
                                              String fechaIngreso, int creditosAcumulados) {
        try {
            // Verificar que el alumno existe
            Optional<Alumno> alumnoExistente = repositorio.buscarPorCodigo(codigoAlumno);
            if (!alumnoExistente.isPresent()) {
                return new RespuestaOperacion(false, 
                    "No se encontró un alumno con el código: " + codigoAlumno);
            }
            
            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                return new RespuestaOperacion(false, "El nombre es obligatorio");
            }
            
            if (apellido == null || apellido.trim().isEmpty()) {
                return new RespuestaOperacion(false, "El apellido es obligatorio");
            }
            
            if (dni == null || !dni.matches("\\d{8}")) {
                return new RespuestaOperacion(false, "El DNI debe tener 8 dígitos");
            }
            
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return new RespuestaOperacion(false, "El email no tiene un formato válido");
            }
            
            if (ciclo < 1 || ciclo > 12) {
                return new RespuestaOperacion(false, "El ciclo debe estar entre 1 y 12");
            }
            
            if (promedio < 0 || promedio > 20) {
                return new RespuestaOperacion(false, "El promedio debe estar entre 0 y 20");
            }
            
            // Crear alumno actualizado
            Alumno alumnoActualizado = new Alumno(
                nombre.trim(), apellido.trim(), dni.trim(), fechaNacimiento,
                direccion, telefono, email.trim().toLowerCase(),
                codigoAlumno.trim().toUpperCase(), carrera.trim(), ciclo,
                promedio, estado, fechaIngreso, creditosAcumulados
            );
            
            // Actualizar en el repositorio
            if (repositorio.actualizar(alumnoActualizado)) {
                return new RespuestaOperacion(true, 
                    "Alumno actualizado exitosamente: " + alumnoActualizado.getNombreCompleto(),
                    alumnoActualizado);
            } else {
                return new RespuestaOperacion(false, 
                    "No se pudo actualizar el alumno. Verifique los datos.");
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error inesperado al modificar alumno: " + e.getMessage());
        }
    }
    
    /**
     * Eliminar un alumno
     */
    public RespuestaOperacion eliminarAlumno(String codigoAlumno) {
        try {
            if (codigoAlumno == null || codigoAlumno.trim().isEmpty()) {
                return new RespuestaOperacion(false, 
                    "Debe proporcionar un código de alumno válido");
            }
            
            // Verificar que existe
            Optional<Alumno> alumno = repositorio.buscarPorCodigo(codigoAlumno);
            if (!alumno.isPresent()) {
                return new RespuestaOperacion(false, 
                    "No se encontró un alumno con el código: " + codigoAlumno);
            }
            
            String nombreCompleto = alumno.get().getNombreCompleto();
            
            // Eliminar
            if (repositorio.eliminar(codigoAlumno)) {
                return new RespuestaOperacion(true, 
                    "Alumno eliminado exitosamente: " + nombreCompleto);
            } else {
                return new RespuestaOperacion(false, 
                    "No se pudo eliminar el alumno");
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error inesperado al eliminar alumno: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIONES DE BÚSQUEDA ====================
    
    /**
     * Buscar alumno por código
     */
    public RespuestaOperacion buscarPorCodigo(String codigoAlumno) {
        try {
            if (codigoAlumno == null || codigoAlumno.trim().isEmpty()) {
                return new RespuestaOperacion(false, 
                    "Debe proporcionar un código de alumno");
            }
            
            Optional<Alumno> alumno = repositorio.buscarPorCodigo(codigoAlumno);
            
            if (alumno.isPresent()) {
                return new RespuestaOperacion(true, 
                    "Alumno encontrado", alumno.get());
            } else {
                return new RespuestaOperacion(false, 
                    "No se encontró un alumno con el código: " + codigoAlumno);
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al buscar alumno: " + e.getMessage());
        }
    }
    
    /**
     * Buscar alumnos por nombre (búsqueda parcial)
     */
    public RespuestaOperacion buscarPorNombre(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return new RespuestaOperacion(false, 
                    "Debe proporcionar un término de búsqueda");
            }
            
            List<Alumno> alumnos = repositorio.buscarPorNombre(nombre);
            
            if (alumnos.isEmpty()) {
                return new RespuestaOperacion(false, 
                    "No se encontraron alumnos con el término: " + nombre);
            }
            
            return new RespuestaOperacion(true, 
                "Se encontraron " + alumnos.size() + " alumno(s)", alumnos);
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al buscar alumnos: " + e.getMessage());
        }
    }
    
    /**
     * Buscar alumno por DNI
     */
    public RespuestaOperacion buscarPorDNI(String dni) {
        try {
            if (dni == null || dni.trim().isEmpty()) {
                return new RespuestaOperacion(false, "Debe proporcionar un DNI");
            }
            
            Optional<Alumno> alumno = repositorio.buscarPorDNI(dni);
            
            if (alumno.isPresent()) {
                return new RespuestaOperacion(true, 
                    "Alumno encontrado", alumno.get());
            } else {
                return new RespuestaOperacion(false, 
                    "No se encontró un alumno con el DNI: " + dni);
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al buscar alumno: " + e.getMessage());
        }
    }
    
    /**
     * Obtener todos los alumnos
     */
    public RespuestaOperacion obtenerTodos() {
        try {
            List<Alumno> alumnos = repositorio.obtenerTodos();
            
            if (alumnos.isEmpty()) {
                return new RespuestaOperacion(false, 
                    "No hay alumnos registrados en el sistema");
            }
            
            return new RespuestaOperacion(true, 
                "Total de alumnos: " + alumnos.size(), alumnos);
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al obtener alumnos: " + e.getMessage());
        }
    }
    
    /**
     * Buscar alumnos por carrera
     */
    public RespuestaOperacion buscarPorCarrera(String carrera) {
        try {
            List<Alumno> alumnos = repositorio.buscarPorCarrera(carrera);
            
            if (alumnos.isEmpty()) {
                return new RespuestaOperacion(false, 
                    "No se encontraron alumnos en la carrera: " + carrera);
            }
            
            return new RespuestaOperacion(true, 
                "Se encontraron " + alumnos.size() + " alumno(s) en " + carrera, 
                alumnos);
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al buscar por carrera: " + e.getMessage());
        }
    }
    
    /**
     * Buscar alumnos por estado
     */
    public RespuestaOperacion buscarPorEstado(String estado) {
        try {
            List<Alumno> alumnos = repositorio.buscarPorEstado(estado);
            
            if (alumnos.isEmpty()) {
                return new RespuestaOperacion(false, 
                    "No se encontraron alumnos con estado: " + estado);
            }
            
            return new RespuestaOperacion(true, 
                "Se encontraron " + alumnos.size() + " alumno(s) " + estado.toLowerCase() + "(s)",
                alumnos);
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al buscar por estado: " + e.getMessage());
        }
    }
    
    // ==================== ESTADÍSTICAS ====================
    
    /**
     * Obtener estadísticas generales
     */
    public EstadisticasDTO obtenerEstadisticas() {
        int total = repositorio.contarTotal();
        int activos = repositorio.contarPorEstado("Activo");
        int inactivos = repositorio.contarPorEstado("Inactivo");
        int egresados = repositorio.contarPorEstado("Egresado");
        int enRiesgo = repositorio.obtenerEnRiesgoAcademico().size();
        
        // Si usamos la implementación en memoria, podemos obtener más datos
        double promedioGeneral = 0.0;
        if (repositorio instanceof AlumnoRepositoryMemoria) {
            AlumnoRepositoryMemoria repo = (AlumnoRepositoryMemoria) repositorio;
            promedioGeneral = repo.obtenerEstadisticas().getPromedioGeneral();
        }
        
        return new EstadisticasDTO(total, activos, inactivos, egresados, 
                                  enRiesgo, promedioGeneral);
    }
    
    /**
     * Obtener alumnos en riesgo académico
     */
    public RespuestaOperacion obtenerAlumnosEnRiesgo() {
        try {
            List<Alumno> alumnos = repositorio.obtenerEnRiesgoAcademico();
            
            if (alumnos.isEmpty()) {
                return new RespuestaOperacion(true, 
                    "No hay alumnos en riesgo académico");
            }
            
            return new RespuestaOperacion(true, 
                "Se encontraron " + alumnos.size() + " alumno(s) en riesgo académico",
                alumnos);
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al obtener alumnos en riesgo: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIONES ADICIONALES ====================
    
    /**
     * Cambiar el estado de un alumno
     */
    public RespuestaOperacion cambiarEstado(String codigoAlumno, String nuevoEstado) {
        try {
            Optional<Alumno> alumnoOpt = repositorio.buscarPorCodigo(codigoAlumno);
            
            if (!alumnoOpt.isPresent()) {
                return new RespuestaOperacion(false, 
                    "No se encontró el alumno con código: " + codigoAlumno);
            }
            
            Alumno alumno = alumnoOpt.get();
            String estadoAnterior = alumno.getEstado();
            alumno.cambiarEstado(nuevoEstado);
            
            if (repositorio.actualizar(alumno)) {
                return new RespuestaOperacion(true, 
                    String.format("Estado cambiado de '%s' a '%s' para %s",
                        estadoAnterior, nuevoEstado, alumno.getNombreCompleto()),
                    alumno);
            } else {
                return new RespuestaOperacion(false, 
                    "No se pudo actualizar el estado");
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al cambiar estado: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar el promedio de un alumno
     */
    public RespuestaOperacion actualizarPromedio(String codigoAlumno, double nuevoPromedio) {
        try {
            if (nuevoPromedio < 0 || nuevoPromedio > 20) {
                return new RespuestaOperacion(false, 
                    "El promedio debe estar entre 0 y 20");
            }
            
            Optional<Alumno> alumnoOpt = repositorio.buscarPorCodigo(codigoAlumno);
            
            if (!alumnoOpt.isPresent()) {
                return new RespuestaOperacion(false, 
                    "No se encontró el alumno con código: " + codigoAlumno);
            }
            
            Alumno alumno = alumnoOpt.get();
            double promedioAnterior = alumno.getPromedio();
            alumno.actualizarPromedio(nuevoPromedio);
            
            if (repositorio.actualizar(alumno)) {
                return new RespuestaOperacion(true, 
                    String.format("Promedio actualizado de %.2f a %.2f para %s",
                        promedioAnterior, nuevoPromedio, alumno.getNombreCompleto()),
                    alumno);
            } else {
                return new RespuestaOperacion(false, 
                    "No se pudo actualizar el promedio");
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al actualizar promedio: " + e.getMessage());
        }
    }
    
    /**
     * Obtener información de la fuente de datos
     */
    public String obtenerInfoRepositorio() {
        return "Fuente de datos: " + repositorio.obtenerFuenteDatos() + 
               "\nTotal de registros: " + repositorio.contarTotal();
    }
}