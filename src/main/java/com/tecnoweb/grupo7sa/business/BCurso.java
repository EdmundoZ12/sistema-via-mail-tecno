package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DCurso;
import com.tecnoweb.grupo7sa.data.DUsuario;
import com.tecnoweb.grupo7sa.data.DGestion;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BCurso {

    private final DCurso dCurso;
    private final DUsuario dUsuario;
    private final DGestion dGestion;

    public BCurso() {
        this.dCurso = new DCurso();
        this.dUsuario = new DUsuario();
        this.dGestion = new DGestion();
    }

    // CU3 - LÓGICA DE NEGOCIO PARA CURSOS

    /**
     * Crear nuevo curso con validaciones completas
     */
    public String save(String nombre, String descripcion, int duracionHoras, String nivel, String logoUrl,
                       int tutorId, int gestionId, String aula, int cuposTotales, Date fechaInicio, Date fechaFin) {

        // Validaciones básicas obligatorias
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre del curso es obligatorio";
        }

        if (duracionHoras <= 0) {
            return "Error: La duración debe ser mayor a 0 horas";
        }

        if (nivel == null || nivel.trim().isEmpty()) {
            return "Error: El nivel es obligatorio";
        }

        if (!isValidNivel(nivel)) {
            return "Error: El nivel debe ser Básico, Intermedio o Avanzado";
        }

        if (tutorId <= 0) {
            return "Error: Debe asignar un tutor válido";
        }

        if (gestionId <= 0) {
            return "Error: Debe asignar una gestión válida";
        }

        if (aula == null || aula.trim().isEmpty()) {
            return "Error: El aula es obligatoria";
        }

        if (cuposTotales <= 0) {
            return "Error: Los cupos totales deben ser mayor a 0";
        }

        if (fechaInicio == null) {
            return "Error: La fecha de inicio es obligatoria";
        }

        if (fechaFin == null) {
            return "Error: La fecha de fin es obligatoria";
        }

        // Validaciones de fechas
        if (!isValidDateRange(fechaInicio, fechaFin)) {
            return "Error: La fecha de fin debe ser posterior a la fecha de inicio";
        }

        // Validación de duración mínima (al menos 7 días)
        if (!isValidCourseDuration(fechaInicio, fechaFin)) {
            return "Error: El curso debe tener una duración mínima de 7 días";
        }

        // Verificar que el tutor existe y tiene rol TUTOR
        if (!isValidTutor(tutorId)) {
            return "Error: El tutor seleccionado no existe o no tiene rol TUTOR";
        }

        // Verificar que la gestión existe y está activa
        if (!isValidGestion(gestionId)) {
            return "Error: La gestión seleccionada no existe o no está activa";
        }

        // Verificar que las fechas del curso estén dentro de la gestión
        if (!isCourseDateWithinGestion(fechaInicio, fechaFin, gestionId)) {
            return "Error: Las fechas del curso deben estar dentro del período de la gestión";
        }

        // Validar que no haya conflicto de aula y horario
        if (hasAulaConflict(aula, fechaInicio, fechaFin)) {
            return "Error: Ya existe otro curso programado en la misma aula durante estas fechas";
        }

        try {
            String result = dCurso.save(
                    nombre.trim(),
                    descripcion != null ? descripcion.trim() : null,
                    duracionHoras,
                    nivel.trim(),
                    logoUrl != null ? logoUrl.trim() : null,
                    tutorId,
                    gestionId,
                    aula.trim(),
                    cuposTotales,
                    fechaInicio,
                    fechaFin
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
            dUsuario.disconnect();
            dGestion.disconnect();
        }
    }

    /**
     * Actualizar curso existente con validaciones
     */
    public String update(int id, String nombre, String descripcion, int duracionHoras, String nivel, String logoUrl,
                         int tutorId, int gestionId, String aula, int cuposTotales, Date fechaInicio, Date fechaFin) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el curso existe
        if (dCurso.findOneById(id) == null) {
            return "Error: No se encontró el curso con ID: " + id;
        }

        // Aplicar las mismas validaciones que en save
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre del curso es obligatorio";
        }

        if (duracionHoras <= 0) {
            return "Error: La duración debe ser mayor a 0 horas";
        }

        if (nivel == null || nivel.trim().isEmpty()) {
            return "Error: El nivel es obligatorio";
        }

        if (!isValidNivel(nivel)) {
            return "Error: El nivel debe ser Básico, Intermedio o Avanzado";
        }

        if (tutorId <= 0) {
            return "Error: Debe asignar un tutor válido";
        }

        if (gestionId <= 0) {
            return "Error: Debe asignar una gestión válida";
        }

        if (aula == null || aula.trim().isEmpty()) {
            return "Error: El aula es obligatoria";
        }

        if (cuposTotales <= 0) {
            return "Error: Los cupos totales deben ser mayor a 0";
        }

        if (fechaInicio == null || fechaFin == null) {
            return "Error: Las fechas son obligatorias";
        }

        if (!isValidDateRange(fechaInicio, fechaFin)) {
            return "Error: La fecha de fin debe ser posterior a la fecha de inicio";
        }

        if (!isValidTutor(tutorId)) {
            return "Error: El tutor seleccionado no existe o no tiene rol TUTOR";
        }

        if (!isValidGestion(gestionId)) {
            return "Error: La gestión seleccionada no existe o no está activa";
        }

        try {
            String result = dCurso.update(
                    id,
                    nombre.trim(),
                    descripcion != null ? descripcion.trim() : null,
                    duracionHoras,
                    nivel.trim(),
                    logoUrl != null ? logoUrl.trim() : null,
                    tutorId,
                    gestionId,
                    aula.trim(),
                    cuposTotales,
                    fechaInicio,
                    fechaFin
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
            dUsuario.disconnect();
            dGestion.disconnect();
        }
    }

    /**
     * Desactivar curso (soft delete)
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el curso existe
        if (dCurso.findOneById(id) == null) {
            return "Error: No se encontró el curso con ID: " + id;
        }

        // Verificar si el curso tiene inscripciones
        if (hasCourseInscriptions(id)) {
            return "Error: No se puede desactivar el curso porque tiene inscripciones";
        }

        try {
            String result = dCurso.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Eliminar curso permanentemente ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String deletePermanent(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el curso existe
        if (dCurso.findOneById(id) == null) {
            return "Error: No se encontró el curso con ID: " + id;
        }

        // Verificar si el curso tiene inscripciones
        if (hasCourseInscriptions(id)) {
            return "Error: No se puede eliminar permanentemente el curso porque tiene inscripciones";
        }

        try {
            String result = dCurso.deletePermanent(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Reactivar curso
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el curso existe
        if (dCurso.findOneById(id) == null) {
            return "Error: No se encontró el curso con ID: " + id;
        }

        try {
            String result = dCurso.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Listar todos los cursos con información completa ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dCurso.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Buscar cursos con cupos disponibles ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findWithSpots() {
        try {
            List<String[]> result = dCurso.findWithSpots();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Buscar curso por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dCurso.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Buscar cursos por gestión
     */
    public List<String[]> findByGestion(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dCurso.findByGestion(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Buscar cursos por tutor
     */
    public List<String[]> findByTutor(int tutorId) {
        if (tutorId <= 0) {
            System.out.println("Error: El ID de tutor debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dCurso.findByTutor(tutorId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    /**
     * Actualizar cupos del curso ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updateCupos(int cursoId, int nuevoCupos) {
        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        if (nuevoCupos <= 0) {
            return "Error: Los nuevos cupos deben ser mayor a 0";
        }

        // Verificar que el curso existe
        String[] curso = dCurso.findOneById(cursoId);
        if (curso == null) {
            return "Error: No se encontró el curso con ID: " + cursoId;
        }

        // Verificar que los nuevos cupos no sean menores a los ocupados
        int cuposOcupados = Integer.parseInt(curso[8]);
        if (nuevoCupos < cuposOcupados) {
            return "Error: Los nuevos cupos (" + nuevoCupos + ") no pueden ser menores a los cupos ocupados (" + cuposOcupados + ")";
        }

        try {
            String result = dCurso.updateCupos(cursoId, nuevoCupos);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

    // Métodos auxiliares de validación

    private boolean isValidDateRange(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return fechaFin.after(fechaInicio);
    }

    private boolean isValidCourseDuration(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }

        LocalDate inicio = fechaInicio.toLocalDate();
        LocalDate fin = fechaFin.toLocalDate();
        long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin);

        return dias >= 7; // Mínimo 7 días
    }

    private boolean isValidNivel(String nivel) {
        if (nivel == null || nivel.trim().isEmpty()) {
            return false;
        }

        String nivelUpper = nivel.toUpperCase();
        return nivelUpper.equals("BÁSICO") || nivelUpper.equals("BASICO") ||
                nivelUpper.equals("INTERMEDIO") || nivelUpper.equals("AVANZADO");
    }

    private boolean isValidTutor(int tutorId) {
        try {
            String[] usuario = dUsuario.findOneById(tutorId);
            return usuario != null && usuario[6].equals("TUTOR") && usuario[8].equals("true");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidGestion(int gestionId) {
        try {
            String[] gestion = dGestion.findOneById(gestionId);
            return gestion != null && gestion[5].equals("true"); // activo = true
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCourseDateWithinGestion(Date fechaInicio, Date fechaFin, int gestionId) {
        try {
            String[] gestion = dGestion.findOneById(gestionId);
            if (gestion == null) return false;

            Date gestionInicio = Date.valueOf(gestion[3]);
            Date gestionFin = Date.valueOf(gestion[4]);

            return !fechaInicio.before(gestionInicio) && !fechaFin.after(gestionFin);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasAulaConflict(String aula, Date fechaInicio, Date fechaFin) {
        // Esta implementación básica siempre retorna false por ahora
        // Se puede implementar una consulta para verificar conflictos de aula
        return false;
    }

    private boolean hasCourseInscriptions(int cursoId) {
        // Esta implementación básica siempre retorna false por ahora
        // Se implementará completamente cuando se desarrolle CU4
        return false;
    }

    public void disconnect() {
        if (dCurso != null) {
            dCurso.disconnect();
        }
        if (dUsuario != null) {
            dUsuario.disconnect();
        }
        if (dGestion != null) {
            dGestion.disconnect();
        }
    }
}