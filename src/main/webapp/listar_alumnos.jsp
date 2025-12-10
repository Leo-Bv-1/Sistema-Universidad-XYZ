<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="src.modelo.Alumno" %>
<%
    List<Alumno> alumnos = (List<Alumno>) request.getAttribute("alumnos");
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getParameter("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Alumnos - Universidad Sideral Carreon</title>
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
                    <h1>Lista de Estudiantes</h1>
                    <p class="breadcrumb">Inicio / Sistema / Estudiantes</p>
                </div>
            </div>

            <% if (mensaje != null) { %>
                <div class="alert success">
                    Operación realizada: <%= mensaje %>
                </div>
            <% } %>

            <% if (error != null) { %>
                <div class="alert error">
                    <%= error %>
                </div>
            <% } %>

            <div class="dashboard-grid">
                <div class="actions-section">
                    <form action="alumnos" method="get" class="search-form">
                        <input type="hidden" name="action" value="buscar">
                        <input type="text" name="q" placeholder="Buscar por código o nombre..." class="search-input">
                        <button type="submit" class="action-button tertiary">Buscar</button>
                    </form>
                    <br>
                    <a href="alumnos?action=crear" class="action-button primary">Nuevo Alumno</a>

                    <table class="students-table">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Nombre Completo</th>
                                <th>DNI</th>
                                <th>Carrera</th>
                                <th>Ciclo</th>
                                <th>Promedio</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (alumnos != null && !alumnos.isEmpty()) {
                                for (Alumno a : alumnos) { %>
                                <tr>
                                    <td><%= a.getCodigoAlumno() %></td>
                                    <td><%= a.getNombreCompleto() %></td>
                                    <td><%= a.getDni() %></td>
                                    <td><%= a.getCarrera() %></td>
                                    <td><%= a.getCiclo() %></td>
                                    <td><%= a.getPromedio() %></td>
                                    <td><%= a.getEstado() %></td>
                                    <td>
                                        <a href="alumnos?action=editar&codigo=<%= a.getCodigoAlumno() %>" class="btn-small edit">Editar</a>
                                        <a href="alumnos?action=eliminar&codigo=<%= a.getCodigoAlumno() %>" class="btn-small delete" onclick="return confirm('¿Seguro que desea eliminar este alumno?');">Eliminar</a>
                                    </td>
                                </tr>
                            <%  }
                               } else { %>
                                <tr>
                                    <td colspan="8">No hay alumnos registrados.</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
