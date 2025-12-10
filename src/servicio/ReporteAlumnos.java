package src.servicio;

import java.time.LocalDate;
import java.util.List;
import src.modelo.*;

class ReporteAlumnos {
    private List<Alumno> alumnos;
    private FiltroReporte filtro;
    private LocalDate fechaGeneracion;
    
    public ReporteAlumnos(List<Alumno> alumnos, FiltroReporte filtro) {
        this.alumnos = alumnos;
        this.filtro = filtro;
        this.fechaGeneracion = LocalDate.now();
    }
    
    public List<Alumno> getAlumnos() { return alumnos; }
    public FiltroReporte getFiltro() { return filtro; }
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    
    public int getCantidad() { return alumnos.size(); }
    
    public double getPromedioGeneral() {
        return alumnos.stream()
                .mapToDouble(Alumno::getPromedio)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE ALUMNOS ===\n");
        sb.append("Fecha: ").append(fechaGeneracion).append("\n");
        sb.append("Total de alumnos: ").append(alumnos.size()).append("\n");
        sb.append("Promedio general: ").append(String.format("%.2f", getPromedioGeneral())).append("\n\n");
        
        alumnos.forEach(a -> {
            sb.append(a.obtenerResumen()).append("\n\n");
        });
        
        return sb.toString();
    }
}