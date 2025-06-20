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
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(), configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    public String save(String nombre, String apellido, String email, String registro, String telefono, String carnet, String password, String rol) {
        String query = "INSERT INTO usuario (nombre, apellido, email, registro, telefono, carnet, password, rol, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, registro);
            ps.setString(5, telefono);
            ps.setString(6, carnet);
            ps.setString(7, password);
            ps.setString(8, rol);
            ps.setBoolean(9, true);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Usuario creado exitosamente";
            } else {
                return "Error: No se pudo crear el usuario";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String update(int id, String nombre, String apellido, String email, String registro, String telefono, String carnet, String password, String rol) {
        String query = "UPDATE usuario SET nombre = ?, apellido = ?, email = ?, registro = ?, telefono = ?, carnet = ?, password = ?, rol = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, registro);
            ps.setString(5, telefono);
            ps.setString(6, carnet);
            ps.setString(7, password);
            ps.setString(8, rol);
            ps.setInt(9, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Usuario actualizado exitosamente";
            } else {
                return "Error: No se pudo actualizar el usuario";
            }

        } catch (SQLException e) {
            return "Error: en el Sistema " + e.getMessage();
        }
    }

    public String delete(int id) {
        String query = "UPDATE usuario SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Usuario eliminado exitosamente";
            } else {
                return "Error: No se pudo eliminar el usuario";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<String[]> findAllUsers() {
        String query = "SELECT id, nombre, apellido, email, registro, telefono, carnet, rol FROM usuario WHERE activo = true";
        List<String[]> usuarios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] usuario = new String[8];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("email");
                usuario[4] = rs.getString("registro");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("carnet");
                usuario[7] = rs.getString("rol");
                usuarios.add(usuario);

                // Mostrar cada usuario en consola
                System.out.println("Usuario: ID=" + usuario[0] +
                        ", Nombre=" + usuario[1] +
                        ", Apellido=" + usuario[2] +
                        ", Email=" + usuario[3] +
                        ", Registro=" + usuario[4] +
                        ", Telefono=" + usuario[5] +
                        ", Carnet=" + usuario[6] +
                        ", Rol=" + usuario[7]);
            }

            rs.close();
            ps.close();

            System.out.println("Total usuarios encontrados: " + usuarios.size());

        } catch (SQLException e) {
            // En caso de error, retorna lista vacía
            System.out.println("Error: " + e.getMessage());
        }

        return usuarios;
    }

    public String[] findOneByRegistro(int id) {
        String query = "SELECT id, nombre, apellido, email, registro, telefono, carnet, rol FROM usuario WHERE id = ? AND activo = true";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] usuario = new String[8];
                usuario[0] = String.valueOf(rs.getInt("id"));
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido");
                usuario[3] = rs.getString("email");
                usuario[4] = rs.getString("registro");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("carnet");
                usuario[7] = rs.getString("rol");

                rs.close();
                ps.close();

                return usuario;
            } else {
                rs.close();
                ps.close();

                return null; // Usuario no encontrado
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}