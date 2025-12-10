package src.modelo;

public class RespuestaOperacion {
    private boolean exito;
    private String mensaje;
    private Object datos;
    
    public RespuestaOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = null;
    }
    
    public RespuestaOperacion(boolean exito, String mensaje, Object datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }
    
    public boolean isExito() {
        return exito;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public Object getDatos() {
        return datos;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getDatosComoTipo(Class<T> tipo) {
        if (datos != null && tipo.isInstance(datos)) {
            return (T) datos;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return (exito ? "✓ " : "✗ ") + mensaje;
    }
}
