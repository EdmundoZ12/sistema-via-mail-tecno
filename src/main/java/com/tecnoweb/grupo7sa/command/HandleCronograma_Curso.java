package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BCronograma_Curso;

import java.sql.Date;
import java.util.List;

public class HandleCronograma_Curso {

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 5) {
            try {
                int cursoGestionId = Integer.parseInt(partsOfParams[0].trim());
                String descripcion = partsOfParams[1].trim();
                String fase = partsOfParams[2].trim();
                Date fechaInicio = Date.valueOf(partsOfParams[3].trim());
                Date fechaFin = Date.valueOf(partsOfParams[4].trim());

                BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
                String result = bCronograma_Curso.save(cursoGestionId, descripcion, fase, fechaInicio, fechaFin);
                return result;
            } catch (NumberFormatException e) {
                return "Error: El ID del curso-gestión debe ser un número válido. " + e.getMessage();
            } catch (IllegalArgumentException e) {
                return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar cronograma de curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 5 (cursoGestionId, descripcion, fase, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 6) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                int cursoGestionId = Integer.parseInt(partsOfParams[1].trim());
                String descripcion = partsOfParams[2].trim();
                String fase = partsOfParams[3].trim();
                Date fechaInicio = Date.valueOf(partsOfParams[4].trim());
                Date fechaFin = Date.valueOf(partsOfParams[5].trim());

                BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
                String result = bCronograma_Curso.update(id, cursoGestionId, descripcion, fase, fechaInicio, fechaFin);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID y curso-gestión ID deben ser números válidos. " + e.getMessage();
            } catch (IllegalArgumentException e) {
                return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar cronograma de curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 6 (id, cursoGestionId, descripcion, fase, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            String result = bCronograma_Curso.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar cronograma de curso: " + e.getMessage();
        }
    }

    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            String result = bCronograma_Curso.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar cronograma de curso: " + e.getMessage();
        }
    }

    public static String findAll() {
        try {
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            List<String[]> cronogramas = bCronograma_Curso.findAllCronogramas();

            if (cronogramas == null || cronogramas.isEmpty()) {
                return "No hay cronogramas de cursos registrados.";
            }

            StringBuilder sb = new StringBuilder("=== CRONOGRAMAS DE CURSOS ===\n");
            for (String[] cronograma : cronogramas) {
                // Array: [0]id, [1]cursoGestionId, [2]descripcion, [3]fase, [4]fechaInicio, [5]fechaFin, [6]activo
                sb.append("ID: ").append(cronograma[0])
                        .append(" | Curso-Gestión: ").append(cronograma[1])
                        .append(" | Descripción: ").append(cronograma[2])
                        .append(" | Fase: ").append(cronograma[3])
                        .append(" | Inicio: ").append(cronograma[4])
                        .append(" | Fin: ").append(cronograma[5])
                        .append(" | Activo: ").append(cronograma[6].equals("true") ? "Sí" : "No").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar cronogramas de cursos: " + e.getMessage();
        }
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            String[] cronograma = bCronograma_Curso.findOneById(id);

            if (cronograma == null) {
                return "No se encontró cronograma de curso con ID: " + id;
            }

            // Array: [0]id, [1]cursoGestionId, [2]descripcion, [3]fase, [4]fechaInicio, [5]fechaFin, [6]activo
            return "Cronograma de curso encontrado:\n" +
                    "ID: " + cronograma[0] + "\n" +
                    "Curso-Gestión ID: " + cronograma[1] + "\n" +
                    "Descripción: " + cronograma[2] + "\n" +
                    "Fase: " + cronograma[3] + "\n" +
                    "Fecha Inicio: " + cronograma[4] + "\n" +
                    "Fecha Fin: " + cronograma[5] + "\n" +
                    "Activo: " + (cronograma[6].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar cronograma de curso: " + e.getMessage();
        }
    }

    public static String findByCursoGestion(String params) {
        try {
            int cursoGestionId = Integer.parseInt(params.trim());
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            List<String[]> cronogramas = bCronograma_Curso.findByCursoGestion(cursoGestionId);

            if (cronogramas == null || cronogramas.isEmpty()) {
                return "No se encontraron cronogramas para el curso-gestión ID: " + cursoGestionId;
            }

            StringBuilder sb = new StringBuilder("=== CRONOGRAMAS PARA CURSO-GESTIÓN ID: " + cursoGestionId + " ===\n");
            for (String[] cronograma : cronogramas) {
                sb.append("ID: ").append(cronograma[0])
                        .append(" | Descripción: ").append(cronograma[2])
                        .append(" | Fase: ").append(cronograma[3])
                        .append(" | Inicio: ").append(cronograma[4])
                        .append(" | Fin: ").append(cronograma[5])
                        .append(" | Activo: ").append(cronograma[6].equals("true") ? "Sí" : "No").append("\n");
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso-gestión debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar cronogramas por curso-gestión: " + e.getMessage();
        }
    }

    public static String findByFase(String params) {
        try {
            String fase = params.trim();
            BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
            List<String[]> cronogramas = bCronograma_Curso.findByFase(fase);

            if (cronogramas == null || cronogramas.isEmpty()) {
                return "No se encontraron cronogramas en la fase: " + fase;
            }

            StringBuilder sb = new StringBuilder("=== CRONOGRAMAS EN FASE: " + fase.toUpperCase() + " ===\n");
            for (String[] cronograma : cronogramas) {
                sb.append("ID: ").append(cronograma[0])
                        .append(" | Curso-Gestión: ").append(cronograma[1])
                        .append(" | Descripción: ").append(cronograma[2])
                        .append(" | Inicio: ").append(cronograma[4])
                        .append(" | Fin: ").append(cronograma[5])
                        .append(" | Activo: ").append(cronograma[6].equals("true") ? "Sí" : "No").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar cronogramas por fase: " + e.getMessage();
        }
    }

    public static String findByFechaRange(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                Date fechaInicio = Date.valueOf(partsOfParams[0].trim());
                Date fechaFin = Date.valueOf(partsOfParams[1].trim());

                BCronograma_Curso bCronograma_Curso = new BCronograma_Curso();
                List<String[]> cronogramas = bCronograma_Curso.findByFechaRange(fechaInicio, fechaFin);

                if (cronogramas == null || cronogramas.isEmpty()) {
                    return "No se encontraron cronogramas en el rango de fechas: " + fechaInicio + " a " + fechaFin;
                }

                StringBuilder sb = new StringBuilder("=== CRONOGRAMAS EN RANGO: " + fechaInicio + " a " + fechaFin + " ===\n");
                for (String[] cronograma : cronogramas) {
                    sb.append("ID: ").append(cronograma[0])
                            .append(" | Curso-Gestión: ").append(cronograma[1])
                            .append(" | Descripción: ").append(cronograma[2])
                            .append(" | Fase: ").append(cronograma[3])
                            .append(" | Inicio: ").append(cronograma[4])
                            .append(" | Fin: ").append(cronograma[5])
                            .append(" | Activo: ").append(cronograma[6].equals("true") ? "Sí" : "No").append("\n");
                }
                return sb.toString();
            } catch (IllegalArgumentException e) {
                return "Error: Formato de fecha inválido. Use formato YYYY-MM-DD. " + e.getMessage();
            } catch (Exception e) {
                return "Error al buscar cronogramas por rango de fechas: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para findByFechaRange. Esperados: 2 (fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }
}