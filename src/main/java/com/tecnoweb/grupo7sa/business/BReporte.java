package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DReporte;

import java.util.List;

public class BReporte {

    private final DReporte dReporte;

    public BReporte() {
        this.dReporte = new DReporte();
    }

    // CU8 - LÓGICA DE NEGOCIO PARA REPORTES Y ESTADÍSTICAS

    /**
     * Reporte detallado de inscripciones por curso
     */
    public List<String[]> inscripcionesPorCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dReporte.inscripcionesPorCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Índice de aprobación por curso ⭐ ENDPOINT PRINCIPAL
     */
    public String[] aprobacionPorCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dReporte.aprobacionPorCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Reporte de asistencia por curso ⭐ ENDPOINT PRINCIPAL
     */
    public String[] asistenciaPorCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dReporte.asistenciaPorCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Rendimiento académico por gestión ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> rendimientoPorGestion(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dReporte.rendimientoPorGestion(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Distribución de participantes por tipo ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> participantesPorTipo() {
        try {
            List<String[]> result = dReporte.participantesPorTipo();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Ingresos por gestión ⭐ ENDPOINT PRINCIPAL
     */
    public String[] ingresosPorGestion(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dReporte.ingresosPorGestion(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Ingresos por curso específico
     */
    public String[] ingresosPorCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dReporte.ingresosPorCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Ingresos por mes
     */
    public String[] ingresosPorMes(int año, int mes) {
        if (año < 2020 || año > 2030) {
            System.out.println("Error: El año debe estar entre 2020 y 2030");
            return null;
        }

        if (mes < 1 || mes > 12) {
            System.out.println("Error: El mes debe estar entre 1 y 12");
            return null;
        }

        try {
            String[] result = dReporte.ingresosPorMes(año, mes);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Preinscripciones sin pago ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> pagosVencidos() {
        try {
            List<String[]> result = dReporte.pagosVencidos();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Certificados emitidos por gestión
     */
    public List<String[]> certificadosEmitidos(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dReporte.certificadosEmitidos(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Porcentaje de ocupación de cursos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> ocupacionCursos() {
        try {
            List<String[]> result = dReporte.ocupacionCursos();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Ranking de tutores más activos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> tutoresMasActivos() {
        try {
            List<String[]> result = dReporte.tutoresMasActivos();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Cursos más populares ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> cursosPopulares() {
        try {
            List<String[]> result = dReporte.cursosPopulares();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Dashboard general del sistema ⭐ ENDPOINT PRINCIPAL
     * KPIs globales más importantes
     */
    public String[] dashboardGeneral() {
        try {
            String[] result = dReporte.dashboardGeneral();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * KPIs específicos por gestión
     */
    public String[] kpisGestion(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dReporte.kpisGestion(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    /**
     * Análisis de tendencias de inscripciones ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> tendenciasInscripciones() {
        try {
            List<String[]> result = dReporte.tendenciasInscripciones();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dReporte.disconnect();
        }
    }

    public void disconnect() {
        if (dReporte != null) {
            dReporte.disconnect();
        }
    }
}