package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BPrecioCurso;

import java.util.List;

public class HandlePrecio {

    // CU3 - HANDLERS PARA GESTIÓN DE PRECIOS DE CURSOS

    /**
     * Crear nuevo precio diferenciado
     * Parámetros: cursoId, tipoParticipanteId, precio
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                int cursoId = Integer.parseInt(partsOfParams[0].trim());
                int tipoParticipanteId = Integer.parseInt(partsOfParams[1].trim());
                double precio = Double.parseDouble(partsOfParams[2].trim());

                BPrecioCurso bPrecioCurso = new BPrecioCurso();
                String result = bPrecioCurso.save(cursoId, tipoParticipanteId, precio);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores numéricos deben ser válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar precio: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 3 (cursoId, tipoParticipanteId, precio), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar precio existente
     * Parámetros: id, cursoId, tipoParticipanteId, precio
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 4) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                int cursoId = Integer.parseInt(partsOfParams[1].trim());
                int tipoParticipanteId = Integer.parseInt(partsOfParams[2].trim());
                double precio = Double.parseDouble(partsOfParams[3].trim());

                BPrecioCurso bPrecioCurso = new BPrecioCurso();
                String result = bPrecioCurso.update(id, cursoId, tipoParticipanteId, precio);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores numéricos deben ser válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar precio: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 4 (id, cursoId, tipoParticipanteId, precio), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar solo el monto del precio ⭐ FUNCIONALIDAD ESPECIAL
     * Parámetros: id, nuevoPrecio
     */
    public static String updatePrecio(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                double nuevoPrecio = Double.parseDouble(partsOfParams[1].trim());

                BPrecioCurso bPrecioCurso = new BPrecioCurso();
                String result = bPrecioCurso.updatePrecio(id, nuevoPrecio);
                return result;
            } catch (NumberFormatException e) {
                return "Error: Los valores numéricos deben ser válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar precio: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para updatePrecio. Esperados: 2 (id, nuevoPrecio), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar precio
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            String result = bPrecioCurso.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar precio: " + e.getMessage();
        }
    }

    /**
     * Reactivar precio
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            String result = bPrecioCurso.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar precio: " + e.getMessage();
        }
    }

    /**
     * Listar todos los precios activos
     */
    public static String findAll() {
        try {
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            List<String[]> precios = bPrecioCurso.findAll();

            if (precios == null || precios.isEmpty()) {
                return "No hay precios registrados.";
            }

            StringBuilder sb = new StringBuilder("=== PRECIOS DE CURSOS ===\n");
            for (String[] precio : precios) {
                // Array: [0]id, [1]cursoId, [2]tipoParticipanteId, [3]precio, [4]activo, 
                // [5]cursoNombre, [6]tipoCodigo, [7]tipoDescripcion
                sb.append("ID: ").append(precio[0])
                        .append(" | Curso: ").append(precio[5])
                        .append(" | Tipo: ").append(precio[6])
                        .append(" (").append(precio[7]).append(")")
                        .append(" | Precio: $").append(precio[3])
                        .append("\n");
            }
            sb.append("Total precios: ").append(precios.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar precios: " + e.getMessage();
        }
    }

    /**
     * Buscar precio por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            String[] precio = bPrecioCurso.findOneById(id);

            if (precio == null) {
                return "No se encontró precio con ID: " + id;
            }

            // Array: [0]id, [1]cursoId, [2]tipoParticipanteId, [3]precio, [4]activo, 
            // [5]cursoNombre, [6]tipoCodigo, [7]tipoDescripcion
            return "Precio encontrado:\n" +
                    "ID: " + precio[0] + "\n" +
                    "Curso: " + precio[5] + " (ID: " + precio[1] + ")\n" +
                    "Tipo Participante: " + precio[6] + " - " + precio[7] + " (ID: " + precio[2] + ")\n" +
                    "Precio: $" + precio[3] + "\n" +
                    "Activo: " + (precio[4].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar precio: " + e.getMessage();
        }
    }

    /**
     * Obtener precios de un curso específico ⭐ ENDPOINT PRINCIPAL
     * Parámetros: cursoId
     */
    public static String findByCurso(String params) {
        try {
            int cursoId = Integer.parseInt(params.trim());
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            List<String[]> precios = bPrecioCurso.findByCurso(cursoId);

            if (precios == null || precios.isEmpty()) {
                return "No hay precios definidos para el curso especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PRECIOS DEL CURSO ===\n");
            String cursoNombre = precios.get(0)[5]; // Nombre del curso
            sb.append("Curso: ").append(cursoNombre).append(" (ID: ").append(cursoId).append(")\n\n");

            for (String[] precio : precios) {
                sb.append("• ").append(precio[6]).append(" (").append(precio[7]).append(")")
                        .append(": $").append(precio[3]).append("\n");
            }
            sb.append("\nTotal tipos de precio: ").append(precios.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del curso debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar precios por curso: " + e.getMessage();
        }
    }

    /**
     * Buscar precios por tipo de participante
     * Parámetros: tipoParticipanteId
     */
    public static String findByTipo(String params) {
        try {
            int tipoParticipanteId = Integer.parseInt(params.trim());
            BPrecioCurso bPrecioCurso = new BPrecioCurso();
            List<String[]> precios = bPrecioCurso.findByTipo(tipoParticipanteId);

            if (precios == null || precios.isEmpty()) {
                return "No hay precios definidos para el tipo de participante especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PRECIOS POR TIPO DE PARTICIPANTE ===\n");
            String tipoNombre = precios.get(0)[6] + " - " + precios.get(0)[7];
            sb.append("Tipo: ").append(tipoNombre).append(" (ID: ").append(tipoParticipanteId).append(")\n\n");

            for (String[] precio : precios) {
                sb.append("• ").append(precio[5]).append(": $").append(precio[3]).append("\n");
            }
            sb.append("\nTotal cursos con precio: ").append(precios.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El ID del tipo debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar precios por tipo: " + e.getMessage();
        }
    }

    /**
     * Obtener precio específico para un curso y tipo de participante ⭐ ENDPOINT PRINCIPAL
     * Para calcular costo exacto de inscripción
     * Parámetros: cursoId, tipoParticipanteId
     */
    public static String findEspecifico(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                int cursoId = Integer.parseInt(partsOfParams[0].trim());
                int tipoParticipanteId = Integer.parseInt(partsOfParams[1].trim());

                BPrecioCurso bPrecioCurso = new BPrecioCurso();
                String[] precio = bPrecioCurso.findEspecifico(cursoId, tipoParticipanteId);

                if (precio == null) {
                    return "No se encontró precio específico para el curso " + cursoId + " y tipo de participante " + tipoParticipanteId;
                }

                // Array: [0]id, [1]cursoId, [2]tipoParticipanteId, [3]precio, [4]activo, 
                // [5]cursoNombre, [6]tipoCodigo, [7]tipoDescripcion
                return "Precio específico encontrado:\n" +
                        "Curso: " + precio[5] + " (ID: " + precio[1] + ")\n" +
                        "Tipo: " + precio[6] + " - " + precio[7] + " (ID: " + precio[2] + ")\n" +
                        "Precio: $" + precio[3] + "\n" +
                        "ID Precio: " + precio[0];
            } catch (NumberFormatException e) {
                return "Error: Los IDs deben ser numéricos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al buscar precio específico: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para findEspecifico. Esperados: 2 (cursoId, tipoParticipanteId), Recibidos: " + partsOfParams.length;
        }
    }
}