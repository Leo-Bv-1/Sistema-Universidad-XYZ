package src.modelo;
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String fechaNacimiento;
    protected String direccion;
    protected String telefono;
    protected String email;

    // Constructor vacío
    public Persona() {
    }

    // Constructor con parámetros
    public Persona(String nombre, String apellido, String dni, String fechaNacimiento, 
                   String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método abstracto para obtener nombre completo
    public abstract String getNombreCompleto();

    // Método abstracto para validar datos
    public abstract boolean validarDatos();

    // Método toString
    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Método para validar formato de email
    protected boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    // Método para validar DNI (8 dígitos)
    protected boolean validarDNI(String dni) {
        if (dni == null || dni.isEmpty()) return false;
        return dni.matches("\\d{8}");
    }

    // Método para validar teléfono (9 dígitos)
    protected boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) return false;
        return telefono.matches("\\d{9}");
    }
}
