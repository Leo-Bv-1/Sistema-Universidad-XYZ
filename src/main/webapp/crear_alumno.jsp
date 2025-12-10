<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Alumno - Universidad Sideral Carreon</title>
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
                    <h1>Registrar Nuevo Alumno</h1>
                    <p class="breadcrumb">Inicio / Sistema / Estudiantes / Crear</p>
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
                        <input type="hidden" name="action" value="crear">

                        <div class="form-group">
                            <label>Código:</label>
                            <input type="text" name="codigo" required>
                        </div>
                        <div class="form-group">
                            <label>Nombre:</label>
                            <input type="text" name="nombre" required>
                        </div>
                        <div class="form-group">
                            <label>Apellido:</label>
                            <input type="text" name="apellido" required>
                        </div>
                        <div class="form-group">
                            <label>DNI:</label>
                            <input type="text" name="dni" required pattern="\d{8}" title="8 dígitos">
                        </div>
                        <div class="form-group">
                            <label>Fecha Nacimiento (YYYY-MM-DD):</label>
                            <input type="date" name="fechaNac" required>
                        </div>
                        <div class="form-group">
                            <label>Dirección:</label>
                            <input type="text" name="direccion" required>
                        </div>
                        <div class="form-group">
                            <label>Teléfono:</label>
                            <input type="text" name="telefono" required>
                        </div>
                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="email" required>
                        </div>
                        <div class="form-group">
                            <label>Carrera:</label>
                            <input type="text" name="carrera" required>
                        </div>
                        <div class="form-group">
                            <label>Ciclo:</label>
                            <input type="number" name="ciclo" min="1" max="12" required>
                        </div>
                        <div class="form-group">
                            <label>Promedio:</label>
                            <input type="number" name="promedio" step="0.01" min="0" max="20" required>
                        </div>
                        <div class="form-group">
                            <label>Fecha Ingreso (YYYY-MM-DD):</label>
                            <input type="date" name="fechaIngreso" required>
                        </div>

                        <button type="submit" class="action-button primary">Guardar Alumno</button>
                        <a href="alumnos?action=listar" class="action-button secondary">Cancelar</a>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
