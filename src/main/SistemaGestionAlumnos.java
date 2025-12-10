package src.main;

import java.util.List;
import java.util.Scanner;
import src.controlador.*;
import src.modelo.*;
import src.servicio.*;

public class SistemaGestionAlumnos {
    
    private static AlumnoController controller;
    private static AlumnoService service;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        // Inicializar componentes
        controller = new AlumnoController();
        service = new AlumnoService();
        scanner = new Scanner(System.in);
        
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN DE ALUMNOS           ║");
        System.out.println("║   Universidad Sideral Carreon             ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    crearAlumno();
                    break;
                case 2:
                    modificarAlumno();
                    break;
                case 3:
                    buscarAlumno();
                    break;
                case 4:
                    eliminarAlumno();
                    break;
                case 5:
                    mostrarTodosLosAlumnos();
                    break;
                case 6:
                    mostrarEstadisticas();
                    break;
                case 7:
                    mostrarAlumnosEnRiesgo();
                    break;
                case 8:
                    buscarPorCarrera();
                    break;
                case 9:
                    mostrarAlumnosDestacados();
                    break;
                case 10:
                    cambiarEstadoAlumno();
                    break;
                case 11:
                    actualizarPromedioAlumno();
                    break;
                case 12:
                    mostrarDistribucionCarreras();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n✓ Gracias por usar el sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("\n✗ Opción no válida. Intente nuevamente.");
            }
            
            if (continuar && opcion != 0) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              MENÚ PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("  OPERACIONES CRUD:");
        System.out.println("    1. ➕ Crear Alumno");
        System.out.println("    2. ✏️  Modificar Alumno");
        System.out.println("    3. 🔍 Buscar Alumno");
        System.out.println("    4. 🗑️  Eliminar Alumno");
        System.out.println();
        System.out.println("  CONSULTAS:");
        System.out.println("    5. 📋 Mostrar Todos los Alumnos");
        System.out.println("    6. 📊 Ver Estadísticas");
        System.out.println("    7. ⚠️  Alumnos en Riesgo Académico");
        System.out.println("    8. 🎓 Buscar por Carrera");
        System.out.println("    9. ⭐ Alumnos Destacados");
        System.out.println();
        System.out.println("  ACTUALIZACIONES:");
        System.out.println("   10. 🔄 Cambiar Estado de Alumno");
        System.out.println("   11. 📈 Actualizar Promedio");
        System.out.println("   12. 📉 Distribución por Carreras");
        System.out.println();
        System.out.println("    0. 🚪 Salir");
        System.out.println("=".repeat(50));
        System.out.print("Seleccione una opción: ");
    }
    
    private static int leerOpcion() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            return opcion;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    // ==================== OPERACIÓN 1: CREAR ALUMNO ====================
    
    private static void crearAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           ➕ CREAR NUEVO ALUMNO");
        System.out.println("=".repeat(50));
        
        try {
            // Generar código automático
            String codigo = service.generarCodigoAlumno();
            System.out.println("Código asignado: " + codigo);
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            
            System.out.print("DNI (8 dígitos): ");
            String dni = scanner.nextLine().trim();
            
            System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
            String fechaNac = scanner.nextLine().trim();
            
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine().trim();
            
            System.out.print("Teléfono (9 dígitos): ");
            String telefono = scanner.nextLine().trim();
            
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Carrera: ");
            String carrera = scanner.nextLine().trim();
            
            System.out.print("Ciclo (1-12): ");
            int ciclo = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Promedio actual (0-20): ");
            double promedio = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
            String fechaIngreso = scanner.nextLine().trim();
            
            // Llamar al controlador
            RespuestaOperacion respuesta = controller.crearAlumno(
                codigo, nombre, apellido, dni, fechaNac, direccion,
                telefono, email, carrera, ciclo, promedio, fechaIngreso
            );
            
            System.out.println("\n" + respuesta);
            
            if (respuesta.isExito()) {
                Alumno alumno = respuesta.getDatosComoTipo(Alumno.class);
                if (alumno != null) {
                    System.out.println("\nDatos del alumno creado:");
                    System.out.println(alumno.obtenerResumen());
                }
            }
            
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIÓN 2: MODIFICAR ALUMNO ====================
    
    private static void modificarAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            ✏️  MODIFICAR ALUMNO");
        System.out.println("=".repeat(50));
        
        System.out.print("Ingrese el código del alumno a modificar: ");
        String codigo = scanner.nextLine().trim();
        
        // Buscar el alumno
        RespuestaOperacion busqueda = controller.buscarPorCodigo(codigo);
        
        if (!busqueda.isExito()) {
            System.out.println("\n" + busqueda);
            return;
        }
        
        Alumno alumno = busqueda.getDatosComoTipo(Alumno.class);
        System.out.println("\nAlumno encontrado:");
        System.out.println(alumno.obtenerResumen());
        
        System.out.println("\nIngrese los nuevos datos (Enter para mantener el actual):");
        
        try {
            System.out.print("Nombre [" + alumno.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) nombre = alumno.getNombre();
            
            System.out.print("Apellido [" + alumno.getApellido() + "]: ");
            String apellido = scanner.nextLine().trim();
            if (apellido.isEmpty()) apellido = alumno.getApellido();
            
            System.out.print("DNI [" + alumno.getDni() + "]: ");
            String dni = scanner.nextLine().trim();
            if (dni.isEmpty()) dni = alumno.getDni();
            
            System.out.print("Email [" + alumno.getEmail() + "]: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) email = alumno.getEmail();
            
            System.out.print("Carrera [" + alumno.getCarrera() + "]: ");
            String carrera = scanner.nextLine().trim();
            if (carrera.isEmpty()) carrera = alumno.getCarrera();
            
            System.out.print("Ciclo [" + alumno.getCiclo() + "]: ");
            String cicloStr = scanner.nextLine().trim();
            int ciclo = cicloStr.isEmpty() ? alumno.getCiclo() : Integer.parseInt(cicloStr);
            
            System.out.print("Promedio [" + alumno.getPromedio() + "]: ");
            String promedioStr = scanner.nextLine().trim();
            double promedio = promedioStr.isEmpty() ? alumno.getPromedio() : Double.parseDouble(promedioStr);
            
            System.out.print("Estado [" + alumno.getEstado() + "] (Activo/Inactivo/Egresado/Retirado): ");
            String estado = scanner.nextLine().trim();
            if (estado.isEmpty()) estado = alumno.getEstado();
            
            // Llamar al controlador
            RespuestaOperacion respuesta = controller.modificarAlumno(
                codigo, nombre, apellido, dni, alumno.getFechaNacimiento(),
                alumno.getDireccion(), alumno.getTelefono(), email, carrera,
                ciclo, promedio, estado, alumno.getFechaIngreso(),
                alumno.getCreditosAcumulados()
            );
            
            System.out.println("\n" + respuesta);
            
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== OPERACIÓN 3: BUSCAR ALUMNO ====================
    
    private static void buscarAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             🔍 BUSCAR ALUMNO");
        System.out.println("=".repeat(50));
        System.out.println("1. Buscar por código");
        System.out.println("2. Buscar por nombre/apellido");
        System.out.println("3. Buscar por DNI");
        System.out.print("\nSeleccione tipo de búsqueda: ");
        
        int opcion = leerOpcion();
        
        switch (opcion) {
            case 1:
                System.out.print("Ingrese el código: ");
                String codigo = scanner.nextLine().trim();
                RespuestaOperacion resp1 = controller.buscarPorCodigo(codigo);
                System.out.println("\n" + resp1);
                if (resp1.isExito()) {
                    Alumno a = resp1.getDatosComoTipo(Alumno.class);
                    System.out.println("\n" + a.obtenerResumen());
                }
                break;
                
            case 2:
                System.out.print("Ingrese nombre o apellido: ");
                String nombre = scanner.nextLine().trim();
                RespuestaOperacion resp2 = controller.buscarPorNombre(nombre);
                System.out.println("\n" + resp2);
                if (resp2.isExito()) {
                    @SuppressWarnings("unchecked")
                    List<Alumno> alumnos = (List<Alumno>) resp2.getDatos();
                    mostrarListaAlumnos(alumnos);
                }
                break;
                
            case 3:
                System.out.print("Ingrese el DNI: ");
                String dni = scanner.nextLine().trim();
                RespuestaOperacion resp3 = controller.buscarPorDNI(dni);
                System.out.println("\n" + resp3);
                if (resp3.isExito()) {
                    Alumno a = resp3.getDatosComoTipo(Alumno.class);
                    System.out.println("\n" + a.obtenerResumen());
                }
                break;
                
            default:
                System.out.println("\n✗ Opción no válida");
        }
    }
    
    // ==================== OPERACIÓN 4: ELIMINAR ALUMNO ====================
    
    private static void eliminarAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            🗑️  ELIMINAR ALUMNO");
        System.out.println("=".repeat(50));
        
        System.out.print("Ingrese el código del alumno a eliminar: ");
        String codigo = scanner.nextLine().trim();
        
        // Buscar primero para confirmar
        RespuestaOperacion busqueda = controller.buscarPorCodigo(codigo);
        
        if (!busqueda.isExito()) {
            System.out.println("\n" + busqueda);
            return;
        }
        
        Alumno alumno = busqueda.getDatosComoTipo(Alumno.class);
        System.out.println("\nAlumno a eliminar:");
        System.out.println(alumno.obtenerResumen());
        
        System.out.print("\n¿Está seguro de eliminar este alumno? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        
        if (confirmacion.equals("S")) {
            RespuestaOperacion respuesta = controller.eliminarAlumno(codigo);
            System.out.println("\n" + respuesta);
        } else {
            System.out.println("\n✓ Operación cancelada");
        }
    }
    
    // ==================== OPERACIÓN 5: MOSTRAR TODOS ====================
    
    private static void mostrarTodosLosAlumnos() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          📋 LISTA DE TODOS LOS ALUMNOS");
        System.out.println("=".repeat(50));
        
        RespuestaOperacion respuesta = controller.obtenerTodos();
        System.out.println("\n" + respuesta);
        
        if (respuesta.isExito()) {
            @SuppressWarnings("unchecked")
            List<Alumno> alumnos = (List<Alumno>) respuesta.getDatos();
            mostrarListaAlumnos(alumnos);
        }
    }
    
    // ==================== OPERACIÓN 6: ESTADÍSTICAS ====================
    
    private static void mostrarEstadisticas() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             📊 ESTADÍSTICAS DEL SISTEMA");
        System.out.println("=".repeat(50));
        
        EstadisticasDTO stats = controller.obtenerEstadisticas();
        System.out.println("\n" + stats);
    }
    
    // ==================== OPERACIÓN 7: ALUMNOS EN RIESGO ====================
    
    private static void mostrarAlumnosEnRiesgo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         ⚠️  ALUMNOS EN RIESGO ACADÉMICO");
        System.out.println("=".repeat(50));
        
        RespuestaOperacion respuesta = controller.obtenerAlumnosEnRiesgo();
        System.out.println("\n" + respuesta);
        
        if (respuesta.isExito()) {
            @SuppressWarnings("unchecked")
            List<Alumno> alumnos = (List<Alumno>) respuesta.getDatos();
            if (!alumnos.isEmpty()) {
                mostrarListaAlumnos(alumnos);
            }
        }
    }
    
    // ==================== OPERACIÓN 8: BUSCAR POR CARRERA ====================
    
    private static void buscarPorCarrera() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            🎓 BUSCAR POR CARRERA");
        System.out.println("=".repeat(50));
        
        System.out.print("Ingrese el nombre de la carrera: ");
        String carrera = scanner.nextLine().trim();
        
        RespuestaOperacion respuesta = controller.buscarPorCarrera(carrera);
        System.out.println("\n" + respuesta);
        
        if (respuesta.isExito()) {
            @SuppressWarnings("unchecked")
            List<Alumno> alumnos = (List<Alumno>) respuesta.getDatos();
            mostrarListaAlumnos(alumnos);
        }
    }
    
    // ==================== OPERACIÓN 9: ALUMNOS DESTACADOS ====================
    
    private static void mostrarAlumnosDestacados() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            ⭐ ALUMNOS DESTACADOS");
        System.out.println("=".repeat(50));
        
        System.out.print("¿Cuántos alumnos desea ver? (default 10): ");
        String limiteStr = scanner.nextLine().trim();
        int limite = limiteStr.isEmpty() ? 10 : Integer.parseInt(limiteStr);
        
        List<Alumno> destacados = service.obtenerAlumnosDestacados(limite);
        
        System.out.println("\nTop " + destacados.size() + " alumnos con mejor promedio:");
        mostrarListaAlumnos(destacados);
    }
    
    // ==================== OPERACIÓN 10: CAMBIAR ESTADO ====================
    
    private static void cambiarEstadoAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          🔄 CAMBIAR ESTADO DE ALUMNO");
        System.out.println("=".repeat(50));
        
        System.out.print("Ingrese el código del alumno: ");
        String codigo = scanner.nextLine().trim();
        
        System.out.println("\nEstados disponibles:");
        System.out.println("1. Activo");
        System.out.println("2. Inactivo");
        System.out.println("3. Egresado");
        System.out.println("4. Retirado");
        System.out.print("\nSeleccione el nuevo estado: ");
        
        int opcion = leerOpcion();
        String estado = "";
        
        switch (opcion) {
            case 1: estado = "Activo"; break;
            case 2: estado = "Inactivo"; break;
            case 3: estado = "Egresado"; break;
            case 4: estado = "Retirado"; break;
            default:
                System.out.println("\n✗ Opción no válida");
                return;
        }
        
        RespuestaOperacion respuesta = controller.cambiarEstado(codigo, estado);
        System.out.println("\n" + respuesta);
    }
    
    // ==================== OPERACIÓN 11: ACTUALIZAR PROMEDIO ====================
    
    private static void actualizarPromedioAlumno() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           📈 ACTUALIZAR PROMEDIO");
        System.out.println("=".repeat(50));
        
        System.out.print("Ingrese el código del alumno: ");
        String codigo = scanner.nextLine().trim();
        
        System.out.print("Ingrese el nuevo promedio (0-20): ");
        try {
            double promedio = Double.parseDouble(scanner.nextLine().trim());
            
            RespuestaOperacion respuesta = controller.actualizarPromedio(codigo, promedio);
            System.out.println("\n" + respuesta);
            
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Error: El promedio debe ser un número válido");
        }
    }
    
    // ==================== OPERACIÓN 12: DISTRIBUCIÓN ====================
    
    private static void mostrarDistribucionCarreras() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         📉 DISTRIBUCIÓN POR CARRERAS");
        System.out.println("=".repeat(50));
        
        String distribucion = service.obtenerDistribucionPorCarrera();
        System.out.println("\n" + distribucion);
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private static void mostrarListaAlumnos(List<Alumno> alumnos) {
        System.out.println("\n" + "-".repeat(80));
        System.out.printf("%-10s %-25s %-10s %-8s %-12s %-10s\n",
                "Código", "Nombre Completo", "DNI", "Ciclo", "Promedio", "Estado");
        System.out.println("-".repeat(80));
        
        for (Alumno a : alumnos) {
            System.out.printf("%-10s %-25s %-10s %-8d %-12.2f %-10s\n",
                    a.getCodigoAlumno(),
                    truncarTexto(a.getNombreCompleto(), 25),
                    a.getDni(),
                    a.getCiclo(),
                    a.getPromedio(),
                    a.getEstado());
        }
        
        System.out.println("-".repeat(80));
        System.out.println("Total: " + alumnos.size() + " alumno(s)");
    }
    
    private static String truncarTexto(String texto, int maxLongitud) {
        if (texto.length() <= maxLongitud) {
            return texto;
        }
        return texto.substring(0, maxLongitud - 3) + "...";
    }
}