package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BGestion;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HandleGestion {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 4) {
            try {
                String descripcion = partsOfParams[0].trim();
                String fechaInicioStr = partsOfParams[1].trim();
                String fechaFinStr = partsOfParams[2].trim();
                String nombre = partsOfParams[3].trim();

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
                String result = bGestion.crearGestion(descripcion, fechaInicio, fechaFin, nombre);
                return result;
            } catch (Exception e) {
                return "Error al guardar gestión: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 4 (descripcion,fechaInicio,fechaFin,nombre), Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 5) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String descripcion = partsOfParams[1].trim();
                String fechaInicioStr = partsOfParams[2].trim();
                String fechaFinStr = partsOfParams[3].trim();
                String nombre = partsOfParams[4].trim();

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
                String result = bGestion.actualizarGestion(id, descripcion, fechaInicio, fechaFin, nombre);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar gestión: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 5 (id,descripcion,fechaInicio,fechaFin,nombre), Recibidos: " + partsOfParams.length;
        }
    }

    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String result = bGestion.desactivarGestion(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al desactivar gestión: " + e.getMessage();
        }
    }

    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String result = bGestion.reactivarGestion(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar gestión: " + e.getMessage();
        }
    }

    public static String findAll() {
        try {
            BGestion bGestion = new BGestion();
            List<String[]> gestiones = bGestion.obtenerGestiones();

            if (gestiones == null || gestiones.isEmpty()) {
                return "No hay gestiones registradas.";
            }

            StringBuilder sb = new StringBuilder("=== GESTIONES REGISTRADAS ===\n");
            for (String[] gestion : gestiones) {
                // [0]=id, [1]=descripcion, [2]=estado, [3]=fecha_inicio, [4]=fecha_fin, [5]=nombre
                sb.append("ID: ").append(gestion[0])
                        .append(" | Nombre: ").append(gestion[5])
                        .append(" | Estado: ").append(gestion[2].equals("true") ? "Activo" : "Inactivo")
                        .append(" | Inicio: ").append(gestion[3])
                        .append(" | Fin: ").append(gestion[4]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar gestiones: " + e.getMessage();
        }
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BGestion bGestion = new BGestion();
            String[] gestion = bGestion.obtenerUnaGestion(id);

            if (gestion == null) {
                return "No se encontró gestión con ID: " + id;
            }

            // [0]=id, [1]=descripcion, [2]=estado, [3]=fecha_inicio, [4]=fecha_fin, [5]=nombre
            return "Gestión encontrada:\n" +
                    "ID: " + gestion[0] + "\n" +
                    "Nombre: " + gestion[5] + "\n" +
                    "Descripción: " + (gestion[1] != null ? gestion[1] : "Sin descripción") + "\n" +
                    "Estado: " + (gestion[2].equals("true") ? "Activo" : "Inactivo") + "\n" +
                    "Fecha Inicio: " + gestion[3] + "\n" +
                    "Fecha Fin: " + gestion[4];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar gestión: " + e.getMessage();
        }
    }

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
                List<String[]> gestiones = bGestion.obtenerGestionesPorFecha(fechaInicio, fechaFin);

                if (gestiones == null || gestiones.isEmpty()) {
                    return "No hay gestiones en el rango de fechas especificado.";
                }

                StringBuilder sb = new StringBuilder("=== GESTIONES POR RANGO DE FECHAS ===\n");
                for (String[] gestion : gestiones) {
                    sb.append("ID: ").append(gestion[0])
                            .append(" | Nombre: ").append(gestion[5])
                            .append(" | Estado: ").append(gestion[2].equals("true") ? "Activo" : "Inactivo")
                            .append(" | Inicio: ").append(gestion[3])
                            .append(" | Fin: ").append(gestion[4]).append("\n");
                }
                return sb.toString();
            } catch (Exception e) {
                return "Error al buscar gestiones por rango de fechas: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para findByDateRange. Esperados: 2 (fechaInicio,fechaFin), Recibidos: " + partsOfParams.length;
        }
    }

    public static String findByName(String params) {
        try {
            String nombre = params.trim();
            BGestion bGestion = new BGestion();
            List<String[]> gestiones = bGestion.buscarGestionesPorNombre(nombre);

            if (gestiones == null || gestiones.isEmpty()) {
                return "No hay gestiones que coincidan con el nombre: " + nombre;
            }

            StringBuilder sb = new StringBuilder("=== GESTIONES POR NOMBRE ===\n");
            for (String[] gestion : gestiones) {
                sb.append("ID: ").append(gestion[0])
                        .append(" | Nombre: ").append(gestion[5])
                        .append(" | Estado: ").append(gestion[2].equals("true") ? "Activo" : "Inactivo")
                        .append(" | Inicio: ").append(gestion[3])
                        .append(" | Fin: ").append(gestion[4])
                        .append(" | Descripción: ").append(gestion[1] != null ? gestion[1] : "Sin descripción").append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar gestiones por nombre: " + e.getMessage();
        }
    }

    /**
     * Método auxiliar para convertir String a java.sql.Date
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