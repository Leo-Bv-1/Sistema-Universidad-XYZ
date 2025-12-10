package src.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import src.modelo.*;;

/**
 * Implementación en memoria del repositorio de Alumnos
 * Esta clase puede ser fácilmente reemplazada por una implementación con base de datos
 */
public class AlumnoRepositoryMemoria implements IAlumnoRepository {
    
    private List<Alumno> alumnos;
    private static AlumnoRepositoryMemoria instancia;
    
    // Constructor privado (Singleton)
    private AlumnoRepositoryMemoria() {
        this.alumnos = new ArrayList<>();
        cargarDatosIniciales();
    }
    
    // Método para obtener la instancia única
    public static synchronized AlumnoRepositoryMemoria getInstancia() {
        if (instancia == null) {
            instancia = new AlumnoRepositoryMemoria();
        }
        return instancia;
    }
    
    // Cargar datos de prueba
    private void cargarDatosIniciales() {
        alumnos.add(new Alumno(
            "Juan Carlos", "Pérez García", "12345678", "2000-05-15",
            "Av. Principal 123, Lima", "987654321", "juan.perez@sideral.edu.pe",
            "2020001", "Ingeniería de Sistemas", 5, 15.5, "Activo", "2020-03-01", 120
        ));
        
        alumnos.add(new Alumno(
            "María Fernanda", "García López", "87654321", "2001-08-20",
            "Calle Secundaria 456, Lima", "912345678", "maria.garcia@sideral.edu.pe",
            "2021002", "Administración de Empresas", 3, 16.2, "Activo", "2021-03-01", 75
        ));
        
        alumnos.add(new Alumno(
            "Carlos Alberto", "López Martínez", "11223344", "1999-12-10",
            "Jr. Los Álamos 789, Lima", "998877665", "carlos.lopez@sideral.edu.pe",
            "2019003", "Derecho", 7, 14.8, "Activo", "2019-03-01", 165
        ));
        
        alumnos.add(new Alumno(
            "Ana Sofía", "Rodríguez Sánchez", "44556677", "2002-03-25",
            "Av. Universitaria 321, Lima", "955443322", "ana.rodriguez@sideral.edu.pe",
            "2022004", "Medicina", 2, 17.3, "Activo", "2022-03-01", 45
        ));
        
        alumnos.add(new Alumno(
            "Pedro Miguel", "Torres Flores", "99887766", "2001-11-05",
            "Calle Los Pinos 654, Lima", "966554433", "pedro.torres@sideral.edu.pe",
            "2021005", "Ingeniería Civil", 4, 13.9, "Activo", "2021-03-01", 90
        ));
        
        alumnos.add(new Alumno(
            "Lucía Isabel", "Díaz Romero", "55667788", "2000-07-18",
            "Jr. Las Flores 147, Lima", "977665544", "lucia.diaz@sideral.edu.pe",
            "2020006", "Psicología", 6, 10.5, "Activo", "2020-03-01", 135
        ));
    }
    
    @Override
    public boolean guardar(Alumno alumno) {
        if (alumno == null || !alumno.validarDatos()) {
            return false;
        }
        
        // Verificar que no exista ya
        if (existe(alumno.getCodigoAlumno())) {
            return false;
        }
        
        // Verificar que el DNI no esté duplicado
        if (existePorDNI(alumno.getDni())) {
            return false;
        }
        
        return alumnos.add(alumno);
    }
    
    @Override
    public boolean actualizar(Alumno alumno) {
        if (alumno == null || !alumno.validarDatos()) {
            return false;
        }
        
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getCodigoAlumno().equals(alumno.getCodigoAlumno())) {
                // Verificar que el DNI no esté duplicado en otro alumno
                Alumno alumnoExistente = buscarPorDNI(alumno.getDni()).orElse(null);
                if (alumnoExistente != null && 
                    !alumnoExistente.getCodigoAlumno().equals(alumno.getCodigoAlumno())) {
                    return false;
                }
                
                alumnos.set(i, alumno);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean eliminar(String codigoAlumno) {
        return alumnos.removeIf(a -> a.getCodigoAlumno().equals(codigoAlumno));
    }
    
    @Override
    public Optional<Alumno> buscarPorCodigo(String codigoAlumno) {
        return alumnos.stream()
                .filter(a -> a.getCodigoAlumno().equals(codigoAlumno))
                .findFirst();
    }
    
    @Override
    public List<Alumno> obtenerTodos() {
        return new ArrayList<>(alumnos);
    }
    
    @Override
    public List<Alumno> buscarPorNombre(String termino) {
        String terminoLower = termino.toLowerCase();
        return alumnos.stream()
                .filter(a -> a.getNombre().toLowerCase().contains(terminoLower) ||
                           a.getApellido().toLowerCase().contains(terminoLower) ||
                           a.getNombreCompleto().toLowerCase().contains(terminoLower))
                .sorted(Comparator.comparing(Alumno::getApellido)
                       .thenComparing(Alumno::getNombre))
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Alumno> buscarPorDNI(String dni) {
        return alumnos.stream()
                .filter(a -> a.getDni().equals(dni))
                .findFirst();
    }
    
    @Override
    public List<Alumno> buscarPorCarrera(String carrera) {
        return alumnos.stream()
                .filter(a -> a.getCarrera().equalsIgnoreCase(carrera))
                .sorted(Comparator.comparing(Alumno::getApellido))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Alumno> buscarPorEstado(String estado) {
        return alumnos.stream()
                .filter(a -> a.getEstado().equalsIgnoreCase(estado))
                .sorted(Comparator.comparing(Alumno::getApellido))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Alumno> buscarPorCiclo(int ciclo) {
        return alumnos.stream()
                .filter(a -> a.getCiclo() == ciclo)
                .sorted(Comparator.comparing(Alumno::getApellido))
                .collect(Collectors.toList());
    }
    
    @Override
    public int contarTotal() {
        return alumnos.size();
    }
    
    @Override
    public int contarPorEstado(String estado) {
        return (int) alumnos.stream()
                .filter(a -> a.getEstado().equalsIgnoreCase(estado))
                .count();
    }
    
    @Override
    public List<Alumno> obtenerEnRiesgoAcademico() {
        return alumnos.stream()
                .filter(Alumno::enRiesgoAcademico)
                .sorted(Comparator.comparing(Alumno::getPromedio))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existe(String codigoAlumno) {
        return alumnos.stream()
                .anyMatch(a -> a.getCodigoAlumno().equals(codigoAlumno));
    }
    
    @Override
    public boolean existePorDNI(String dni) {
        return alumnos.stream()
                .anyMatch(a -> a.getDni().equals(dni));
    }
    
    @Override
    public void limpiar() {
        alumnos.clear();
    }
    
    @Override
    public String obtenerFuenteDatos() {
        return "Memoria (ArrayList)";
    }
    
    // Métodos adicionales útiles
    
    /**
     * Obtiene alumnos ordenados por promedio descendente
     */
    public List<Alumno> obtenerMejoresPromedios(int limite) {
        return alumnos.stream()
                .filter(a -> a.getEstado().equals("Activo"))
                .sorted(Comparator.comparing(Alumno::getPromedio).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene estadísticas generales
     */
    public EstadisticasAlumnos obtenerEstadisticas() {
        int total = contarTotal();
        int activos = contarPorEstado("Activo");
        int enRiesgo = obtenerEnRiesgoAcademico().size();
        
        double promedioGeneral = alumnos.stream()
                .filter(a -> a.getEstado().equals("Activo"))
                .mapToDouble(Alumno::getPromedio)
                .average()
                .orElse(0.0);
        
        return new EstadisticasAlumnos(total, activos, enRiesgo, promedioGeneral);
    }
    
    /**
     * Clase interna para estadísticas
     */
    public static class EstadisticasAlumnos {
        private int total;
        private int activos;
        private int enRiesgo;
        private double promedioGeneral;
        
        public EstadisticasAlumnos(int total, int activos, int enRiesgo, double promedioGeneral) {
            this.total = total;
            this.activos = activos;
            this.enRiesgo = enRiesgo;
            this.promedioGeneral = promedioGeneral;
        }
        
        public int getTotal() { return total; }
        public int getActivos() { return activos; }
        public int getEnRiesgo() { return enRiesgo; }
        public double getPromedioGeneral() { return promedioGeneral; }
        
        @Override
        public String toString() {
            return String.format(
                "Estadísticas:\n" +
                "- Total de alumnos: %d\n" +
                "- Alumnos activos: %d\n" +
                "- Alumnos en riesgo: %d\n" +
                "- Promedio general: %.2f",
                total, activos, enRiesgo, promedioGeneral
            );
        }
    }
}