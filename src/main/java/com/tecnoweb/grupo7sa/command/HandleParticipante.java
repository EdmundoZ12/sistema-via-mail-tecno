package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BParticipante;

import java.util.List;

public class HandleParticipante {

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 10) {
            try {
                String nombre = partsOfParams[0].trim();
                String apellido = partsOfParams[1].trim();
                String carnet = partsOfParams[2].trim();
                String registro = partsOfParams[3].trim();
                String carrera = partsOfParams[4].trim();
                String email = partsOfParams[5].trim();
                String facultad = partsOfParams[6].trim();
                String telefono = partsOfParams[7].trim();
                String universidad = partsOfParams[8].trim();
                int tipoParticipanteId = Integer.parseInt(partsOfParams[9].trim());

                BParticipante bParticipante = new BParticipante();
                // ⭐ ACTUALIZADO: Nuevo orden de parámetros según BParticipante
                String result = bParticipante.save(nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId);
                return result;
            } catch (NumberFormatException e) {
                return "Error: tipoParticipanteId debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al guardar participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 10, Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 11) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String apellido = partsOfParams[2].trim();
                String carnet = partsOfParams[3].trim();
                String registro = partsOfParams[4].trim();
                String carrera = partsOfParams[5].trim();
                String email = partsOfParams[6].trim();
                String facultad = partsOfParams[7].trim();
                String telefono = partsOfParams[8].trim();
                String universidad = partsOfParams[9].trim();
                int tipoParticipanteId = Integer.parseInt(partsOfParams[10].trim());

                BParticipante bParticipante = new BParticipante();
                // ⭐ ACTUALIZADO: Nuevo orden de parámetros según BParticipante
                String result = bParticipante.update(id, nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID y tipoParticipanteId deben ser números válidos. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar participante: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 11, Recibidos: " + partsOfParams.length;
        }
    }

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

    public static String findAll() {
        try {
            BParticipante bParticipante = new BParticipante();
            List<String[]> participantes = bParticipante.findAllParticipantes();

            if (participantes == null || participantes.isEmpty()) {
                return "No hay participantes registrados.";
            }

            StringBuilder sb = new StringBuilder("=== PARTICIPANTES REGISTRADOS ===\n");
            for (String[] participante : participantes) {
                // ⭐ CORREGIDO: Índices según nuevo array [12]
                // [0]=id, [1]=apellido, [2]=carnet, [3]=registro, [4]=carrera, [5]=email, [6]=facultad, [7]=nombre, [8]=telefono, [9]=universidad, [10]=tipo_id, [11]=tipo_nombre
                sb.append("ID: ").append(participante[0])
                        .append(" | Nombre: ").append(participante[7]).append(" ").append(participante[1])
                        .append(" | Email: ").append(participante[5])
                        .append(" | Carnet: ").append(participante[2])
                        .append(" | Registro: ").append(participante[3]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar participantes: " + e.getMessage();
        }
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findOneById(id);

            if (participante == null) {
                return "No se encontró participante con ID: " + id;
            }

            // ⭐ CORREGIDO: Índices según nuevo array [12]
            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Nombre: " + participante[7] + " " + participante[1] + "\n" +
                    "Email: " + participante[5] + "\n" +
                    "Carnet: " + participante[2] + "\n" +
                    "Registro: " + participante[3] + "\n" +
                    "Teléfono: " + participante[8] + "\n" +
                    "Carrera: " + participante[4] + "\n" +
                    "Facultad: " + participante[6] + "\n" +
                    "Universidad: " + participante[9] + "\n" +
                    "Tipo: " + participante[11];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar participante: " + e.getMessage();
        }
    }

    public static String findByCarnet(String params) {
        try {
            String carnet = params.trim();
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findOneByCarnet(carnet);

            if (participante == null) {
                return "No se encontró participante con carnet: " + carnet;
            }

            // ⭐ CORREGIDO: Índices según nuevo array [12]
            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Nombre: " + participante[7] + " " + participante[1] + "\n" +
                    "Email: " + participante[5] + "\n" +
                    "Carnet: " + participante[2] + "\n" +
                    "Registro: " + participante[3] + "\n" +
                    "Teléfono: " + participante[8] + "\n" +
                    "Carrera: " + participante[4] + "\n" +
                    "Facultad: " + participante[6] + "\n" +
                    "Universidad: " + participante[9] + "\n" +
                    "Tipo: " + participante[11];
        } catch (Exception e) {
            return "Error al buscar participante por carnet: " + e.getMessage();
        }
    }

    public static String findByRegistro(String params) {
        try {
            String registro = params.trim();
            BParticipante bParticipante = new BParticipante();
            String[] participante = bParticipante.findOneByRegistro(registro);

            if (participante == null) {
                return "No se encontró participante con registro: " + registro;
            }

            return "Participante encontrado:\n" +
                    "ID: " + participante[0] + "\n" +
                    "Nombre: " + participante[7] + " " + participante[1] + "\n" +
                    "Email: " + participante[5] + "\n" +
                    "Carnet: " + participante[2] + "\n" +
                    "Registro: " + participante[3] + "\n" +
                    "Teléfono: " + participante[8] + "\n" +
                    "Carrera: " + participante[4] + "\n" +
                    "Facultad: " + participante[6] + "\n" +
                    "Universidad: " + participante[9] + "\n" +
                    "Tipo: " + participante[11];
        } catch (Exception e) {
            return "Error al buscar participante por registro: " + e.getMessage();
        }
    }

    public static String findByTipo(String params) {
        try {
            int tipoParticipanteId = Integer.parseInt(params.trim());
            BParticipante bParticipante = new BParticipante();
            List<String[]> participantes = bParticipante.findByTipoParticipante(tipoParticipanteId);

            if (participantes == null || participantes.isEmpty()) {
                return "No hay participantes del tipo especificado.";
            }

            StringBuilder sb = new StringBuilder("=== PARTICIPANTES POR TIPO ===\n");
            for (String[] participante : participantes) {
                sb.append("ID: ").append(participante[0])
                        .append(" | Nombre: ").append(participante[7]).append(" ").append(participante[1])
                        .append(" | Email: ").append(participante[5])
                        .append(" | Carnet: ").append(participante[2])
                        .append(" | Registro: ").append(participante[3])
                        .append(" | Tipo: ").append(participante[11]).append("\n");
            }
            return sb.toString();
        } catch (NumberFormatException e) {
            return "Error: El tipo de participante debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar participantes por tipo: " + e.getMessage();
        }
    }
}