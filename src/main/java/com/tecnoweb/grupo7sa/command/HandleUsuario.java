package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BUsuario;

import java.util.List;

public class HandleUsuario {

    // CU1 - HANDLERS PARA GESTIÓN DE USUARIOS

    /**
     * Crear nuevo usuario
     * Parámetros: nombre, apellido, carnet, email, telefono, password, rol, registro
     */
    public static String save(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 8) {
            try {
                String nombre = partsOfParams[0].trim();
                String apellido = partsOfParams[1].trim();
                String carnet = partsOfParams[2].trim();
                String email = partsOfParams[3].trim();
                String telefono = partsOfParams[4].trim();
                String password = partsOfParams[5].trim();
                String rol = partsOfParams[6].trim();
                String registro = partsOfParams[7].trim();

                BUsuario bUsuario = new BUsuario();
                String result = bUsuario.save(nombre, apellido, carnet, email, telefono, password, rol, registro);
                return result;
            } catch (Exception e) {
                return "Error al guardar usuario: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para save. Esperados: 8 (nombre, apellido, carnet, email, telefono, password, rol, registro), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Actualizar usuario existente
     * Parámetros: id, nombre, apellido, carnet, email, telefono, password, rol, registro
     */
    public static String update(String params) {
        String[] partsOfParams = params.split(",\\s*");

        if (partsOfParams.length == 9) {
            try {
                int id = Integer.parseInt(partsOfParams[0].trim());
                String nombre = partsOfParams[1].trim();
                String apellido = partsOfParams[2].trim();
                String carnet = partsOfParams[3].trim();
                String email = partsOfParams[4].trim();
                String telefono = partsOfParams[5].trim();
                String password = partsOfParams[6].trim();
                String rol = partsOfParams[7].trim();
                String registro = partsOfParams[8].trim();

                BUsuario bUsuario = new BUsuario();
                String result = bUsuario.update(id, nombre, apellido, carnet, email, telefono, password, rol, registro);
                return result;
            } catch (NumberFormatException e) {
                return "Error: ID debe ser un número válido. " + e.getMessage();
            } catch (Exception e) {
                return "Error al actualizar usuario: " + e.getMessage();
            }
        } else {
            return "Error: Número de parámetros incorrecto para update. Esperados: 9 (id, nombre, apellido, carnet, email, telefono, password, rol, registro), Recibidos: " + partsOfParams.length;
        }
    }

    /**
     * Desactivar usuario
     * Parámetros: id
     */
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

    /**
     * Reactivar usuario
     * Parámetros: id
     */
    public static String reactivate(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BUsuario bUsuario = new BUsuario();
            String result = bUsuario.reactivate(id);
            return result;
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al reactivar usuario: " + e.getMessage();
        }
    }

    /**
     * Listar todos los usuarios activos
     */
    public static String findAll() {
        try {
            BUsuario bUsuario = new BUsuario();
            List<String[]> usuarios = bUsuario.findAllUsers();

            if (usuarios == null || usuarios.isEmpty()) {
                return "No hay usuarios registrados.";
            }

            StringBuilder sb = new StringBuilder("=== USUARIOS REGISTRADOS ===\n");
            for (String[] usuario : usuarios) {
                // Array: [0]id, [1]nombre, [2]apellido, [3]carnet, [4]email, [5]telefono, [6]rol, [7]registro, [8]activo
                sb.append("ID: ").append(usuario[0])
                        .append(" | Nombre: ").append(usuario[1]).append(" ").append(usuario[2])
                        .append(" | Carnet: ").append(usuario[3])
                        .append(" | Email: ").append(usuario[4])
                        .append(" | Rol: ").append(usuario[6])
                        .append(" | Registro: ").append(usuario[7]).append("\n");
            }
            sb.append("Total usuarios: ").append(usuarios.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al recuperar usuarios: " + e.getMessage();
        }
    }

    /**
     * Buscar usuario por ID
     * Parámetros: id
     */
    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BUsuario bUsuario = new BUsuario();
            String[] usuario = bUsuario.findOneById(id);

            if (usuario == null) {
                return "No se encontró usuario con ID: " + id;
            }

            // Array: [0]id, [1]nombre, [2]apellido, [3]carnet, [4]email, [5]telefono, [6]rol, [7]registro, [8]activo
            return "Usuario encontrado:\n" +
                    "ID: " + usuario[0] + "\n" +
                    "Nombre: " + usuario[1] + " " + usuario[2] + "\n" +
                    "Carnet: " + usuario[3] + "\n" +
                    "Email: " + usuario[4] + "\n" +
                    "Teléfono: " + usuario[5] + "\n" +
                    "Rol: " + usuario[6] + "\n" +
                    "Registro: " + usuario[7] + "\n" +
                    "Activo: " + (usuario[8].equals("true") ? "Sí" : "No");
        } catch (NumberFormatException e) {
            return "Error: El ID debe ser numérico. " + e.getMessage();
        } catch (Exception e) {
            return "Error al buscar usuario: " + e.getMessage();
        }
    }

    /**
     * Buscar usuarios por rol
     * Parámetros: rol (RESPONSABLE, ADMINISTRATIVO, TUTOR)
     */
    public static String findByRole(String params) {
        try {
            String rol = params.trim();
            BUsuario bUsuario = new BUsuario();
            List<String[]> usuarios = bUsuario.findByRole(rol);

            if (usuarios == null || usuarios.isEmpty()) {
                return "No hay usuarios con el rol: " + rol;
            }

            StringBuilder sb = new StringBuilder("=== USUARIOS CON ROL: " + rol.toUpperCase() + " ===\n");
            for (String[] usuario : usuarios) {
                sb.append("ID: ").append(usuario[0])
                        .append(" | Nombre: ").append(usuario[1]).append(" ").append(usuario[2])
                        .append(" | Carnet: ").append(usuario[3])
                        .append(" | Email: ").append(usuario[4])
                        .append(" | Registro: ").append(usuario[7]).append("\n");
            }
            sb.append("Total usuarios ").append(rol).append(": ").append(usuarios.size());
            return sb.toString();
        } catch (Exception e) {
            return "Error al buscar usuarios por rol: " + e.getMessage();
        }
    }

    /**
     * Buscar usuario por carnet
     * Parámetros: carnet
     */
    public static String findByCarnet(String params) {
        try {
            String carnet = params.trim();
            BUsuario bUsuario = new BUsuario();
            String[] usuario = bUsuario.findByCarnet(carnet);

            if (usuario == null) {
                return "No se encontró usuario con carnet: " + carnet;
            }

            return "Usuario encontrado:\n" +
                    "ID: " + usuario[0] + "\n" +
                    "Nombre: " + usuario[1] + " " + usuario[2] + "\n" +
                    "Carnet: " + usuario[3] + "\n" +
                    "Email: " + usuario[4] + "\n" +
                    "Teléfono: " + usuario[5] + "\n" +
                    "Rol: " + usuario[6] + "\n" +
                    "Registro: " + usuario[7] + "\n" +
                    "Activo: " + (usuario[8].equals("true") ? "Sí" : "No");
        } catch (Exception e) {
            return "Error al buscar usuario por carnet: " + e.getMessage();
        }
    }

    /**
     * Buscar usuario por email
     * Parámetros: email
     */
    public static String findByEmail(String params) {
        try {
            String email = params.trim();
            BUsuario bUsuario = new BUsuario();
            String[] usuario = bUsuario.findByEmail(email);

            if (usuario == null) {
                return "No se encontró usuario con email: " + email;
            }

            return "Usuario encontrado:\n" +
                    "ID: " + usuario[0] + "\n" +
                    "Nombre: " + usuario[1] + " " + usuario[2] + "\n" +
                    "Carnet: " + usuario[3] + "\n" +
                    "Email: " + usuario[4] + "\n" +
                    "Teléfono: " + usuario[5] + "\n" +
                    "Rol: " + usuario[6] + "\n" +
                    "Registro: " + usuario[7] + "\n" +
                    "Activo: " + (usuario[8].equals("true") ? "Sí" : "No");
        } catch (Exception e) {
            return "Error al buscar usuario por email: " + e.getMessage();
        }
    }
}