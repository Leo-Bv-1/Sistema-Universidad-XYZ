package src.servicio;

import java.util.List;

class ValidationResult {
    private List<String> errores;
    
    public ValidationResult() {
        this.errores = new java.util.ArrayList<>();
    }
    
    public void agregarError(String error) {
        errores.add(error);
    }
    
    public boolean esValido() {
        return errores.isEmpty();
    }
    
    public List<String> getErrores() {
        return new java.util.ArrayList<>(errores);
    }
    
    public String getMensajeErrores() {
        if (errores.isEmpty()) {
            return "Sin errores";
        }
        return String.join("\n", errores);
    }
    
    @Override
    public String toString() {
        if (esValido()) {
            return "✓ Validación exitosa";
        }
        return "✗ Errores encontrados:\n" + getMensajeErrores();
    }
}