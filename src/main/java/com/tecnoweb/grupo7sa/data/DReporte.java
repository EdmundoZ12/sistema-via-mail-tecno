package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DReporte {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DReporte() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU8 - MÉTODOS PARA REPORTES Y ESTADÍSTICAS

    /**
     * Reporte detallado de inscripciones por curso
     */
    public List<String[]> inscripcionesPorCurso(int cursoId) {
        String query = "SELECT " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, " +
                "p.carnet, p.email, " +
                "tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion, " +
                "i.fecha_inscripcion, i.estado, i.nota_final, " +
                "c.nombre AS curso_nombre, c.aula " +
                "FROM INSCRIPCION i " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN TIPO_PARTICIPANTE tp ON p.tipo_participante_id = tp.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE c.id = ? " +
                "ORDER BY i.fecha_inscripcion";

        List<String[]> reporte = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[10];
                registro[0] = rs.getString("participante_nombre");
                registro[1] = rs.getString("carnet");
                registro[2] = rs.getString("email");
                registro[3] = rs.getString("tipo_codigo");
                registro[4] = rs.getString("tipo_descripcion");
                registro[5] = rs.getTimestamp("fecha_inscripcion").toString();
                registro[6] = rs.getString("estado");
                registro[7] = rs.getDouble("nota_final") != 0 ? String.valueOf(rs.getDouble("nota_final")) : "";
                registro[8] = rs.getString("curso_nombre");
                registro[9] = rs.getString("aula");
                reporte.add(registro);
            }

            rs.close();
            ps.close();
            System.out.println("Reporte inscripciones curso " + cursoId + ": " + reporte.size() + " registros");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return reporte;
    }

    /**
     * Índice de aprobación por curso ⭐ ENDPOINT PRINCIPAL
     */
    public String[] aprobacionPorCurso(int cursoId) {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "c.cupos_totales, c.cupos_ocupados, " +
                "COUNT(*) AS total_inscripciones, " +
                "COUNT(CASE WHEN i.estado = 'INSCRITO' THEN 1 END) AS activos, " +
                "COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) AS aprobados, " +
                "COUNT(CASE WHEN i.estado = 'REPROBADO' THEN 1 END) AS reprobados, " +
                "COUNT(CASE WHEN i.estado = 'RETIRADO' THEN 1 END) AS retirados, " +
                "ROUND(COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) * 100.0 / " +
                "COUNT(CASE WHEN i.estado IN ('APROBADO', 'REPROBADO') THEN 1 END), 2) AS porcentaje_aprobacion, " +
                "ROUND(AVG(CASE WHEN i.nota_final > 0 THEN i.nota_final END), 2) AS promedio_notas " +
                "FROM CURSO c " +
                "LEFT JOIN INSCRIPCION i ON c.id = i.curso_id " +
                "WHERE c.id = ? " +
                "GROUP BY c.id, c.nombre, c.cupos_totales, c.cupos_ocupados";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] aprobacion = new String[10];
                aprobacion[0] = rs.getString("curso_nombre");
                aprobacion[1] = String.valueOf(rs.getInt("cupos_totales"));
                aprobacion[2] = String.valueOf(rs.getInt("cupos_ocupados"));
                aprobacion[3] = String.valueOf(rs.getInt("total_inscripciones"));
                aprobacion[4] = String.valueOf(rs.getInt("activos"));
                aprobacion[5] = String.valueOf(rs.getInt("aprobados"));
                aprobacion[6] = String.valueOf(rs.getInt("reprobados"));
                aprobacion[7] = String.valueOf(rs.getInt("retirados"));
                aprobacion[8] = String.valueOf(rs.getDouble("porcentaje_aprobacion"));
                aprobacion[9] = String.valueOf(rs.getDouble("promedio_notas"));

                System.out.println("Aprobación curso '" + aprobacion[0] + "': " + aprobacion[8] + "% aprobación");

                rs.close();
                ps.close();
                return aprobacion;
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
     * Reporte de asistencia por curso ⭐ ENDPOINT PRINCIPAL
     */
    public String[] asistenciaPorCurso(int cursoId) {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "COUNT(DISTINCT i.id) AS total_estudiantes, " +
                "COUNT(a.id) AS total_registros_asistencia, " +
                "COUNT(CASE WHEN a.estado = 'PRESENTE' THEN 1 END) AS total_presentes, " +
                "COUNT(CASE WHEN a.estado = 'AUSENTE' THEN 1 END) AS total_ausentes, " +
                "COUNT(CASE WHEN a.estado = 'JUSTIFICADO' THEN 1 END) AS total_justificados, " +
                "ROUND(COUNT(CASE WHEN a.estado = 'PRESENTE' THEN 1 END) * 100.0 / COUNT(a.id), 2) AS porcentaje_asistencia_general " +
                "FROM CURSO c " +
                "LEFT JOIN INSCRIPCION i ON c.id = i.curso_id " +
                "LEFT JOIN ASISTENCIA a ON i.id = a.inscripcion_id " +
                "WHERE c.id = ? " +
                "GROUP BY c.id, c.nombre";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] asistencia = new String[7];
                asistencia[0] = rs.getString("curso_nombre");
                asistencia[1] = String.valueOf(rs.getInt("total_estudiantes"));
                asistencia[2] = String.valueOf(rs.getInt("total_registros_asistencia"));
                asistencia[3] = String.valueOf(rs.getInt("total_presentes"));
                asistencia[4] = String.valueOf(rs.getInt("total_ausentes"));
                asistencia[5] = String.valueOf(rs.getInt("total_justificados"));
                asistencia[6] = String.valueOf(rs.getDouble("porcentaje_asistencia_general"));

                System.out.println("Asistencia curso '" + asistencia[0] + "': " + asistencia[6] + "% asistencia general");

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
     * Rendimiento académico por gestión ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> rendimientoPorGestion(int gestionId) {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "COUNT(*) AS total_inscripciones, " +
                "COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) AS aprobados, " +
                "COUNT(CASE WHEN i.estado = 'REPROBADO' THEN 1 END) AS reprobados, " +
                "ROUND(COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) * 100.0 / " +
                "COUNT(CASE WHEN i.estado IN ('APROBADO', 'REPROBADO') THEN 1 END), 2) AS porcentaje_aprobacion, " +
                "ROUND(AVG(CASE WHEN i.nota_final > 0 THEN i.nota_final END), 2) AS promedio_notas " +
                "FROM CURSO c " +
                "LEFT JOIN INSCRIPCION i ON c.id = i.curso_id " +
                "WHERE c.gestion_id = ? " +
                "GROUP BY c.id, c.nombre " +
                "ORDER BY porcentaje_aprobacion DESC";

        List<String[]> rendimiento = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[6];
                registro[0] = rs.getString("curso_nombre");
                registro[1] = String.valueOf(rs.getInt("total_inscripciones"));
                registro[2] = String.valueOf(rs.getInt("aprobados"));
                registro[3] = String.valueOf(rs.getInt("reprobados"));
                registro[4] = String.valueOf(rs.getDouble("porcentaje_aprobacion"));
                registro[5] = String.valueOf(rs.getDouble("promedio_notas"));
                rendimiento.add(registro);
            }

            rs.close();
            ps.close();
            System.out.println("Rendimiento gestión " + gestionId + ": " + rendimiento.size() + " cursos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return rendimiento;
    }

    /**
     * Distribución de participantes por tipo ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> participantesPorTipo() {
        String query = "SELECT " +
                "tp.codigo, tp.descripcion, " +
                "COUNT(DISTINCT p.id) AS total_participantes, " +
                "COUNT(i.id) AS total_inscripciones, " +
                "ROUND(COUNT(DISTINCT p.id) * 100.0 / " +
                "(SELECT COUNT(*) FROM PARTICIPANTE WHERE activo = true), 2) AS porcentaje_participantes " +
                "FROM TIPO_PARTICIPANTE tp " +
                "LEFT JOIN PARTICIPANTE p ON tp.id = p.tipo_participante_id AND p.activo = true " +
                "LEFT JOIN INSCRIPCION i ON p.id = i.participante_id " +
                "WHERE tp.activo = true " +
                "GROUP BY tp.id, tp.codigo, tp.descripcion " +
                "ORDER BY total_participantes DESC";

        List<String[]> distribucion = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[5];
                registro[0] = rs.getString("codigo");
                registro[1] = rs.getString("descripcion");
                registro[2] = String.valueOf(rs.getInt("total_participantes"));
                registro[3] = String.valueOf(rs.getInt("total_inscripciones"));
                registro[4] = String.valueOf(rs.getDouble("porcentaje_participantes"));
                distribucion.add(registro);

                System.out.println("Tipo " + registro[0] + ": " + registro[2] + " participantes (" + registro[4] + "%)");
            }

            rs.close();
            ps.close();
            System.out.println("Distribución por tipos: " + distribucion.size() + " tipos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return distribucion;
    }

    /**
     * Ingresos por gestión ⭐ ENDPOINT PRINCIPAL
     */
    public String[] ingresosPorGestion(int gestionId) {
        String query = "SELECT " +
                "g.nombre AS gestion_nombre, " +
                "COUNT(p.id) AS total_pagos, " +
                "SUM(p.monto) AS ingresos_totales, " +
                "ROUND(AVG(p.monto), 2) AS pago_promedio, " +
                "MIN(p.monto) AS pago_minimo, " +
                "MAX(p.monto) AS pago_maximo, " +
                "COUNT(DISTINCT c.id) AS cursos_con_ingresos " +
                "FROM GESTION g " +
                "LEFT JOIN CURSO c ON g.id = c.gestion_id " +
                "LEFT JOIN PREINSCRIPCION pr ON c.id = pr.curso_id " +
                "LEFT JOIN PAGO p ON pr.id = p.preinscripcion_id " +
                "WHERE g.id = ? " +
                "GROUP BY g.id, g.nombre";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] ingresos = new String[7];
                ingresos[0] = rs.getString("gestion_nombre");
                ingresos[1] = String.valueOf(rs.getInt("total_pagos"));
                ingresos[2] = String.valueOf(rs.getDouble("ingresos_totales"));
                ingresos[3] = String.valueOf(rs.getDouble("pago_promedio"));
                ingresos[4] = String.valueOf(rs.getDouble("pago_minimo"));
                ingresos[5] = String.valueOf(rs.getDouble("pago_maximo"));
                ingresos[6] = String.valueOf(rs.getInt("cursos_con_ingresos"));

                System.out.println("Ingresos gestión '" + ingresos[0] + "': $" + ingresos[2]);

                rs.close();
                ps.close();
                return ingresos;
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
     * Ingresos por curso específico
     */
    public String[] ingresosPorCurso(int cursoId) {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "COUNT(p.id) AS total_pagos, " +
                "SUM(p.monto) AS ingresos_totales, " +
                "ROUND(AVG(p.monto), 2) AS pago_promedio, " +
                "c.cupos_totales, c.cupos_ocupados " +
                "FROM CURSO c " +
                "LEFT JOIN PREINSCRIPCION pr ON c.id = pr.curso_id " +
                "LEFT JOIN PAGO p ON pr.id = p.preinscripcion_id " +
                "WHERE c.id = ? " +
                "GROUP BY c.id, c.nombre, c.cupos_totales, c.cupos_ocupados";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] ingresos = new String[6];
                ingresos[0] = rs.getString("curso_nombre");
                ingresos[1] = String.valueOf(rs.getInt("total_pagos"));
                ingresos[2] = String.valueOf(rs.getDouble("ingresos_totales"));
                ingresos[3] = String.valueOf(rs.getDouble("pago_promedio"));
                ingresos[4] = String.valueOf(rs.getInt("cupos_totales"));
                ingresos[5] = String.valueOf(rs.getInt("cupos_ocupados"));

                rs.close();
                ps.close();
                return ingresos;
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
     * Ingresos por mes
     */
    public String[] ingresosPorMes(int año, int mes) {
        String query = "SELECT " +
                "COUNT(*) AS total_pagos, " +
                "SUM(monto) AS ingresos_totales, " +
                "ROUND(AVG(monto), 2) AS pago_promedio, " +
                "COUNT(DISTINCT pr.curso_id) AS cursos_diferentes " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "WHERE EXTRACT(YEAR FROM p.fecha_pago) = ? AND EXTRACT(MONTH FROM p.fecha_pago) = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, año);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] ingresos = new String[4];
                ingresos[0] = String.valueOf(rs.getInt("total_pagos"));
                ingresos[1] = String.valueOf(rs.getDouble("ingresos_totales"));
                ingresos[2] = String.valueOf(rs.getDouble("pago_promedio"));
                ingresos[3] = String.valueOf(rs.getInt("cursos_diferentes"));

                rs.close();
                ps.close();
                return ingresos;
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
     * Preinscripciones sin pago ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> pagosVencidos() {
        String query = "SELECT " +
                "pr.id AS preinscripcion_id, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, " +
                "p.carnet, p.email, " +
                "c.nombre AS curso_nombre, " +
                "pr.fecha_preinscripcion, " +
                "pc.precio AS monto_pendiente " +
                "FROM PREINSCRIPCION pr " +
                "JOIN PARTICIPANTE p ON pr.participante_id = p.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "LEFT JOIN PAGO pg ON pr.id = pg.preinscripcion_id " +
                "LEFT JOIN PRECIO_CURSO pc ON c.id = pc.curso_id AND p.tipo_participante_id = pc.tipo_participante_id " +
                "WHERE pr.estado = 'APROBADA' AND pg.id IS NULL " +
                "ORDER BY pr.fecha_preinscripcion";

        List<String[]> vencidos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[7];
                registro[0] = String.valueOf(rs.getInt("preinscripcion_id"));
                registro[1] = rs.getString("participante_nombre");
                registro[2] = rs.getString("carnet");
                registro[3] = rs.getString("email");
                registro[4] = rs.getString("curso_nombre");
                registro[5] = rs.getTimestamp("fecha_preinscripcion").toString();
                registro[6] = String.valueOf(rs.getDouble("monto_pendiente"));
                vencidos.add(registro);
            }

            rs.close();
            ps.close();
            System.out.println("Pagos vencidos: " + vencidos.size() + " preinscripciones");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return vencidos;
    }

    /**
     * Certificados emitidos por gestión
     */
    public List<String[]> certificadosEmitidos(int gestionId) {
        String query = "SELECT " +
                "c.tipo, " +
                "COUNT(*) AS total_certificados, " +
                "COUNT(DISTINCT cur.id) AS cursos_diferentes " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "WHERE cur.gestion_id = ? " +
                "GROUP BY c.tipo " +
                "ORDER BY total_certificados DESC";

        List<String[]> certificados = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[3];
                registro[0] = rs.getString("tipo");
                registro[1] = String.valueOf(rs.getInt("total_certificados"));
                registro[2] = String.valueOf(rs.getInt("cursos_diferentes"));
                certificados.add(registro);
            }

            rs.close();
            ps.close();
            System.out.println("Certificados gestión " + gestionId + ": " + certificados.size() + " tipos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return certificados;
    }

    /**
     * Porcentaje de ocupación de cursos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> ocupacionCursos() {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "c.aula, " +
                "c.cupos_totales, " +
                "c.cupos_ocupados, " +
                "ROUND(c.cupos_ocupados * 100.0 / c.cupos_totales, 2) AS porcentaje_ocupacion, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, " +
                "g.nombre AS gestion_nombre " +
                "FROM CURSO c " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE c.activo = true " +
                "ORDER BY porcentaje_ocupacion DESC";

        List<String[]> ocupacion = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[7];
                registro[0] = rs.getString("curso_nombre");
                registro[1] = rs.getString("aula");
                registro[2] = String.valueOf(rs.getInt("cupos_totales"));
                registro[3] = String.valueOf(rs.getInt("cupos_ocupados"));
                registro[4] = String.valueOf(rs.getDouble("porcentaje_ocupacion"));
                registro[5] = rs.getString("tutor_nombre");
                registro[6] = rs.getString("gestion_nombre");
                ocupacion.add(registro);

                System.out.println("Ocupación: " + registro[0] + " - " + registro[4] + "%");
            }

            rs.close();
            ps.close();
            System.out.println("Reporte ocupación: " + ocupacion.size() + " cursos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return ocupacion;
    }

    /**
     * Ranking de tutores más activos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> tutoresMasActivos() {
        String query = "SELECT " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre, " +
                "u.email, " +
                "COUNT(c.id) AS total_cursos, " +
                "SUM(c.cupos_ocupados) AS total_estudiantes, " +
                "ROUND(AVG(c.cupos_ocupados * 100.0 / c.cupos_totales), 2) AS promedio_ocupacion " +
                "FROM USUARIO u " +
                "LEFT JOIN CURSO c ON u.id = c.tutor_id AND c.activo = true " +
                "WHERE u.rol = 'TUTOR' AND u.activo = true " +
                "GROUP BY u.id, u.nombre, u.apellido, u.email " +
                "ORDER BY total_cursos DESC, total_estudiantes DESC";

        List<String[]> tutores = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[5];
                registro[0] = rs.getString("tutor_nombre");
                registro[1] = rs.getString("email");
                registro[2] = String.valueOf(rs.getInt("total_cursos"));
                registro[3] = String.valueOf(rs.getInt("total_estudiantes"));
                registro[4] = String.valueOf(rs.getDouble("promedio_ocupacion"));
                tutores.add(registro);

                System.out.println("Tutor: " + registro[0] + " - " + registro[2] + " cursos, " + registro[3] + " estudiantes");
            }

            rs.close();
            ps.close();
            System.out.println("Ranking tutores: " + tutores.size() + " tutores");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tutores;
    }

    /**
     * Cursos más populares ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> cursosPopulares() {
        String query = "SELECT " +
                "c.nombre AS curso_nombre, " +
                "c.nivel, " +
                "c.cupos_ocupados, " +
                "c.cupos_totales, " +
                "ROUND(c.cupos_ocupados * 100.0 / c.cupos_totales, 2) AS porcentaje_ocupacion, " +
                "COUNT(pr.id) AS total_preinscripciones, " +
                "CONCAT(u.nombre, ' ', u.apellido) AS tutor_nombre " +
                "FROM CURSO c " +
                "LEFT JOIN PREINSCRIPCION pr ON c.id = pr.curso_id " +
                "JOIN USUARIO u ON c.tutor_id = u.id " +
                "WHERE c.activo = true " +
                "GROUP BY c.id, c.nombre, c.nivel, c.cupos_ocupados, c.cupos_totales, u.nombre, u.apellido " +
                "ORDER BY total_preinscripciones DESC, porcentaje_ocupacion DESC";

        List<String[]> populares = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[7];
                registro[0] = rs.getString("curso_nombre");
                registro[1] = rs.getString("nivel");
                registro[2] = String.valueOf(rs.getInt("cupos_ocupados"));
                registro[3] = String.valueOf(rs.getInt("cupos_totales"));
                registro[4] = String.valueOf(rs.getDouble("porcentaje_ocupacion"));
                registro[5] = String.valueOf(rs.getInt("total_preinscripciones"));
                registro[6] = rs.getString("tutor_nombre");
                populares.add(registro);

                System.out.println("Curso popular: " + registro[0] + " - " + registro[5] + " preinscripciones");
            }

            rs.close();
            ps.close();
            System.out.println("Ranking cursos populares: " + populares.size() + " cursos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return populares;
    }

    /**
     * Dashboard general del sistema ⭐ ENDPOINT PRINCIPAL
     * KPIs globales más importantes
     */
    public String[] dashboardGeneral() {
        String query = "SELECT " +
                "(SELECT COUNT(*) FROM PARTICIPANTE WHERE activo = true) AS total_participantes, " +
                "(SELECT COUNT(*) FROM CURSO WHERE activo = true) AS total_cursos, " +
                "(SELECT COUNT(*) FROM INSCRIPCION WHERE estado != 'RETIRADO') AS total_inscripciones, " +
                "(SELECT COUNT(*) FROM PREINSCRIPCION WHERE estado = 'PENDIENTE') AS preinscripciones_pendientes, " +
                "(SELECT ROUND(COUNT(CASE WHEN estado = 'APROBADO' THEN 1 END) * 100.0 / " +
                "COUNT(CASE WHEN estado IN ('APROBADO', 'REPROBADO') THEN 1 END), 2) " +
                "FROM INSCRIPCION) AS porcentaje_aprobacion_global, " +
                "(SELECT SUM(monto) FROM PAGO) AS ingresos_totales, " +
                "(SELECT COUNT(*) FROM CERTIFICADO) AS certificados_emitidos, " +
                "(SELECT ROUND(AVG(cupos_ocupados * 100.0 / cupos_totales), 2) " +
                "FROM CURSO WHERE activo = true) AS promedio_ocupacion_cursos, " +
                "(SELECT COUNT(*) FROM USUARIO WHERE rol = 'TUTOR' AND activo = true) AS tutores_activos, " +
                "(SELECT COUNT(*) FROM GESTION WHERE activo = true AND CURRENT_DATE BETWEEN fecha_inicio AND fecha_fin) AS gestiones_vigentes";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] dashboard = new String[10];
                dashboard[0] = String.valueOf(rs.getInt("total_participantes"));
                dashboard[1] = String.valueOf(rs.getInt("total_cursos"));
                dashboard[2] = String.valueOf(rs.getInt("total_inscripciones"));
                dashboard[3] = String.valueOf(rs.getInt("preinscripciones_pendientes"));
                dashboard[4] = String.valueOf(rs.getDouble("porcentaje_aprobacion_global"));
                dashboard[5] = String.valueOf(rs.getDouble("ingresos_totales"));
                dashboard[6] = String.valueOf(rs.getInt("certificados_emitidos"));
                dashboard[7] = String.valueOf(rs.getDouble("promedio_ocupacion_cursos"));
                dashboard[8] = String.valueOf(rs.getInt("tutores_activos"));
                dashboard[9] = String.valueOf(rs.getInt("gestiones_vigentes"));

                System.out.println("Dashboard: " + dashboard[0] + " participantes, " + dashboard[1] + " cursos, $" + dashboard[5] + " ingresos");

                rs.close();
                ps.close();
                return dashboard;
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
     * KPIs específicos por gestión
     */
    public String[] kpisGestion(int gestionId) {
        String query = "SELECT " +
                "g.nombre AS gestion_nombre, " +
                "(SELECT COUNT(*) FROM CURSO WHERE gestion_id = ? AND activo = true) AS cursos_gestion, " +
                "(SELECT COUNT(*) FROM INSCRIPCION i JOIN CURSO c ON i.curso_id = c.id WHERE c.gestion_id = ?) AS inscripciones_gestion, " +
                "(SELECT ROUND(COUNT(CASE WHEN i.estado = 'APROBADO' THEN 1 END) * 100.0 / " +
                "COUNT(CASE WHEN i.estado IN ('APROBADO', 'REPROBADO') THEN 1 END), 2) " +
                "FROM INSCRIPCION i JOIN CURSO c ON i.curso_id = c.id WHERE c.gestion_id = ?) AS aprobacion_gestion, " +
                "(SELECT SUM(p.monto) FROM PAGO p JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN CURSO c ON pr.curso_id = c.id WHERE c.gestion_id = ?) AS ingresos_gestion, " +
                "(SELECT COUNT(*) FROM CERTIFICADO cert JOIN INSCRIPCION i ON cert.inscripcion_id = i.id " +
                "JOIN CURSO c ON i.curso_id = c.id WHERE c.gestion_id = ?) AS certificados_gestion " +
                "FROM GESTION g WHERE g.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ps.setInt(2, gestionId);
            ps.setInt(3, gestionId);
            ps.setInt(4, gestionId);
            ps.setInt(5, gestionId);
            ps.setInt(6, gestionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] kpis = new String[6];
                kpis[0] = rs.getString("gestion_nombre");
                kpis[1] = String.valueOf(rs.getInt("cursos_gestion"));
                kpis[2] = String.valueOf(rs.getInt("inscripciones_gestion"));
                kpis[3] = String.valueOf(rs.getDouble("aprobacion_gestion"));
                kpis[4] = String.valueOf(rs.getDouble("ingresos_gestion"));
                kpis[5] = String.valueOf(rs.getInt("certificados_gestion"));

                System.out.println("KPIs gestión '" + kpis[0] + "': " + kpis[1] + " cursos, " + kpis[2] + " inscripciones");

                rs.close();
                ps.close();
                return kpis;
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
     * Análisis de tendencias de inscripciones ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> tendenciasInscripciones() {
        String query = "SELECT " +
                "EXTRACT(YEAR FROM i.fecha_inscripcion) AS año, " +
                "EXTRACT(MONTH FROM i.fecha_inscripcion) AS mes, " +
                "COUNT(*) AS total_inscripciones, " +
                "COUNT(DISTINCT i.curso_id) AS cursos_diferentes, " +
                "COUNT(DISTINCT i.participante_id) AS participantes_unicos " +
                "FROM INSCRIPCION i " +
                "WHERE i.fecha_inscripcion >= CURRENT_DATE - INTERVAL '12 months' " +
                "GROUP BY EXTRACT(YEAR FROM i.fecha_inscripcion), EXTRACT(MONTH FROM i.fecha_inscripcion) " +
                "ORDER BY año DESC, mes DESC";

        List<String[]> tendencias = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] registro = new String[5];
                registro[0] = String.valueOf(rs.getInt("año"));
                registro[1] = String.valueOf(rs.getInt("mes"));
                registro[2] = String.valueOf(rs.getInt("total_inscripciones"));
                registro[3] = String.valueOf(rs.getInt("cursos_diferentes"));
                registro[4] = String.valueOf(rs.getInt("participantes_unicos"));
                tendencias.add(registro);

                System.out.println("Tendencia " + registro[1] + "/" + registro[0] + ": " + registro[2] + " inscripciones");
            }

            rs.close();
            ps.close();
            System.out.println("Tendencias de inscripciones: " + tendencias.size() + " meses");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return tendencias;
    }
}