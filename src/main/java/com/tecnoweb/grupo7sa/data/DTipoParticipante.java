package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTipoParticipante {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DTipoParticipante() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(), configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    public String save(String nombre, String codigo, String descripcion) {
        String query = "INSERT INTO tipo_participante (nombre, codigo, descripcion, activo) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setString(3, descripcion);
            ps.setBoolean(4, true);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Tipo de participante creado exitosamente";
            } else {
                return "Error: No se pudo crear el tipo de participante";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String update(int id, String nombre, String codigo, String descripcion) {
        String query = "UPDATE tipo_participante SET nombre = ?, codigo = ?, descripcion = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setString(3, descripcion);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Tipo de participante actualizado exitosamente";
            } else {
                return "Error: No se pudo actualizar el tipo de participante";
            }

        } catch (SQLException e) {
            return "Error: en el Sistema " + e.getMessage();
        }
    }

    public String delete(int id) {
        String query = "UPDATE tipo_participante SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Tipo de participante eliminado exitosamente";
            } else {
                return "Error: No se pudo eliminar el tipo de participante";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<String[]> findAllTipos() {
        String query = "SELECT id, nombre, codigo, descripcion, activo FROM tipo_participante WHERE activo = true";
        List<String[]> tipos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] tipo = new String[5];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("nombre");
                tipo[2] = rs.getString("codigo");
                tipo[3] = rs.getString("descripcion");
                tipo[4] = String.valueOf(rs.getBoolean("activo"));
                tipos.add(tipo);

                // Mostrar cada tipo en consola
                System.out.println("Tipo: ID=" + tipo[0] +
                        ", Nombre=" + tipo[1] +
                        ", Codigo=" + tipo[2] +
                        ", Descripcion=" + tipo[3] +
                        ", Activo=" + tipo[4]);
            }

            rs.close();
            ps.close();

            if (tipos.isEmpty()) {
                System.out.println("No se encontraron tipos de participante activos en el sistema");
            } else {
                System.out.println("Total tipos encontrados: " + tipos.size());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tipos;
    }

    public String[] findOneById(int id) {
        String query = "SELECT id, nombre, codigo, descripcion, activo FROM tipo_participante WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] tipo = new String[5];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("nombre");
                tipo[2] = rs.getString("codigo");
                tipo[3] = rs.getString("descripcion");
                tipo[4] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Tipo encontrado por ID: ID=" + tipo[0] +
                        ", Nombre=" + tipo[1] +
                        ", Codigo=" + tipo[2] +
                        ", Descripcion=" + tipo[3] +
                        ", Activo=" + tipo[4]);

                rs.close();
                ps.close();

                return tipo;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró el tipo de participante con ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo por ID: " + e.getMessage());
            return null;
        }
    }

    public String[] findOneByCodigo(String codigo) {
        String query = "SELECT id, nombre, codigo, descripcion, activo FROM tipo_participante WHERE codigo = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] tipo = new String[5];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("nombre");
                tipo[2] = rs.getString("codigo");
                tipo[3] = rs.getString("descripcion");
                tipo[4] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Tipo encontrado por codigo: ID=" + tipo[0] +
                        ", Nombre=" + tipo[1] +
                        ", Codigo=" + tipo[2] +
                        ", Descripcion=" + tipo[3] +
                        ", Activo=" + tipo[4]);

                rs.close();
                ps.close();

                return tipo;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró el tipo de participante con codigo: " + codigo);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo por codigo: " + e.getMessage());
            return null;
        }
    }
}