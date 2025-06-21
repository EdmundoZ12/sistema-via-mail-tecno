package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BCurso;

import java.util.List;

public class HandleCurso {

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 7) {
            try {
                String nombre = partsOfParams[0].trim();
                String descripcion = partsOfParams[1].trim();
                String logoUrl = partsOfParams[2].trim();
                int duracionHoras = Integer.parseInt(partsOfParams[3].trim());
                String modalidad = partsOfParams[4].trim();
                String nivel = partsOfParams[5].trim();
                String requisitos = partsOfParams[6].trim();

                BCurso bCurso = new BCurso();
                String result = bCurso.save(nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos);
                return result;
            } catch (NumberFormatException e) {
                return "Error: duracionHoras debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 7 (nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos), Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 9) {  // Ahora son 9 parámetros (agregamos activo)
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String descripcion = partsOfParams[2].trim();
                String logoUrl = partsOfParams[3].trim();
                int duracionHoras = Integer.parseInt(partsOfParams[4].trim());
                String modalidad = partsOfParams[5].trim();
                String nivel = partsOfParams[6].trim();
                String requisitos = partsOfParams[7].trim();
                boolean activo = Boolean.parseBoolean(partsOfParams[8].trim());

                BCurso bCurso = new BCurso();
                String result = bCurso.update(id, nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos, activo);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID y duracionHoras deben ser números válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar curso: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 9 (id, nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos, activo), Recibidos: " + partsOfParams.length;
        }
    }

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

    public static String findAll() {
        try {
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findAllCursos();

            if (cursos == null || cursos.isEmpty()) {
                return "No hay cursos registrados.";
            }

            StringBuilder sb = new StringBuilder("=== CURSOS REGISTRADOS ===\n");
            for (String[] curso : cursos) {
                sb.append("ID: ").append(curso[0])
                        .append(" | Nombre: ").append(curso[1])
                        .append(" | Duración: ").append(curso[4]).append(" horas")
                        .append(" | Modalidad: ").append(curso[5] != null ? curso[5] : "No especificada")
                        .append(" | Nivel: ").append(curso[6] != null ? curso[6] : "No especificado").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar cursos: " + e.getMessage();
        }
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BCurso bCurso = new BCurso();
            String[] curso = bCurso.findOneById(id);

            if (curso == null) {
                return "No se encontró curso con ID: " + id;
            }

            return "Curso encontrado:\n" +
                    "ID: " + curso[0] + "\n" +
                    "Nombre: " + curso[1] + "\n" +
                    "Descripción: " + (curso[2] != null ? curso[2] : "No especificada") + "\n" +
                    "Logo URL: " + (curso[3] != null ? curso[3] : "No especificada") + "\n" +
                    "Duración: " + curso[4] + " horas\n" +
                    "Modalidad: " + (curso[5] != null ? curso[5] : "No especificada") + "\n" +
                    "Nivel: " + (curso[6] != null ? curso[6] : "No especificado") + "\n" +
                    "Requisitos: " + (curso[7] != null ? curso[7] : "No especificados") + "\n" +
                    "Activo: " + (curso[8] != null ? curso[8] : "No especificado");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar curso: " + e.getMessage();
        }
    }

    public static String findByNombre(String params) {
        try {
            String nombre = params.trim();
            BCurso bCurso = new BCurso();
            String[] curso = bCurso.findOneByNombre(nombre);

            if (curso == null) {
                return "No se encontró curso con nombre: " + nombre;
            }

            return "Curso encontrado:\n" +
                    "ID: " + curso[0] + "\n" +
                    "Nombre: " + curso[1] + "\n" +
                    "Descripción: " + (curso[2] != null ? curso[2] : "No especificada") + "\n" +
                    "Logo URL: " + (curso[3] != null ? curso[3] : "No especificada") + "\n" +
                    "Duración: " + curso[4] + " horas\n" +
                    "Modalidad: " + (curso[5] != null ? curso[5] : "No especificada") + "\n" +
                    "Nivel: " + (curso[6] != null ? curso[6] : "No especificado") + "\n" +
                    "Requisitos: " + (curso[7] != null ? curso[7] : "No especificados") + "\n" +
                    "Activo: " + (curso[8] != null ? curso[8] : "No especificado");
        } catch (Exception e) {
            return "Error al buscar curso por nombre: " + e.getMessage();
        }
    }

    public static String findByModalidad(String params) {
        try {
            String modalidad = params.trim();
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findByModalidad(modalidad);

            if (cursos == null || cursos.isEmpty()) {
                return "No se encontraron cursos con modalidad: " + modalidad;
            }

            StringBuilder sb = new StringBuilder("=== CURSOS CON MODALIDAD: " + modalidad.toUpperCase() + " ===\n");
            for (String[] curso : cursos) {
                sb.append("ID: ").append(curso[0])
                        .append(" | Nombre: ").append(curso[1])
                        .append(" | Duración: ").append(curso[4]).append(" horas")
                        .append(" | Nivel: ").append(curso[6] != null ? curso[6] : "No especificado").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar cursos por modalidad: " + e.getMessage();
        }
    }

    public static String findByNivel(String params) {
        try {
            String nivel = params.trim();
            BCurso bCurso = new BCurso();
            List<String[]> cursos = bCurso.findByNivel(nivel);

            if (cursos == null || cursos.isEmpty()) {
                return "No se encontraron cursos con nivel: " + nivel;
            }

            StringBuilder sb = new StringBuilder("=== CURSOS CON NIVEL: " + nivel.toUpperCase() + " ===\n");
            for (String[] curso : cursos) {
                sb.append("ID: ").append(curso[0])
                        .append(" | Nombre: ").append(curso[1])
                        .append(" | Duración: ").append(curso[4]).append(" horas")
                        .append(" | Modalidad: ").append(curso[5] != null ? curso[5] : "No especificada").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar cursos por nivel: " + e.getMessage();
        }
    }

    public static String findByDuracion(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int duracionMinima = Integer.parseInt(partsOfParams[0].trim());
                int duracionMaxima = Integer.parseInt(partsOfParams[1].trim());

                BCurso bCurso = new BCurso();
                List<String[]> cursos = bCurso.findByDuracion(duracionMinima, duracionMaxima);

                if (cursos == null || cursos.isEmpty()) {
                    return "No se encontraron cursos con duración entre " + duracionMinima + " y " + duracionMaxima + " horas";
                }

                StringBuilder sb = new StringBuilder("=== CURSOS CON DURACIÓN ENTRE " + duracionMinima + "-" + duracionMaxima + " HORAS ===\n");
                for (String[] curso : cursos) {
                    sb.append("ID: ").append(curso[0])
                            .append(" | Nombre: ").append(curso[1])
                            .append(" | Duración: ").append(curso[4]).append(" horas")
                            .append(" | Modalidad: ").append(curso[5] != null ? curso[5] : "No especificada")
                            .append(" | Nivel: ").append(curso[6] != null ? curso[6] : "No especificado").append("\n");
                }
                return sb.toString();
            } catch (NumberFormatException e) {
                return "Error: duracionMinima y duracionMaxima deben ser números válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al buscar cursos por duración: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para findByDuracion. Esperados: 2 (duracionMinima, duracionMaxima), Recibidos: " + partsOfParams.length;
        }
    }
}