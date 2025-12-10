package src.servicio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import src.repositorio.*;
import src.modelo.*;
import src.controlador.*;


/**
 * Capa de servicio que contiene la lógica de negocio adicional
 * Esta clase complementa al controlador con operaciones más complejas
 */
public class AlumnoService {
    
    private IAlumnoRepository repositorio;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public AlumnoService(IAlumnoRepository repositorio) {
        this.repositorio = repositorio;
    }
    
    public AlumnoService() {
        this.repositorio = AlumnoRepositoryMemoria.getInstancia();
    }
    
    // ==================== VALIDACIONES DE NEGOCIO ====================
    
    /**
     * Valida que un código de alumno tenga el formato correcto
     * Formato esperado: 4 dígitos del año + 3 dígitos secuenciales (ej: 2024001)
     */
    public boolean validarFormatoCodigoAlumno(String codigo) {
        if (codigo == null || codigo.length() != 7) {
            return false;
        }
        return codigo.matches("\\d{7}");
    }
    
    /**
     * Genera un código de alumno único
     */
    public String generarCodigoAlumno() {
        int anioActual = LocalDate.now().getYear();
        List<Alumno> alumnos = repositorio.obtenerTodos();
        
        // Filtrar alumnos del año actual
        String prefijo = String.valueOf(anioActual);
        long cantidad = alumnos.stream()
                .filter(a -> a.getCodigoAlumno().startsWith(prefijo))
                .count();
        
        int siguiente = (int) cantidad + 1;
        return String.format("%d%03d", anioActual, siguiente);
    }
    
    /**
     * Valida que una fecha tenga el formato correcto
     */
    public boolean validarFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(fecha, FORMATO_FECHA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Calcula la edad de una persona a partir de su fecha de nacimiento
     */
    public int calcularEdad(String fechaNacimiento) {
        try {
            LocalDate fechaNac = LocalDate.parse(fechaNacimiento, FORMATO_FECHA);
            LocalDate ahora = LocalDate.now();
            return ahora.getYear() - fechaNac.getYear();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Valida que un alumno tenga la edad mínima para estudios universitarios
     */
    public boolean validarEdadMinima(String fechaNacimiento, int edadMinima) {
        int edad = calcularEdad(fechaNacimiento);
        return edad >= edadMinima;
    }
    
    // ==================== OPERACIONES DE NEGOCIO ====================
    
    /**
     * Procesa la matrícula de un alumno
     */
    public RespuestaOperacion procesarMatricula(Alumno alumno, int creditosMatriculados) {
        try {
            if (alumno == null) {
                return new RespuestaOperacion(false, "Alumno no válido");
            }
            
            // Verificar que el alumno esté activo
            if (!alumno.getEstado().equals("Activo")) {
                return new RespuestaOperacion(false, 
                    "El alumno debe estar activo para matricularse");
            }
            
            // Verificar límite de créditos por ciclo (típicamente 22-24)
            if (creditosMatriculados > 24) {
                return new RespuestaOperacion(false, 
                    "No se puede matricular más de 24 créditos por ciclo");
            }
            
            // Si está en riesgo académico, limitar créditos
            if (alumno.enRiesgoAcademico() && creditosMatriculados > 18) {
                return new RespuestaOperacion(false, 
                    "Alumno en riesgo académico. Máximo 18 créditos permitidos");
            }
            
            return new RespuestaOperacion(true, 
                String.format("Matrícula procesada: %d créditos para %s",
                    creditosMatriculados, alumno.getNombreCompleto()));
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al procesar matrícula: " + e.getMessage());
        }
    }
    
    /**
     * Evalúa si un alumno puede graduarse
     */
    public RespuestaOperacion evaluarGraduacion(String codigoAlumno, int creditosRequeridos) {
        try {
            Alumno alumno = repositorio.buscarPorCodigo(codigoAlumno).orElse(null);
            
            if (alumno == null) {
                return new RespuestaOperacion(false, 
                    "Alumno no encontrado");
            }
            
            // Verificar requisitos de graduación
            boolean cumpleCreditos = alumno.getCreditosAcumulados() >= creditosRequeridos;
            boolean cumplePromedio = alumno.getPromedio() >= 11.0;
            boolean estaActivo = alumno.getEstado().equals("Activo");
            
            if (cumpleCreditos && cumplePromedio && estaActivo) {
                return new RespuestaOperacion(true, 
                    String.format("%s cumple todos los requisitos para graduarse. " +
                                 "Créditos: %d/%d, Promedio: %.2f",
                                 alumno.getNombreCompleto(),
                                 alumno.getCreditosAcumulados(),
                                 creditosRequeridos,
                                 alumno.getPromedio()),
                    alumno);
            } else {
                StringBuilder razones = new StringBuilder("No cumple requisitos:\n");
                if (!cumpleCreditos) {
                    razones.append(String.format("- Faltan %d créditos\n",
                        creditosRequeridos - alumno.getCreditosAcumulados()));
                }
                if (!cumplePromedio) {
                    razones.append(String.format("- Promedio insuficiente (%.2f < 11.0)\n",
                        alumno.getPromedio()));
                }
                if (!estaActivo) {
                    razones.append("- Estado no activo\n");
                }
                
                return new RespuestaOperacion(false, razones.toString());
            }
            
        } catch (Exception e) {
            return new RespuestaOperacion(false, 
                "Error al evaluar graduación: " + e.getMessage());
        }
    }
    
    /**
     * Genera un reporte de alumnos con filtros
     */
    public ReporteAlumnos generarReporte(FiltroReporte filtro) {
        List<Alumno> alumnos = repositorio.obtenerTodos();
        
        // Aplicar filtros
        if (filtro.getCarrera() != null) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getCarrera().equalsIgnoreCase(filtro.getCarrera()))
                    .collect(Collectors.toList());
        }
        
        if (filtro.getEstado() != null) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getEstado().equalsIgnoreCase(filtro.getEstado()))
                    .collect(Collectors.toList());
        }
        
        if (filtro.getCicloMinimo() > 0) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getCiclo() >= filtro.getCicloMinimo())
                    .collect(Collectors.toList());
        }
        
        if (filtro.getPromedioMinimo() > 0) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getPromedio() >= filtro.getPromedioMinimo())
                    .collect(Collectors.toList());
        }
        
        return new ReporteAlumnos(alumnos, filtro);
    }
    
    /**
     * Identifica alumnos que necesitan tutoría
     */
    public List<Alumno> identificarAlumnosParaTutoria() {
        return repositorio.obtenerTodos().stream()
                .filter(a -> a.getEstado().equals("Activo"))
                .filter(a -> a.getPromedio() < 13.0)
                .sorted((a1, a2) -> Double.compare(a1.getPromedio(), a2.getPromedio()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene alumnos destacados (top performers)
     */
    public List<Alumno> obtenerAlumnosDestacados(int limite) {
        return repositorio.obtenerTodos().stream()
                .filter(a -> a.getEstado().equals("Activo"))
                .sorted((a1, a2) -> Double.compare(a2.getPromedio(), a1.getPromedio()))
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene distribución de alumnos por carrera
     */
    public String obtenerDistribucionPorCarrera() {
        List<Alumno> alumnos = repositorio.obtenerTodos();
        
        // Agrupar por carrera y contar
        var distribucion = alumnos.stream()
                .collect(Collectors.groupingBy(
                        Alumno::getCarrera,
                        Collectors.counting()
                ));
        
        StringBuilder reporte = new StringBuilder("=== DISTRIBUCIÓN POR CARRERA ===\n");
        distribucion.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    double porcentaje = (entry.getValue() * 100.0) / alumnos.size();
                    reporte.append(String.format("%-30s: %3d (%.1f%%)\n",
                            entry.getKey(), entry.getValue(), porcentaje));
                });
        
        return reporte.toString();
    }
    
    /**
     * Valida integridad de datos del alumno
     */
    public ValidationResult validarIntegridadDatos(Alumno alumno) {
        ValidationResult result = new ValidationResult();
        
        if (alumno == null) {
            result.agregarError("El alumno es nulo");
            return result;
        }
        
        // Validar datos personales
        if (alumno.getNombre() == null || alumno.getNombre().trim().isEmpty()) {
            result.agregarError("El nombre es obligatorio");
        }
        
        if (alumno.getApellido() == null || alumno.getApellido().trim().isEmpty()) {
            result.agregarError("El apellido es obligatorio");
        }
        
        if (alumno.getDni() == null || !alumno.getDni().matches("\\d{8}")) {
            result.agregarError("DNI inválido (debe tener 8 dígitos)");
        }
        
        if (alumno.getEmail() == null || !alumno.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            result.agregarError("Email inválido");
        }
        
        // Validar datos académicos
        if (alumno.getCodigoAlumno() == null || alumno.getCodigoAlumno().trim().isEmpty()) {
            result.agregarError("Código de alumno es obligatorio");
        }
        
        if (alumno.getCarrera() == null || alumno.getCarrera().trim().isEmpty()) {
            result.agregarError("La carrera es obligatoria");
        }
        
        if (alumno.getCiclo() < 1 || alumno.getCiclo() > 12) {
            result.agregarError("Ciclo inválido (debe estar entre 1 y 12)");
        }
        
        if (alumno.getPromedio() < 0 || alumno.getPromedio() > 20) {
            result.agregarError("Promedio inválido (debe estar entre 0 y 20)");
        }
        
        return result;
    }
}