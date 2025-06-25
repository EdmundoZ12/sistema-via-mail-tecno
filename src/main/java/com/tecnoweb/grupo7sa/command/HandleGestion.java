package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BGestion;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HandleGestion {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // CU2 - HANDLERS PARA GESTIÓN DE PERÍODOS ACADÉMICOS

    /**
     * Crear nueva gestión
     * Parámetros: nombre, descripcion, fechaInicio, fechaFin
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 4) {
            try {
                String nombre = partsOfParams[0].trim();
                String descripcion = partsOfParams[1].trim();
                String fechaInicioStr = partsOfParams[2].trim();
                String fechaFinStr = partsOfParams[3].trim();

                // Convertir "null" string a null real para descripción
                if ("null".equalsIgnoreCase(descripcion)) descripcion = null;

                // Convertir strings a Date
                Date fechaInicio = convertStringToDate(fechaInicioStr);
                Date fechaFin = convertStringToDate(fechaFinStr);

                if (fechaInicio == null) {
                    return "Error: Formato de fecha de inicio inválido. Use formato yyyy-MM-dd";
                }
                if (fechaFin == null) {
                    return "Error: Formato de fecha de fin inválido. Use formato yyyy-MM-dd";
                }

                BGestion bGestion = new BGestion();
                String result = bGestion.save(nombre, descripcion, fechaInicio, fechaFin);
                return result;
            } catch (Exception e) {
                return "Error al guardar gestión: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 4 (nombre, descripcion, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar gestión existente
     * Parámetros: id, nombre, descripcion, fechaInicio, fechaFin
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 5) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String descripcion = partsOfParams[2].trim();
                String fechaInicioStr = partsOfParams[3].trim();
                String fechaFinStr = partsOfParams[4].trim();

                // Convertir "null" string a null real para descripción
                if ("null".equalsIgnoreCase(descripcion)) descripcion = null;

                // Convertir strings a Date
                Date fechaInicio = convertStringToDate(fechaInicioStr);
                Date fechaFin = convertStringToDate(fechaFinStr);

                if (fechaInicio == null) {
                    return "Error: Formato de fecha de inicio inválido. Use formato yyyy-MM-dd";
                }
                if (fechaFin == null) {
                    return "Error: Formato de fecha de fin inválido. Use formato yyyy-MM-dd";
                }

                BGestion bGestion = new BGestion();
                String result = bGestion.update(id, nombre, descripcion, fechaInicio, fechaFin);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar gestión: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 5 (id, nombre, descripcion, fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar gestión
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String result = bGestion.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar gestión: " + e.getMessage();
        }
    }

    /**
     * Reactivar gestión
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String result = bGestion.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar gestión: " + e.getMessage();
        }
    }

    /**
     * Listar todas las gestiones activas
     */
    public static String findAll() {
        try {
            BGestion bGestion = new BGestion();
            List<String[]> gestiones = bGestion.findAll();

            if (gestiones == null || gestiones.isEmpty()) {
                return "No hay gestiones registradas.";
            }

            StringBuilder sb = new StringBuilder("=== GESTIONES REGISTRADAS ===\n");
            for (String[] gestion : gestiones) {
                // Array: [0]id, [1]nombre, [2]descripcion, [3]fechaInicio, [4]fechaFin, [5]activo
                sb.append("ID: ").append(gestion[0])
                        .append(" | Nombre: ").append(gestion[1])
                        .append(" | Inicio: ").append(gestion[3])
                        .append(" | Fin: ").append(gestion[4])
                        .append(" | Activo: ").append(gestion[5].equals("true") ? "Sí" : "No");

                if (gestion[2] != null && !gestion[2].isEmpty()) {
                    sb.append(" | Descripción: ").append(gestion[2]);
                }
                sb.append("\n");
            }
            sb.append("Total gestiones: ").append(gestiones.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar gestiones: " + e.getMessage();
        }
    }

    /**
     * Buscar gestión por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String[] gestion = bGestion.findOneById(id);

            if (gestion == null) {
                return "No se encontró gestión con ID: " + id;
            }

            // Array: [0]id, [1]nombre, [2]descripcion, [3]fechaInicio, [4]fechaFin, [5]activo
            return "Gestión encontrada:\n" +
                    "ID: " + gestion[0] + "\n" +
                    "Nombre: " + gestion[1] + "\n" +
                    "Descripción: " + (gestion[2] != null ? gestion[2] : "Sin descripción") + "\n" +
                    "Fecha Inicio: " + gestion[3] + "\n" +
                    "Fecha Fin: " + gestion[4] + "\n" +
                    "Activo: " + (gestion[5].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar gestión: " + e.getMessage();
        }
    }

    /**
     * Buscar gestiones por rango de fechas
     * Parámetros: fechaInicio, fechaFin
     */
    public static String findByDateRange(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                String fechaInicioStr = partsOfParams[0].trim();
                String fechaFinStr = partsOfParams[1].trim();

                // Convertir strings a Date
                Date fechaInicio = convertStringToDate(fechaInicioStr);
                Date fechaFin = convertStringToDate(fechaFinStr);

                if (fechaInicio == null) {
                    return "Error: Formato de fecha de inicio inválido. Use formato yyyy-MM-dd";
                }
                if (fechaFin == null) {
                    return "Error: Formato de fecha de fin inválido. Use formato yyyy-MM-dd";
                }

                BGestion bGestion = new BGestion();
                List<String[]> gestiones = bGestion.findByDateRange(fechaInicio, fechaFin);

                if (gestiones == null || gestiones.isEmpty()) {
                    return "No hay gestiones en el rango de fechas especificado.";
                }

                StringBuilder sb = new StringBuilder("=== GESTIONES POR RANGO DE FECHAS ===\n");
                for (String[] gestion : gestiones) {
                    sb.append("ID: ").append(gestion[0])
                            .append(" | Nombre: ").append(gestion[1])
                            .append(" | Inicio: ").append(gestion[3])
                            .append(" | Fin: ").append(gestion[4]).append("\n");
                }
                sb.append("Total gestiones en rango: ").append(gestiones.size());
                return sb.toString();
            } catch (Exception e) {
                return "Error al buscar gestiones por rango de fechas: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para findByDateRange. Esperados: 2 (fechaInicio, fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Obtener gestiones vigentes actualmente (⭐ ENDPOINT IMPORTANTE)
     */
    public static String findCurrent() {
        try {
            BGestion bGestion = new BGestion();
            List<String[]> gestiones = bGestion.findCurrent();

            if (gestiones == null || gestiones.isEmpty()) {
                return "No hay gestiones vigentes actualmente.";
            }

            StringBuilder sb = new StringBuilder("=== GESTIONES VIGENTES ACTUALMENTE ===\n");
            for (String[] gestion : gestiones) {
                sb.append("ID: ").append(gestion[0])
                        .append(" | Nombre: ").append(gestion[1])
                        .append(" | Inicio: ").append(gestion[3])
                        .append(" | Fin: ").append(gestion[4]);

                if (gestion[2] != null && !gestion[2].isEmpty()) {
                    sb.append(" | Descripción: ").append(gestion[2]);
                }
                sb.append("\n");
            }
            sb.append("Total gestiones vigentes: ").append(gestiones.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar gestiones vigentes: " + e.getMessage();
        }
    }

    /**
     * Buscar gestiones por nombre (búsqueda parcial)
     * Parámetros: nombre
     */
    public static String findByName(String params) {
        try {
            String nombre = params.trim();
            BGestion bGestion = new BGestion();
            List<String[]> gestiones = bGestion.findByName(nombre);

            if (gestiones == null || gestiones.isEmpty()) {
                return "No hay gestiones que coincidan con el nombre: " + nombre;
            }

            StringBuilder sb = new StringBuilder("=== GESTIONES POR NOMBRE ===\n");
            for (String[] gestion : gestiones) {
                sb.append("ID: ").append(gestion[0])
                        .append(" | Nombre: ").append(gestion[1])
                        .append(" | Inicio: ").append(gestion[3])
                        .append(" | Fin: ").append(gestion[4]);

                if (gestion[2] != null && !gestion[2].isEmpty()) {
                    sb.append(" | Descripción: ").append(gestion[2]);
                }
                sb.append("\n");
            }
            sb.append("Total gestiones encontradas: ").append(gestiones.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar gestiones por nombre: " + e.getMessage();
        }
    }

    /**
     * Método auxiliar para convertir String a java.sql.Date
     *
     * @param dateString Fecha en formato yyyy-MM-dd
     * @return java.sql.Date o null si el formato es inválido
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