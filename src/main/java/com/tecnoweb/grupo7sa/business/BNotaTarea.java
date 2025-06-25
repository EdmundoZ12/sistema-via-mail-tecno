package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DNotaTarea;
import com.tecnoweb.grupo7sa.data.DTarea;
import com.tecnoweb.grupo7sa.data.DInscripcion;

import java.util.List;

public class BNotaTarea {

    private final DNotaTarea dNotaTarea;
    private final DTarea dTarea;
    private final DInscripcion dInscripcion;

    public BNotaTarea() {
        this.dNotaTarea = new DNotaTarea();
        this.dTarea = new DTarea();
        this.dInscripcion = new DInscripcion();
    }

    // CU5 - LÓGICA DE NEGOCIO PARA NOTAS DE TAREAS

    /**
     * Registrar nota de tarea con validaciones
     */
    public String save(int tareaId, int inscripcionId, double nota) {

        // Validaciones básicas
        if (tareaId <= 0) {
            return "Error: El ID de la tarea debe ser mayor a 0";
        }

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (nota < 0 || nota > 100) {
            return "Error: La nota debe estar entre 0 y 100";
        }

        // Verificar que la tarea existe
        String[] tarea = dTarea.findOneById(tareaId);
        if (tarea == null) {
            return "Error: No se encontró la tarea con ID: " + tareaId;
        }

        // Verificar que la inscripción existe y está activa
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        if ("RETIRADO".equals(inscripcion[6])) {
            return "Error: No se pueden registrar notas para estudiantes retirados";
        }

        // Verificar que la tarea pertenece al mismo curso de la inscripción
        int tareasCursoId = Integer.parseInt(tarea[1]);
        int inscripcionCursoId = Integer.parseInt(inscripcion[2]);

        if (tareasCursoId != inscripcionCursoId) {
            return "Error: La tarea no pertenece al curso del estudiante inscrito";
        }

        try {
            String result = dNotaTarea.save(tareaId, inscripcionId, nota);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dNotaTarea.disconnect();
            dTarea.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Actualizar nota de tarea
     */
    public String update(int id, int tareaId, int inscripcionId, double nota) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (tareaId <= 0) {
            return "Error: El ID de la tarea debe ser mayor a 0";
        }

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (nota < 0 || nota > 100) {
            return "Error: La nota debe estar entre 0 y 100";
        }

        // Verificar que la nota existe
        if (dNotaTarea.findOneById(id) == null) {
            return "Error: No se encontró la nota con ID: " + id;
        }

        // Verificar que la tarea existe
        if (dTarea.findOneById(tareaId) == null) {
            return "Error: No se encontró la tarea con ID: " + tareaId;
        }

        // Verificar que la inscripción existe
        if (dInscripcion.findOneById(inscripcionId) == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        try {
            String result = dNotaTarea.update(id, tareaId, inscripcionId, nota);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dNotaTarea.disconnect();
            dTarea.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Eliminar nota de tarea
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que la nota existe
        if (dNotaTarea.findOneById(id) == null) {
            return "Error: No se encontró la nota con ID: " + id;
        }

        try {
            String result = dNotaTarea.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dNotaTarea.disconnect();
        }
    }

    /**
     * Listar todas las notas
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dNotaTarea.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
        }
    }

    /**
     * Buscar nota por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dNotaTarea.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
        }
    }

    /**
     * Buscar notas por tarea
     */
    public List<String[]> findByTarea(int tareaId) {
        if (tareaId <= 0) {
            System.out.println("Error: El ID de la tarea debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dNotaTarea.findByTarea(tareaId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
        }
    }

    /**
     * Buscar notas por inscripción ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByInscripcion(int inscripcionId) {
        if (inscripcionId <= 0) {
            System.out.println("Error: El ID de inscripción debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dNotaTarea.findByInscripcion(inscripcionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
        }
    }

    /**
     * Calcular promedio de notas ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] calcularPromedio(int inscripcionId) {
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
            String[] result = dNotaTarea.calcularPromedio(inscripcionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Obtener estadísticas por tarea ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getEstadisticasTarea(int tareaId) {
        if (tareaId <= 0) {
            System.out.println("Error: El ID de la tarea debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dNotaTarea.getEstadisticasTarea(tareaId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dNotaTarea.disconnect();
        }
    }

    public void disconnect() {
        if (dNotaTarea != null) {
            dNotaTarea.disconnect();
        }
        if (dTarea != null) {
            dTarea.disconnect();
        }
        if (dInscripcion != null) {
            dInscripcion.disconnect();
        }
    }
}