package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BNotaTarea;

import java.util.List;

public class HandleNotaTarea {

    // CU5 - HANDLER PARA NOTAS DE TAREAS

    /**
     * Registrar nueva nota
     * Parámetros: tareaId, inscripcionId, nota
     */
    public static String save(String params) {
        String[] parts = params.split(",\\s*");

        if (parts.length < 3) {
            return "Error: Se requieren 3 parámetros: tareaId, inscripcionId, nota";
        }

        try {
            int tareaId = Integer.parseInt(parts[0].trim());
            int inscripcionId = Integer.parseInt(parts[1].trim());
            double nota = Double.parseDouble(parts[2].trim());

            BNotaTarea bNotaTarea = new BNotaTarea();
            return bNotaTarea.save(tareaId, inscripcionId, nota);
        } catch (NumberFormatException e) {
            return "Error: tareaId, inscripcionId y nota deben ser válidos. " + e.getMessage();
        }
    }

    /**
     * Actualizar nota
     * Parámetros: id, tareaId, inscripcionId, nota
     */
    public static String update(String params) {
        String[] parts = params.split(",\\s*");

        if (parts.length < 4) {
            return "Error: Se requieren 4 parámetros: id, tareaId, inscripcionId, nota";
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            int tareaId = Integer.parseInt(parts[1].trim());
            int inscripcionId = Integer.parseInt(parts[2].trim());
            double nota = Double.parseDouble(parts[3].trim());

            BNotaTarea bNotaTarea = new BNotaTarea();
            return bNotaTarea.update(id, tareaId, inscripcionId, nota);
        } catch (NumberFormatException e) {
            return "Error: id, tareaId, inscripcionId y nota deben ser válidos. " + e.getMessage();
        }
    }

    /**
     * Eliminar nota
     * Parámetro: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            return bNotaTarea.delete(id);
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }

    /**
     * Listar todas las notas
     */
    public static String findAll() {
        BNotaTarea bNotaTarea = new BNotaTarea();
        List<String[]> notas = bNotaTarea.findAll();

        if (notas == null || notas.isEmpty()) {
            return "No hay notas registradas.";
        }

        StringBuilder sb = new StringBuilder("=== LISTA DE NOTAS ===\n");
        for (String[] nota : notas) {
            // [0]id, [1]tareaId, [2]inscripcionId, [3]nota
            sb.append("ID: ").append(nota[0])
                    .append(" | Tarea ID: ").append(nota[1])
                    .append(" | Inscripción ID: ").append(nota[2])
                    .append(" | Nota: ").append(nota[3])
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Buscar nota por ID
     * Parámetro: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            String[] nota = bNotaTarea.findOneById(id);

            if (nota == null) return "No se encontró nota con ID: " + id;

            return "Nota encontrada:\n" +
                    "ID: " + nota[0] +
                    " | Tarea ID: " + nota[1] +
                    " | Inscripción ID: " + nota[2] +
                    " | Nota: " + nota[3];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }

    /**
     * Buscar notas por tarea
     * Parámetro: tareaId
     */
    public static String findByTarea(String params) {
        try {
            int tareaId = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            List<String[]> notas = bNotaTarea.findByTarea(tareaId);

            if (notas == null || notas.isEmpty()) {
                return "No hay notas registradas para la tarea con ID: " + tareaId;
            }

            StringBuilder sb = new StringBuilder("=== NOTAS PARA LA TAREA " + tareaId + " ===\n");
            for (String[] nota : notas) {
                sb.append("ID: ").append(nota[0])
                        .append(" | Inscripción ID: ").append(nota[2])
                        .append(" | Nota: ").append(nota[3])
                        .append("\n");
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }

    /**
     * Buscar notas por inscripción
     * Parámetro: inscripcionId
     */
    public static String findByInscripcion(String params) {
        try {
            int inscripcionId = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            List<String[]> notas = bNotaTarea.findByInscripcion(inscripcionId);

            if (notas == null || notas.isEmpty()) {
                return "No hay notas registradas para la inscripción con ID: " + inscripcionId;
            }

            StringBuilder sb = new StringBuilder("=== NOTAS DE LA INSCRIPCIÓN " + inscripcionId + " ===\n");
            for (String[] nota : notas) {
                sb.append("ID: ").append(nota[0])
                        .append(" | Tarea ID: ").append(nota[1])
                        .append(" | Nota: ").append(nota[3])
                        .append("\n");
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }

    /**
     * Calcular promedio de un estudiante
     * Parámetro: inscripcionId
     */
    public static String calcularPromedio(String params) {
        try {
            int inscripcionId = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            String[] promedio = bNotaTarea.calcularPromedio(inscripcionId);

            if (promedio == null) {
                return "No se pudo calcular el promedio para la inscripción ID: " + inscripcionId;
            }

            return "Promedio calculado para inscripción " + inscripcionId + ": " + promedio[0];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }

    /**
     * Obtener estadísticas por tarea
     * Parámetro: tareaId
     */
    public static String estadisticasTarea(String params) {
        try {
            int tareaId = Integer.parseInt(params.trim());
            BNotaTarea bNotaTarea = new BNotaTarea();
            String[] estadisticas = bNotaTarea.getEstadisticasTarea(tareaId);

            if (estadisticas == null) {
                return "No se encontraron estadísticas para la tarea ID: " + tareaId;
            }

            return "Estadísticas para tarea " + tareaId + ":\n" +
                    "- Nota mínima: " + estadisticas[0] + "\n" +
                    "- Nota máxima: " + estadisticas[1] + "\n" +
                    "- Promedio: " + estadisticas[2];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        }
    }
}
