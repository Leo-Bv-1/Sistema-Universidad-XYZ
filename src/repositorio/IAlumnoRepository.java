package src.repositorio;

import java.util.List;
import java.util.Optional;
import src.modelo.*;;;

/**
 * Interface para el repositorio de Alumnos
 * Esta interface define el contrato para el acceso a datos
 * Permite implementaciones con diferentes fuentes de datos (memoria, BD, archivo, etc.)
 */
public interface IAlumnoRepository {
    
    // Operaciones CRUD básicas
    
    /**
     * Guarda un nuevo alumno en el repositorio
     * @param alumno El alumno a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     */
    boolean guardar(Alumno alumno);
    
    /**
     * Actualiza un alumno existente en el repositorio
     * @param alumno El alumno con los datos actualizados
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    boolean actualizar(Alumno alumno);
    
    /**
     * Elimina un alumno del repositorio por su código
     * @param codigoAlumno El código del alumno a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    boolean eliminar(String codigoAlumno);
    
    /**
     * Busca un alumno por su código
     * @param codigoAlumno El código del alumno a buscar
     * @return Optional con el alumno si existe, Optional.empty() si no existe
     */
    Optional<Alumno> buscarPorCodigo(String codigoAlumno);
    
    /**
     * Obtiene todos los alumnos del repositorio
     * @return Lista con todos los alumnos
     */
    List<Alumno> obtenerTodos();
    
    // Métodos de búsqueda avanzada
    
    /**
     * Busca alumnos por nombre o apellido (búsqueda parcial)
     * @param termino Término de búsqueda
     * @return Lista de alumnos que coinciden con el término
     */
    List<Alumno> buscarPorNombre(String termino);
    
    /**
     * Busca alumnos por DNI
     * @param dni DNI del alumno
     * @return Optional con el alumno si existe
     */
    Optional<Alumno> buscarPorDNI(String dni);
    
    /**
     * Busca alumnos por carrera
     * @param carrera Nombre de la carrera
     * @return Lista de alumnos de esa carrera
     */
    List<Alumno> buscarPorCarrera(String carrera);
    
    /**
     * Busca alumnos por estado
     * @param estado Estado del alumno (Activo, Inactivo, etc.)
     * @return Lista de alumnos con ese estado
     */
    List<Alumno> buscarPorEstado(String estado);
    
    /**
     * Busca alumnos por ciclo
     * @param ciclo Número de ciclo
     * @return Lista de alumnos en ese ciclo
     */
    List<Alumno> buscarPorCiclo(int ciclo);
    
    // Métodos de estadísticas y conteo
    
    /**
     * Cuenta el total de alumnos en el repositorio
     * @return Número total de alumnos
     */
    int contarTotal();
    
    /**
     * Cuenta los alumnos activos
     * @return Número de alumnos activos
     */
    int contarPorEstado(String estado);
    
    /**
     * Obtiene alumnos en riesgo académico
     * @return Lista de alumnos en riesgo
     */
    List<Alumno> obtenerEnRiesgoAcademico();
    
    // Métodos de validación
    
    /**
     * Verifica si existe un alumno con el código dado
     * @param codigoAlumno Código a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existe(String codigoAlumno);
    
    /**
     * Verifica si existe un alumno con el DNI dado
     * @param dni DNI a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorDNI(String dni);
    
    // Métodos de mantenimiento
    
    /**
     * Limpia todos los datos del repositorio
     * Útil para testing o reiniciar el sistema
     */
    void limpiar();
    
    /**
     * Obtiene la fuente de datos actual
     * @return Descripción de la fuente de datos
     */
    String obtenerFuenteDatos();
}