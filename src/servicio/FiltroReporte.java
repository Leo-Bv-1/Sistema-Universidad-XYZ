package src.servicio;

/**
 * Clase para filtros de reporte
 */
class FiltroReporte {
    private String carrera;
    private String estado;
    private int cicloMinimo;
    private double promedioMinimo;
    
    public FiltroReporte() {
        this.cicloMinimo = 0;
        this.promedioMinimo = 0.0;
    }
    
    // Getters y Setters
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getCicloMinimo() { return cicloMinimo; }
    public void setCicloMinimo(int cicloMinimo) { this.cicloMinimo = cicloMinimo; }
    
    public double getPromedioMinimo() { return promedioMinimo; }
    public void setPromedioMinimo(double promedioMinimo) { 
        this.promedioMinimo = promedioMinimo; 
    }
}