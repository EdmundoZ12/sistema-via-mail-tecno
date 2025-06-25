package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DPreinscripcion;
import com.tecnoweb.grupo7sa.data.DParticipante;
import com.tecnoweb.grupo7sa.data.DCurso;
import com.tecnoweb.grupo7sa.data.DInscripcion;

import java.util.List;

public class BPreinscripcion {

    private final DPreinscripcion dPreinscripcion;
    private final DParticipante dParticipante;
    private final DCurso dCurso;
    private final DInscripcion dInscripcion;

    public BPreinscripcion() {
        this.dPreinscripcion = new DPreinscripcion();
        this.dParticipante = new DParticipante();
        this.dCurso = new DCurso();
        this.dInscripcion = new DInscripcion();
    }

    // CU4 - LÓGICA DE NEGOCIO PARA PREINSCRIPCIONES

    /**
     * Crear nueva preinscripción con validaciones
     */
    public String save(int participanteId, int cursoId, String observaciones) {

        // Validaciones básicas
        if (participanteId <= 0) {
            return "Error: El ID del participante debe ser mayor a 0";
        }

        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        // Verificar que el participante existe y está activo
        if (!existsParticipante(participanteId)) {
            return "Error: El participante seleccionado no existe o no está activo";
        }

        // Verificar que el curso existe y está activo
        if (!existsCurso(cursoId)) {
            return "Error: El curso seleccionado no existe o no está activo";
        }

        // Verificar que el curso tiene cupos disponibles
        if (!hasCuposDisponibles(cursoId)) {
            return "Error: El curso no tiene cupos disponibles";
        }

        // Verificar que no existe ya una preinscripción pendiente
        if (existsPreinscripcionPendiente(participanteId, cursoId)) {
            return "Error: Ya existe una preinscripción pendiente para este participante y curso";
        }

        // Verificar que no está ya inscrito en el curso
        if (existsInscripcionActiva(participanteId, cursoId)) {
            return "Error: El participante ya está inscrito en este curso";
        }

        try {
            String result = dPreinscripcion.save(participanteId, cursoId, observaciones);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPreinscripcion.disconnect();
            dParticipante.disconnect();
            dCurso.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Aprobar preinscripción ⭐ FUNCIONALIDAD ESPECIAL
     * Aprueba y crea inscripción automáticamente
     */
    public String approve(int preinscripcionId, String observaciones) {

        if (preinscripcionId <= 0) {
            return "Error: El ID de preinscripción debe ser mayor a 0";
        }

        // Verificar que la preinscripción existe
        String[] preinscripcion = dPreinscripcion.findOneById(preinscripcionId);
        if (preinscripcion == null) {
            return "Error: No se encontró la preinscripción con ID: " + preinscripcionId;
        }

        // Verificar que está en estado PENDIENTE
        if (!"PENDIENTE".equals(preinscripcion[4])) {
            return "Error: Solo se pueden aprobar preinscripciones en estado PENDIENTE";
        }

        // Verificar que el curso aún tiene cupos disponibles
        int cursoId = Integer.parseInt(preinscripcion[2]);
        if (!hasCuposDisponibles(cursoId)) {
            return "Error: El curso ya no tiene cupos disponibles";
        }

        try {
            String result = dPreinscripcion.approve(preinscripcionId, observaciones);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPreinscripcion.disconnect();
            dCurso.disconnect();
        }
    }

    /**
     * Rechazar preinscripción
     */
    public String reject(int preinscripcionId, String observaciones) {

        if (preinscripcionId <= 0) {
            return "Error: El ID de preinscripción debe ser mayor a 0";
        }

        // Verificar que la preinscripción existe
        String[] preinscripcion = dPreinscripcion.findOneById(preinscripcionId);
        if (preinscripcion == null) {
            return "Error: No se encontró la preinscripción con ID: " + preinscripcionId;
        }

        // Verificar que está en estado PENDIENTE
        if (!"PENDIENTE".equals(preinscripcion[4])) {
            return "Error: Solo se pueden rechazar preinscripciones en estado PENDIENTE";
        }

        if (observaciones == null || observaciones.trim().isEmpty()) {
            return "Error: Las observaciones son obligatorias para rechazar una preinscripción";
        }

        try {
            String result = dPreinscripcion.reject(preinscripcionId, observaciones);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Listar preinscripciones pendientes con precios ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dPreinscripcion.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Buscar preinscripción por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPreinscripcion.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Buscar preinscripciones por curso
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dPreinscripcion.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Buscar preinscripciones por participante
     */
    public List<String[]> findByParticipante(int participanteId) {
        if (participanteId <= 0) {
            System.out.println("Error: El ID del participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dPreinscripcion.findByParticipante(participanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPreinscripcion.disconnect();
        }
    }

    // Métodos auxiliares de validación

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

    /**
     * Verificar si el curso tiene cupos disponibles
     */
    private boolean hasCuposDisponibles(int cursoId) {
        try {
            String[] curso = dCurso.findOneById(cursoId);
            if (curso == null) return false;

            int cuposTotales = Integer.parseInt(curso[7]);
            int cuposOcupados = Integer.parseInt(curso[8]);

            return cuposOcupados < cuposTotales;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si existe preinscripción pendiente
     */
    private boolean existsPreinscripcionPendiente(int participanteId, int cursoId) {
        try {
            List<String[]> preinscripciones = dPreinscripcion.findByParticipante(participanteId);
            if (preinscripciones == null) return false;

            for (String[] preinscripcion : preinscripciones) {
                int preCursoId = Integer.parseInt(preinscripcion[2]);
                String estado = preinscripcion[4];

                if (preCursoId == cursoId && "PENDIENTE".equals(estado)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si existe inscripción activa
     */
    private boolean existsInscripcionActiva(int participanteId, int cursoId) {
        try {
            return dInscripcion.existsInscripcion(participanteId, cursoId);
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dPreinscripcion != null) {
            dPreinscripcion.disconnect();
        }
        if (dParticipante != null) {
            dParticipante.disconnect();
        }
        if (dCurso != null) {
            dCurso.disconnect();
        }
        if (dInscripcion != null) {
            dInscripcion.disconnect();
        }
    }
}