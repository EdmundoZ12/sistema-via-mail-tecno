package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DPreinscripcion {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DPreinscripcion() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU4 - MÉTODOS PARA GESTIÓN DE PREINSCRIPCIONES

    /**
     * Crear nueva preinscripción
     *
     * @param participanteId ID del participante
     * @param cursoId        ID del curso
     * @param observaciones  Observaciones adicionales
     * @return Mensaje de resultado
     */
    public String save(int participanteId, int cursoId, String observaciones) {
        String query = "INSERT INTO PREINSCRIPCION (participante_id, curso_id, fecha_preinscripcion, estado, observaciones) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, participanteId);
            ps.setInt(2, cursoId);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, "PENDIENTE");
            ps.setString(5, observaciones);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Preinscripción creada exitosamente" : "Error: No se pudo crear la preinscripción";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Aprobar preinscripción ⭐ FUNCIONALIDAD ESPECIAL
     * Cambia estado a APROBADA y crea inscripción automáticamente
     */
    public String approve(int preinscripcionId, String observaciones) {
        try {
            databaseConection.openConnection().setAutoCommit(false);

            // 1. Actualizar preinscripción a APROBADA
            String updateQuery = "UPDATE PREINSCRIPCION SET estado = 'APROBADA', observaciones = ? WHERE id = ?";
            PreparedStatement ps1 = databaseConection.openConnection().prepareStatement(updateQuery);
            ps1.setString(1, observaciones);
            ps1.setInt(2, preinscripcionId);
            int updateResult = ps1.executeUpdate();
            ps1.close();

            if (updateResult == 0) {
                databaseConection.openConnection().rollback();
                return "Error: No se encontró la preinscripción con ID: " + preinscripcionId;
            }

            // 2. Obtener datos de la preinscripción
            String selectQuery = "SELECT participante_id, curso_id FROM PREINSCRIPCION WHERE id = ?";
            PreparedStatement ps2 = databaseConection.openConnection().prepareStatement(selectQuery);
            ps2.setInt(1, preinscripcionId);
            ResultSet rs = ps2.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps2.close();
                databaseConection.openConnection().rollback();
                return "Error: No se pudo obtener datos de la preinscripción";
            }

            int participanteId = rs.getInt("participante_id");
            int cursoId = rs.getInt("curso_id");
            rs.close();
            ps2.close();

            // 3. Crear inscripción automáticamente
            String insertInscripcionQuery = "INSERT INTO INSCRIPCION (participante_id, curso_id, preinscripcion_id, fecha_inscripcion, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps3 = databaseConection.openConnection().prepareStatement(insertInscripcionQuery);
            ps3.setInt(1, participanteId);
            ps3.setInt(2, cursoId);
            ps3.setInt(3, preinscripcionId);
            ps3.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps3.setString(5, "INSCRITO");
            ps3.setString(6, "Inscripción automática por aprobación de preinscripción");
            int inscripcionResult = ps3.executeUpdate();
            ps3.close();

            if (inscripcionResult == 0) {
                databaseConection.openConnection().rollback();
                return "Error: No se pudo crear la inscripción";
            }

            // 4. Actualizar cupos ocupados del curso
            String updateCuposQuery = "UPDATE CURSO SET cupos_ocupados = cupos_ocupados + 1 WHERE id = ?";
            PreparedStatement ps4 = databaseConection.openConnection().prepareStatement(updateCuposQuery);
            ps4.setInt(1, cursoId);
            int cuposResult = ps4.executeUpdate();
            ps4.close();

            if (cuposResult == 0) {
                databaseConection.openConnection().rollback();
                return "Error: No se pudo actualizar los cupos del curso";
            }

            databaseConection.openConnection().commit();
            return "Preinscripción aprobada e inscripción creada exitosamente";

        } catch (SQLException e) {
            try {
                databaseConection.openConnection().rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error en rollback: " + rollbackEx.getMessage());
            }
            return "Error al aprobar preinscripción: " + e.getMessage();
        } finally {
            try {
                databaseConection.openConnection().setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restaurando autocommit: " + e.getMessage());
            }
        }
    }

    /**
     * Rechazar preinscripción
     */
    public String reject(int preinscripcionId, String observaciones) {
        String query = "UPDATE PREINSCRIPCION SET estado = 'RECHAZADA', observaciones = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, observaciones);
            ps.setInt(2, preinscripcionId);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Preinscripción rechazada exitosamente" : "Error: No se pudo rechazar la preinscripción";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todas las preinscripciones pendientes con precios ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findAll() {
        String query = "SELECT pr.id, pr.participante_id, pr.curso_id, pr.fecha_preinscripcion, pr.estado, pr.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, p.email, " +
                "c.nombre AS curso_nombre, c.aula, c.cupos_totales, c.cupos_ocupados, " +
                "tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, " +
                "pc.precio " +
                "FROM PREINSCRIPCION pr " +
                "JOIN PARTICIPANTE p ON pr.participante_id = p.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "LEFT JOIN PRECIO_CURSO pc ON c.id = pc.curso_id AND tp.id = pc.tipo_participante_id " +
                "WHERE pr.estado = 'PENDIENTE' " +
                "ORDER BY pr.fecha_preinscripcion";

        List<String[]> preinscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] preinscripcion = new String[16];
                preinscripcion[0] = String.valueOf(rs.getInt("id"));
                preinscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                preinscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                preinscripcion[3] = rs.getTimestamp("fecha_preinscripcion").toString();
                preinscripcion[4] = rs.getString("estado");
                preinscripcion[5] = rs.getString("observaciones");
                preinscripcion[6] = rs.getString("participante_nombre");
                preinscripcion[7] = rs.getString("carnet");
                preinscripcion[8] = rs.getString("email");
                preinscripcion[9] = rs.getString("curso_nombre");
                preinscripcion[10] = rs.getString("aula");
                preinscripcion[11] = String.valueOf(rs.getInt("cupos_totales"));
                preinscripcion[12] = String.valueOf(rs.getInt("cupos_ocupados"));
                preinscripcion[13] = rs.getString("tipo_codigo");
                preinscripcion[14] = rs.getString("tipo_descripcion");
                preinscripcion[15] = rs.getDouble("precio") != 0 ? String.valueOf(rs.getDouble("precio")) : "0.00";
                preinscripciones.add(preinscripcion);

                System.out.println("Preinscripción pendiente: " + preinscripcion[6] + " - " + preinscripcion[9] + " - $" + preinscripcion[15]);
            }

            rs.close();
            ps.close();
            System.out.println("Total preinscripciones pendientes: " + preinscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return preinscripciones;
    }

    /**
     * Buscar preinscripción por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT pr.id, pr.participante_id, pr.curso_id, pr.fecha_preinscripcion, pr.estado, pr.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM PREINSCRIPCION pr " +
                "JOIN PARTICIPANTE p ON pr.participante_id = p.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE pr.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] preinscripcion = new String[9];
                preinscripcion[0] = String.valueOf(rs.getInt("id"));
                preinscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                preinscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                preinscripcion[3] = rs.getTimestamp("fecha_preinscripcion").toString();
                preinscripcion[4] = rs.getString("estado");
                preinscripcion[5] = rs.getString("observaciones");
                preinscripcion[6] = rs.getString("participante_nombre");
                preinscripcion[7] = rs.getString("carnet");
                preinscripcion[8] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return preinscripcion;
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
     * Buscar preinscripciones por curso
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT pr.id, pr.participante_id, pr.curso_id, pr.fecha_preinscripcion, pr.estado, pr.observaciones, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, p.email " +
                "FROM PREINSCRIPCION pr " +
                "JOIN PARTICIPANTE p ON pr.participante_id = p.id " +
                "WHERE pr.curso_id = ? ORDER BY pr.fecha_preinscripcion";

        List<String[]> preinscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] preinscripcion = new String[9];
                preinscripcion[0] = String.valueOf(rs.getInt("id"));
                preinscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                preinscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                preinscripcion[3] = rs.getTimestamp("fecha_preinscripcion").toString();
                preinscripcion[4] = rs.getString("estado");
                preinscripcion[5] = rs.getString("observaciones");
                preinscripcion[6] = rs.getString("participante_nombre");
                preinscripcion[7] = rs.getString("carnet");
                preinscripcion[8] = rs.getString("email");
                preinscripciones.add(preinscripcion);
            }

            rs.close();
            ps.close();
            System.out.println("Preinscripciones para curso " + cursoId + ": " + preinscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return preinscripciones;
    }

    /**
     * Buscar preinscripciones por participante
     */
    public List<String[]> findByParticipante(int participanteId) {
        String query = "SELECT pr.id, pr.participante_id, pr.curso_id, pr.fecha_preinscripcion, pr.estado, pr.observaciones, " +
                "c.nombre AS curso_nombre, c.aula " +
                "FROM PREINSCRIPCION pr " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE pr.participante_id = ? ORDER BY pr.fecha_preinscripcion DESC";

        List<String[]> preinscripciones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, participanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] preinscripcion = new String[8];
                preinscripcion[0] = String.valueOf(rs.getInt("id"));
                preinscripcion[1] = String.valueOf(rs.getInt("participante_id"));
                preinscripcion[2] = String.valueOf(rs.getInt("curso_id"));
                preinscripcion[3] = rs.getTimestamp("fecha_preinscripcion").toString();
                preinscripcion[4] = rs.getString("estado");
                preinscripcion[5] = rs.getString("observaciones");
                preinscripcion[6] = rs.getString("curso_nombre");
                preinscripcion[7] = rs.getString("aula");
                preinscripciones.add(preinscripcion);
            }

            rs.close();
            ps.close();
            System.out.println("Preinscripciones del participante " + participanteId + ": " + preinscripciones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return preinscripciones;
    }
}