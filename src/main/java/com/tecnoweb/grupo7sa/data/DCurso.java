package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DCurso {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DCurso() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU3 - MÉTODOS PARA GESTIÓN DE CURSOS

    /**
     * Crear nuevo curso con información completa
     *
     * @param nombre        Nombre del curso
     * @param descripcion   Descripción del curso
     * @param duracionHoras Duración en horas
     * @param nivel         Nivel del curso (Básico, Intermedio, Avanzado)
     * @param logoUrl       URL del logo del curso (opcional)
     * @param tutorId       ID del tutor asignado
     * @param gestionId     ID de la gestión académica
     * @param aula          Aula asignada
     * @param cuposTotales  Número total de cupos
     * @param fechaInicio   Fecha de inicio del curso
     * @param fechaFin      Fecha de finalización del curso
     * @return Mensaje de resultado
     */
    public String save(String nombre, String descripcion, int duracionHoras, String nivel, String logoUrl,
                       int tutorId, int gestionId, String aula, int cuposTotales, Date fechaInicio, Date fechaFin) {
        String query = "INSERT INTO CURSO (nombre, descripcion, duracion_horas, nivel, logo_url, tutor_id, gestion_id, " +
                "aula, cupos_totales, cupos_ocupados, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, duracionHoras);
            ps.setString(4, nivel);
            ps.setString(5, logoUrl);
            ps.setInt(6, tutorId);
            ps.setInt(7, gestionId);
            ps.setString(8, aula);
            ps.setInt(9, cuposTotales);
            ps.setInt(10, 0); // cupos_ocupados inicia en 0
            ps.setDate(11, fechaInicio);
            ps.setDate(12, fechaFin);
            ps.setBoolean(13, true); // activo por defecto

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Curso creado exitosamente" : "Error: No se pudo crear el curso";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar curso existente
     */
    public String update(int id, String nombre, String descripcion, int duracionHoras, String nivel, String logoUrl,
                         int tutorId, int gestionId, String aula, int cuposTotales, Date fechaInicio, Date fechaFin) {
        String query = "UPDATE CURSO SET nombre = ?, descripcion = ?, duracion_horas = ?, nivel = ?, logo_url = ?, " +
                "tutor_id = ?, gestion_id = ?, aula = ?, cupos_totales = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, duracionHoras);
            ps.setString(4, nivel);
            ps.setString(5, logoUrl);
            ps.setInt(6, tutorId);
            ps.setInt(7, gestionId);
            ps.setString(8, aula);
            ps.setInt(9, cuposTotales);
            ps.setDate(10, fechaInicio);
            ps.setDate(11, fechaFin);
            ps.setInt(12, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Curso actualizado exitosamente" : "Error: No se pudo actualizar el curso";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar curso (soft delete)
     */
    public String delete(int id) {
        String query = "UPDATE CURSO SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Curso desactivado exitosamente" : "Error: No se pudo desactivar el curso";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Eliminar curso permanentemente (hard delete) ⭐ FUNCIONALIDAD ESPECIAL
     * Elimina el curso y todos sus precios asociados de forma transaccional
     */
    public String deletePermanent(int id) {
        try {
            databaseConection.openConnection().setAutoCommit(false);

            // 1. Eliminar precios asociados
            String deletePreciosQuery = "DELETE FROM PRECIO_CURSO WHERE curso_id = ?";
            PreparedStatement ps1 = databaseConection.openConnection().prepareStatement(deletePreciosQuery);
            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            // 2. Eliminar curso
            String deleteCursoQuery = "DELETE FROM CURSO WHERE id = ?";
            PreparedStatement ps2 = databaseConection.openConnection().prepareStatement(deleteCursoQuery);
            ps2.setInt(1, id);
            int result = ps2.executeUpdate();
            ps2.close();

            if (result > 0) {
                databaseConection.openConnection().commit();
                return "Curso eliminado permanentemente junto con sus precios";
            } else {
                databaseConection.openConnection().rollback();
                return "Error: No se encontró el curso para eliminar";
            }

        } catch (SQLException e) {
            try {
                databaseConection.openConnection().rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
            }
            return "Error al eliminar curso permanentemente: " + e.getMessage();
        } finally {
            try {
                databaseConection.openConnection().setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restaurando autocommit: " + e.getMessage());
            }
        }
    }

    /**
     * Reactivar curso
     */
    public String reactivate(int id) {
        String query = "UPDATE CURSO SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Curso reactivado exitosamente" : "Error: No se pudo reactivar el curso";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los cursos con información completa ⭐ ENDPOINT PRINCIPAL
     * Incluye tutor, gestión y información de cupos
     */
    public List<String[]> findAll() {
        String query = "SELECT c.id, c.nombre, c.descripcion, c.duracion_horas, c.nivel, c.logo_url, " +
                "c.aula, c.cupos_totales, c.cupos_ocupados, c.fecha_inicio, c.fecha_fin, c.activo, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, u.email AS tutor_email, " +
                "g.nombre AS gestion_nombre, g.fecha_inicio AS gestion_inicio, g.fecha_fin AS gestion_fin " +
                "FROM CURSO c " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.activo = true ORDER BY c.fecha_inicio DESC";

        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[17];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = String.valueOf(rs.getInt("duracion_horas"));
                curso[4] = rs.getString("nivel");
                curso[5] = rs.getString("logo_url");
                curso[6] = rs.getString("aula");
                curso[7] = String.valueOf(rs.getInt("cupos_totales"));
                curso[8] = String.valueOf(rs.getInt("cupos_ocupados"));
                curso[9] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                curso[10] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                curso[11] = String.valueOf(rs.getBoolean("activo"));
                curso[12] = rs.getString("tutor_nombre");
                curso[13] = rs.getString("tutor_email");
                curso[14] = rs.getString("gestion_nombre");
                curso[15] = rs.getDate("gestion_inicio") != null ? rs.getDate("gestion_inicio").toString() : "";
                curso[16] = rs.getDate("gestion_fin") != null ? rs.getDate("gestion_fin").toString() : "";
                cursos.add(curso);

                System.out.println("Curso: " + curso[1] + " - Tutor: " + curso[12] + " - Cupos: " + curso[8] + "/" + curso[7]);
            }

            rs.close();
            ps.close();
            System.out.println("Total cursos activos: " + cursos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return cursos;
    }

    /**
     * Buscar cursos con cupos disponibles ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findWithSpots() {
        String query = "SELECT c.id, c.nombre, c.descripcion, c.duracion_horas, c.nivel, c.logo_url, " +
                "c.aula, c.cupos_totales, c.cupos_ocupados, c.fecha_inicio, c.fecha_fin, c.activo, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, u.email AS tutor_email, " +
                "g.nombre AS gestion_nombre, (c.cupos_totales - c.cupos_ocupados) AS cupos_disponibles " +
                "FROM CURSO c " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.activo = true AND c.cupos_ocupados < c.cupos_totales " +
                "ORDER BY cupos_disponibles DESC, c.fecha_inicio";

        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[17];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = String.valueOf(rs.getInt("duracion_horas"));
                curso[4] = rs.getString("nivel");
                curso[5] = rs.getString("logo_url");
                curso[6] = rs.getString("aula");
                curso[7] = String.valueOf(rs.getInt("cupos_totales"));
                curso[8] = String.valueOf(rs.getInt("cupos_ocupados"));
                curso[9] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                curso[10] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                curso[11] = String.valueOf(rs.getBoolean("activo"));
                curso[12] = rs.getString("tutor_nombre");
                curso[13] = rs.getString("tutor_email");
                curso[14] = rs.getString("gestion_nombre");
                curso[15] = String.valueOf(rs.getInt("cupos_disponibles"));
                curso[16] = ""; // Campo extra para mantener consistencia
                cursos.add(curso);

                System.out.println("Curso disponible: " + curso[1] + " - Cupos libres: " + curso[15]);
            }

            rs.close();
            ps.close();
            System.out.println("Total cursos con cupos disponibles: " + cursos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return cursos;
    }

    /**
     * Buscar curso por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT c.id, c.nombre, c.descripcion, c.duracion_horas, c.nivel, c.logo_url, " +
                "c.aula, c.cupos_totales, c.cupos_ocupados, c.fecha_inicio, c.fecha_fin, c.activo, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, u.email AS tutor_email, " +
                "g.nombre AS gestion_nombre " +
                "FROM CURSO c " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] curso = new String[15];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = String.valueOf(rs.getInt("duracion_horas"));
                curso[4] = rs.getString("nivel");
                curso[5] = rs.getString("logo_url");
                curso[6] = rs.getString("aula");
                curso[7] = String.valueOf(rs.getInt("cupos_totales"));
                curso[8] = String.valueOf(rs.getInt("cupos_ocupados"));
                curso[9] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                curso[10] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                curso[11] = String.valueOf(rs.getBoolean("activo"));
                curso[12] = rs.getString("tutor_nombre");
                curso[13] = rs.getString("tutor_email");
                curso[14] = rs.getString("gestion_nombre");

                rs.close();
                ps.close();
                return curso;
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
     * Buscar cursos por gestión académica
     */
    public List<String[]> findByGestion(int gestionId) {
        String query = "SELECT c.id, c.nombre, c.descripcion, c.duracion_horas, c.nivel, " +
                "c.aula, c.cupos_totales, c.cupos_ocupados, c.fecha_inicio, c.fecha_fin, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM CURSO c " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "WHERE c.gestion_id = ? AND c.activo = true ORDER BY c.nombre";

        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[11];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = String.valueOf(rs.getInt("duracion_horas"));
                curso[4] = rs.getString("nivel");
                curso[5] = rs.getString("aula");
                curso[6] = String.valueOf(rs.getInt("cupos_totales"));
                curso[7] = String.valueOf(rs.getInt("cupos_ocupados"));
                curso[8] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                curso[9] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                curso[10] = rs.getString("tutor_nombre");
                cursos.add(curso);
            }

            rs.close();
            ps.close();
            System.out.println("Cursos en gestión " + gestionId + ": " + cursos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return cursos;
    }

    /**
     * Buscar cursos por tutor
     */
    public List<String[]> findByTutor(int tutorId) {
        String query = "SELECT c.id, c.nombre, c.descripcion, c.duracion_horas, c.nivel, " +
                "c.aula, c.cupos_totales, c.cupos_ocupados, c.fecha_inicio, c.fecha_fin, " +
                "g.nombre AS gestion_nombre " +
                "FROM CURSO c " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.tutor_id = ? AND c.activo = true ORDER BY c.fecha_inicio DESC";

        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tutorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[11];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = String.valueOf(rs.getInt("duracion_horas"));
                curso[4] = rs.getString("nivel");
                curso[5] = rs.getString("aula");
                curso[6] = String.valueOf(rs.getInt("cupos_totales"));
                curso[7] = String.valueOf(rs.getInt("cupos_ocupados"));
                curso[8] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                curso[9] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                curso[10] = rs.getString("gestion_nombre");
                cursos.add(curso);
            }

            rs.close();
            ps.close();
            System.out.println("Cursos del tutor " + tutorId + ": " + cursos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return cursos;
    }

    /**
     * Actualizar cupos del curso ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updateCupos(int cursoId, int nuevoCupos) {
        String query = "UPDATE CURSO SET cupos_totales = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, nuevoCupos);
            ps.setInt(2, cursoId);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Cupos actualizados exitosamente" : "Error: No se pudieron actualizar los cupos";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}