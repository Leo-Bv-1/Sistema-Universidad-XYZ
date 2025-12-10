<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="src.modelo.Alumno" %>
<%
    Alumno alumno = (Alumno) request.getAttribute("alumno");
    String error = (String) request.getAttribute("error");
    if (alumno == null) {
        response.sendRedirect("alumnos?action=listar");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Alumno - Universidad Sideral Carreon</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-brand">
                <div class="university-seal"></div>
                <div class="brand-text">
                    <span class="brand-name">Sideral Carreon</span>
                    <span class="brand-tagline">Excelencia Académica</span>
                </div>
            </div>
            <div class="nav-links">
                <a href="alumnos?action=dashboard" class="nav-link">Inicio</a>
                <a href="alumnos?action=listar" class="nav-link active">Sistema</a>
            </div>
        </div>
    </nav>

    <div class="main-wrapper">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h3>Gestión Académica</h3>
                <p>Portal Administrativo</p>
            </div>
            <nav class="sidebar-menu">
                <a href="alumnos?action=dashboard" class="menu-item">
                    <div class="menu-icon dashboard-icon"></div>
                    <span>Panel Principal</span>
                </a>
                <a href="alumnos?action=listar" class="menu-item active">
                    <div class="menu-icon students-icon"></div>
                    <span>Estudiantes</span>
                </a>
            </nav>
        </aside>

        <main class="main-content">
            <div class="content-header">
                <div class="page-title">
                    <h1>Editar Alumno: <%= alumno.getCodigoAlumno() %></h1>
                    <p class="breadcrumb">Inicio / Sistema / Estudiantes / Editar</p>
                </div>
            </div>

            <% if (error != null) { %>
                <div class="alert error">
                    <%= error %>
                </div>
            <% } %>

            <div class="dashboard-grid">
                <div class="actions-section">
                    <form action="alumnos" method="post" class="student-form">
                        <input type="hidden" name="action" value="editar">
                        <input type="hidden" name="codigo" value="<%= alumno.getCodigoAlumno() %>">
                        <!-- Hidden fields for data not editable or handled differently -->
                        <input type="hidden" name="fechaNac" value="<%= alumno.getFechaNacimiento() %>">
                        <input type="hidden" name="fechaIngreso" value="<%= alumno.getFechaIngreso() %>">

                        <div class="form-group">
                            <label>Nombre:</label>
                            <input type="text" name="nombre" value="<%= alumno.getNombre() %>" required>
                        </div>
                        <div class="form-group">
                            <label>Apellido:</label>
                            <input type="text" name="apellido" value="<%= alumno.getApellido() %>" required>
                        </div>
                        <div class="form-group">
                            <label>DNI:</label>
                            <input type="text" name="dni" value="<%= alumno.getDni() %>" required pattern="\d{8}" title="8 dígitos">
                        </div>
                        <div class="form-group">
                            <label>Dirección:</label>
                            <input type="text" name="direccion" value="<%= alumno.getDireccion() %>" required>
                        </div>
                        <div class="form-group">
                            <label>Teléfono:</label>
                            <input type="text" name="telefono" value="<%= alumno.getTelefono() %>" required>
                        </div>
                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="email" value="<%= alumno.getEmail() %>" required>
                        </div>
                        <div class="form-group">
                            <label>Carrera:</label>
                            <input type="text" name="carrera" value="<%= alumno.getCarrera() %>" required>
                        </div>
                        <div class="form-group">
                            <label>Ciclo:</label>
                            <input type="number" name="ciclo" value="<%= alumno.getCiclo() %>" min="1" max="12" required>
                        </div>
                        <div class="form-group">
                            <label>Promedio:</label>
                            <input type="number" name="promedio" value="<%= alumno.getPromedio() %>" step="0.01" min="0" max="20" required>
                        </div>
                         <div class="form-group">
                            <label>Estado:</label>
                            <select name="estado">
                                <option value="Activo" <%= "Activo".equals(alumno.getEstado()) ? "selected" : "" %>>Activo</option>
                                <option value="Inactivo" <%= "Inactivo".equals(alumno.getEstado()) ? "selected" : "" %>>Inactivo</option>
                                <option value="Egresado" <%= "Egresado".equals(alumno.getEstado()) ? "selected" : "" %>>Egresado</option>
                                <option value="Retirado" <%= "Retirado".equals(alumno.getEstado()) ? "selected" : "" %>>Retirado</option>
                            </select>
                        </div>

                        <button type="submit" class="action-button primary">Actualizar Alumno</button>
                        <a href="alumnos?action=listar" class="action-button secondary">Cancelar</a>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
