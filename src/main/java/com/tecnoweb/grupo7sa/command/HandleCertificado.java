package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BCertificado;

import java.util.List;

public class HandleCertificado {

    public static String save(String params) {
        String[] parts = params.split(",\s*");
        if (parts.length < 4)
            return "Error: Se requieren 4 parámetros: inscripcionId, tipo, codigoVerificacion, urlPdf";

        try {
            int inscripcionId = Integer.parseInt(parts[0].trim());
            String tipo = parts[1].trim();
            String codigo = parts[2].trim();
            String urlPdf = parts[3].trim();
            BCertificado b = new BCertificado();
            return b.save(inscripcionId, tipo, codigo, urlPdf);
        } catch (Exception e) {
            return "Error: Entrada inválida. " + e.getMessage();
        }
    }

    public static String update(String params) {
        String[] parts = params.split(",\s*");
        if (parts.length < 5)
            return "Error: Se requieren 5 parámetros: id, inscripcionId, tipo, codigoVerificacion, urlPdf";

        try {
            int id = Integer.parseInt(parts[0].trim());
            int inscripcionId = Integer.parseInt(parts[1].trim());
            String tipo = parts[2].trim();
            String codigo = parts[3].trim();
            String urlPdf = parts[4].trim();
            BCertificado b = new BCertificado();
            return b.update(id, inscripcionId, tipo, codigo, urlPdf);
        } catch (Exception e) {
            return "Error: Entrada inválida. " + e.getMessage();
        }
    }

    public static String delete(String idStr) {
        try {
            int id = Integer.parseInt(idStr.trim());
            return new BCertificado().delete(id);
        } catch (Exception e) {
            return "Error: ID inválido.";
        }
    }

    public static String generar(String idStr) {
        try {
            int inscripcionId = Integer.parseInt(idStr.trim());
            return new BCertificado().generar(inscripcionId);
        } catch (Exception e) {
            return "Error: ID inválido.";
        }
    }

    public static String findAll() {
        List<String[]> data = new BCertificado().findAll();
        if (data == null || data.isEmpty()) return "No hay certificados registrados.";

        StringBuilder sb = new StringBuilder("=== LISTA DE CERTIFICADOS ===\n");
        for (String[] c : data) {
            sb.append("ID: ").append(c[0])
                    .append(" | Inscripción: ").append(c[1])
                    .append(" | Tipo: ").append(c[2])
                    .append(" | Código: ").append(c[3])
                    .append("\n");
        }
        return sb.toString();
    }

    public static String findOneById(String idStr) {
        try {
            int id = Integer.parseInt(idStr.trim());
            String[] c = new BCertificado().findOneById(id);
            if (c == null) return "Certificado no encontrado.";
            return "ID: " + c[0] + " | Inscripción: " + c[1] + " | Tipo: " + c[2] + " | Código: " + c[3];
        } catch (Exception e) {
            return "Error: ID inválido.";
        }
    }

    public static String findByCurso(String cursoStr) {
        try {
            int cursoId = Integer.parseInt(cursoStr.trim());
            List<String[]> data = new BCertificado().findByCurso(cursoId);
            if (data == null || data.isEmpty()) return "No hay certificados para ese curso.";
            StringBuilder sb = new StringBuilder("=== CERTIFICADOS DEL CURSO ===\n");
            for (String[] c : data) {
                sb.append("ID: ").append(c[0]).append(" | Participante: ").append(c[2]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error: ID inválido.";
        }
    }

    public static String findByParticipante(String idStr) {
        try {
            int id = Integer.parseInt(idStr.trim());
            List<String[]> data = new BCertificado().findByParticipante(id);
            if (data == null || data.isEmpty()) return "No hay certificados del participante.";
            StringBuilder sb = new StringBuilder("=== CERTIFICADOS DEL PARTICIPANTE ===\n");
            for (String[] c : data) {
                sb.append("ID: ").append(c[0]).append(" | Tipo: ").append(c[2]).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error: ID inválido.";
        }
    }

    public static String findByTipo(String tipo) {
        List<String[]> data = new BCertificado().findByTipo(tipo);
        if (data == null || data.isEmpty()) return "No hay certificados de tipo " + tipo;
        StringBuilder sb = new StringBuilder("=== CERTIFICADOS TIPO " + tipo + " ===\n");
        for (String[] c : data) {
            sb.append("ID: ").append(c[0]).append(" | Inscripción: ").append(c[1]).append("\n");
        }
        return sb.toString();
    }

    public static String verificar(String codigo) {
        String[] cert = new BCertificado().verificar(codigo);
        if (cert == null) return "Certificado no encontrado con ese código.";
        return "Certificado válido: ID: " + cert[0] + " | Tipo: " + cert[2];
    }

    public static String estadisticas() {
        List<String[]> stats = new BCertificado().getEstadisticas();
        if (stats == null || stats.isEmpty()) return "No hay estadísticas disponibles.";
        StringBuilder sb = new StringBuilder("=== ESTADÍSTICAS DE CERTIFICADOS ===\n");
        for (String[] s : stats) {
            sb.append("Tipo: ").append(s[0]).append(" | Cantidad: ").append(s[1]).append("\n");
        }
        return sb.toString();
    }
}
