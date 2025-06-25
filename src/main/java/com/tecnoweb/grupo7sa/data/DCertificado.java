package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DCertificado {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DCertificado() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU7 - MÉTODOS PARA GESTIÓN DE CERTIFICADOS

    /**
     * Crear certificado manual
     */
    public String save(int inscripcionId, String tipo, String codigoVerificacion, String urlPdf) {
        String query = "INSERT INTO CERTIFICADO (inscripcion_id, tipo, codigo_verificacion, url_pdf, fecha_emision) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ps.setString(2, tipo);
            ps.setString(3, codigoVerificacion);
            ps.setString(4, urlPdf);
            ps.setDate(5, new Date(System.currentTimeMillis()));

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Certificado creado exitosamente" : "Error: No se pudo crear el certificado";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar certificado
     */
    public String update(int id, int inscripcionId, String tipo, String codigoVerificacion, String urlPdf) {
        String query = "UPDATE CERTIFICADO SET inscripcion_id = ?, tipo = ?, codigo_verificacion = ?, url_pdf = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, inscripcionId);
            ps.setString(2, tipo);
            ps.setString(3, codigoVerificacion);
            ps.setString(4, urlPdf);
            ps.setInt(5, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Certificado actualizado exitosamente" : "Error: No se pudo actualizar el certificado";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Eliminar certificado
     */
    public String delete(int id) {
        String query = "DELETE FROM CERTIFICADO WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Certificado eliminado exitosamente" : "Error: No se pudo eliminar el certificado";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Generar certificado automáticamente ⭐ FUNCIONALIDAD ESPECIAL
     * Evalúa el estado de la inscripción y genera el certificado apropiado
     */
    public String generar(int inscripcionId) {
        // Primero verificar el estado de la inscripción
        String queryInscripcion = "SELECT i.estado, i.nota_final, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, " +
                "c.nombre AS curso_nombre " +
                "FROM INSCRIPCION i " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO c ON i.curso_id = c.id " +
                "WHERE i.id = ?";

        try {
            PreparedStatement ps1 = databaseConection.openConnection().prepareStatement(queryInscripcion);
            ps1.setInt(1, inscripcionId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                rs1.close();
                ps1.close();
                return "Error: No se encontró la inscripción con ID: " + inscripcionId;
            }

            String estado = rs1.getString("estado");
            double notaFinal = rs1.getDouble("nota_final");
            String participanteNombre = rs1.getString("participante_nombre");
            String cursoNombre = rs1.getString("curso_nombre");

            rs1.close();
            ps1.close();

            // Verificar si ya existe un certificado
            String queryExiste = "SELECT id FROM CERTIFICADO WHERE inscripcion_id = ?";
            PreparedStatement ps2 = databaseConection.openConnection().prepareStatement(queryExiste);
            ps2.setInt(1, inscripcionId);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                rs2.close();
                ps2.close();
                return "Error: Ya existe un certificado para esta inscripción";
            }
            rs2.close();
            ps2.close();

            // Determinar tipo de certificado según estado y nota
            String tipoCertificado;
            String codigoVerificacion = generarCodigoVerificacion();
            String urlPdf = "/certificados/" + codigoVerificacion + ".pdf";

            switch (estado) {
                case "APROBADO":
                    if (notaFinal >= 90) {
                        tipoCertificado = "MENCION_HONOR";
                    } else {
                        tipoCertificado = "APROBACION";
                    }
                    break;
                case "REPROBADO":
                case "INSCRITO":
                    tipoCertificado = "PARTICIPACION";
                    break;
                default:
                    return "Error: El estado '" + estado + "' no permite generar certificado";
            }

            // Crear el certificado
            String queryCertificado = "INSERT INTO CERTIFICADO (inscripcion_id, tipo, codigo_verificacion, url_pdf, fecha_emision) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps3 = databaseConection.openConnection().prepareStatement(queryCertificado);
            ps3.setInt(1, inscripcionId);
            ps3.setString(2, tipoCertificado);
            ps3.setString(3, codigoVerificacion);
            ps3.setString(4, urlPdf);
            ps3.setDate(5, new Date(System.currentTimeMillis()));

            int result = ps3.executeUpdate();
            ps3.close();

            if (result > 0) {
                return "Certificado de " + tipoCertificado + " generado exitosamente para " + participanteNombre +
                        " - Código: " + codigoVerificacion;
            } else {
                return "Error: No se pudo generar el certificado";
            }

        } catch (SQLException e) {
            return "Error al generar certificado: " + e.getMessage();
        }
    }

    /**
     * Listar todos los certificados
     */
    public List<String[]> findAll() {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, c.url_pdf, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, i.nota_final, i.estado " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "ORDER BY c.fecha_emision DESC";

        List<String[]> certificados = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("url_pdf");
                certificado[6] = rs.getString("participante_nombre");
                certificado[7] = rs.getString("carnet");
                certificado[8] = rs.getString("curso_nombre");
                certificado[9] = String.valueOf(rs.getDouble("nota_final"));
                certificado[10] = rs.getString("estado");
                certificados.add(certificado);

                System.out.println("Certificado: " + certificado[6] + " - " + certificado[2] + " - " + certificado[8]);
            }

            rs.close();
            ps.close();
            System.out.println("Total certificados: " + certificados.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return certificados;
    }

    /**
     * Buscar certificado por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, c.url_pdf, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, i.nota_final, i.estado " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "WHERE c.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("url_pdf");
                certificado[6] = rs.getString("participante_nombre");
                certificado[7] = rs.getString("carnet");
                certificado[8] = rs.getString("curso_nombre");
                certificado[9] = String.valueOf(rs.getDouble("nota_final"));
                certificado[10] = rs.getString("estado");

                rs.close();
                ps.close();
                return certificado;
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
     * Buscar certificados por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, c.url_pdf, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, i.nota_final, i.estado " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "WHERE cur.id = ? " +
                "ORDER BY c.tipo, p.apellido";

        List<String[]> certificados = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("url_pdf");
                certificado[6] = rs.getString("participante_nombre");
                certificado[7] = rs.getString("carnet");
                certificado[8] = rs.getString("curso_nombre");
                certificado[9] = String.valueOf(rs.getDouble("nota_final"));
                certificado[10] = rs.getString("estado");
                certificados.add(certificado);
            }

            rs.close();
            ps.close();
            System.out.println("Certificados para curso " + cursoId + ": " + certificados.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return certificados;
    }

    /**
     * Buscar certificados por participante ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByParticipante(int participanteId) {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, c.url_pdf, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, i.nota_final, i.estado " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "WHERE p.id = ? " +
                "ORDER BY c.fecha_emision DESC";

        List<String[]> certificados = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, participanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("url_pdf");
                certificado[6] = rs.getString("participante_nombre");
                certificado[7] = rs.getString("carnet");
                certificado[8] = rs.getString("curso_nombre");
                certificado[9] = String.valueOf(rs.getDouble("nota_final"));
                certificado[10] = rs.getString("estado");
                certificados.add(certificado);
            }

            rs.close();
            ps.close();
            System.out.println("Certificados del participante " + participanteId + ": " + certificados.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return certificados;
    }

    /**
     * Buscar certificados por tipo
     */
    public List<String[]> findByTipo(String tipo) {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, c.url_pdf, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, i.nota_final, i.estado " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "WHERE c.tipo = ? " +
                "ORDER BY c.fecha_emision DESC";

        List<String[]> certificados = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("url_pdf");
                certificado[6] = rs.getString("participante_nombre");
                certificado[7] = rs.getString("carnet");
                certificado[8] = rs.getString("curso_nombre");
                certificado[9] = String.valueOf(rs.getDouble("nota_final"));
                certificado[10] = rs.getString("estado");
                certificados.add(certificado);
            }

            rs.close();
            ps.close();
            System.out.println("Certificados tipo '" + tipo + "': " + certificados.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return certificados;
    }

    /**
     * Verificar certificado por código ⭐ FUNCIONALIDAD ESPECIAL
     * Para validación pública
     */
    public String[] verificar(String codigoVerificacion) {
        String query = "SELECT c.id, c.inscripcion_id, c.tipo, c.codigo_verificacion, c.fecha_emision, " +
                "CONCAT(p.nombre, ' ', p.apellido) AS participante_nombre, p.carnet, " +
                "cur.nombre AS curso_nombre, cur.duracion_horas, " +
                "g.nombre AS gestion_nombre, i.nota_final " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN PARTICIPANTE p ON i.participante_id = p.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "JOIN GESTION g ON cur.gestion_id = g.id " +
                "WHERE c.codigo_verificacion = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, codigoVerificacion);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] certificado = new String[11];
                certificado[0] = String.valueOf(rs.getInt("id"));
                certificado[1] = String.valueOf(rs.getInt("inscripcion_id"));
                certificado[2] = rs.getString("tipo");
                certificado[3] = rs.getString("codigo_verificacion");
                certificado[4] = rs.getDate("fecha_emision").toString();
                certificado[5] = rs.getString("participante_nombre");
                certificado[6] = rs.getString("carnet");
                certificado[7] = rs.getString("curso_nombre");
                certificado[8] = String.valueOf(rs.getInt("duracion_horas"));
                certificado[9] = rs.getString("gestion_nombre");
                certificado[10] = String.valueOf(rs.getDouble("nota_final"));

                System.out.println("Certificado verificado: " + certificado[5] + " - " + certificado[7] + " - " + certificado[2]);

                rs.close();
                ps.close();
                return certificado;
            }

            rs.close();
            ps.close();
            System.out.println("Código de verificación no válido: " + codigoVerificacion);
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtener estadísticas de certificados ⭐ FUNCIONALIDAD ESPECIAL
     */
    public List<String[]> getEstadisticas() {
        String query = "SELECT " +
                "c.tipo, " +
                "COUNT(*) AS total_certificados, " +
                "COUNT(DISTINCT cur.id) AS cursos_diferentes, " +
                "ROUND(AVG(i.nota_final), 2) AS promedio_notas " +
                "FROM CERTIFICADO c " +
                "JOIN INSCRIPCION i ON c.inscripcion_id = i.id " +
                "JOIN CURSO cur ON i.curso_id = cur.id " +
                "GROUP BY c.tipo " +
                "ORDER BY total_certificados DESC";

        List<String[]> estadisticas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] estadistica = new String[4];
                estadistica[0] = rs.getString("tipo");
                estadistica[1] = String.valueOf(rs.getInt("total_certificados"));
                estadistica[2] = String.valueOf(rs.getInt("cursos_diferentes"));
                estadistica[3] = String.valueOf(rs.getDouble("promedio_notas"));
                estadisticas.add(estadistica);

                System.out.println("Estadística: " + estadistica[0] + " - " + estadistica[1] + " certificados");
            }

            rs.close();
            ps.close();
            System.out.println("Estadísticas de certificados: " + estadisticas.size() + " tipos");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return estadisticas;
    }

    /**
     * Generar código único de verificación
     */
    private String generarCodigoVerificacion() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        // Formato: CER-XXXXXX (6 caracteres alfanuméricos)
        codigo.append("CER-");
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 6; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return codigo.toString();
    }
}