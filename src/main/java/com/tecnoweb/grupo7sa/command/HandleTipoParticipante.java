package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BTipoParticipante;

import java.util.List;

public class HandleTipoParticipante {

    // CU1 - HANDLERS PARA GESTIÓN DE TIPOS DE PARTICIPANTE

    /**
     * Crear nuevo tipo de participante
     * Parámetros: codigo, descripcion
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 2) {
            try {
                String codigo = partsOfParams[0].trim();
                String descripcion = partsOfParams[1].trim();

                BTipoParticipante bTipoParticipante = new BTipoParticipante();
                String result = bTipoParticipante.save(codigo, descripcion);
                return result;
            } catch (Exception e) {
                return "Error al guardar tipo de participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 2 (codigo, descripcion), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar tipo de participante existente
     * Parámetros: id, codigo, descripcion
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String codigo = partsOfParams[1].trim();
                String descripcion = partsOfParams[2].trim();

                BTipoParticipante bTipoParticipante = new BTipoParticipante();
                String result = bTipoParticipante.update(id, codigo, descripcion);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar tipo de participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 3 (id, codigo, descripcion), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar tipo de participante
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            String result = bTipoParticipante.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar tipo de participante: " + e.getMessage();
        }
    }

    /**
     * Reactivar tipo de participante
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            String result = bTipoParticipante.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar tipo de participante: " + e.getMessage();
        }
    }

    /**
     * Listar todos los tipos de participante activos
     */
    public static String findAll() {
        try {
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            List<String[]> tipos = bTipoParticipante.findAllTipos();

            if (tipos == null || tipos.isEmpty()) {
                return "No hay tipos de participante registrados.";
            }

            StringBuilder sb = new StringBuilder("=== TIPOS DE PARTICIPANTE ===\n");
            for (String[] tipo : tipos) {
                // Array: [0]id, [1]codigo, [2]descripcion, [3]activo
                sb.append("ID: ").append(tipo[0])
                        .append(" | Código: ").append(tipo[1])
                        .append(" | Descripción: ").append(tipo[2])
                        .append(" | Activo: ").append(tipo[3].equals("true") ? "Sí" : "No").append("\n");
            }
            sb.append("Total tipos: ").append(tipos.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar tipos de participante: " + e.getMessage();
        }
    }

    /**
     * Buscar tipo de participante por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            String[] tipo = bTipoParticipante.findOneById(id);

            if (tipo == null) {
                return "No se encontró tipo de participante con ID: " + id;
            }

            return "Tipo de participante encontrado:\n" +
                    "ID: " + tipo[0] + "\n" +
                    "Código: " + tipo[1] + "\n" +
                    "Descripción: " + tipo[2] + "\n" +
                    "Activo: " + (tipo[3].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar tipo de participante: " + e.getMessage();
        }
    }

    /**
     * Buscar tipo de participante por código
     * Parámetros: codigo
     */
    public static String findByCodigo(String params) {
        try {
            String codigo = params.trim();
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            String[] tipo = bTipoParticipante.findByCodigo(codigo);

            if (tipo == null) {
                return "No se encontró tipo de participante con código: " + codigo;
            }

            return "Tipo de participante encontrado:\n" +
                    "ID: " + tipo[0] + "\n" +
                    "Código: " + tipo[1] + "\n" +
                    "Descripción: " + tipo[2] + "\n" +
                    "Activo: " + (tipo[3].equals("true") ? "Sí" : "No");
        } catch (Exception e) {
            return "Error al buscar tipo de participante por código: " + e.getMessage();
        }
    }
}