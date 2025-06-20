package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BUsuario;

import java.util.List;

public class HandleUsuario {

    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 8) {
            try {
                String nombre = partsOfParams[0].trim();
                String apellido = partsOfParams[1].trim();
                String email = partsOfParams[2].trim();
                String registro = partsOfParams[3].trim();
                String telefono = partsOfParams[4].trim();
                String carnet = partsOfParams[5].trim();
                String password = partsOfParams[6].trim();
                String rol = partsOfParams[7].trim();

                BUsuario bUsuario = new BUsuario();
                String result = bUsuario.save(nombre, apellido, email, registro, telefono, carnet, password, rol);
                return result;
            } catch (Exception e) {
                return "Error al guardar usuario: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 8 (nombre, apellido, email, registro, telefono, carnet, password, rol), Recibidos: " + partsOfParams.length;
        }
    }

    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 9) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String apellido = partsOfParams[2].trim();
                String email = partsOfParams[3].trim();
                String registro = partsOfParams[4].trim();
                String telefono = partsOfParams[5].trim();
                String carnet = partsOfParams[6].trim();
                String password = partsOfParams[7].trim();
                String rol = partsOfParams[8].trim();

                BUsuario bUsuario = new BUsuario();
                String result = bUsuario.update(id, nombre, apellido, email, registro, telefono, carnet, password, rol);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar usuario: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 9 (id, nombre, apellido, email, registro, telefono, carnet, password, rol), Recibidos: " + partsOfParams.length;
        }
    }

    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BUsuario bUsuario = new BUsuario();
            String result = bUsuario.delete(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar usuario: " + e.getMessage();
        }
    }

    public static String findAll() {
        try {
            BUsuario bUsuario = new BUsuario();
            List<String[]> usuarios = bUsuario.findAllUsers();

            if (usuarios == null || usuarios.isEmpty()) {
                return "No hay usuarios registrados.";
            }

            StringBuilder sb = new StringBuilder("=== USUARIOS REGISTRADOS ===\n");
            for (String[] usuario : usuarios) {
                sb.append("ID: ").append(usuario[0])
                        .append(" | Nombre: ").append(usuario[1]).append(" ").append(usuario[2])
                        .append(" | Email: ").append(usuario[3])
                        .append(" | Registro: ").append(usuario[4])
                        .append(" | Rol: ").append(usuario[8]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar usuarios: " + e.getMessage();
        }
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BUsuario bUsuario = new BUsuario();
            String[] usuario = bUsuario.findOneById(id);

            if (usuario == null) {
                return "No se encontró usuario con ID: " + id;
            }

            return "Usuario encontrado:\n" +
                    "ID: " + usuario[0] + "\n" +
                    "Nombre: " + usuario[1] + " " + usuario[2] + "\n" +
                    "Email: " + usuario[3] + "\n" +
                    "Registro: " + usuario[4] + "\n" +
                    "Teléfono: " + usuario[5] + "\n" +
                    "Carnet: " + usuario[6] + "\n" +
                    "Rol: " + usuario[8];
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar usuario: " + e.getMessage();
        }
    }

    public static String findByRegister(String params) {
        try {
            String registro = params.trim();
            BUsuario bUsuario = new BUsuario();
            String[] usuario = bUsuario.findOneByRegister(registro);

            if (usuario == null) {
                return "No se encontró usuario con registro: " + registro;
            }

            return "Usuario encontrado:\n" +
                    "ID: " + usuario[0] + "\n" +
                    "Nombre: " + usuario[1] + " " + usuario[2] + "\n" +
                    "Email: " + usuario[3] + "\n" +
                    "Registro: " + usuario[4] + "\n" +
                    "Teléfono: " + usuario[5] + "\n" +
                    "Carnet: " + usuario[6] + "\n" +
                    "Rol: " + usuario[8];
        } catch (Exception e) {
            return "Error al buscar usuario por registro: " + e.getMessage();
        }
    }
}