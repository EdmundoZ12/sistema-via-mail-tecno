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
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU1 - MÉTODOS PARA GESTIÓN DE TIPOS DE PARTICIPANTE

    /**
     * Crear nuevo tipo de participante
     */
    public String save(String codigo, String descripcion) {
        String query = "INSERT INTO TIPO_PARTICIPANTE (codigo, descripcion, activo) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, codigo);
            ps.setString(2, descripcion);
            ps.setBoolean(3, true);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tipo de participante creado exitosamente" : "Error: No se pudo crear el tipo de participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar tipo de participante existente
     */
    public String update(int id, String codigo, String descripcion) {
        String query = "UPDATE TIPO_PARTICIPANTE SET codigo = ?, descripcion = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, codigo);
            ps.setString(2, descripcion);
            ps.setInt(3, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tipo de participante actualizado exitosamente" : "Error: No se pudo actualizar el tipo de participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar tipo de participante (soft delete)
     */
    public String delete(int id) {
        String query = "UPDATE TIPO_PARTICIPANTE SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tipo de participante desactivado exitosamente" : "Error: No se pudo desactivar el tipo de participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Reactivar tipo de participante
     */
    public String reactivate(int id) {
        String query = "UPDATE TIPO_PARTICIPANTE SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tipo de participante reactivado exitosamente" : "Error: No se pudo reactivar el tipo de participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los tipos de participante activos
     */
    public List<String[]> findAllTipos() {
        String query = "SELECT id, codigo, descripcion, activo FROM TIPO_PARTICIPANTE WHERE activo = true ORDER BY codigo";
        List<String[]> tipos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] tipo = new String[4];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("codigo");
                tipo[2] = rs.getString("descripcion");
                tipo[3] = String.valueOf(rs.getBoolean("activo"));
                tipos.add(tipo);

                System.out.println("Tipo: " + tipo[1] + " - " + tipo[2]);
            }

            rs.close();
            ps.close();
            System.out.println("Total tipos activos: " + tipos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tipos;
    }

    /**
     * Buscar tipo por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT id, codigo, descripcion, activo FROM TIPO_PARTICIPANTE WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] tipo = new String[4];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("codigo");
                tipo[2] = rs.getString("descripcion");
                tipo[3] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Tipo encontrado: " + tipo[1] + " - " + tipo[2]);
                rs.close();
                ps.close();
                return tipo;
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
     * Buscar tipo por código
     */
    public String[] findByCodigo(String codigo) {
        String query = "SELECT id, codigo, descripcion, activo FROM TIPO_PARTICIPANTE WHERE codigo = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] tipo = new String[4];
                tipo[0] = String.valueOf(rs.getInt("id"));
                tipo[1] = rs.getString("codigo");
                tipo[2] = rs.getString("descripcion");
                tipo[3] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Tipo encontrado por código: " + tipo[1] + " - " + tipo[2]);
                rs.close();
                ps.close();
                return tipo;
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