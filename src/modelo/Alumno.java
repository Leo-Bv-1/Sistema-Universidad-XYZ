package src.modelo;

public class Alumno extends Persona {
    private String codigoAlumno;
    private String carrera;
    private int ciclo;
    private double promedio;
    private String estado; // Activo, Inactivo, Egresado, Retirado
    private String fechaIngreso;
    private int creditosAcumulados;

    // Constructor vacío
    public Alumno() {
        super();
        this.estado = "Activo";
        this.creditosAcumulados = 0;
    }

    // Constructor con parámetros esenciales
    public Alumno(String codigoAlumno, String nombre, String apellido, String dni, 
                  String carrera, String email) {
        super(nombre, apellido, dni, "", "", "", email);
        this.codigoAlumno = codigoAlumno;
        this.carrera = carrera;
        this.ciclo = 1;
        this.promedio = 0.0;
        this.estado = "Activo";
        this.creditosAcumulados = 0;
    }

    // Constructor completo
    public Alumno(String nombre, String apellido, String dni, String fechaNacimiento,
                  String direccion, String telefono, String email,
                  String codigoAlumno, String carrera, int ciclo, double promedio,
                  String estado, String fechaIngreso, int creditosAcumulados) {
        super(nombre, apellido, dni, fechaNacimiento, direccion, telefono, email);
        this.codigoAlumno = codigoAlumno;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.promedio = promedio;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.creditosAcumulados = creditosAcumulados;
    }

    // Getters y Setters
    public String getCodigoAlumno() {
        return codigoAlumno;
    }

    public void setCodigoAlumno(String codigoAlumno) {
        this.codigoAlumno = codigoAlumno;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getCreditosAcumulados() {
        return creditosAcumulados;
    }

    public void setCreditosAcumulados(int creditosAcumulados) {
        this.creditosAcumulados = creditosAcumulados;
    }

    // Implementación de método abstracto
    @Override
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // Implementación de validación de datos
    @Override
    public boolean validarDatos() {
        boolean datosPersonalesValidos = nombre != null && !nombre.trim().isEmpty() &&
                                         apellido != null && !apellido.trim().isEmpty() &&
                                         dni != null && validarDNI(dni) &&
                                         email != null && validarEmail(email);

        boolean datosAcademicosValidos = codigoAlumno != null && !codigoAlumno.trim().isEmpty() &&
                                        carrera != null && !carrera.trim().isEmpty() &&
                                        ciclo > 0 && ciclo <= 12 &&
                                        promedio >= 0 && promedio <= 20;

        return datosPersonalesValidos && datosAcademicosValidos;
    }

    // Método para calcular si el alumno está en riesgo académico
    public boolean enRiesgoAcademico() {
        return promedio < 11.0 && estado.equals("Activo");
    }

    // Método para verificar si puede avanzar de ciclo
    public boolean puedeAvanzarCiclo() {
        return promedio >= 13.0 && estado.equals("Activo") && ciclo < 12;
    }

    // Método para avanzar al siguiente ciclo
    public void avanzarCiclo() {
        if (puedeAvanzarCiclo()) {
            this.ciclo++;
        }
    }

    // Método para actualizar el promedio
    public void actualizarPromedio(double nuevoPromedio) {
        if (nuevoPromedio >= 0 && nuevoPromedio <= 20) {
            this.promedio = nuevoPromedio;
        }
    }

    // Método para cambiar el estado del alumno
    public void cambiarEstado(String nuevoEstado) {
        String[] estadosValidos = {"Activo", "Inactivo", "Egresado", "Retirado"};
        for (String estado : estadosValidos) {
            if (estado.equalsIgnoreCase(nuevoEstado)) {
                this.estado = estado;
                return;
            }
        }
    }

    // Método para agregar créditos
    public void agregarCreditos(int creditos) {
        if (creditos > 0) {
            this.creditosAcumulados += creditos;
        }
    }

    // Método para verificar si puede graduarse
    public boolean puedeGraduarse(int creditosRequeridos) {
        return creditosAcumulados >= creditosRequeridos && promedio >= 11.0;
    }

    // Método para obtener nivel académico
    public String getNivelAcademico() {
        if (ciclo <= 2) return "Inicial";
        if (ciclo <= 6) return "Intermedio";
        if (ciclo <= 10) return "Avanzado";
        return "Finalizando";
    }

    // Método toString mejorado
    @Override
    public String toString() {
        return String.format(
            "Alumno[Código: %s, Nombre: %s %s, DNI: %s, Carrera: %s, " +
            "Ciclo: %d, Promedio: %.2f, Estado: %s, Email: %s]",
            codigoAlumno, nombre, apellido, dni, carrera, ciclo, promedio, estado, email
        );
    }

    // Método para obtener información resumida
    public String obtenerResumen() {
        return String.format(
            "%s - %s (%s)\nCarrera: %s | Ciclo: %d | Promedio: %.2f\nEstado: %s",
            codigoAlumno, getNombreCompleto(), dni, carrera, ciclo, promedio, estado
        );
    }
}