package src.controlador;
/**
 * Clase DTO para estadísticas
 */
public class EstadisticasDTO {
    private int total;
    private int activos;
    private int inactivos;
    private int egresados;
    private int enRiesgo;
    private double promedioGeneral;
    
    public EstadisticasDTO(int total, int activos, int inactivos, int egresados,
                          int enRiesgo, double promedioGeneral) {
        this.total = total;
        this.activos = activos;
        this.inactivos = inactivos;
        this.egresados = egresados;
        this.enRiesgo = enRiesgo;
        this.promedioGeneral = promedioGeneral;
    }
    
    // Getters
    public int getTotal() { return total; }
    public int getActivos() { return activos; }
    public int getInactivos() { return inactivos; }
    public int getEgresados() { return egresados; }
    public int getEnRiesgo() { return enRiesgo; }
    public double getPromedioGeneral() { return promedioGeneral; }
    
    @Override
    public String toString() {
        return String.format(
            "=== ESTADÍSTICAS DEL SISTEMA ===\n" +
            "Total de alumnos: %d\n" +
            "├─ Activos: %d (%.1f%%)\n" +
            "├─ Inactivos: %d (%.1f%%)\n" +
            "├─ Egresados: %d (%.1f%%)\n" +
            "├─ En riesgo académico: %d\n" +
            "└─ Promedio general: %.2f",
            total,
            activos, (total > 0 ? (activos * 100.0 / total) : 0),
            inactivos, (total > 0 ? (inactivos * 100.0 / total) : 0),
            egresados, (total > 0 ? (egresados * 100.0 / total) : 0),
            enRiesgo,
            promedioGeneral
        );
    }
}