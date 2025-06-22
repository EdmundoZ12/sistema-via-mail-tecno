package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DGestion;

import java.sql.Date;
import java.util.List;

public class BGestion {

    private final DGestion dGestion;

    public BGestion() {
        this.dGestion = new DGestion();
    }

    public String crearGestion(String descripcion, Date fechaInicio, Date fechaFin, String nombre) {

        // Validación del nombre (obligatorio)
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        // Validación de fecha de inicio (obligatoria)
        if (fechaInicio == null) {
            return "Error: La fecha de inicio es obligatoria";
        }

        // Validación de fecha de fin (obligatoria)
        if (fechaFin == null) {
            return "Error: La fecha de fin es obligatoria";
        }

        // Validación que la fecha de fin sea posterior a la fecha de inicio
        if (fechaFin.before(fechaInicio)) {
            return "Error: La fecha de fin debe ser posterior a la fecha de inicio";
        }

        // Validación que las fechas no sean iguales
        if (fechaFin.equals(fechaInicio)) {
            return "Error: La fecha de fin debe ser diferente a la fecha de inicio";
        }

        try {
            String result = dGestion.crearGestion(
                    descripcion != null ? descripcion.trim() : null,
                    fechaInicio,
                    fechaFin,
                    nombre.trim()
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    public String actualizarGestion(int id, String descripcion, Date fechaInicio, Date fechaFin, String nombre) {

        // Validación del ID
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validación del nombre (obligatorio)
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        // Validación de fecha de inicio (obligatoria)
        if (fechaInicio == null) {
            return "Error: La fecha de inicio es obligatoria";
        }

        // Validación de fecha de fin (obligatoria)
        if (fechaFin == null) {
            return "Error: La fecha de fin es obligatoria";
        }

        // Validación que la fecha de fin sea posterior a la fecha de inicio
        if (fechaFin.before(fechaInicio)) {
            return "Error: La fecha de fin debe ser posterior a la fecha de inicio";
        }

        // Validación que las fechas no sean iguales
        if (fechaFin.equals(fechaInicio)) {
            return "Error: La fecha de fin debe ser diferente a la fecha de inicio";
        }

        try {
            String result = dGestion.actualizarGestion(
                    id,
                    descripcion != null ? descripcion.trim() : null,
                    fechaInicio,
                    fechaFin,
                    nombre.trim()
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    public String desactivarGestion(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Llamar a la capa de datos
        try {
            String result = dGestion.desactivarGestion(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    public String reactivarGestion(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Llamar a la capa de datos
        try {
            String result = dGestion.reactivarGestion(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dGestion.disconnect();
        }
    }

    public List<String[]> obtenerGestiones() {

        // Llamar a la capa de datos
        try {
            List<String[]> result = dGestion.obtenerGestiones();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    public String[] obtenerUnaGestion(int id) {

        // Validaciones de negocio
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        // Llamar a la capa de datos
        try {
            String[] result = dGestion.obtenerUnaGestion(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    public List<String[]> obtenerGestionesPorFecha(Date fechaInicio, Date fechaFin) {

        // Validaciones de negocio
        if (fechaInicio == null) {
            System.out.println("Error: La fecha de inicio es obligatoria");
            return null;
        }

        if (fechaFin == null) {
            System.out.println("Error: La fecha de fin es obligatoria");
            return null;
        }

        if (fechaFin.before(fechaInicio)) {
            System.out.println("Error: La fecha de fin debe ser posterior a la fecha de inicio");
            return null;
        }

        // Llamar a la capa de datos
        try {
            List<String[]> result = dGestion.obtenerGestionesPorFecha(fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }

    public List<String[]> buscarGestionesPorNombre(String nombre) {

        // Validaciones de negocio
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return null;
        }

        // Llamar a la capa de datos
        try {
            List<String[]> result = dGestion.buscarGestionesPorNombre(nombre.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dGestion.disconnect();
        }
    }
}