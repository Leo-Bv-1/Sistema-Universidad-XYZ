package src.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.controlador.AlumnoController;
import src.modelo.Alumno;
import src.modelo.RespuestaOperacion;
import src.modelo.EstadisticasDTO;

@WebServlet("/alumnos")
public class AlumnoServlet extends HttpServlet {

    private AlumnoController controller;

    @Override
    public void init() throws ServletException {
        // Initialize controller. The controller defaults to in-memory repository.
        controller = new AlumnoController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

        switch (action) {
            case "dashboard":
                mostrarDashboard(req, resp);
                break;
            case "listar":
                listarAlumnos(req, resp);
                break;
            case "buscar":
                mostrarBuscar(req, resp);
                break;
            case "crear":
                mostrarCrear(req, resp);
                break;
            case "editar":
                mostrarEditar(req, resp);
                break;
            case "eliminar":
                eliminarAlumno(req, resp);
                break;
            default:
                mostrarDashboard(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect("alumnos?action=dashboard");
            return;
        }

        switch (action) {
            case "crear":
                procesarCrear(req, resp);
                break;
            case "editar":
                procesarEditar(req, resp);
                break;
            default:
                resp.sendRedirect("alumnos?action=dashboard");
        }
    }

    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstadisticasDTO stats = controller.obtenerEstadisticas();
        req.setAttribute("stats", stats);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void listarAlumnos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RespuestaOperacion respuesta = controller.obtenerTodos();
        if (respuesta.isExito()) {
            req.setAttribute("alumnos", respuesta.getDatos());
        }
        req.getRequestDispatcher("/listar_alumnos.jsp").forward(req, resp);
    }

    private void mostrarBuscar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String query = req.getParameter("q");
         if (query != null && !query.trim().isEmpty()) {
             // Try by code first
             RespuestaOperacion res = controller.buscarPorCodigo(query);
             if (!res.isExito()) {
                 // Try by name
                 res = controller.buscarPorNombre(query);
             }

             if (res.isExito()) {
                 if (res.getDatos() instanceof List) {
                     req.setAttribute("alumnos", res.getDatos());
                 } else {
                     req.setAttribute("alumnos", java.util.Collections.singletonList(res.getDatos()));
                 }
             } else {
                 req.setAttribute("error", "No se encontraron alumnos.");
             }
         }
         req.getRequestDispatcher("/listar_alumnos.jsp").forward(req, resp);
    }

    private void mostrarCrear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/crear_alumno.jsp").forward(req, resp);
    }

    private void procesarCrear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String codigo = req.getParameter("codigo");
            String nombre = req.getParameter("nombre");
            String apellido = req.getParameter("apellido");
            String dni = req.getParameter("dni");
            String fechaNac = req.getParameter("fechaNac");
            String direccion = req.getParameter("direccion");
            String telefono = req.getParameter("telefono");
            String email = req.getParameter("email");
            String carrera = req.getParameter("carrera");
            int ciclo = Integer.parseInt(req.getParameter("ciclo"));
            double promedio = Double.parseDouble(req.getParameter("promedio"));
            String fechaIngreso = req.getParameter("fechaIngreso");

            RespuestaOperacion res = controller.crearAlumno(
                codigo, nombre, apellido, dni, fechaNac, direccion,
                telefono, email, carrera, ciclo, promedio, fechaIngreso
            );

            if (res.isExito()) {
                resp.sendRedirect("alumnos?action=listar&mensaje=creado");
            } else {
                req.setAttribute("error", res.getMensaje());
                req.getRequestDispatcher("/crear_alumno.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al procesar datos: " + e.getMessage());
            req.getRequestDispatcher("/crear_alumno.jsp").forward(req, resp);
        }
    }

    private void mostrarEditar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codigo = req.getParameter("codigo");
        RespuestaOperacion res = controller.buscarPorCodigo(codigo);

        if (res.isExito()) {
            req.setAttribute("alumno", res.getDatos());
            req.getRequestDispatcher("/editar_alumno.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("alumnos?action=listar&error=noencontrado");
        }
    }

    private void procesarEditar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String codigo = req.getParameter("codigo");
            String nombre = req.getParameter("nombre");
            String apellido = req.getParameter("apellido");
            String dni = req.getParameter("dni");
            String fechaNac = req.getParameter("fechaNac"); // Not editable in simple version? Let's assume passed or fetch original
            // Re-fetch original to get non-editable fields if necessary, or pass hidden fields

            // For simplicity, assume all fields passed or we handle defaults in controller if passed as empty,
            // but controller logic checks empties.
            // Let's assume the form sends everything.

            String direccion = req.getParameter("direccion");
            String telefono = req.getParameter("telefono");
            String email = req.getParameter("email");
            String carrera = req.getParameter("carrera");
            int ciclo = Integer.parseInt(req.getParameter("ciclo"));
            double promedio = Double.parseDouble(req.getParameter("promedio"));
            String estado = req.getParameter("estado");

            // These might be hidden or we fetch them
            String fechaIngreso = req.getParameter("fechaIngreso");
            int creditos = 0; // Simplified

            RespuestaOperacion res = controller.modificarAlumno(
                codigo, nombre, apellido, dni, fechaNac, direccion,
                telefono, email, carrera, ciclo, promedio, estado, fechaIngreso, creditos
            );

            if (res.isExito()) {
                resp.sendRedirect("alumnos?action=listar&mensaje=actualizado");
            } else {
                req.setAttribute("error", res.getMensaje());
                // We need to re-set the alumno object to re-render form
                // But the form params are available, usually good practice to reconstruct object or just forward
                req.setAttribute("alumno", res.getDatos()); // if it returns the partial object?
                req.getRequestDispatcher("/editar_alumno.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/editar_alumno.jsp").forward(req, resp);
        }
    }

    private void eliminarAlumno(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codigo = req.getParameter("codigo");
        controller.eliminarAlumno(codigo);
        resp.sendRedirect("alumnos?action=listar&mensaje=eliminado");
    }
}
