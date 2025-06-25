package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BInscripcion;

import java.util.List;

public class HandleInscripcion {

    // CU4 - HANDLERS PARA GESTIÓN DE INSCRIPCIONES

    /**
     * Listar todas las inscripciones activas
     */
    public static String findAll() {
        try {
            BInscripcion bInscripcion = new BInscripcion();
            List<String[]> inscripciones = bInscripcion.findAll();

            if (inscripciones == null || inscripciones.isEmpty()) {
                return "No hay inscripciones registradas.";
            }

            StringBuilder sb = new StringBuilder("=== INSCRIPCIONES ACTIVAS ===\n");
            for (String[] inscripcion : inscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]preinscripcionId, [4]fechaInscripcion,
                // [5]notaFinal, [6]estado, [7]observaciones, [8]participanteNombre, [9]carnet, [10]email,
                // [11]cursoNombre, [12]aula
                sb.append("ID: ").append(inscripcion[0])
                        .append(" | Participante: ").append(inscripcion[8])
                        .append(" (").append(inscripcion[9]).append(")")
                        .append(" | Curso: ").append(inscripcion[11])
                        .append(" | Estado: ").append(inscripcion[6]);

                if (!inscripcion[5].isEmpty()) {
                    sb.append(" | Nota: ").append(inscripcion[5]);
                }

                sb.append(" | Fecha: ").append(inscripcion[4].substring(0, 19)).append("\n");
            }
            sb.append("Total inscripciones: ").append(inscripciones.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar inscripciones: " + e.getMessage();
        }
    }

    /**
     * Buscar inscripción por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BInscripcion bInscripcion = new BInscripcion();
            String[] inscripcion = bInscripcion.findOneById(id);

            if (inscripcion == null) {
                return "No se encontró inscripción con ID: " + id;
            }

            // Array: [0]id, [1]participanteId, [2]cursoId, [3]preinscripcionId, [4]fechaInscripcion,
            // [5]notaFinal, [6]estado, [7]observaciones, [8]participanteNombre, [9]carnet, [10]cursoNombre
            return "Inscripción encontrada:\n" +
                    "ID: " + inscripcion[0] + "\n" +
                    "Participante: " + inscripcion[8] + " (" + inscripcion[9] + ")\n" +
                    "Curso: " + inscripcion[10] + "\n" +
                    "Fecha inscripción: " + inscripcion[4].substring(0, 19) + "\n" +
                    "Estado: " + inscripcion[6] + "\n" +
                    "Nota final: " + (!inscripcion[5].isEmpty() ? inscripcion[5] : "Sin calificar") + "\n" +
                    "Observaciones: " + (inscripcion[7] != null ? inscripcion[7] : "Ninguna");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar inscripción: " + e.getMessage();
        }
    }

    /**
     * Lista de inscritos por curso ⭐ ENDPOINT PRINCIPAL
     * Parámetros: cursoId
     */
    public static String findByCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BInscripcion bInscripcion = new BInscripcion();
            List<String[]> inscripciones = bInscripcion.findByCurso(cursoId);

            if (inscripciones == null || inscripciones.isEmpty()) {
                return "No hay estudiantes inscritos en el curso especificado.";
            }

            StringBuilder sb = new StringBuilder("=== LISTA DE ESTUDIANTES INSCRITOS ===\n");
            sb.append("Curso ID: ").append(cursoId).append("\n\n");

            int contadorInscrito = 0, contadorAprobado = 0, contadorReprobado = 0;
            double sumaNotas = 0;
            int contadorConNota = 0;

            for (String[] inscripcion : inscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]preinscripcionId, [4]fechaInscripcion,
                // [5]notaFinal, [6]estado, [7]observaciones, [8]participanteNombre, [9]carnet, [10]email,
                // [11]tipoCodigo, [12]tipoDescripcion
                sb.append("• ").append(inscripcion[8])
                        .append(" (").append(inscripcion[9]).append(")")
                        .append(" | Tipo: ").append(inscripcion[11])
                        .append(" | Estado: ").append(inscripcion[6]);

                if (!inscripcion[5].isEmpty()) {
                    double nota = Double.parseDouble(inscripcion[5]);
                    sb.append(" | Nota: ").append(inscripcion[5]);
                    sumaNotas += nota;
                    contadorConNota++;
                }

                sb.append(" | Email: ").append(inscripcion[10]).append("\n");

                // Contar por estado
                switch (inscripcion[6]) {
                    case "INSCRITO":
                        contadorInscrito++;
                        break;
                    case "APROBADO":
                        contadorAprobado++;
                        break;
                    case "REPROBADO":
                        contadorReprobado++;
                        break;
                }
            }

            // Resumen estadístico
            sb.append("\n=== RESUMEN ===\n");
            sb.append("Total estudiantes: ").append(inscripciones.size()).append("\n");
            sb.append("Inscritos: ").append(contadorInscrito);
            sb.append(" | Aprobados: ").append(contadorAprobado);
            sb.append(" | Reprobados: ").append(contadorReprobado).append("\n");

            if (contadorConNota > 0) {
                double promedio = sumaNotas / contadorConNota;
                sb.append("Promedio de notas: ").append(String.format("%.2f", promedio));
            }

            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar inscripciones por curso: " + e.getMessage();
        }
    }

    /**
     * Historial de un participante
     * Parámetros: participanteId
     */
    public static String findByParticipante(String params) {
        try {
            int participanteId = Integer.parseInt(params.trim());
            BInscripcion bInscripcion = new BInscripcion();
            List<String[]> inscripciones = bInscripcion.findByParticipante(participanteId);

            if (inscripciones == null || inscripciones.isEmpty()) {
                return "No hay historial de inscripciones para el participante especificado.";
            }

            StringBuilder sb = new StringBuilder("=== HISTORIAL DE INSCRIPCIONES ===\n");
            sb.append("Participante ID: ").append(participanteId).append("\n\n");

            for (String[] inscripcion : inscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]preinscripcionId, [4]fechaInscripcion,
                // [5]notaFinal, [6]estado, [7]observaciones, [8]cursoNombre, [9]aula, [10]duracionHoras, [11]gestionNombre
                sb.append("• Curso: ").append(inscripcion[8])
                        .append(" (").append(inscripcion[10]).append(" horas)")
                        .append("\n  Gestión: ").append(inscripcion[11])
                        .append(" | Aula: ").append(inscripcion[9])
                        .append(" | Estado: ").append(inscripcion[6]);

                if (!inscripcion[5].isEmpty()) {
                    sb.append(" | Nota: ").append(inscripcion[5]);
                }

                sb.append("\n  Fecha inscripción: ").append(inscripcion[4].substring(0, 19)).append("\n\n");
            }
            sb.append("Total cursos tomados: ").append(inscripciones.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del participante debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar historial del participante: " + e.getMessage();
        }
    }

    /**
     * Calificar estudiante ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: inscripcionId, notaFinal, estado
     */
    public static String updateNota(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                int inscripcionId = Integer.parseInt(partsOfParams[0].trim());
                double notaFinal = Double.parseDouble(partsOfParams[1].trim());
                String estado = partsOfParams[2].trim().toUpperCase();

                BInscripcion bInscripcion = new BInscripcion();
                String result = bInscripcion.updateNota(inscripcionId, notaFinal, estado);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID y nota deben ser valores numéricos válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al calificar estudiante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para updateNota. Esperados: 3 (inscripcionId, notaFinal, estado), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Retirar participante ⭐ FUNCIONALIDAD ESPECIAL
     * Retira estudiante y libera cupo
     * Parámetros: inscripcionId, observaciones
     */
    public static String withdraw(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int inscripcionId = Integer.parseInt(partsOfParams[0].trim());
                String observaciones = partsOfParams[1].trim();

                BInscripcion bInscripcion = new BInscripcion();
                String result = bInscripcion.withdraw(inscripcionId, observaciones);
                return result;
            } catch (NumberFormatException e) {
                return "Error: El ID debe ser numérico. " + e.getMessage();
            } catch (Exception e) {
                return "Error al retirar participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para withdraw. Esperados: 2 (inscripcionId, observaciones), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Obtener estadísticas por curso ⭐ ENDPOINT PRINCIPAL
     */
    public static String getEstadisticas() {
        try {
            BInscripcion bInscripcion = new BInscripcion();
            List<String[]> estadisticas = bInscripcion.getEstadisticas();

            if (estadisticas == null || estadisticas.isEmpty()) {
                return "No hay estadísticas disponibles.";
            }

            StringBuilder sb = new StringBuilder("=== ESTADÍSTICAS POR CURSO ===\n");
            for (String[] estadistica : estadisticas) {
                // Array: [0]cursoId, [1]cursoNombre, [2]aula, [3]cuposTotales, [4]cuposOcupados,
                // [5]totalInscripciones, [6]activos, [7]aprobados, [8]reprobados, [9]retirados,
                // [10]promedioNotas, [11]tutorNombre, [12]gestionNombre

                double porcentajeOcupacion = (Double.parseDouble(estadistica[4]) / Double.parseDouble(estadistica[3])) * 100;

                sb.append("Curso: ").append(estadistica[1])
                        .append(" | Aula: ").append(estadistica[2])
                        .append(" | Tutor: ").append(estadistica[11])
                        .append("\n  Gestión: ").append(estadistica[12])
                        .append(" | Cupos: ").append(estadistica[4]).append("/").append(estadistica[3])
                        .append(" (").append(String.format("%.1f", porcentajeOcupacion)).append("%)")
                        .append("\n  Inscripciones: Total=").append(estadistica[5])
                        .append(" | Activos=").append(estadistica[6])
                        .append(" | Aprobados=").append(estadistica[7])
                        .append(" | Reprobados=").append(estadistica[8])
                        .append(" | Retirados=").append(estadistica[9]);

                if (!estadistica[10].equals("0.00")) {
                    sb.append("\n  Promedio de notas: ").append(estadistica[10]);
                }

                sb.append("\n\n");
            }
            sb.append("Total cursos con estadísticas: ").append(estadisticas.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al obtener estadísticas: " + e.getMessage();
        }
    }
}