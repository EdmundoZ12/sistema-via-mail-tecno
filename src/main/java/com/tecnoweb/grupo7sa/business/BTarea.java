package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DTarea;
import com.tecnoweb.grupo7sa.data.DCurso;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BTarea {

    private final DTarea dTarea;
    private final DCurso dCurso;

    public BTarea() {
        this.dTarea = new DTarea();
        this.dCurso = new DCurso();
    }

    // CU5 - LÓGICA DE NEGOCIO PARA TAREAS

    /**
     * Crear nueva tarea con validaciones
     */
    public String save(int cursoId, String titulo, String descripcion, Date fechaAsignacion) {

        // Validaciones básicas
        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            return "Error: El título es obligatorio";
        }

        if (titulo.trim().length() > 100) {
            return "Error: El título no puede exceder 100 caracteres";
        }

        if (fechaAsignacion == null) {
            return "Error: La fecha de asignación es obligatoria";
        }

        // Verificar que el curso existe y está activo
        String[] curso = dCurso.findOneById(cursoId);
        if (curso == null) {
            return "Error: No se encontró el curso con ID: " + cursoId;
        }

        if (!"true".equals(curso[11])) {
            return "Error: No se pueden crear tareas para cursos inactivos";
        }

        // Validar que la fecha esté dentro del período del curso
        if (!isDateWithinCourse(fechaAsignacion, curso)) {
            return "Error: La fecha de asignación debe estar dentro del período del curso";
        }

        try {
            String result = dTarea.save(cursoId, titulo.trim(), descripcion != null ? descripcion.trim() : null, fechaAsignacion);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTarea.disconnect();
            dCurso.disconnect();
        }
    }

    /**
     * Actualizar tarea existente
     */
    public String update(int id, int cursoId, String titulo, String descripcion, Date fechaAsignacion) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            return "Error: El título es obligatorio";
        }

        if (titulo.trim().length() > 100) {
            return "Error: El título no puede exceder 100 caracteres";
        }

        if (fechaAsignacion == null) {
            return "Error: La fecha de asignación es obligatoria";
        }

        // Verificar que la tarea existe
        if (dTarea.findOneById(id) == null) {
            return "Error: No se encontró la tarea con ID: " + id;
        }

        // Verificar que el curso existe y está activo
        String[] curso = dCurso.findOneById(cursoId);
        if (curso == null) {
            return "Error: No se encontró el curso con ID: " + cursoId;
        }

        if (!"true".equals(curso[11])) {
            return "Error: No se pueden actualizar tareas de cursos inactivos";
        }

        try {
            String result = dTarea.update(id, cursoId, titulo.trim(), descripcion != null ? descripcion.trim() : null, fechaAsignacion);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTarea.disconnect();
            dCurso.disconnect();
        }
    }

    /**
     * Eliminar tarea
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que la tarea existe
        if (dTarea.findOneById(id) == null) {
            return "Error: No se encontró la tarea con ID: " + id;
        }

        try {
            String result = dTarea.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTarea.disconnect();
        }
    }

    /**
     * Listar todas las tareas
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dTarea.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTarea.disconnect();
        }
    }

    /**
     * Buscar tarea por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dTarea.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTarea.disconnect();
        }
    }

    /**
     * Buscar tareas por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dTarea.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTarea.disconnect();
        }
    }

    /**
     * Buscar tareas por fecha
     */
    public List<String[]> findByFecha(Date fecha) {
        if (fecha == null) {
            System.out.println("Error: La fecha es obligatoria");
            return null;
        }

        try {
            List<String[]> result = dTarea.findByFecha(fecha);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTarea.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Verificar que la fecha esté dentro del período del curso
     */
    private boolean isDateWithinCourse(Date fechaAsignacion, String[] curso) {
        try {
            Date fechaInicioCurso = Date.valueOf(curso[9]);  // fecha_inicio del curso
            Date fechaFinCurso = Date.valueOf(curso[10]);    // fecha_fin del curso

            return !fechaAsignacion.before(fechaInicioCurso) && !fechaAsignacion.after(fechaFinCurso);
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dTarea != null) {
            dTarea.disconnect();
        }
        if (dCurso != null) {
            dCurso.disconnect();
        }
    }
}