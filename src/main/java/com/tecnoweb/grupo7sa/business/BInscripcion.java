package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DInscripcion;
import com.tecnoweb.grupo7sa.data.DParticipante;
import com.tecnoweb.grupo7sa.data.DCurso;

import java.util.List;

public class BInscripcion {

    private final DInscripcion dInscripcion;
    private final DParticipante dParticipante;
    private final DCurso dCurso;

    public BInscripcion() {
        this.dInscripcion = new DInscripcion();
        this.dParticipante = new DParticipante();
        this.dCurso = new DCurso();
    }

    // CU4 - LÓGICA DE NEGOCIO PARA INSCRIPCIONES

    /**
     * Listar todas las inscripciones activas
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dInscripcion.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Buscar inscripción por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dInscripcion.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Lista de inscritos por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dInscripcion.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Historial de un participante
     */
    public List<String[]> findByParticipante(int participanteId) {
        if (participanteId <= 0) {
            System.out.println("Error: El ID del participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dInscripcion.findByParticipante(participanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Calificar estudiante ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updateNota(int inscripcionId, double notaFinal, String estado) {

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (notaFinal < 0 || notaFinal > 100) {
            return "Error: La nota debe estar entre 0 y 100";
        }

        if (estado == null || estado.trim().isEmpty()) {
            return "Error: El estado es obligatorio";
        }

        if (!isValidEstadoNota(estado)) {
            return "Error: El estado debe ser APROBADO o REPROBADO";
        }

        // Verificar que la inscripción existe
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        // Verificar que está en estado INSCRITO
        if (!"INSCRITO".equals(inscripcion[6])) {
            return "Error: Solo se pueden calificar inscripciones en estado INSCRITO";
        }

        // Validar coherencia entre nota y estado
        if (!isNotaCoherenteConEstado(notaFinal, estado)) {
            return "Error: La nota no es coherente con el estado (APROBADO >= 51, REPROBADO < 51)";
        }

        try {
            String result = dInscripcion.updateNota(inscripcionId, notaFinal, estado);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Retirar participante ⭐ FUNCIONALIDAD ESPECIAL
     * Retira estudiante y libera cupo
     */
    public String withdraw(int inscripcionId, String observaciones) {

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (observaciones == null || observaciones.trim().isEmpty()) {
            return "Error: Las observaciones son obligatorias para retirar un participante";
        }

        // Verificar que la inscripción existe
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        // Verificar que no está ya retirado
        if ("RETIRADO".equals(inscripcion[6])) {
            return "Error: El participante ya está retirado de este curso";
        }

        try {
            String result = dInscripcion.withdraw(inscripcionId, observaciones);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dInscripcion.disconnect();
        }
    }

    /**
     * Obtener estadísticas por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> getEstadisticas() {
        try {
            List<String[]> result = dInscripcion.getEstadisticas();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dInscripcion.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Validar estados válidos para calificación
     */
    private boolean isValidEstadoNota(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }

        String estadoUpper = estado.toUpperCase();
        return estadoUpper.equals("APROBADO") || estadoUpper.equals("REPROBADO");
    }

    /**
     * Validar coherencia entre nota y estado
     */
    private boolean isNotaCoherenteConEstado(double nota, String estado) {
        String estadoUpper = estado.toUpperCase();

        if (estadoUpper.equals("APROBADO")) {
            return nota >= 51.0; // Nota mínima para aprobar
        } else if (estadoUpper.equals("REPROBADO")) {
            return nota < 51.0; // Nota insuficiente
        }

        return false;
    }

    /**
     * Verificar si existe el participante y está activo
     */
    private boolean existsParticipante(int participanteId) {
        try {
            String[] participante = dParticipante.findOneById(participanteId);
            return participante != null && participante[10].equals("true"); // activo = true
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si existe el curso y está activo
     */
    private boolean existsCurso(int cursoId) {
        try {
            String[] curso = dCurso.findOneById(cursoId);
            return curso != null && curso[11].equals("true"); // activo = true
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dInscripcion != null) {
            dInscripcion.disconnect();
        }
        if (dParticipante != null) {
            dParticipante.disconnect();
        }
        if (dCurso != null) {
            dCurso.disconnect();
        }
    }
}