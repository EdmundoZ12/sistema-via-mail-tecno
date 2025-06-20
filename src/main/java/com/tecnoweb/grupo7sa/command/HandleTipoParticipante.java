package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BTipoParticipante;

import java.util.List;

public class HandleTipoParticipante {

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 3) {
            try {
                String nombre = partsOfParams[0].trim();
                String codigo = partsOfParams[1].trim();
                String descripcion = partsOfParams[2].trim();

                BTipoParticipante bTipoParticipante = new BTipoParticipante();
                String result = bTipoParticipante.save(nombre, codigo, descripcion);
                return result;
            } catch (Exception e) {
                return "Error al guardar tipo de participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 3 (nombre, codigo, descripcion), Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 4) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String codigo = partsOfParams[2].trim();
                String descripcion = partsOfParams[3].trim();

                BTipoParticipante bTipoParticipante = new BTipoParticipante();
                String result = bTipoParticipante.update(id, nombre, codigo, descripcion);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar tipo de participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 4 (id, nombre, codigo, descripcion), Recibidos: " + partsOfParams.length;
        }
    }

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

    public static String findAll() {
        try {
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            List<String[]> tipos = bTipoParticipante.findAllTipos();

            if (tipos == null || tipos.isEmpty()) {
                return "No hay tipos de participante registrados.";
            }

            StringBuilder sb = new StringBuilder("=== TIPOS DE PARTICIPANTE ===\n");
            for (String[] tipo : tipos) {
                sb.append("ID: ").append(tipo[0])
                        .append(" | Nombre: ").append(tipo[1])
                        .append(" | Código: ").append(tipo[2])
                        .append(" | Descripción: ").append(tipo[3]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar tipos de participante: " + e.getMessage();
        }
    }

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
                    "Nombre: " + tipo[1] + "\n" +
                    "Código: " + tipo[2] + "\n" +
                    "Descripción: " + tipo[3];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar tipo de participante: " + e.getMessage();
        }
    }

    public static String findByCodigo(String params) {
        try {
            String codigo = params.trim();
            BTipoParticipante bTipoParticipante = new BTipoParticipante();
            String[] tipo = bTipoParticipante.findOneByCodigo(codigo);

            if (tipo == null) {
                return "No se encontró tipo de participante con código: " + codigo;
            }

            return "Tipo de participante encontrado:\n" +
                    "ID: " + tipo[0] + "\n" +
                    "Nombre: " + tipo[1] + "\n" +
                    "Código: " + tipo[2] + "\n" +
                    "Descripción: " + tipo[3];
        } catch (Exception e) {
            return "Error al buscar tipo de participante por código: " + e.getMessage();
        }
    }
}