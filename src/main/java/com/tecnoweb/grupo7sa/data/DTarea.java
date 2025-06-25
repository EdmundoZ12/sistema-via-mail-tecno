package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTarea {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DTarea() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU5 - MÉTODOS PARA GESTIÓN DE TAREAS

    /**
     * Crear nueva tarea para un curso
     */
    public String save(int cursoId, String titulo, String descripcion, Date fechaAsignacion) {
        String query = "INSERT INTO TAREA (curso_id, titulo, descripcion, fecha_asignacion) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ps.setString(2, titulo);
            ps.setString(3, descripcion);
            ps.setDate(4, fechaAsignacion);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tarea creada exitosamente" : "Error: No se pudo crear la tarea";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar tarea existente
     */
    public String update(int id, int cursoId, String titulo, String descripcion, Date fechaAsignacion) {
        String query = "UPDATE TAREA SET curso_id = ?, titulo = ?, descripcion = ?, fecha_asignacion = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ps.setString(2, titulo);
            ps.setString(3, descripcion);
            ps.setDate(4, fechaAsignacion);
            ps.setInt(5, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tarea actualizada exitosamente" : "Error: No se pudo actualizar la tarea";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Eliminar tarea
     */
    public String delete(int id) {
        String query = "DELETE FROM TAREA WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Tarea eliminada exitosamente" : "Error: No se pudo eliminar la tarea";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todas las tareas
     */
    public List<String[]> findAll() {
        String query = "SELECT t.id, t.curso_id, t.titulo, t.descripcion, t.fecha_asignacion, " +
                "c.nombre AS curso_nombre, CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM TAREA t " +
                "JOIN CURSO c ON t.curso_id = c.id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "ORDER BY t.fecha_asignacion DESC";

        List<String[]> tareas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] tarea = new String[7];
                tarea[0] = String.valueOf(rs.getInt("id"));
                tarea[1] = String.valueOf(rs.getInt("curso_id"));
                tarea[2] = rs.getString("titulo");
                tarea[3] = rs.getString("descripcion");
                tarea[4] = rs.getDate("fecha_asignacion").toString();
                tarea[5] = rs.getString("curso_nombre");
                tarea[6] = rs.getString("tutor_nombre");
                tareas.add(tarea);

                System.out.println("Tarea: " + tarea[2] + " - " + tarea[5] + " - " + tarea[4]);
            }

            rs.close();
            ps.close();
            System.out.println("Total tareas: " + tareas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tareas;
    }

    /**
     * Buscar tarea por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT t.id, t.curso_id, t.titulo, t.descripcion, t.fecha_asignacion, " +
                "c.nombre AS curso_nombre, CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM TAREA t " +
                "JOIN CURSO c ON t.curso_id = c.id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "WHERE t.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] tarea = new String[7];
                tarea[0] = String.valueOf(rs.getInt("id"));
                tarea[1] = String.valueOf(rs.getInt("curso_id"));
                tarea[2] = rs.getString("titulo");
                tarea[3] = rs.getString("descripcion");
                tarea[4] = rs.getDate("fecha_asignacion").toString();
                tarea[5] = rs.getString("curso_nombre");
                tarea[6] = rs.getString("tutor_nombre");

                rs.close();
                ps.close();
                return tarea;
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
     * Buscar tareas por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT t.id, t.curso_id, t.titulo, t.descripcion, t.fecha_asignacion, " +
                "c.nombre AS curso_nombre, CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM TAREA t " +
                "JOIN CURSO c ON t.curso_id = c.id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "WHERE t.curso_id = ? " +
                "ORDER BY t.fecha_asignacion DESC";

        List<String[]> tareas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] tarea = new String[7];
                tarea[0] = String.valueOf(rs.getInt("id"));
                tarea[1] = String.valueOf(rs.getInt("curso_id"));
                tarea[2] = rs.getString("titulo");
                tarea[3] = rs.getString("descripcion");
                tarea[4] = rs.getDate("fecha_asignacion").toString();
                tarea[5] = rs.getString("curso_nombre");
                tarea[6] = rs.getString("tutor_nombre");
                tareas.add(tarea);
            }

            rs.close();
            ps.close();
            System.out.println("Tareas para curso " + cursoId + ": " + tareas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tareas;
    }

    /**
     * Buscar tareas por fecha
     */
    public List<String[]> findByFecha(Date fecha) {
        String query = "SELECT t.id, t.curso_id, t.titulo, t.descripcion, t.fecha_asignacion, " +
                "c.nombre AS curso_nombre, CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM TAREA t " +
                "JOIN CURSO c ON t.curso_id = c.id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "WHERE t.fecha_asignacion = ? " +
                "ORDER BY c.nombre";

        List<String[]> tareas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fecha);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] tarea = new String[7];
                tarea[0] = String.valueOf(rs.getInt("id"));
                tarea[1] = String.valueOf(rs.getInt("curso_id"));
                tarea[2] = rs.getString("titulo");
                tarea[3] = rs.getString("descripcion");
                tarea[4] = rs.getDate("fecha_asignacion").toString();
                tarea[5] = rs.getString("curso_nombre");
                tarea[6] = rs.getString("tutor_nombre");
                tareas.add(tarea);
            }

            rs.close();
            ps.close();
            System.out.println("Tareas para fecha " + fecha + ": " + tareas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tareas;
    }
}