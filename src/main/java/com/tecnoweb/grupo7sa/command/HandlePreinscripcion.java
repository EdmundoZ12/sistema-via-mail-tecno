package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BPreinscripcion;

import java.util.List;

public class HandlePreinscripcion {

    // CU4 - HANDLERS PARA GESTIÓN DE PREINSCRIPCIONES

    /**
     * Crear nueva preinscripción
     * Parámetros: participanteId, cursoId, observaciones
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                int participanteId = Integer.parseInt(partsOfParams[0].trim());
                int cursoId = Integer.parseInt(partsOfParams[1].trim());
                String observaciones = partsOfParams[2].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(observaciones)) observaciones = null;

                BPreinscripcion bPreinscripcion = new BPreinscripcion();
                String result = bPreinscripcion.save(participanteId, cursoId, observaciones);
                return result;
            } catch (NumberFormatException e) {
                return "Error: participanteId y cursoId deben ser números válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar preinscripción: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 3 (participanteId, cursoId, observaciones), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Aprobar preinscripción ⭐ FUNCIONALIDAD ESPECIAL
     * Aprueba y crea inscripción automáticamente
     * Parámetros: preinscripcionId, observaciones
     */
    public static String approve(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int preinscripcionId = Integer.parseInt(partsOfParams[0].trim());
                String observaciones = partsOfParams[1].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(observaciones)) observaciones = null;

                BPreinscripcion bPreinscripcion = new BPreinscripcion();
                String result = bPreinscripcion.approve(preinscripcionId, observaciones);
                return result;
            } catch (NumberFormatException e) {
                return "Error: El ID de preinscripción debe ser numérico. " + e.getMessage();
            } catch (Exception e) {
                return "Error al aprobar preinscripción: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para approve. Esperados: 2 (preinscripcionId, observaciones), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Rechazar preinscripción
     * Parámetros: preinscripcionId, observaciones
     */
    public static String reject(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int preinscripcionId = Integer.parseInt(partsOfParams[0].trim());
                String observaciones = partsOfParams[1].trim();

                BPreinscripcion bPreinscripcion = new BPreinscripcion();
                String result = bPreinscripcion.reject(preinscripcionId, observaciones);
                return result;
            } catch (NumberFormatException e) {
                return "Error: El ID de preinscripción debe ser numérico. " + e.getMessage();
            } catch (Exception e) {
                return "Error al rechazar preinscripción: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para reject. Esperados: 2 (preinscripcionId, observaciones), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Listar todas las preinscripciones pendientes con precios ⭐ ENDPOINT PRINCIPAL
     */
    public static String findAll() {
        try {
            BPreinscripcion bPreinscripcion = new BPreinscripcion();
            List<String[]> preinscripciones = bPreinscripcion.findAll();

            if (preinscripciones == null || preinscripciones.isEmpty()) {
                return "No hay preinscripciones pendientes.";
            }

            StringBuilder sb = new StringBuilder("=== PREINSCRIPCIONES PENDIENTES CON PRECIOS ===\n");
            for (String[] preinscripcion : preinscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]fechaPreinscripcion, [4]estado, [5]observaciones,
                // [6]participanteNombre, [7]carnet, [8]email, [9]cursoNombre, [10]aula, [11]cuposTotales,
                // [12]cuposOcupados, [13]tipoCodigo, [14]tipoDescripcion, [15]precio
                int cuposDisponibles = Integer.parseInt(preinscripcion[11]) - Integer.parseInt(preinscripcion[12]);

                sb.append("ID: ").append(preinscripcion[0])
                        .append(" | Participante: ").append(preinscripcion[6])
                        .append(" (").append(preinscripcion[7]).append(")")
                        .append(" | Curso: ").append(preinscripcion[9])
                        .append("\n   Tipo: ").append(preinscripcion[13])
                        .append(" (").append(preinscripcion[14]).append(")")
                        .append(" | Precio: $").append(preinscripcion[15])
                        .append(" | Cupos disponibles: ").append(cuposDisponibles)
                        .append("\n   Email: ").append(preinscripcion[8])
                        .append(" | Fecha: ").append(preinscripcion[3].substring(0, 19))
                        .append("\n   Observaciones: ").append(preinscripcion[5] != null ? preinscripcion[5] : "Ninguna")
                        .append("\n\n");
            }
            sb.append("Total preinscripciones pendientes: ").append(preinscripciones.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar preinscripciones: " + e.getMessage();
        }
    }

    /**
     * Buscar preinscripción por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPreinscripcion bPreinscripcion = new BPreinscripcion();
            String[] preinscripcion = bPreinscripcion.findOneById(id);

            if (preinscripcion == null) {
                return "No se encontró preinscripción con ID: " + id;
            }

            // Array: [0]id, [1]participanteId, [2]cursoId, [3]fechaPreinscripcion, [4]estado, [5]observaciones,
            // [6]participanteNombre, [7]carnet, [8]cursoNombre
            return "Preinscripción encontrada:\n" +
                    "ID: " + preinscripcion[0] + "\n" +
                    "Participante: " + preinscripcion[6] + " (" + preinscripcion[7] + ")\n" +
                    "Curso: " + preinscripcion[8] + "\n" +
                    "Fecha preinscripción: " + preinscripcion[3].substring(0, 19) + "\n" +
                    "Estado: " + preinscripcion[4] + "\n" +
                    "Observaciones: " + (preinscripcion[5] != null ? preinscripcion[5] : "Ninguna");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar preinscripción: " + e.getMessage();
        }
    }

    /**
     * Buscar preinscripciones por curso
     * Parámetros: cursoId
     */
    public static String findByCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BPreinscripcion bPreinscripcion = new BPreinscripcion();
            List<String[]> preinscripciones = bPreinscripcion.findByCurso(cursoId);

            if (preinscripciones == null || preinscripciones.isEmpty()) {
                return "No hay preinscripciones para el curso especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PREINSCRIPCIONES POR CURSO ===\n");
            sb.append("Curso ID: ").append(cursoId).append("\n\n");

            for (String[] preinscripcion : preinscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]fechaPreinscripcion, [4]estado, [5]observaciones,
                // [6]participanteNombre, [7]carnet, [8]email
                sb.append("• ID: ").append(preinscripcion[0])
                        .append(" | Participante: ").append(preinscripcion[6])
                        .append(" (").append(preinscripcion[7]).append(")")
                        .append(" | Estado: ").append(preinscripcion[4])
                        .append("\n  Email: ").append(preinscripcion[8])
                        .append(" | Fecha: ").append(preinscripcion[3].substring(0, 19))
                        .append("\n  Observaciones: ").append(preinscripcion[5] != null ? preinscripcion[5] : "Ninguna")
                        .append("\n\n");
            }
            sb.append("Total preinscripciones: ").append(preinscripciones.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar preinscripciones por curso: " + e.getMessage();
        }
    }

    /**
     * Buscar preinscripciones por participante
     * Parámetros: participanteId
     */
    public static String findByParticipante(String params) {
        try {
            int participanteId = Integer.parseInt(params.trim());
            BPreinscripcion bPreinscripcion = new BPreinscripcion();
            List<String[]> preinscripciones = bPreinscripcion.findByParticipante(participanteId);

            if (preinscripciones == null || preinscripciones.isEmpty()) {
                return "No hay preinscripciones para el participante especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PREINSCRIPCIONES POR PARTICIPANTE ===\n");
            sb.append("Participante ID: ").append(participanteId).append("\n\n");

            for (String[] preinscripcion : preinscripciones) {
                // Array: [0]id, [1]participanteId, [2]cursoId, [3]fechaPreinscripcion, [4]estado, [5]observaciones,
                // [6]cursoNombre, [7]aula
                sb.append("• ID: ").append(preinscripcion[0])
                        .append(" | Curso: ").append(preinscripcion[6])
                        .append(" (Aula: ").append(preinscripcion[7]).append(")")
                        .append(" | Estado: ").append(preinscripcion[4])
                        .append("\n  Fecha: ").append(preinscripcion[3].substring(0, 19))
                        .append("\n  Observaciones: ").append(preinscripcion[5] != null ? preinscripcion[5] : "Ninguna")
                        .append("\n\n");
            }
            sb.append("Total preinscripciones: ").append(preinscripciones.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del participante debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar preinscripciones por participante: " + e.getMessage();
        }
    }
}