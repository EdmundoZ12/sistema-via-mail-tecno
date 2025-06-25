package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BAsistencia;

import java.sql.Date;
import java.util.List;

public class HandleAsistencia {

    // CU5 - HANDLERS PARA GESTIÓN DE ASISTENCIA

    /**
     * Registrar nueva asistencia
     * Parámetros: inscripcionId, fecha, estado
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                int inscripcionId = Integer.parseInt(partsOfParams[0].trim());
                Date fecha = Date.valueOf(partsOfParams[1].trim());
                String estado = partsOfParams[2].trim();

                BAsistencia bAsistencia = new BAsistencia();
                String result = bAsistencia.save(inscripcionId, fecha, estado);
                return result;
            } catch (NumberFormatException e) {
                return "Error: El ID de inscripción debe ser numérico. " + e.getMessage();
            } catch (IllegalArgumentException e) {
                return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
            } catch (Exception e) {
                return "Error al registrar asistencia: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 3 (inscripcionId, fecha, estado), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar asistencia existente
     * Parámetros: id, inscripcionId, fecha, estado
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 4) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                int inscripcionId = Integer.parseInt(partsOfParams[1].trim());
                Date fecha = Date.valueOf(partsOfParams[2].trim());
                String estado = partsOfParams[3].trim();

                BAsistencia bAsistencia = new BAsistencia();
                String result = bAsistencia.update(id, inscripcionId, fecha, estado);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los IDs deben ser numéricos. " + e.getMessage();
            } catch (IllegalArgumentException e) {
                return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar asistencia: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 4 (id, inscripcionId, fecha, estado), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Eliminar asistencia
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            String result = bAsistencia.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar asistencia: " + e.getMessage();
        }
    }

    /**
     * Listar todas las asistencias
     */
    public static String findAll() {
        try {
            BAsistencia bAsistencia = new BAsistencia();
            List<String[]> asistencias = bAsistencia.findAll();

            if (asistencias == null || asistencias.isEmpty()) {
                return "No hay asistencias registradas.";
            }

            StringBuilder sb = new StringBuilder("=== ASISTENCIAS REGISTRADAS ===\n");
            for (String[] asistencia : asistencias) {
                // Array: [0]id, [1]inscripcion_id, [2]fecha, [3]estado, [4]participante_nombre, [5]carnet, [6]curso_nombre
                sb.append("ID: ").append(asistencia[0])
                        .append(" | Estudiante: ").append(asistencia[4])
                        .append(" (").append(asistencia[5]).append(")")
                        .append(" | Curso: ").append(asistencia[6])
                        .append(" | Fecha: ").append(asistencia[2])
                        .append(" | Estado: ").append(asistencia[3])
                        .append("\n");
            }
            sb.append("Total asistencias: ").append(asistencias.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar asistencias: " + e.getMessage();
        }
    }

    /**
     * Buscar asistencia por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            String[] asistencia = bAsistencia.findOneById(id);

            if (asistencia == null) {
                return "No se encontró asistencia con ID: " + id;
            }

            return "Asistencia encontrada:\n" +
                    "ID: " + asistencia[0] + "\n" +
                    "Estudiante: " + asistencia[4] + " (" + asistencia[5] + ")\n" +
                    "Curso: " + asistencia[6] + "\n" +
                    "Fecha: " + asistencia[2] + "\n" +
                    "Estado: " + asistencia[3] + "\n" +
                    "ID Inscripción: " + asistencia[1];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar asistencia: " + e.getMessage();
        }
    }

    /**
     * Buscar asistencias por curso ⭐ ENDPOINT PRINCIPAL
     * Parámetros: cursoId
     */
    public static String findByCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            List<String[]> asistencias = bAsistencia.findByCurso(cursoId);

            if (asistencias == null || asistencias.isEmpty()) {
                return "No hay asistencias registradas para el curso especificado.";
            }

            StringBuilder sb = new StringBuilder("=== ASISTENCIAS POR CURSO ===\n");
            sb.append("Curso ID: ").append(cursoId).append("\n\n");

            for (String[] asistencia : asistencias) {
                sb.append("• ").append(asistencia[4])
                        .append(" (").append(asistencia[5]).append(")")
                        .append(" | Fecha: ").append(asistencia[2])
                        .append(" | Estado: ").append(asistencia[3])
                        .append("\n");
            }
            sb.append("\nTotal registros: ").append(asistencias.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar asistencias por curso: " + e.getMessage();
        }
    }

    /**
     * Buscar asistencias por inscripción ⭐ ENDPOINT PRINCIPAL
     * Parámetros: inscripcionId
     */
    public static String findByInscripcion(String params) {
        try {
            int inscripcionId = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            List<String[]> asistencias = bAsistencia.findByInscripcion(inscripcionId);

            if (asistencias == null || asistencias.isEmpty()) {
                return "No hay asistencias registradas para la inscripción especificada.";
            }

            StringBuilder sb = new StringBuilder("=== ASISTENCIAS POR INSCRIPCIÓN ===\n");
            sb.append("Inscripción ID: ").append(inscripcionId).append("\n");
            sb.append("Estudiante: ").append(asistencias.get(0)[4]).append("\n");
            sb.append("Curso: ").append(asistencias.get(0)[6]).append("\n\n");

            for (String[] asistencia : asistencias) {
                sb.append("• Fecha: ").append(asistencia[2])
                        .append(" | Estado: ").append(asistencia[3])
                        .append("\n");
            }
            sb.append("\nTotal registros: ").append(asistencias.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID de inscripción debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar asistencias por inscripción: " + e.getMessage();
        }
    }

    /**
     * Buscar asistencias por fecha
     * Parámetros: fecha
     */
    public static String findByFecha(String params) {
        try {
            Date fecha = Date.valueOf(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            List<String[]> asistencias = bAsistencia.findByFecha(fecha);

            if (asistencias == null || asistencias.isEmpty()) {
                return "No hay asistencias registradas para la fecha: " + fecha;
            }

            StringBuilder sb = new StringBuilder("=== ASISTENCIAS POR FECHA ===\n");
            sb.append("Fecha: ").append(fecha).append("\n\n");

            for (String[] asistencia : asistencias) {
                sb.append("• ").append(asistencia[4])
                        .append(" (").append(asistencia[5]).append(")")
                        .append(" | Curso: ").append(asistencia[6])
                        .append(" | Estado: ").append(asistencia[3])
                        .append("\n");
            }
            sb.append("\nTotal registros: ").append(asistencias.size());
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar asistencias por fecha: " + e.getMessage();
        }
    }

    /**
     * Calcular porcentaje de asistencia ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: inscripcionId
     */
    public static String getPorcentaje(String params) {
        try {
            int inscripcionId = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            String[] porcentaje = bAsistencia.getPorcentaje(inscripcionId);

            if (porcentaje == null) {
                return "No se encontraron datos de asistencia para la inscripción: " + inscripcionId;
            }

            return "Porcentaje de asistencia:\n" +
                    "Inscripción ID: " + inscripcionId + "\n" +
                    "Total clases: " + porcentaje[0] + "\n" +
                    "Presentes: " + porcentaje[1] + "\n" +
                    "Ausentes: " + porcentaje[2] + "\n" +
                    "Justificados: " + porcentaje[3] + "\n" +
                    "Porcentaje de asistencia: " + porcentaje[4] + "%";
        } catch (NumberFormatException e) {
            return "Error: El ID de inscripción debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al calcular porcentaje de asistencia: " + e.getMessage();
        }
    }

    /**
     * Obtener reporte completo de asistencia por curso ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: cursoId
     */
    public static String getReporteCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BAsistencia bAsistencia = new BAsistencia();
            List<String[]> reporte = bAsistencia.getReporteCurso(cursoId);

            if (reporte == null || reporte.isEmpty()) {
                return "No hay datos de asistencia para el curso especificado.";
            }

            StringBuilder sb = new StringBuilder("=== REPORTE DE ASISTENCIA POR CURSO ===\n");
            sb.append("Curso ID: ").append(cursoId).append("\n\n");

            for (String[] registro : reporte) {
                // Array: [0]participante_nombre, [1]carnet, [2]total_clases, [3]presentes, [4]ausentes, [5]justificados, [6]porcentaje_asistencia
                sb.append("• ").append(registro[0])
                        .append(" (").append(registro[1]).append(")")
                        .append(" | Asistencia: ").append(registro[6]).append("%")
                        .append(" | Clases: ").append(registro[2])
                        .append(" (P:").append(registro[3])
                        .append(" A:").append(registro[4])
                        .append(" J:").append(registro[5]).append(")")
                        .append("\n");
            }
            sb.append("\nTotal estudiantes: ").append(reporte.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al generar reporte de asistencia: " + e.getMessage();
        }
    }
}