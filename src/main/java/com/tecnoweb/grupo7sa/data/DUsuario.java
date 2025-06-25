package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DUsuario {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DUsuario() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU1 - MÉTODOS PARA GESTIÓN DE USUARIOS

    /**
     * Crear nuevo usuario
     */
    public String save(String nombre, String apellido, String carnet, String email, String telefono,
                       String password, String rol, String registro) {
        String query = "INSERT INTO USUARIO (nombre, apellido, carnet, email, telefono, password, rol, registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, carnet);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.setString(6, password);
            ps.setString(7, rol);
            ps.setString(8, registro);
            ps.setBoolean(9, true);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Usuario creado exitosamente" : "Error: No se pudo crear el usuario";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar usuario existente
     */
    public String update(int id, String nombre, String apellido, String carnet, String email,
                         String telefono, String password, String rol, String registro) {
        String query = "UPDATE USUARIO SET nombre = ?, apellido = ?, carnet = ?, email = ?, telefono = ?, password = ?, rol = ?, registro = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, carnet);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.setString(6, password);
            ps.setString(7, rol);
            ps.setString(8, registro);
            ps.setInt(9, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Usuario actualizado exitosamente" : "Error: No se pudo actualizar el usuario";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar usuario (soft delete)
     */
    public String delete(int id) {
        String query = "UPDATE USUARIO SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Usuario desactivado exitosamente" : "Error: No se pudo desactivar el usuario";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Reactivar usuario
     */
    public String reactivate(int id) {
        String query = "UPDATE USUARIO SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Usuario reactivado exitosamente" : "Error: No se pudo reactivar el usuario";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los usuarios activos
     */
    public List<String[]> findAllUsers() {
        String query = "SELECT id, nombre, apellido, carnet, email, telefono, rol, registro, activo FROM USUARIO WHERE activo = true ORDER BY rol, apellido";
        List<String[]> usuarios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] usuario = new String[9];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("carnet");
                usuario[4] = rs.getString("email");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("rol");
                usuario[7] = rs.getString("registro");
                usuario[8] = String.valueOf(rs.getBoolean("activo"));
                usuarios.add(usuario);

                System.out.println("Usuario: ID=" + usuario[0] + ", Nombre=" + usuario[1] + " " + usuario[2] +
                        ", Rol=" + usuario[6] + ", Email=" + usuario[4]);
            }

            rs.close();
            ps.close();
            System.out.println("Total usuarios activos: " + usuarios.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Buscar usuario por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT id, nombre, apellido, carnet, email, telefono, rol, registro, activo FROM USUARIO WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] usuario = new String[9];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("carnet");
                usuario[4] = rs.getString("email");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("rol");
                usuario[7] = rs.getString("registro");
                usuario[8] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Usuario encontrado: " + usuario[1] + " " + usuario[2] + " - " + usuario[6]);
                rs.close();
                ps.close();
                return usuario;
            }

            rs.close();
            ps.close();
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Listar usuarios por rol específico
     */
    public List<String[]> findByRole(String rol) {
        String query = "SELECT id, nombre, apellido, carnet, email, telefono, rol, registro, activo FROM USUARIO WHERE rol = ? AND activo = true ORDER BY apellido";
        List<String[]> usuarios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] usuario = new String[9];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("carnet");
                usuario[4] = rs.getString("email");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("rol");
                usuario[7] = rs.getString("registro");
                usuario[8] = String.valueOf(rs.getBoolean("activo"));
                usuarios.add(usuario);
            }

            rs.close();
            ps.close();
            System.out.println("Usuarios con rol '" + rol + "': " + usuarios.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Buscar usuario por carnet
     */
    public String[] findByCarnet(String carnet) {
        String query = "SELECT id, nombre, apellido, carnet, email, telefono, rol, registro, activo FROM USUARIO WHERE carnet = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] usuario = new String[9];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("carnet");
                usuario[4] = rs.getString("email");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("rol");
                usuario[7] = rs.getString("registro");
                usuario[8] = String.valueOf(rs.getBoolean("activo"));

                rs.close();
                ps.close();
                return usuario;
            }

            rs.close();
            ps.close();
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Buscar usuario por email
     */
    public String[] findByEmail(String email) {
        String query = "SELECT id, nombre, apellido, carnet, email, telefono, rol, registro, activo FROM USUARIO WHERE email = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] usuario = new String[9];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("carnet");
                usuario[4] = rs.getString("email");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("rol");
                usuario[7] = rs.getString("registro");
                usuario[8] = String.valueOf(rs.getBoolean("activo"));

                rs.close();
                ps.close();
                return usuario;
            }

            rs.close();
            ps.close();
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}