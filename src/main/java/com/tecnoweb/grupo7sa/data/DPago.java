package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DPago {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DPago() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU6 - MÉTODOS PARA GESTIÓN DE PAGOS

    /**
     * Registrar nuevo pago
     */
    public String save(int preinscripcionId, double monto, String recibo) {
        String query = "INSERT INTO PAGO (preinscripcion_id, monto, recibo, fecha_pago) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, preinscripcionId);
            ps.setDouble(2, monto);
            ps.setString(3, recibo);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Pago registrado exitosamente" : "Error: No se pudo registrar el pago";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar pago existente
     */
    public String update(int id, int preinscripcionId, double monto, String recibo) {
        String query = "UPDATE PAGO SET preinscripcion_id = ?, monto = ?, recibo = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, preinscripcionId);
            ps.setDouble(2, monto);
            ps.setString(3, recibo);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Pago actualizado exitosamente" : "Error: No se pudo actualizar el pago";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Anular pago (delete)
     */
    public String delete(int id) {
        String query = "DELETE FROM PAGO WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Pago anulado exitosamente" : "Error: No se pudo anular el pago";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los pagos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findAll() {
        String query = "SELECT p.id, p.preinscripcion_id, p.fecha_pago, p.monto, p.recibo, " +
                "CONCAT(part.nombre, ' ', part.apellido) AS participante_nombre, part.carnet, " +
                "c.nombre AS curso_nombre, c.aula, " +
                "tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN PARTICIPANTE part ON pr.participante_id = part.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON part.tipo_participante_id = tp.id " +
                "ORDER BY p.fecha_pago DESC";

        List<String[]> pagos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] pago = new String[11];
                pago[0] = String.valueOf(rs.getInt("id"));
                pago[1] = String.valueOf(rs.getInt("preinscripcion_id"));
                pago[2] = rs.getTimestamp("fecha_pago").toString();
                pago[3] = String.valueOf(rs.getDouble("monto"));
                pago[4] = rs.getString("recibo");
                pago[5] = rs.getString("participante_nombre");
                pago[6] = rs.getString("carnet");
                pago[7] = rs.getString("curso_nombre");
                pago[8] = rs.getString("aula");
                pago[9] = rs.getString("tipo_codigo");
                pago[10] = rs.getString("tipo_descripcion");
                pagos.add(pago);

                System.out.println("Pago: " + pago[5] + " - " + pago[7] + " - $" + pago[3] + " - " + pago[4]);
            }

            rs.close();
            ps.close();
            System.out.println("Total pagos registrados: " + pagos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return pagos;
    }

    /**
     * Buscar pago por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT p.id, p.preinscripcion_id, p.fecha_pago, p.monto, p.recibo, " +
                "CONCAT(part.nombre, ' ', part.apellido) AS participante_nombre, part.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN PARTICIPANTE part ON pr.participante_id = part.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE p.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] pago = new String[8];
                pago[0] = String.valueOf(rs.getInt("id"));
                pago[1] = String.valueOf(rs.getInt("preinscripcion_id"));
                pago[2] = rs.getTimestamp("fecha_pago").toString();
                pago[3] = String.valueOf(rs.getDouble("monto"));
                pago[4] = rs.getString("recibo");
                pago[5] = rs.getString("participante_nombre");
                pago[6] = rs.getString("carnet");
                pago[7] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return pago;
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
     * Verificar si ya se pagó una preinscripción ⭐ ENDPOINT PRINCIPAL
     */
    public String[] findByPreinscripcion(int preinscripcionId) {
        String query = "SELECT p.id, p.preinscripcion_id, p.fecha_pago, p.monto, p.recibo, " +
                "CONCAT(part.nombre, ' ', part.apellido) AS participante_nombre, part.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN PARTICIPANTE part ON pr.participante_id = part.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE p.preinscripcion_id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, preinscripcionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] pago = new String[8];
                pago[0] = String.valueOf(rs.getInt("id"));
                pago[1] = String.valueOf(rs.getInt("preinscripcion_id"));
                pago[2] = rs.getTimestamp("fecha_pago").toString();
                pago[3] = String.valueOf(rs.getDouble("monto"));
                pago[4] = rs.getString("recibo");
                pago[5] = rs.getString("participante_nombre");
                pago[6] = rs.getString("carnet");
                pago[7] = rs.getString("curso_nombre");

                System.out.println("Pago encontrado para preinscripción " + preinscripcionId + ": $" + pago[3]);

                rs.close();
                ps.close();
                return pago;
            }

            rs.close();
            ps.close();
            System.out.println("No hay pago registrado para preinscripción " + preinscripcionId);
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Buscar pago por número de recibo
     */
    public String[] findByRecibo(String recibo) {
        String query = "SELECT p.id, p.preinscripcion_id, p.fecha_pago, p.monto, p.recibo, " +
                "CONCAT(part.nombre, ' ', part.apellido) AS participante_nombre, part.carnet, " +
                "c.nombre AS curso_nombre " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN PARTICIPANTE part ON pr.participante_id = part.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE p.recibo = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, recibo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] pago = new String[8];
                pago[0] = String.valueOf(rs.getInt("id"));
                pago[1] = String.valueOf(rs.getInt("preinscripcion_id"));
                pago[2] = rs.getTimestamp("fecha_pago").toString();
                pago[3] = String.valueOf(rs.getDouble("monto"));
                pago[4] = rs.getString("recibo");
                pago[5] = rs.getString("participante_nombre");
                pago[6] = rs.getString("carnet");
                pago[7] = rs.getString("curso_nombre");

                rs.close();
                ps.close();
                return pago;
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
     * Buscar pagos por rango de fechas ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByRangoFecha(Date fechaInicio, Date fechaFin) {
        String query = "SELECT p.id, p.preinscripcion_id, p.fecha_pago, p.monto, p.recibo, " +
                "CONCAT(part.nombre, ' ', part.apellido) AS participante_nombre, part.carnet, " +
                "c.nombre AS curso_nombre, c.aula " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN PARTICIPANTE part ON pr.participante_id = part.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "WHERE DATE(p.fecha_pago) BETWEEN ? AND ? " +
                "ORDER BY p.fecha_pago DESC";

        List<String[]> pagos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] pago = new String[9];
                pago[0] = String.valueOf(rs.getInt("id"));
                pago[1] = String.valueOf(rs.getInt("preinscripcion_id"));
                pago[2] = rs.getTimestamp("fecha_pago").toString();
                pago[3] = String.valueOf(rs.getDouble("monto"));
                pago[4] = rs.getString("recibo");
                pago[5] = rs.getString("participante_nombre");
                pago[6] = rs.getString("carnet");
                pago[7] = rs.getString("curso_nombre");
                pago[8] = rs.getString("aula");
                pagos.add(pago);
            }

            rs.close();
            ps.close();
            System.out.println("Pagos en rango " + fechaInicio + " a " + fechaFin + ": " + pagos.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return pagos;
    }

    /**
     * Obtener ingresos por mes ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getIngresosPorMes(int año, int mes) {
        String query = "SELECT " +
                "COUNT(*) AS total_pagos, " +
                "SUM(monto) AS ingresos_totales, " +
                "ROUND(AVG(monto), 2) AS pago_promedio, " +
                "MIN(monto) AS pago_minimo, " +
                "MAX(monto) AS pago_maximo " +
                "FROM PAGO " +
                "WHERE EXTRACT(YEAR FROM fecha_pago) = ? AND EXTRACT(MONTH FROM fecha_pago) = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, año);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] ingresos = new String[5];
                ingresos[0] = String.valueOf(rs.getInt("total_pagos"));
                ingresos[1] = String.valueOf(rs.getDouble("ingresos_totales"));
                ingresos[2] = String.valueOf(rs.getDouble("pago_promedio"));
                ingresos[3] = String.valueOf(rs.getDouble("pago_minimo"));
                ingresos[4] = String.valueOf(rs.getDouble("pago_maximo"));

                System.out.println("Ingresos " + mes + "/" + año + ": $" + ingresos[1] + " (" + ingresos[0] + " pagos)");

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
     * Obtener ingresos por gestión ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getIngresosPorGestion(int gestionId) {
        String query = "SELECT " +
                "COUNT(*) AS total_pagos, " +
                "SUM(p.monto) AS ingresos_totales, " +
                "ROUND(AVG(p.monto), 2) AS pago_promedio, " +
                "COUNT(DISTINCT c.id) AS cursos_con_pagos, " +
                "g.nombre AS gestion_nombre " +
                "FROM PAGO p " +
                "JOIN PREINSCRIPCION pr ON p.preinscripcion_id = pr.id " +
                "JOIN CURSO c ON pr.curso_id = c.id " +
                "JOIN GESTION g ON c.gestion_id = g.id " +
                "WHERE g.id = ? " +
                "GROUP BY g.id, g.nombre";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, gestionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] ingresos = new String[5];
                ingresos[0] = String.valueOf(rs.getInt("total_pagos"));
                ingresos[1] = String.valueOf(rs.getDouble("ingresos_totales"));
                ingresos[2] = String.valueOf(rs.getDouble("pago_promedio"));
                ingresos[3] = String.valueOf(rs.getInt("cursos_con_pagos"));
                ingresos[4] = rs.getString("gestion_nombre");

                System.out.println("Ingresos gestión '" + ingresos[4] + "': $" + ingresos[1] + " (" + ingresos[0] + " pagos)");

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
}