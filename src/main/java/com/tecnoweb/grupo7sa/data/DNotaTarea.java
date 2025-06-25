package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DNotaTarea {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DNotaTarea() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU5 - MÉTODOS PARA GESTIÓN DE NOTAS DE TAREAS

    /**
     * Registrar nota de tarea
     */
    public String save(int tareaId, int inscripcionId, double nota) {
        String query = "INSERT INTO NOTA_TAREA (tarea_id, inscripcion_id, nota) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tareaId);
            ps.setInt(2, inscripcionId);
            ps.setDouble(3, nota);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Nota de tarea registrada exitosamente" : "Error: No se pudo registrar la nota";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar nota de tarea
     */
    public String update(int id, int tareaId, int inscripcionId, double nota) {
        String query = "UPDATE NOTA_TAREA SET tarea_id = ?, inscripcion_id = ?, nota = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tareaId);
            ps.setInt(2, inscripcionId);
            ps.setDouble(3, nota);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Nota de tarea actualizada exitosamente" : "Error: No se pudo actualizar la nota";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Eliminar nota de tarea
     */
    public String delete(int id) {
        String query = "DELETE FROM NOTA_TAREA WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Nota de tarea eliminada exitosamente" : "Error: No se pudo eliminar la nota";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todas las notas de tareas
     */
    public List<String[]> findAll() {
        String query = "SELECT nt.id, nt.tarea_id, nt.inscripcion_id, nt.nota, " +
                "t.titulo AS tarea_titulo, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN TAREA t ON nt.tarea_id = t.id " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "ORDER BY c.nombre, t.titulo, p.apellido";

        List<String[]> notas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] nota = new String[8];
                nota[0] = String.valueOf(rs.getInt("id"));
                nota[1] = String.valueOf(rs.getInt("tarea_id"));
                nota[2] = String.valueOf(rs.getInt("inscripcion_id"));
                nota[3] = String.valueOf(rs.getDouble("nota"));
                nota[4] = rs.getString("tarea_titulo");
                nota[5] = rs.getString("participante_nombre");
                nota[6] = rs.getString("carnet");
                nota[7] = rs.getString("curso_nombre");
                notas.add(nota);

                System.out.println("Nota: " + nota[5] + " - " + nota[4] + " - " + nota[3]);
            }

            rs.close();
            ps.close();
            System.out.println("Total notas de tareas: " + notas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return notas;
    }

    /**
     * Buscar nota por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT nt.id, nt.tarea_id, nt.inscripcion_id, nt.nota, " +
                "t.titulo AS tarea_titulo, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN TAREA t ON nt.tarea_id = t.id " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE nt.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] nota = new String[8];
                nota[0] = String.valueOf(rs.getInt("id"));
                nota[1] = String.valueOf(rs.getInt("tarea_id"));
                nota[2] = String.valueOf(rs.getInt("inscripcion_id"));
                nota[3] = String.valueOf(rs.getDouble("nota"));
                nota[4] = rs.getString("tarea_titulo");
                nota[5] = rs.getString("participante_nombre");
                nota[6] = rs.getString("carnet");
                nota[7] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return nota;
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
     * Buscar notas por tarea
     */
    public List<String[]> findByTarea(int tareaId) {
        String query = "SELECT nt.id, nt.tarea_id, nt.inscripcion_id, nt.nota, " +
                "t.titulo AS tarea_titulo, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN TAREA t ON nt.tarea_id = t.id " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE nt.tarea_id = ? " +
                "ORDER BY nt.nota DESC, p.apellido";

        List<String[]> notas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tareaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] nota = new String[8];
                nota[0] = String.valueOf(rs.getInt("id"));
                nota[1] = String.valueOf(rs.getInt("tarea_id"));
                nota[2] = String.valueOf(rs.getInt("inscripcion_id"));
                nota[3] = String.valueOf(rs.getDouble("nota"));
                nota[4] = rs.getString("tarea_titulo");
                nota[5] = rs.getString("participante_nombre");
                nota[6] = rs.getString("carnet");
                nota[7] = rs.getString("curso_nombre");
                notas.add(nota);
            }

            rs.close();
            ps.close();
            System.out.println("Notas para tarea " + tareaId + ": " + notas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return notas;
    }

    /**
     * Buscar notas por inscripción ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByInscripcion(int inscripcionId) {
        String query = "SELECT nt.id, nt.tarea_id, nt.inscripcion_id, nt.nota, " +
                "t.titulo AS tarea_titulo, t.fecha_asignacion, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN TAREA t ON nt.tarea_id = t.id " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE nt.inscripcion_id = ? " +
                "ORDER BY t.fecha_asignacion";

        List<String[]> notas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] nota = new String[9];
                nota[0] = String.valueOf(rs.getInt("id"));
                nota[1] = String.valueOf(rs.getInt("tarea_id"));
                nota[2] = String.valueOf(rs.getInt("inscripcion_id"));
                nota[3] = String.valueOf(rs.getDouble("nota"));
                nota[4] = rs.getString("tarea_titulo");
                nota[5] = rs.getDate("fecha_asignacion").toString();
                nota[6] = rs.getString("participante_nombre");
                nota[7] = rs.getString("carnet");
                nota[8] = rs.getString("curso_nombre");
                notas.add(nota);
            }

            rs.close();
            ps.close();
            System.out.println("Notas para inscripción " + inscripcionId + ": " + notas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return notas;
    }

    /**
     * Calcular promedio de notas ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] calcularPromedio(int inscripcionId) {
        String query = "SELECT " +
                "COUNT(*) AS total_tareas, " +
                "ROUND(AVG(nota), 2) AS promedio, " +
                "MIN(nota) AS nota_minima, " +
                "MAX(nota) AS nota_maxima, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE nt.inscripcion_id = ? " +
                "GROUP BY i.id, p.nombre, p.apellido, c.nombre";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] promedio = new String[6];
                promedio[0] = String.valueOf(rs.getInt("total_tareas"));
                promedio[1] = String.valueOf(rs.getDouble("promedio"));
                promedio[2] = String.valueOf(rs.getDouble("nota_minima"));
                promedio[3] = String.valueOf(rs.getDouble("nota_maxima"));
                promedio[4] = rs.getString("participante_nombre");
                promedio[5] = rs.getString("curso_nombre");

                System.out.println("Promedio para " + promedio[4] + ": " + promedio[1] + " (" + promedio[0] + " tareas)");

                rs.close();
                ps.close();
                return promedio;
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
     * Obtener estadísticas por tarea ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getEstadisticasTarea(int tareaId) {
        String query = "SELECT " +
                "COUNT(*) AS total_estudiantes, " +
                "ROUND(AVG(nota), 2) AS promedio_tarea, " +
                "MIN(nota) AS nota_minima, " +
                "MAX(nota) AS nota_maxima, " +
                "COUNT(CASE WHEN nota >= 51 THEN 1 END) AS aprobados, " +
                "COUNT(CASE WHEN nota < 51 THEN 1 END) AS reprobados, " +
                "t.titulo AS tarea_titulo, " +
                "c.nombre AS curso_nombre " +
                "FROM NOTA_TAREA nt " +
                "JOIN TAREA t ON nt.tarea_id = t.id " +
                "JOIN INSCRIPCION i ON nt.inscripcion_id = i.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE nt.tarea_id = ? " +
                "GROUP BY t.id, t.titulo, c.nombre";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tareaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] estadisticas = new String[8];
                estadisticas[0] = String.valueOf(rs.getInt("total_estudiantes"));
                estadisticas[1] = String.valueOf(rs.getDouble("promedio_tarea"));
                estadisticas[2] = String.valueOf(rs.getDouble("nota_minima"));
                estadisticas[3] = String.valueOf(rs.getDouble("nota_maxima"));
                estadisticas[4] = String.valueOf(rs.getInt("aprobados"));
                estadisticas[5] = String.valueOf(rs.getInt("reprobados"));
                estadisticas[6] = rs.getString("tarea_titulo");
                estadisticas[7] = rs.getString("curso_nombre");

                System.out.println("Estadísticas tarea '" + estadisticas[6] + "': Promedio " + estadisticas[1] + ", " + estadisticas[4] + " aprobados de " + estadisticas[0]);

                rs.close();
                ps.close();
                return estadisticas;
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