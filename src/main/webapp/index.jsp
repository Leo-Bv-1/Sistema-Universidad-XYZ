<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="src.modelo.EstadisticasDTO" %>
<%
    EstadisticasDTO stats = (EstadisticasDTO) request.getAttribute("stats");
    // If null, we might be hitting index.jsp directly, so forward to servlet
    if (stats == null) {
        response.sendRedirect("alumnos?action=dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión Académica - Universidad Sideral Carreon</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <!-- Navigation Bar -->
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
                <a href="alumnos?action=dashboard" class="nav-link active">Inicio</a>
                <a href="alumnos?action=listar" class="nav-link">Sistema</a>
                <a href="#" class="nav-link">Ayuda</a>
            </div>
        </div>
    </nav>

    <!-- Main Container -->
    <div class="main-wrapper">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h3>Gestión Académica</h3>
                <p>Portal Administrativo</p>
            </div>
            <nav class="sidebar-menu">
                <a href="alumnos?action=dashboard" class="menu-item active">
                    <div class="menu-icon dashboard-icon"></div>
                    <span>Panel Principal</span>
                </a>
                <a href="alumnos?action=listar" class="menu-item">
                    <div class="menu-icon students-icon"></div>
                    <span>Estudiantes</span>
                </a>
                <a href="#" class="menu-item">
                    <div class="menu-icon reports-icon"></div>
                    <span>Reportes</span>
                </a>
                <a href="#" class="menu-item">
                    <div class="menu-icon settings-icon"></div>
                    <span>Configuración</span>
                </a>
            </nav>
        </aside>

        <main class="main-content">
            <div class="content-header">
                <div class="page-title">
                    <h1>Sistema de Gestión de Alumnos</h1>
                    <p class="breadcrumb">Inicio / Sistema / Gestión de Alumnos</p>
                </div>
                <div class="user-info">
                    <span class="user-name">Administrador</span>
                    <div class="user-avatar"></div>
                </div>
            </div>

            <div class="dashboard-grid">
                <!-- Stats Cards -->
                <div class="stats-row">
                    <div class="stat-card">
                        <div class="stat-icon stat-icon-students"></div>
                        <div class="stat-info">
                            <span class="stat-label">Total Alumnos</span>
                            <span class="stat-value"><%= stats != null ? stats.getTotalAlumnos() : 0 %></span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon stat-icon-active"></div>
                        <div class="stat-info">
                            <span class="stat-label">Activos</span>
                            <span class="stat-value"><%= stats != null ? stats.getTotalActivos() : 0 %></span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon stat-icon-new"></div>
                        <div class="stat-info">
                            <span class="stat-label">En Riesgo</span>
                            <span class="stat-value"><%= stats != null ? stats.getTotalEnRiesgo() : 0 %></span>
                        </div>
                    </div>
                </div>

                <!-- Action Cards -->
                <div class="actions-section">
                    <h2 class="section-title">Acciones Disponibles</h2>

                    <div class="action-cards">
                        <div class="action-card">
                            <div class="action-icon add-icon"></div>
                            <div class="action-content">
                                <h3>Registrar Alumno</h3>
                                <p>Agregar un nuevo estudiante al sistema académico</p>
                                <a href="alumnos?action=crear" class="action-button primary">Agregar Alumno</a>
                            </div>
                        </div>

                        <div class="action-card">
                            <div class="action-icon view-icon"></div>
                            <div class="action-content">
                                <h3>Consultar Alumnos</h3>
                                <p>Ver y buscar en la base de datos de estudiantes</p>
                                <a href="alumnos?action=listar" class="action-button tertiary">Mostrar Alumnos</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-section">
                <h4>Universidad Sideral Carreon</h4>
                <p>Sistema de Gestión Académica v2.0</p>
            </div>
            <div class="footer-section">
                <p>Campus Principal: Av. Universitaria 1234</p>
                <p>Tel: +51 (01) 234-5678 | Email: info@sideralcarreon.edu.pe</p>
            </div>
            <div class="footer-section">
                <p>&copy; 2024 Universidad Sideral Carreon</p>
                <p>Todos los derechos reservados</p>
            </div>
        </div>
    </footer>
</body>
</html>
