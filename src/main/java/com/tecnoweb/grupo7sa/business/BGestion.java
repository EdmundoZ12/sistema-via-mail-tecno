package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DGestion;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BGestion {

    private final DGestion dGestion;

    public BGestion() {
        this.dGestion = new DGestion();
    }

    // CU2 - LÓGICA DE NEGOCIO PARA GESTIONES ACADÉMICAS

    /**
     * Crear nueva gestión con validaciones de negocio
     */
    public String save(String nombre, String descripcion, Date fechaInicio, Date fechaFin) {

        // Validaciones básicas obligatorias
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
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

        // Validación de duración mínima (al menos 30 días)
        if (!isValidDuration(fechaInicio, fechaFin)) {
            return "Error: La gestión debe tener una duración mínima de 30 días";
        }

        // Validar que no haya conflicto con gestiones existentes
        if (hasDateConflict(fechaInicio, fechaFin)) {
            return "Error: Ya existe una gestión activa que se solapa con estas fechas";
        }

        try {
            String result = dGestion.save(
                    nombre.trim(),
                    descripcion != null ? descripcion.trim() : null,
                    fechaInicio,
                    fechaFin
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Actualizar gestión existente con validaciones
     */
    public String update(int id, String nombre, String descripcion, Date fechaInicio, Date fechaFin) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
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

        // Validación de duración mínima
        if (!isValidDuration(fechaInicio, fechaFin)) {
            return "Error: La gestión debe tener una duración mínima de 30 días";
        }

        // Verificar que la gestión existe
        if (dGestion.findOneById(id) == null) {
            return "Error: No se encontró la gestión con ID: " + id;
        }

        // Validar conflictos de fechas (excluyendo la gestión actual)
        if (hasDateConflictExcluding(id, fechaInicio, fechaFin)) {
            return "Error: Ya existe otra gestión activa que se solapa con estas fechas";
        }

        try {
            String result = dGestion.update(
                    id,
                    nombre.trim(),
                    descripcion != null ? descripcion.trim() : null,
                    fechaInicio,
                    fechaFin
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Desactivar gestión
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que la gestión existe
        if (dGestion.findOneById(id) == null) {
            return "Error: No se encontró la gestión con ID: " + id;
        }

        // Verificar si la gestión está siendo usada por cursos
        if (isGestionBeingUsed(id)) {
            return "Error: No se puede desactivar la gestión porque tiene cursos asignados";
        }

        try {
            String result = dGestion.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Reactivar gestión
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que la gestión existe
        if (dGestion.findOneById(id) == null) {
            return "Error: No se encontró la gestión con ID: " + id;
        }

        try {
            String result = dGestion.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Listar todas las gestiones activas
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dGestion.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Buscar gestión por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dGestion.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Buscar gestiones por rango de fechas
     */
    public List<String[]> findByDateRange(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null) {
            System.out.println("Error: La fecha de inicio es obligatoria");
            return null;
        }

        if (fechaFin == null) {
            System.out.println("Error: La fecha de fin es obligatoria");
            return null;
        }

        if (!isValidDateRange(fechaInicio, fechaFin)) {
            System.out.println("Error: La fecha de fin debe ser posterior a la fecha de inicio");
            return null;
        }

        try {
            List<String[]> result = dGestion.findByDateRange(fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Obtener gestiones vigentes actualmente (⭐ ENDPOINT IMPORTANTE)
     */
    public List<String[]> findCurrent() {
        try {
            List<String[]> result = dGestion.findCurrent();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    /**
     * Buscar gestiones por nombre
     */
    public List<String[]> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return null;
        }

        try {
            List<String[]> result = dGestion.findByName(nombre.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Validar que la fecha de fin sea posterior a la fecha de inicio
     */
    private boolean isValidDateRange(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return fechaFin.after(fechaInicio);
    }

    /**
     * Validar que la gestión tenga duración mínima de 30 días
     */
    private boolean isValidDuration(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }

        LocalDate inicio = fechaInicio.toLocalDate();
        LocalDate fin = fechaFin.toLocalDate();
        long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin);

        return dias >= 30;
    }

    /**
     * Verificar si existe conflicto de fechas con otras gestiones
     */
    private boolean hasDateConflict(Date fechaInicio, Date fechaFin) {
        try {
            List<String[]> gestiones = dGestion.findAll();
            if (gestiones == null || gestiones.isEmpty()) {
                return false;
            }

            for (String[] gestion : gestiones) {
                Date existingInicio = Date.valueOf(gestion[3]);
                Date existingFin = Date.valueOf(gestion[4]);

                // Verificar solapamiento de fechas
                if (!(fechaFin.before(existingInicio) || fechaInicio.after(existingFin))) {
                    return true; // Hay conflicto
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar conflicto de fechas excluyendo una gestión específica
     */
    private boolean hasDateConflictExcluding(int excludeId, Date fechaInicio, Date fechaFin) {
        try {
            List<String[]> gestiones = dGestion.findAll();
            if (gestiones == null || gestiones.isEmpty()) {
                return false;
            }

            for (String[] gestion : gestiones) {
                int gestionId = Integer.parseInt(gestion[0]);
                if (gestionId == excludeId) {
                    continue; // Saltar la gestión que estamos actualizando
                }

                Date existingInicio = Date.valueOf(gestion[3]);
                Date existingFin = Date.valueOf(gestion[4]);

                // Verificar solapamiento de fechas
                if (!(fechaFin.before(existingInicio) || fechaInicio.after(existingFin))) {
                    return true; // Hay conflicto
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si la gestión está siendo usada por cursos
     * Esta implementación básica siempre retorna false por ahora
     * Se implementará completamente cuando se desarrolle CU3
     */
    private boolean isGestionBeingUsed(int gestionId) {
        // TODO: Implementar cuando se desarrolle la tabla CURSO
        // Verificar si existe algún curso con gestion_id = gestionId
        return false;
    }

    public void disconnect() {
        if (dGestion != null) {
            dGestion.disconnect();
        }
    }
}