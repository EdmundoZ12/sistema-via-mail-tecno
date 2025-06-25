package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BCurso;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HandleCurso {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // CU3 - HANDLERS PARA GESTIÓN DE CURSOS

    /**
     * Crear nuevo curso
     * Parámetros: nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 11) {
            try {
                String nombre = partsOfParams[0].trim();
                String descripcion = partsOfParams[1].trim();
                int duracionHoras = Integer.parseInt(partsOfParams[2].trim());
                String nivel = partsOfParams[3].trim();
                String logoUrl = partsOfParams[4].trim();
                int tutorId = Integer.parseInt(partsOfParams[5].trim());
                int gestionId = Integer.parseInt(partsOfParams[6].trim());
                String aula = partsOfParams[7].trim();
                int cuposTotales = Integer.parseInt(partsOfParams[8].trim());
                String fechaInicioStr = partsOfParams[9].trim();
                String fechaFinStr = partsOfParams[10].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(descripcion)) descripcion = null;
                if ("null".equalsIgnoreCase(logoUrl)) logoUrl = null;

                // Convertir strings a Date
                Date fechaInicio = convertStringToDate(fechaInicioStr);
                Date fechaFin = convertStringToDate(fechaFinStr);

                if (fechaInicio == null) {
                    return "Error: Formato de fecha de inicio inválido. Use formato yyyy-MM-dd";
                }
                if (fechaFin == null) {
                    return "Error: Formato de fecha de fin inválido. Use formato yyyy-MM-dd";
                }

                BCurso bCurso = new BCurso();
                String result = bCurso.save(nombre, descripcion, duracionHoras, nivel, logoUrl,
                        tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores numéricos deben ser válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 11 (nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar curso existente
     * Parámetros: id, nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 12) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String descripcion = partsOfParams[2].trim();
                int duracionHoras = Integer.parseInt(partsOfParams[3].trim());
                String nivel = partsOfParams[4].trim();
                String logoUrl = partsOfParams[5].trim();
                int tutorId = Integer.parseInt(partsOfParams[6].trim());
                int gestionId = Integer.parseInt(partsOfParams[7].trim());
                String aula = partsOfParams[8].trim();
                int cuposTotales = Integer.parseInt(partsOfParams[9].trim());
                String fechaInicioStr = partsOfParams[10].trim();
                String fechaFinStr = partsOfParams[11].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(descripcion)) descripcion = null;
                if ("null".equalsIgnoreCase(logoUrl)) logoUrl = null;

                // Convertir strings a Date
                Date fechaInicio = convertStringToDate(fechaInicioStr);
                Date fechaFin = convertStringToDate(fechaFinStr);

                if (fechaInicio == null) {
                    return "Error: Formato de fecha de inicio inválido. Use formato yyyy-MM-dd";
                }
                if (fechaFin == null) {
                    return "Error: Formato de fecha de fin inválido. Use formato yyyy-MM-dd";
                }

                BCurso bCurso = new BCurso();
                String result = bCurso.update(id, nombre, descripcion, duracionHoras, nivel, logoUrl,
                        tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores numéricos deben ser válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 12 (id, nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar curso
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            String result = bCurso.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar curso: " + e.getMessage();
        }
    }

    /**
     * Eliminar curso permanentemente ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: id
     */
    public static String deletePermanent(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            String result = bCurso.deletePermanent(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar curso permanentemente: " + e.getMessage();
        }
    }

    /**
     * Reactivar curso
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            String result = bCurso.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar curso: " + e.getMessage();
        }
    }

    /**
     * Listar todos los cursos con información completa ⭐ ENDPOINT PRINCIPAL
     */
    public static String findAll() {
        try {
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findAll();

            if (cursos == null || cursos.isEmpty()) {
                return "No hay cursos registrados.";
            }

            StringBuilder sb = new StringBuilder("=== CURSOS CON INFORMACIÓN COMPLETA ===\n");
            for (String[] curso : cursos) {
                // Array: [0]id, [1]nombre, [2]descripcion, [3]duracionHoras, [4]nivel, [5]logoUrl,
                // [6]aula, [7]cuposTotales, [8]cuposOcupados, [9]fechaInicio, [10]fechaFin, [11]activo,
                // [12]tutorNombre, [13]tutorEmail, [14]gestionNombre, [15]gestionInicio, [16]gestionFin
                int cuposDisponibles = Integer.parseInt(curso[7]) - Integer.parseInt(curso[8]);

                sb.append("ID: ").append(curso[0])
                        .append(" | Curso: ").append(curso[1])
                        .append(" | Nivel: ").append(curso[4])
                        .append(" | Duración: ").append(curso[3]).append("h")
                        .append(" | Aula: ").append(curso[6])
                        .append("\n   Tutor: ").append(curso[12])
                        .append(" | Gestión: ").append(curso[14])
                        .append(" | Cupos: ").append(curso[8]).append("/").append(curso[7])
                        .append(" (").append(cuposDisponibles).append(" disponibles)")
                        .append(" | Fechas: ").append(curso[9]).append(" al ").append(curso[10])
                        .append("\n");
            }
            sb.append("Total cursos: ").append(cursos.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar cursos: " + e.getMessage();
        }
    }

    /**
     * Buscar cursos con cupos disponibles ⭐ ENDPOINT PRINCIPAL
     */
    public static String findWithSpots() {
        try {
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findWithSpots();

            if (cursos == null || cursos.isEmpty()) {
                return "No hay cursos con cupos disponibles.";
            }

            StringBuilder sb = new StringBuilder("=== CURSOS CON CUPOS DISPONIBLES ===\n");
            for (String[] curso : cursos) {
                // Array incluye cupos_disponibles en índice [15]
                sb.append("ID: ").append(curso[0])
                        .append(" | Curso: ").append(curso[1])
                        .append(" | Nivel: ").append(curso[4])
                        .append(" | Aula: ").append(curso[6])
                        .append(" | Tutor: ").append(curso[12])
                        .append("\n   Cupos disponibles: ").append(curso[15])
                        .append(" (").append(curso[8]).append("/").append(curso[7]).append(" ocupados)")
                        .append(" | Fechas: ").append(curso[9]).append(" al ").append(curso[10])
                        .append("\n");
            }
            sb.append("Total cursos disponibles: ").append(cursos.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar cursos disponibles: " + e.getMessage();
        }
    }

    /**
     * Buscar curso por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            String[] curso = bCurso.findOneById(id);

            if (curso == null) {
                return "No se encontró curso con ID: " + id;
            }

            // Array: [0]id, [1]nombre, [2]descripcion, [3]duracionHoras, [4]nivel, [5]logoUrl,
            // [6]aula, [7]cuposTotales, [8]cuposOcupados, [9]fechaInicio, [10]fechaFin, [11]activo,
            // [12]tutorNombre, [13]tutorEmail, [14]gestionNombre
            int cuposDisponibles = Integer.parseInt(curso[7]) - Integer.parseInt(curso[8]);

            return "Curso encontrado:\n" +
                    "ID: " + curso[0] + "\n" +
                    "Nombre: " + curso[1] + "\n" +
                    "Descripción: " + (curso[2] != null ? curso[2] : "Sin descripción") + "\n" +
                    "Duración: " + curso[3] + " horas\n" +
                    "Nivel: " + curso[4] + "\n" +
                    "Aula: " + curso[6] + "\n" +
                    "Tutor: " + curso[12] + " (" + curso[13] + ")\n" +
                    "Gestión: " + curso[14] + "\n" +
                    "Cupos: " + curso[8] + "/" + curso[7] + " (" + cuposDisponibles + " disponibles)\n" +
                    "Fechas: " + curso[9] + " al " + curso[10] + "\n" +
                    "Activo: " + (curso[11].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar curso: " + e.getMessage();
        }
    }

    /**
     * Buscar cursos por gestión
     * Parámetros: gestionId
     */
    public static String findByGestion(String params) {
        try {
            int gestionId = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findByGestion(gestionId);

            if (cursos == null || cursos.isEmpty()) {
                return "No hay cursos en la gestión especificada.";
            }

            StringBuilder sb = new StringBuilder("=== CURSOS POR GESTIÓN ===\n");
            for (String[] curso : cursos) {
                // Array: [0]id, [1]nombre, [2]descripcion, [3]duracionHoras, [4]nivel, [5]aula,
                // [6]cuposTotales, [7]cuposOcupados, [8]fechaInicio, [9]fechaFin, [10]tutorNombre
                int cuposDisponibles = Integer.parseInt(curso[6]) - Integer.parseInt(curso[7]);

                sb.append("ID: ").append(curso[0])
                        .append(" | Curso: ").append(curso[1])
                        .append(" | Nivel: ").append(curso[4])
                        .append(" | Tutor: ").append(curso[10])
                        .append(" | Cupos: ").append(curso[7]).append("/").append(curso[6])
                        .append(" | Fechas: ").append(curso[8]).append(" al ").append(curso[9])
                        .append("\n");
            }
            sb.append("Total cursos en gestión: ").append(cursos.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID de gestión debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar cursos por gestión: " + e.getMessage();
        }
    }

    /**
     * Buscar cursos por tutor
     * Parámetros: tutorId
     */
    public static String findByTutor(String params) {
        try {
            int tutorId = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findByTutor(tutorId);

            if (cursos == null || cursos.isEmpty()) {
                return "No hay cursos asignados al tutor especificado.";
            }

            StringBuilder sb = new StringBuilder("=== CURSOS POR TUTOR ===\n");
            for (String[] curso : cursos) {
                // Array: [0]id, [1]nombre, [2]descripcion, [3]duracionHoras, [4]nivel, [5]aula,
                // [6]cuposTotales, [7]cuposOcupados, [8]fechaInicio, [9]fechaFin, [10]gestionNombre
                int cuposDisponibles = Integer.parseInt(curso[6]) - Integer.parseInt(curso[7]);

                sb.append("ID: ").append(curso[0])
                        .append(" | Curso: ").append(curso[1])
                        .append(" | Nivel: ").append(curso[4])
                        .append(" | Gestión: ").append(curso[10])
                        .append(" | Cupos: ").append(curso[7]).append("/").append(curso[6])
                        .append(" | Fechas: ").append(curso[8]).append(" al ").append(curso[9])
                        .append("\n");
            }
            sb.append("Total cursos del tutor: ").append(cursos.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID de tutor debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar cursos por tutor: " + e.getMessage();
        }
    }

    /**
     * Actualizar cupos del curso ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: cursoId, nuevoCupos
     */
    public static String updateCupos(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int cursoId = Integer.parseInt(partsOfParams[0].trim());
                int nuevoCupos = Integer.parseInt(partsOfParams[1].trim());

                BCurso bCurso = new BCurso();
                String result = bCurso.updateCupos(cursoId, nuevoCupos);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores deben ser numéricos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar cupos: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para updateCupos. Esperados: 2 (cursoId, nuevoCupos), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Método auxiliar para convertir String a java.sql.Date
     */
    private static Date convertStringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            java.util.Date utilDate = DATE_FORMAT.parse(dateString.trim());
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}