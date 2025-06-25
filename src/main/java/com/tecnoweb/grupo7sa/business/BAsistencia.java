package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DAsistencia;
import com.tecnoweb.grupo7sa.data.DInscripcion;

import java.sql.Date;
import java.util.List;

public class BAsistencia {

    private final DAsistencia dAsistencia;
    private final DInscripcion dInscripcion;

    public BAsistencia() {
        this.dAsistencia = new DAsistencia();
        this.dInscripcion = new DInscripcion();
    }

    // CU5 - LÓGICA DE NEGOCIO PARA ASISTENCIA

    /**
     * Registrar asistencia con validaciones
     */
    public String save(int inscripcionId, Date fecha, String estado) {

        // Validaciones básicas
        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (fecha == null) {
            return "Error: La fecha es obligatoria";
        }

        if (estado == null || estado.trim().isEmpty()) {
            return "Error: El estado es obligatorio";
        }

        if (!isValidEstado(estado)) {
            return "Error: El estado debe ser PRESENTE, AUSENTE o JUSTIFICADO";
        }

        // Verificar que la inscripción existe y está activa
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        if ("RETIRADO".equals(inscripcion[6])) {
            return "Error: No se puede registrar asistencia para estudiantes retirados";
        }

        // Verificar que no exista ya un registro para esta fecha
        if (existeAsistenciaEnFecha(inscripcionId, fecha)) {
            return "Error: Ya existe un registro de asistencia para esta fecha";
        }

        try {
            String result = dAsistencia.save(inscripcionId, fecha, estado.toUpperCase());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dAsistencia.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Actualizar registro de asistencia
     */
    public String update(int id, int inscripcionId, Date fecha, String estado) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (fecha == null) {
            return "Error: La fecha es obligatoria";
        }

        if (estado == null || estado.trim().isEmpty()) {
            return "Error: El estado es obligatorio";
        }

        if (!isValidEstado(estado)) {
            return "Error: El estado debe ser PRESENTE, AUSENTE o JUSTIFICADO";
        }

        // Verificar que el registro existe
        if (dAsistencia.findOneById(id) == null) {
            return "Error: No se encontró el registro de asistencia con ID: " + id;
        }

        try {
            String result = dAsistencia.update(id, inscripcionId, fecha, estado.toUpperCase());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Eliminar registro de asistencia
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el registro existe
        if (dAsistencia.findOneById(id) == null) {
            return "Error: No se encontró el registro de asistencia con ID: " + id;
        }

        try {
            String result = dAsistencia.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Listar todas las asistencias
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dAsistencia.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Buscar asistencia por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dAsistencia.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Buscar asistencias por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dAsistencia.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Buscar asistencias por inscripción ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByInscripcion(int inscripcionId) {
        if (inscripcionId <= 0) {
            System.out.println("Error: El ID de inscripción debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dAsistencia.findByInscripcion(inscripcionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Buscar asistencias por fecha
     */
    public List<String[]> findByFecha(Date fecha) {
        if (fecha == null) {
            System.out.println("Error: La fecha es obligatoria");
            return null;
        }

        try {
            List<String[]> result = dAsistencia.findByFecha(fecha);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    /**
     * Calcular porcentaje de asistencia ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getPorcentaje(int inscripcionId) {
        if (inscripcionId <= 0) {
            System.out.println("Error: El ID de inscripción debe ser mayor a 0");
            return null;
        }

        // Verificar que la inscripción existe
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            System.out.println("Error: No se encontró la inscripción con ID: " + inscripcionId);
            return null;
        }

        try {
            String[] result = dAsistencia.getPorcentaje(inscripcionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Obtener reporte completo de asistencia por curso ⭐ FUNCIONALIDAD ESPECIAL
     */
    public List<String[]> getReporteCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dAsistencia.getReporteCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dAsistencia.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Validar estados de asistencia
     */
    private boolean isValidEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }

        String estadoUpper = estado.toUpperCase();
        return estadoUpper.equals("PRESENTE") ||
                estadoUpper.equals("AUSENTE") ||
                estadoUpper.equals("JUSTIFICADO");
    }

    /**
     * Verificar si ya existe asistencia para una fecha específica
     */
    private boolean existeAsistenciaEnFecha(int inscripcionId, Date fecha) {
        try {
            List<String[]> asistencias = dAsistencia.findByInscripcion(inscripcionId);
            if (asistencias == null) return false;

            for (String[] asistencia : asistencias) {
                if (fecha.toString().equals(asistencia[2])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dAsistencia != null) {
            dAsistencia.disconnect();
        }
        if (dInscripcion != null) {
            dInscripcion.disconnect();
        }
    }
}