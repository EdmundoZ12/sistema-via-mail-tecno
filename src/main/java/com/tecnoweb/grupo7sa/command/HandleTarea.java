package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BTarea;

import java.sql.Date;
import java.util.List;

public class HandleTarea {

    // CU5 - HANDLERS PARA GESTIÓN DE TAREAS

    /**
     * Crear nueva tarea
     * Parámetros: cursoId, titulo, descripcion, fechaAsignacion (YYYY-MM-DD)
     */
    public static String save(String params) {
        String[] parts = params.split(",\\s*");

        if (parts.length < 4) {
            return "Error: Número de parámetros incorrecto. Esperados: cursoId, titulo, descripcion, fechaAsignacion";
        }

        try {
            int cursoId = Integer.parseInt(parts[0].trim());
            String titulo = parts[1].trim();
            String descripcion = parts[2].trim();
            Date fechaAsignacion = Date.valueOf(parts[3].trim());

            BTarea bTarea = new BTarea();
            return bTarea.save(cursoId, titulo, descripcion, fechaAsignacion);
        } catch (NumberFormatException e) {
            return "Error: El cursoId debe ser numérico. " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Error: La fecha debe tener el formato YYYY-MM-DD. " + e.getMessage();
        } catch (Exception e) {
            return "Error al guardar tarea: " + e.getMessage();
        }
    }

    /**
     * Actualizar tarea
     * Parámetros: id, cursoId, titulo, descripcion, fechaAsignacion
     */
    public static String update(String params) {
        String[] parts = params.split(",\\s*");

        if (parts.length < 5) {
            return "Error: Número de parámetros incorrecto. Esperados: id, cursoId, titulo, descripcion, fechaAsignacion";
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            int cursoId = Integer.parseInt(parts[1].trim());
            String titulo = parts[2].trim();
            String descripcion = parts[3].trim();
            Date fechaAsignacion = Date.valueOf(parts[4].trim());

            BTarea bTarea = new BTarea();
            return bTarea.update(id, cursoId, titulo, descripcion, fechaAsignacion);
        } catch (NumberFormatException e) {
            return "Error: id y cursoId deben ser numéricos. " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Error: La fecha debe tener el formato YYYY-MM-DD. " + e.getMessage();
        } catch (Exception e) {
            return "Error al actualizar tarea: " + e.getMessage();
        }
    }

    /**
     * Eliminar tarea
     * Parámetro: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BTarea bTarea = new BTarea();
            return bTarea.delete(id);
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar tarea: " + e.getMessage();
        }
    }

    /**
     * Listar todas las tareas
     */
    public static String findAll() {
        try {
            BTarea bTarea = new BTarea();
            List<String[]> tareas = bTarea.findAll();

            if (tareas == null || tareas.isEmpty()) {
                return "No hay tareas registradas.";
            }

            StringBuilder sb = new StringBuilder("=== LISTA DE TAREAS ===\n");
            for (String[] tarea : tareas) {
                // [0]id, [1]cursoId, [2]titulo, [3]descripcion, [4]fechaAsignacion
                sb.append("ID: ").append(tarea[0])
                        .append(" | Curso ID: ").append(tarea[1])
                        .append(" | Título: ").append(tarea[2])
                        .append(" | Fecha: ").append(tarea[4])
                        .append("\n");
            }
            sb.append("Total tareas: ").append(tareas.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al listar tareas: " + e.getMessage();
        }
    }

    /**
     * Buscar tarea por ID
     * Parámetro: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BTarea bTarea = new BTarea();
            String[] tarea = bTarea.findOneById(id);

            if (tarea == null) {
                return "No se encontró tarea con ID: " + id;
            }

            return "Tarea encontrada:\n" +
                    "ID: " + tarea[0] + "\n" +
                    "Curso ID: " + tarea[1] + "\n" +
                    "Título: " + tarea[2] + "\n" +
                    "Descripción: " + tarea[3] + "\n" +
                    "Fecha de Asignación: " + tarea[4];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar tarea: " + e.getMessage();
        }
    }

    /**
     * Buscar tareas por curso
     * Parámetro: cursoId
     */
    public static String findByCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BTarea bTarea = new BTarea();
            List<String[]> tareas = bTarea.findByCurso(cursoId);

            if (tareas == null || tareas.isEmpty()) {
                return "No hay tareas para el curso con ID: " + cursoId;
            }

            StringBuilder sb = new StringBuilder("=== TAREAS DEL CURSO " + cursoId + " ===\n");
            for (String[] tarea : tareas) {
                sb.append("ID: ").append(tarea[0])
                        .append(" | Título: ").append(tarea[2])
                        .append(" | Fecha: ").append(tarea[4])
                        .append("\n");
            }
            sb.append("Total tareas: ").append(tareas.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar tareas por curso: " + e.getMessage();
        }
    }

    /**
     * Buscar tareas por fecha
     * Parámetro: fecha (YYYY-MM-DD)
     */
    public static String findByFecha(String params) {
        try {
            Date fecha = Date.valueOf(params.trim());
            BTarea bTarea = new BTarea();
            List<String[]> tareas = bTarea.findByFecha(fecha);

            if (tareas == null || tareas.isEmpty()) {
                return "No hay tareas asignadas para la fecha: " + fecha;
            }

            StringBuilder sb = new StringBuilder("=== TAREAS PARA LA FECHA " + fecha + " ===\n");
            for (String[] tarea : tareas) {
                sb.append("ID: ").append(tarea[0])
                        .append(" | Curso ID: ").append(tarea[1])
                        .append(" | Título: ").append(tarea[2])
                        .append("\n");
            }
            sb.append("Total tareas: ").append(tareas.size());
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "Error: La fecha debe tener el formato YYYY-MM-DD. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar tareas por fecha: " + e.getMessage();
        }
    }
}
