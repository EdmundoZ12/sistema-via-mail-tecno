package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DInscripcion {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DInscripcion() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU4 - MÉTODOS PARA GESTIÓN DE INSCRIPCIONES

    /**
     * Listar todas las inscripciones activas
     */
    public List<String[]> findAll() {
        String query = "SELECT i.id, i.participante_id, i.curso_id, i.preinscripcion_id, i.fecha_inscripcion, " +
                "i.nota_final, i.estado, i.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, p.email, " +
                "c.nombre AS curso_nombre, c.aula " +
                "FROM INSCRIPCION i " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE i.estado != 'RETIRADO' " +
                "ORDER BY i.fecha_inscripcion DESC";

        List<String[]> inscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] inscripcion = new String[13];
                inscripcion[0] = String.valueOf(rs.getInt("id"));
                inscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                inscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                inscripcion[3] = String.valueOf(rs.getInt("preinscripcion_id"));
                inscripcion[4] = rs.getTimestamp("fecha_inscripcion").toString();
                inscripcion[5] = rs.getDouble("nota_final") != 0 ? String.valueOf(rs.getDouble("nota_final")) : "";
                inscripcion[6] = rs.getString("estado");
                inscripcion[7] = rs.getString("observaciones");
                inscripcion[8] = rs.getString("participante_nombre");
                inscripcion[9] = rs.getString("carnet");
                inscripcion[10] = rs.getString("email");
                inscripcion[11] = rs.getString("curso_nombre");
                inscripcion[12] = rs.getString("aula");
                inscripciones.add(inscripcion);

                System.out.println("Inscripción: " + inscripcion[8] + " - " + inscripcion[11] + " - " + inscripcion[6]);
            }

            rs.close();
            ps.close();
            System.out.println("Total inscripciones activas: " + inscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return inscripciones;
    }

    /**
     * Buscar inscripción por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT i.id, i.participante_id, i.curso_id, i.preinscripcion_id, i.fecha_inscripcion, " +
                "i.nota_final, i.estado, i.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM INSCRIPCION i " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE i.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] inscripcion = new String[11];
                inscripcion[0] = String.valueOf(rs.getInt("id"));
                inscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                inscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                inscripcion[3] = String.valueOf(rs.getInt("preinscripcion_id"));
                inscripcion[4] = rs.getTimestamp("fecha_inscripcion").toString();
                inscripcion[5] = rs.getDouble("nota_final") != 0 ? String.valueOf(rs.getDouble("nota_final")) : "";
                inscripcion[6] = rs.getString("estado");
                inscripcion[7] = rs.getString("observaciones");
                inscripcion[8] = rs.getString("participante_nombre");
                inscripcion[9] = rs.getString("carnet");
                inscripcion[10] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return inscripcion;
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
     * Lista de inscritos por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT i.id, i.participante_id, i.curso_id, i.preinscripcion_id, i.fecha_inscripcion, " +
                "i.nota_final, i.estado, i.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, p.email, " +
                "tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM INSCRIPCION i " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "WHERE i.curso_id = ? AND i.estado != 'RETIRADO' " +
                "ORDER BY p.apellido, p.nombre";

        List<String[]> inscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] inscripcion = new String[13];
                inscripcion[0] = String.valueOf(rs.getInt("id"));
                inscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                inscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                inscripcion[3] = String.valueOf(rs.getInt("preinscripcion_id"));
                inscripcion[4] = rs.getTimestamp("fecha_inscripcion").toString();
                inscripcion[5] = rs.getDouble("nota_final") != 0 ? String.valueOf(rs.getDouble("nota_final")) : "";
                inscripcion[6] = rs.getString("estado");
                inscripcion[7] = rs.getString("observaciones");
                inscripcion[8] = rs.getString("participante_nombre");
                inscripcion[9] = rs.getString("carnet");
                inscripcion[10] = rs.getString("email");
                inscripcion[11] = rs.getString("tipo_codigo");
                inscripcion[12] = rs.getString("tipo_descripcion");
                inscripciones.add(inscripcion);
            }

            rs.close();
            ps.close();
            System.out.println("Inscripciones en curso " + cursoId + ": " + inscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return inscripciones;
    }

    /**
     * Historial de un participante
     */
    public List<String[]> findByParticipante(int participanteId) {
        String query = "SELECT i.id, i.participante_id, i.curso_id, i.preinscripcion_id, i.fecha_inscripcion, " +
                "i.nota_final, i.estado, i.observaciones, " +
                "c.nombre AS curso_nombre, c.aula, c.duracion_horas, " +
                "g.nombre AS gestion_nombre " +
                "FROM INSCRIPCION i " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE i.participante_id = ? " +
                "ORDER BY i.fecha_inscripcion DESC";

        List<String[]> inscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, participanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] inscripcion = new String[12];
                inscripcion[0] = String.valueOf(rs.getInt("id"));
                inscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                inscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                inscripcion[3] = String.valueOf(rs.getInt("preinscripcion_id"));
                inscripcion[4] = rs.getTimestamp("fecha_inscripcion").toString();
                inscripcion[5] = rs.getDouble("nota_final") != 0 ? String.valueOf(rs.getDouble("nota_final")) : "";
                inscripcion[6] = rs.getString("estado");
                inscripcion[7] = rs.getString("observaciones");
                inscripcion[8] = rs.getString("curso_nombre");
                inscripcion[9] = rs.getString("aula");
                inscripcion[10] = String.valueOf(rs.getInt("duracion_horas"));
                inscripcion[11] = rs.getString("gestion_nombre");
                inscripciones.add(inscripcion);
            }

            rs.close();
            ps.close();
            System.out.println("Historial del participante " + participanteId + ": " + inscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return inscripciones;
    }

    /**
     * Calificar estudiante ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updateNota(int inscripcionId, double notaFinal, String estado) {
        String query = "UPDATE INSCRIPCION SET nota_final = ?, estado = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDouble(1, notaFinal);
            ps.setString(2, estado);
            ps.setInt(3, inscripcionId);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Nota actualizada exitosamente" : "Error: No se pudo actualizar la nota";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Retirar participante ⭐ FUNCIONALIDAD ESPECIAL
     * Retira estudiante y libera cupo
     */
    public String withdraw(int inscripcionId, String observaciones) {
        try {
            databaseConection.openConnection().setAutoCommit(false);

            // 1. Obtener curso_id de la inscripción
            String selectQuery = "SELECT curso_id FROM INSCRIPCION WHERE id = ?";
            PreparedStatement ps1 = databaseConection.openConnection().prepareStatement(selectQuery);
            ps1.setInt(1, inscripcionId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps1.close();
                databaseConection.openConnection().rollback();
                return "Error: No se encontró la inscripción con ID: " + inscripcionId;
            }

            int cursoId = rs.getInt("curso_id");
            rs.close();
            ps1.close();

            // 2. Actualizar inscripción a RETIRADO
            String updateInscripcionQuery = "UPDATE INSCRIPCION SET estado = 'RETIRADO', observaciones = ? WHERE id = ?";
            PreparedStatement ps2 = databaseConection.openConnection().prepareStatement(updateInscripcionQuery);
            ps2.setString(1, observaciones);
            ps2.setInt(2, inscripcionId);
            int updateResult = ps2.executeUpdate();
            ps2.close();

            if (updateResult == 0) {
                databaseConection.openConnection().rollback();
                return "Error: No se pudo actualizar la inscripción";
            }

            // 3. Liberar cupo del curso
            String updateCuposQuery = "UPDATE CURSO SET cupos_ocupados = cupos_ocupados - 1 WHERE id = ? AND cupos_ocupados > 0";
            PreparedStatement ps3 = databaseConection.openConnection().prepareStatement(updateCuposQuery);
            ps3.setInt(1, cursoId);
            int cuposResult = ps3.executeUpdate();
            ps3.close();

            if (cuposResult == 0) {
                databaseConection.openConnection().rollback();
                return "Error: No se pudo liberar el cupo del curso";
            }

            databaseConection.openConnection().commit();
            return "Participante retirado exitosamente y cupo liberado";

        } catch (SQLException e) {
            try {
                databaseConection.openConnection().rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
            }
            return "Error al retirar participante: " + e.getMessage();
        } finally {
            try {
                databaseConection.openConnection().setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restaurando autocommit: " + e.getMessage());
            }
        }
    }

    /**
     * Obtener estadísticas por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> getEstadisticas() {
        String query = "SELECT " +
                "c.id AS curso_id, c.nombre AS curso_nombre, c.aula, " +
                "c.cupos_totales, c.cupos_ocupados, " +
                "COUNT(i.id) AS total_inscripciones, " +
                "COUNT(CASE WHEN i.estado = 'INSCRITO' THEN 1 END) AS activos, " +
                "COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) AS aprobados, " +
                "COUNT(CASE WHEN i.estado = 'REPROBADO' THEN 1 END) AS reprobados, " +
                "COUNT(CASE WHEN i.estado = 'RETIRADO' THEN 1 END) AS retirados, " +
                "ROUND(AVG(CASE WHEN i.nota_final > 0 THEN i.nota_final END), 2) AS promedio_notas, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, " +
                "g.nombre AS gestion_nombre " +
                "FROM CURSO c " +
                "LEFT JOIN INSCRIPCION i ON c.id = i.curso_id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.activo = true " +
                "GROUP BY c.id, c.nombre, c.aula, c.cupos_totales, c.cupos_ocupados, u.nombre, u.apellido, g.nombre " +
                "ORDER BY c.nombre";

        List<String[]> estadisticas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] estadistica = new String[13];
                estadistica[0] = String.valueOf(rs.getInt("curso_id"));
                estadistica[1] = rs.getString("curso_nombre");
                estadistica[2] = rs.getString("aula");
                estadistica[3] = String.valueOf(rs.getInt("cupos_totales"));
                estadistica[4] = String.valueOf(rs.getInt("cupos_ocupados"));
                estadistica[5] = String.valueOf(rs.getInt("total_inscripciones"));
                estadistica[6] = String.valueOf(rs.getInt("activos"));
                estadistica[7] = String.valueOf(rs.getInt("aprobados"));
                estadistica[8] = String.valueOf(rs.getInt("reprobados"));
                estadistica[9] = String.valueOf(rs.getInt("retirados"));
                estadistica[10] = rs.getDouble("promedio_notas") != 0 ? String.valueOf(rs.getDouble("promedio_notas")) : "0.00";
                estadistica[11] = rs.getString("tutor_nombre");
                estadistica[12] = rs.getString("gestion_nombre");
                estadisticas.add(estadistica);

                System.out.println("Estadística curso: " + estadistica[1] + " - Inscritos: " + estadistica[5] + " - Promedio: " + estadistica[10]);
            }

            rs.close();
            ps.close();
            System.out.println("Total cursos con estadísticas: " + estadisticas.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return estadisticas;
    }

    /**
     * Verificar si existe una inscripción específica
     */
    public boolean existsInscripcion(int participanteId, int cursoId) {
        String query = "SELECT COUNT(*) FROM INSCRIPCION WHERE participante_id = ? AND curso_id = ? AND estado != 'RETIRADO'";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, participanteId);
            ps.setInt(2, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                return count > 0;
            }

            rs.close();
            ps.close();
            return false;

        } catch (SQLException e) {
            System.out.println("Error verificando inscripción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Contar inscripciones activas por curso
     */
    public int countInscripcionesByCurso(int cursoId) {
        String query = "SELECT COUNT(*) FROM INSCRIPCION WHERE curso_id = ? AND estado != 'RETIRADO'";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                return count;
            }

            rs.close();
            ps.close();
            return 0;

        } catch (SQLException e) {
            System.out.println("Error contando inscripciones: " + e.getMessage());
            return 0;
        }
    }
}