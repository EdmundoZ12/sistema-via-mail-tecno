package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAsistencia {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DAsistencia() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU5 - MÉTODOS PARA GESTIÓN DE ASISTENCIA

    /**
     * Registrar asistencia de un estudiante
     */
    public String save(int inscripcionId, Date fecha, String estado) {
        String query = "INSERT INTO ASISTENCIA (inscripcion_id, fecha, estado) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ps.setDate(2, fecha);
            ps.setString(3, estado);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Asistencia registrada exitosamente" : "Error: No se pudo registrar la asistencia";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar registro de asistencia
     */
    public String update(int id, int inscripcionId, Date fecha, String estado) {
        String query = "UPDATE ASISTENCIA SET inscripcion_id = ?, fecha = ?, estado = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ps.setDate(2, fecha);
            ps.setString(3, estado);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Asistencia actualizada exitosamente" : "Error: No se pudo actualizar la asistencia";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Eliminar registro de asistencia
     */
    public String delete(int id) {
        String query = "DELETE FROM ASISTENCIA WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Asistencia eliminada exitosamente" : "Error: No se pudo eliminar la asistencia";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todas las asistencias
     */
    public List<String[]> findAll() {
        String query = "SELECT a.id, a.inscripcion_id, a.fecha, a.estado, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "ORDER BY a.fecha DESC, c.nombre, p.apellido";

        List<String[]> asistencias = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = String.valueOf(rs.getInt("id"));
                asistencia[1] = String.valueOf(rs.getInt("inscripcion_id"));
                asistencia[2] = rs.getDate("fecha").toString();
                asistencia[3] = rs.getString("estado");
                asistencia[4] = rs.getString("participante_nombre");
                asistencia[5] = rs.getString("carnet");
                asistencia[6] = rs.getString("curso_nombre");
                asistencias.add(asistencia);

                System.out.println("Asistencia: " + asistencia[4] + " - " + asistencia[6] + " - " + asistencia[2] + " - " + asistencia[3]);
            }

            rs.close();
            ps.close();
            System.out.println("Total asistencias: " + asistencias.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return asistencias;
    }

    /**
     * Buscar asistencia por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT a.id, a.inscripcion_id, a.fecha, a.estado, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE a.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = String.valueOf(rs.getInt("id"));
                asistencia[1] = String.valueOf(rs.getInt("inscripcion_id"));
                asistencia[2] = rs.getDate("fecha").toString();
                asistencia[3] = rs.getString("estado");
                asistencia[4] = rs.getString("participante_nombre");
                asistencia[5] = rs.getString("carnet");
                asistencia[6] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return asistencia;
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
     * Buscar asistencias por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT a.id, a.inscripcion_id, a.fecha, a.estado, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE c.id = ? " +
                "ORDER BY a.fecha DESC, p.apellido";

        List<String[]> asistencias = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = String.valueOf(rs.getInt("id"));
                asistencia[1] = String.valueOf(rs.getInt("inscripcion_id"));
                asistencia[2] = rs.getDate("fecha").toString();
                asistencia[3] = rs.getString("estado");
                asistencia[4] = rs.getString("participante_nombre");
                asistencia[5] = rs.getString("carnet");
                asistencia[6] = rs.getString("curso_nombre");
                asistencias.add(asistencia);
            }

            rs.close();
            ps.close();
            System.out.println("Asistencias para curso " + cursoId + ": " + asistencias.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return asistencias;
    }

    /**
     * Buscar asistencias por inscripción ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByInscripcion(int inscripcionId) {
        String query = "SELECT a.id, a.inscripcion_id, a.fecha, a.estado, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE a.inscripcion_id = ? " +
                "ORDER BY a.fecha";

        List<String[]> asistencias = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = String.valueOf(rs.getInt("id"));
                asistencia[1] = String.valueOf(rs.getInt("inscripcion_id"));
                asistencia[2] = rs.getDate("fecha").toString();
                asistencia[3] = rs.getString("estado");
                asistencia[4] = rs.getString("participante_nombre");
                asistencia[5] = rs.getString("carnet");
                asistencia[6] = rs.getString("curso_nombre");
                asistencias.add(asistencia);
            }

            rs.close();
            ps.close();
            System.out.println("Asistencias para inscripción " + inscripcionId + ": " + asistencias.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return asistencias;
    }

    /**
     * Buscar asistencias por fecha
     */
    public List<String[]> findByFecha(Date fecha) {
        String query = "SELECT a.id, a.inscripcion_id, a.fecha, a.estado, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE a.fecha = ? " +
                "ORDER BY c.nombre, p.apellido";

        List<String[]> asistencias = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fecha);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = String.valueOf(rs.getInt("id"));
                asistencia[1] = String.valueOf(rs.getInt("inscripcion_id"));
                asistencia[2] = rs.getDate("fecha").toString();
                asistencia[3] = rs.getString("estado");
                asistencia[4] = rs.getString("participante_nombre");
                asistencia[5] = rs.getString("carnet");
                asistencia[6] = rs.getString("curso_nombre");
                asistencias.add(asistencia);
            }

            rs.close();
            ps.close();
            System.out.println("Asistencias para fecha " + fecha + ": " + asistencias.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return asistencias;
    }

    /**
     * Calcular porcentaje de asistencia ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getPorcentaje(int inscripcionId) {
        String query = "SELECT " +
                "COUNT(*) AS total_clases, " +
                "COUNT(CASE WHEN estado = 'PRESENTE' THEN 1 END) AS presentes, " +
                "COUNT(CASE WHEN estado = 'AUSENTE' THEN 1 END) AS ausentes, " +
                "COUNT(CASE WHEN estado = 'JUSTIFICADO' THEN 1 END) AS justificados, " +
                "ROUND(COUNT(CASE WHEN estado = 'PRESENTE' THEN 1 END) * 100.0 / COUNT(*), 2) AS porcentaje_asistencia " +
                "FROM ASISTENCIA " +
                "WHERE inscripcion_id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] porcentaje = new String[5];
                porcentaje[0] = String.valueOf(rs.getInt("total_clases"));
                porcentaje[1] = String.valueOf(rs.getInt("presentes"));
                porcentaje[2] = String.valueOf(rs.getInt("ausentes"));
                porcentaje[3] = String.valueOf(rs.getInt("justificados"));
                porcentaje[4] = String.valueOf(rs.getDouble("porcentaje_asistencia"));

                System.out.println("Porcentaje de asistencia para inscripción " + inscripcionId + ": " + porcentaje[4] + "%");

                rs.close();
                ps.close();
                return porcentaje;
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
     * Obtener reporte completo de asistencia por curso ⭐ FUNCIONALIDAD ESPECIAL
     */
    public List<String[]> getReporteCurso(int cursoId) {
        String query = "SELECT " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, " +
                "p.carnet, " +
                "COUNT(*) AS total_clases, " +
                "COUNT(CASE WHEN a.estado = 'PRESENTE' THEN 1 END) AS presentes, " +
                "COUNT(CASE WHEN a.estado = 'AUSENTE' THEN 1 END) AS ausentes, " +
                "COUNT(CASE WHEN a.estado = 'JUSTIFICADO' THEN 1 END) AS justificados, " +
                "ROUND(COUNT(CASE WHEN a.estado = 'PRESENTE' THEN 1 END) * 100.0 / COUNT(*), 2) AS porcentaje_asistencia " +
                "FROM ASISTENCIA a " +
                "JOIN INSCRIPCION i ON a.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE c.id = ? " +
                "GROUP BY i.id, p.nombre, p.apellido, p.carnet " +
                "ORDER BY porcentaje_asistencia DESC, p.apellido";

        List<String[]> reporte = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[7];
                registro[0] = rs.getString("participante_nombre");
                registro[1] = rs.getString("carnet");
                registro[2] = String.valueOf(rs.getInt("total_clases"));
                registro[3] = String.valueOf(rs.getInt("presentes"));
                registro[4] = String.valueOf(rs.getInt("ausentes"));
                registro[5] = String.valueOf(rs.getInt("justificados"));
                registro[6] = String.valueOf(rs.getDouble("porcentaje_asistencia"));
                reporte.add(registro);

                System.out.println("Reporte: " + registro[0] + " - " + registro[6] + "% asistencia");
            }

            rs.close();
            ps.close();
            System.out.println("Reporte de asistencia para curso " + cursoId + ": " + reporte.size() + " estudiantes");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return reporte;
    }
}