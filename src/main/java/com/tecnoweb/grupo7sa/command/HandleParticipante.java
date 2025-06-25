package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BParticipante;

import java.util.List;

public class HandleParticipante {

    // CU1 - HANDLERS PARA GESTIÓN DE PARTICIPANTES

    /**
     * Crear nuevo participante
     * Parámetros: carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 8) {
            try {
                String carnet = partsOfParams[0].trim();
                String nombre = partsOfParams[1].trim();
                String apellido = partsOfParams[2].trim();
                String email = partsOfParams[3].trim();
                String telefono = partsOfParams[4].trim();
                String universidad = partsOfParams[5].trim();
                int tipoParticipanteId = Integer.parseInt(partsOfParams[6].trim());
                String registro = partsOfParams[7].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(telefono)) telefono = null;
                if ("null".equalsIgnoreCase(universidad)) universidad = null;
                if ("null".equalsIgnoreCase(registro)) registro = null;

                BParticipante bParticipante = new BParticipante();
                String result = bParticipante.save(carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro);
                return result;
            } catch (NumberFormatException e) {
                return "Error: tipoParticipanteId debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 8 (carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar participante existente
     * Parámetros: id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 9) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String carnet = partsOfParams[1].trim();
                String nombre = partsOfParams[2].trim();
                String apellido = partsOfParams[3].trim();
                String email = partsOfParams[4].trim();
                String telefono = partsOfParams[5].trim();
                String universidad = partsOfParams[6].trim();
                int tipoParticipanteId = Integer.parseInt(partsOfParams[7].trim());
                String registro = partsOfParams[8].trim();

                // Convertir "null" string a null real
                if ("null".equalsIgnoreCase(telefono)) telefono = null;
                if ("null".equalsIgnoreCase(universidad)) universidad = null;
                if ("null".equalsIgnoreCase(registro)) registro = null;

                BParticipante bParticipante = new BParticipante();
                String result = bParticipante.update(id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID y tipoParticipanteId deben ser números válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 9 (id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar participante
     * Parámetros: id
     */
    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            String result = bParticipante.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar participante: " + e.getMessage();
        }
    }

    /**
     * Reactivar participante
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            String result = bParticipante.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar participante: " + e.getMessage();
        }
    }

    /**
     * Listar todos los participantes activos
     */
    public static String findAll() {
        try {
            BParticipante bParticipante = new BParticipante();
            List<String[]> participantes = bParticipante.findAllParticipantes();

            if (participantes == null || participantes.isEmpty()) {
                return "No hay participantes registrados.";
            }

            StringBuilder sb = new StringBuilder("=== PARTICIPANTES REGISTRADOS ===\n");
            for (String[] participante : participantes) {
                // Array: [0]id, [1]carnet, [2]nombre, [3]apellido, [4]email, [5]telefono, [6]universidad, [7]registro, [8]tipo_codigo, [9]tipo_descripcion, [10]activo
                sb.append("ID: ").append(participante[0])
                        .append(" | Nombre: ").append(participante[2]).append(" ").append(participante[3])
                        .append(" | Carnet: ").append(participante[1])
                        .append(" | Email: ").append(participante[4])
                        .append(" | Tipo: ").append(participante[8])
                        .append(" | Registro: ").append(participante[7] != null ? participante[7] : "N/A").append("\n");
            }
            sb.append("Total participantes: ").append(participantes.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar participantes: " + e.getMessage();
        }
    }

    /**
     * Buscar participante por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findOneById(id);

            if (participante == null) {
                return "No se encontró participante con ID: " + id;
            }

            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Carnet: " + participante[1] + "\n" +
                    "Nombre: " + participante[2] + " " + participante[3] + "\n" +
                    "Email: " + participante[4] + "\n" +
                    "Teléfono: " + (participante[5] != null ? participante[5] : "N/A") + "\n" +
                    "Universidad: " + (participante[6] != null ? participante[6] : "N/A") + "\n" +
                    "Registro: " + (participante[7] != null ? participante[7] : "N/A") + "\n" +
                    "Tipo: " + participante[8] + " - " + participante[9] + "\n" +
                    "Activo: " + (participante[10].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar participante: " + e.getMessage();
        }
    }

    /**
     * Buscar participante por carnet
     * Parámetros: carnet
     */
    public static String findByCarnet(String params) {
        try {
            String carnet = params.trim();
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findByCarnet(carnet);

            if (participante == null) {
                return "No se encontró participante con carnet: " + carnet;
            }

            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Carnet: " + participante[1] + "\n" +
                    "Nombre: " + participante[2] + " " + participante[3] + "\n" +
                    "Email: " + participante[4] + "\n" +
                    "Teléfono: " + (participante[5] != null ? participante[5] : "N/A") + "\n" +
                    "Universidad: " + (participante[6] != null ? participante[6] : "N/A") + "\n" +
                    "Registro: " + (participante[7] != null ? participante[7] : "N/A") + "\n" +
                    "Tipo: " + participante[8] + " - " + participante[9];
        } catch (Exception e) {
            return "Error al buscar participante por carnet: " + e.getMessage();
        }
    }

    /**
     * Buscar participante por email
     * Parámetros: email
     */
    public static String findByEmail(String params) {
        try {
            String email = params.trim();
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findByEmail(email);

            if (participante == null) {
                return "No se encontró participante con email: " + email;
            }

            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Carnet: " + participante[1] + "\n" +
                    "Nombre: " + participante[2] + " " + participante[3] + "\n" +
                    "Email: " + participante[4] + "\n" +
                    "Teléfono: " + (participante[5] != null ? participante[5] : "N/A") + "\n" +
                    "Universidad: " + (participante[6] != null ? participante[6] : "N/A") + "\n" +
                    "Registro: " + (participante[7] != null ? participante[7] : "N/A") + "\n" +
                    "Tipo: " + participante[8] + " - " + participante[9];
        } catch (Exception e) {
            return "Error al buscar participante por email: " + e.getMessage();
        }
    }

    /**
     * Buscar participantes por tipo
     * Parámetros: tipoParticipanteId
     */
    public static String findByTipo(String params) {
        try {
            int tipoParticipanteId = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            List<String[]> participantes = bParticipante.findByTipo(tipoParticipanteId);

            if (participantes == null || participantes.isEmpty()) {
                return "No hay participantes del tipo especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PARTICIPANTES POR TIPO ===\n");
            for (String[] participante : participantes) {
                sb.append("ID: ").append(participante[0])
                        .append(" | Nombre: ").append(participante[2]).append(" ").append(participante[3])
                        .append(" | Carnet: ").append(participante[1])
                        .append(" | Email: ").append(participante[4])
                        .append(" | Tipo: ").append(participante[8]).append("\n");
            }
            sb.append("Total participantes del tipo: ").append(participantes.size());
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El tipo de participante debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar participantes por tipo: " + e.getMessage();
        }
    }

    /**
     * Buscar participante por registro
     * Parámetros: registro
     */
    public static String findByRegistro(String params) {
        try {
            String registro = params.trim();
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findByRegistro(registro);

            if (participante == null) {
                return "No se encontró participante con registro: " + registro;
            }

            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Carnet: " + participante[1] + "\n" +
                    "Nombre: " + participante[2] + " " + participante[3] + "\n" +
                    "Email: " + participante[4] + "\n" +
                    "Teléfono: " + (participante[5] != null ? participante[5] : "N/A") + "\n" +
                    "Universidad: " + (participante[6] != null ? participante[6] : "N/A") + "\n" +
                    "Registro: " + participante[7] + "\n" +
                    "Tipo: " + participante[8] + " - " + participante[9];
        } catch (Exception e) {
            return "Error al buscar participante por registro: " + e.getMessage();
        }
    }
}