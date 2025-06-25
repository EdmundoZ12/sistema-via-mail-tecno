package com.tecnoweb.grupo7sa.handle;

import com.tecnoweb.grupo7sa.business.BReporte;

import java.util.List;

public class HandleReporte {

    private final BReporte bReporte;

    public HandleReporte() {
        this.bReporte = new BReporte();
    }

    public List<String[]> inscripcionesPorCurso(int cursoId) {
        return bReporte.inscripcionesPorCurso(cursoId);
    }

    public String[] aprobacionPorCurso(int cursoId) {
        return bReporte.aprobacionPorCurso(cursoId);
    }

    public String[] asistenciaPorCurso(int cursoId) {
        return bReporte.asistenciaPorCurso(cursoId);
    }

    public List<String[]> rendimientoPorGestion(int gestionId) {
        return bReporte.rendimientoPorGestion(gestionId);
    }

    public List<String[]> participantesPorTipo() {
        return bReporte.participantesPorTipo();
    }

    public String[] ingresosPorGestion(int gestionId) {
        return bReporte.ingresosPorGestion(gestionId);
    }

    public String[] ingresosPorCurso(int cursoId) {
        return bReporte.ingresosPorCurso(cursoId);
    }

    public String[] ingresosPorMes(int año, int mes) {
        return bReporte.ingresosPorMes(año, mes);
    }

    public List<String[]> pagosVencidos() {
        return bReporte.pagosVencidos();
    }

    public List<String[]> certificadosEmitidos(int gestionId) {
        return bReporte.certificadosEmitidos(gestionId);
    }

    public List<String[]> ocupacionCursos() {
        return bReporte.ocupacionCursos();
    }

    public List<String[]> tutoresMasActivos() {
        return bReporte.tutoresMasActivos();
    }

    public List<String[]> cursosPopulares() {
        return bReporte.cursosPopulares();
    }

    public String[] dashboardGeneral() {
        return bReporte.dashboardGeneral();
    }

    public String[] kpisGestion(int gestionId) {
        return bReporte.kpisGestion(gestionId);
    }

    public List<String[]> tendenciasInscripciones() {
        return bReporte.tendenciasInscripciones();
    }

    public void disconnect() {
        bReporte.disconnect();
    }
}
