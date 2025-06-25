package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DParticipante {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DParticipante() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU1 - MÉTODOS PARA GESTIÓN DE PARTICIPANTES

    /**
     * Crear nuevo participante
     */
    public String save(String carnet, String nombre, String apellido, String email, String telefono,
                       String universidad, int tipoParticipanteId, String registro) {
        String query = "INSERT INTO PARTICIPANTE (carnet, nombre, apellido, email, telefono, universidad, tipo_participante_id, activo, registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.setString(6, universidad);
            ps.setInt(7, tipoParticipanteId);
            ps.setBoolean(8, true);
            ps.setString(9, registro); // Puede ser NULL para estudiantes de colegio

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Participante creado exitosamente" : "Error: No se pudo crear el participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar participante existente
     */
    public String update(int id, String carnet, String nombre, String apellido, String email, String telefono,
                         String universidad, int tipoParticipanteId, String registro) {
        String query = "UPDATE PARTICIPANTE SET carnet = ?, nombre = ?, apellido = ?, email = ?, telefono = ?, universidad = ?, tipo_participante_id = ?, registro = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.setString(6, universidad);
            ps.setInt(7, tipoParticipanteId);
            ps.setString(8, registro);
            ps.setInt(9, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Participante actualizado exitosamente" : "Error: No se pudo actualizar el participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar participante (soft delete)
     */
    public String delete(int id) {
        String query = "UPDATE PARTICIPANTE SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Participante desactivado exitosamente" : "Error: No se pudo desactivar el participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Reactivar participante
     */
    public String reactivate(int id) {
        String query = "UPDATE PARTICIPANTE SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Participante reactivado exitosamente" : "Error: No se pudo reactivar el participante";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los participantes activos
     */
    public List<String[]> findAllParticipantes() {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.activo = true ORDER BY p.apellido, p.nombre";
        List<String[]> participantes = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));
                participantes.add(participante);

                System.out.println("Participante: " + participante[2] + " " + participante[3] +
                        " - Carnet: " + participante[1] + " - Tipo: " + participante[8]);
            }

            rs.close();
            ps.close();
            System.out.println("Total participantes activos: " + participantes.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return participantes;
    }

    /**
     * Buscar participante por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Participante encontrado: " + participante[2] + " " + participante[3]);
                rs.close();
                ps.close();
                return participante;
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
     * Buscar participante por carnet
     */
    public String[] findByCarnet(String carnet) {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.carnet = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));

                rs.close();
                ps.close();
                return participante;
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
     * Buscar participante por email
     */
    public String[] findByEmail(String email) {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.email = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));

                rs.close();
                ps.close();
                return participante;
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
     * Buscar participantes por tipo
     */
    public List<String[]> findByTipo(int tipoParticipanteId) {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.tipo_participante_id = ? AND p.activo = true ORDER BY p.apellido, p.nombre";
        List<String[]> participantes = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tipoParticipanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));
                participantes.add(participante);
            }

            rs.close();
            ps.close();
            System.out.println("Participantes del tipo: " + participantes.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return participantes;
    }

    /**
     * Buscar participante por registro (para estudiantes universitarios)
     */
    public String[] findByRegistro(String registro) {
        String query = "SELECT p.id, p.carnet, p.nombre, p.apellido, p.email, p.telefono, p.universidad, " +
                "p.registro, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, p.activo " +
                "FROM PARTICIPANTE p " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE p.registro = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, registro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("carnet");
                participante[2] = rs.getString("nombre");
                participante[3] = rs.getString("apellido");
                participante[4] = rs.getString("email");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("universidad");
                participante[7] = rs.getString("registro");
                participante[8] = rs.getString("tipo_codigo");
                participante[9] = rs.getString("tipo_descripcion");
                participante[10] = String.valueOf(rs.getBoolean("activo"));

                rs.close();
                ps.close();
                return participante;
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