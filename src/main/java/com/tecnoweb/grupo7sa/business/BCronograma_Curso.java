package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DCronograma_Curso;

import java.sql.Date;
import java.util.List;

public class BCronograma_Curso {

    private final DCronograma_Curso dCronograma_Curso;

    public BCronograma_Curso() {
        this.dCronograma_Curso = new DCronograma_Curso();
    }

    public String save(int cursoGestionId, String descripcion, String fase, Date fechaInicio, Date fechaFin) {

        // Validaciones de negocio
        if (cursoGestionId <= 0) {
            return "Error: El ID del curso-gestión debe ser mayor a 0";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }

        if (descripcion.trim().length() > 200) {
            return "Error: La descripción no puede exceder 200 caracteres";
        }

        if (fase == null || fase.trim().isEmpty()) {
            return "Error: La fase es obligatoria";
        }

        if (!isValidFase(fase)) {
            return "Error: La fase debe ser INSCRIPCIONES, CLASES, EXAMENES o EVALUACIONES";
        }

        if (fechaInicio == null) {
            return "Error: La fecha de inicio es obligatoria";
        }

        if (fechaFin == null) {
            return "Error: La fecha de fin es obligatoria";
        }

        if (!isValidDateRange(fechaInicio, fechaFin)) {
            return "Error: La fecha de inicio debe ser anterior a la fecha de fin";
        }

        // Llamar a la capa de datos
        try {
            String result = dCronograma_Curso.save(cursoGestionId, descripcion.trim(),
                    fase.toUpperCase(), fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public String update(int id, int cursoGestionId, String descripcion, String fase, Date fechaInicio, Date fechaFin) {

        // Validaciones de negocio
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (cursoGestionId <= 0) {
            return "Error: El ID del curso-gestión debe ser mayor a 0";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }

        if (descripcion.trim().length() > 200) {
            return "Error: La descripción no puede exceder 200 caracteres";
        }

        if (fase == null || fase.trim().isEmpty()) {
            return "Error: La fase es obligatoria";
        }

        if (!isValidFase(fase)) {
            return "Error: La fase debe ser INSCRIPCIONES, CLASES, EXAMENES o EVALUACIONES";
        }

        if (fechaInicio == null) {
            return "Error: La fecha de inicio es obligatoria";
        }

        if (fechaFin == null) {
            return "Error: La fecha de fin es obligatoria";
        }

        if (!isValidDateRange(fechaInicio, fechaFin)) {
            return "Error: La fecha de inicio debe ser anterior a la fecha de fin";
        }

        // Llamar a la capa de datos
        try {
            String result = dCronograma_Curso.update(id, cursoGestionId, descripcion.trim(),
                    fase.toUpperCase(), fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public String delete(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Llamar a la capa de datos
        try {
            String result = dCronograma_Curso.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public List<String[]> findAllCronogramas() {

        // Llamar a la capa de datos
        try {
            List<String[]> result = dCronograma_Curso.findAllCronogramas();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public String[] findOneById(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        // Llamar a la capa de datos
        try {
            String[] result = dCronograma_Curso.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public List<String[]> findByCursoGestion(int cursoGestionId) {

        // Validaciones de negocio
        if (cursoGestionId <= 0) {
            System.out.println("Error: El ID del curso-gestión debe ser mayor a 0");
            return null;
        }

        // Llamar a la capa de datos
        try {
            List<String[]> result = dCronograma_Curso.findByCursoGestion(cursoGestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public List<String[]> findByFase(String fase) {

        // Validaciones de negocio
        if (fase == null || fase.trim().isEmpty()) {
            System.out.println("Error: La fase es obligatoria");
            return null;
        }

        if (!isValidFase(fase)) {
            System.out.println("Error: La fase debe ser INSCRIPCIONES, CLASES, EXAMENES o EVALUACIONES");
            return null;
        }

        // Llamar a la capa de datos
        try {
            List<String[]> result = dCronograma_Curso.findByFase(fase.toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public List<String[]> findByFechaRange(Date fechaInicio, Date fechaFin) {

        // Validaciones de negocio
        if (fechaInicio == null) {
            System.out.println("Error: La fecha de inicio es obligatoria");
            return null;
        }

        if (fechaFin == null) {
            System.out.println("Error: La fecha de fin es obligatoria");
            return null;
        }

        if (!isValidDateRange(fechaInicio, fechaFin)) {
            System.out.println("Error: La fecha de inicio debe ser anterior a la fecha de fin");
            return null;
        }

        // Llamar a la capa de datos
        try {
            List<String[]> result = dCronograma_Curso.findByFechaRange(fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    public String reactivate(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Llamar a la capa de datos
        try {
            String result = dCronograma_Curso.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCronograma_Curso.disconnect();
        }
    }

    // Métodos auxiliares de validación
    private boolean isValidFase(String fase) {
        if (fase == null || fase.trim().isEmpty()) {
            return false;
        }

        String faseUpper = fase.toUpperCase();
        return faseUpper.equals("INSCRIPCIONES") ||
                faseUpper.equals("CLASES") ||
                faseUpper.equals("EXAMENES") ||
                faseUpper.equals("EVALUACIONES");
    }

    private boolean isValidDateRange(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }

        // La fecha de inicio debe ser anterior o igual a la fecha de fin
        return !fechaInicio.after(fechaFin);
    }
}