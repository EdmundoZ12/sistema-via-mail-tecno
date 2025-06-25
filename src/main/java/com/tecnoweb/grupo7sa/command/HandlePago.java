package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.business.BPago;

import java.sql.Date;
import java.util.List;

public class HandlePago {

    // CU6 - HANDLER PARA PAGOS

    public static String save(String params) {
        String[] parts = params.split(",\s*");

        if (parts.length < 3) return "Error: Se requieren 3 parámetros: preinscripcionId, monto, recibo";

        try {
            int preinscripcionId = Integer.parseInt(parts[0].trim());
            double monto = Double.parseDouble(parts[1].trim());
            String recibo = parts[2].trim();

            BPago bPago = new BPago();
            return bPago.save(preinscripcionId, monto, recibo);
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }

    public static String update(String params) {
        String[] parts = params.split(",\s*");

        if (parts.length < 4) return "Error: Se requieren 4 parámetros: id, preinscripcionId, monto, recibo";

        try {
            int id = Integer.parseInt(parts[0].trim());
            int preinscripcionId = Integer.parseInt(parts[1].trim());
            double monto = Double.parseDouble(parts[2].trim());
            String recibo = parts[3].trim();

            BPago bPago = new BPago();
            return bPago.update(id, preinscripcionId, monto, recibo);
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }

    public static String delete(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPago bPago = new BPago();
            return bPago.delete(id);
        } catch (NumberFormatException e) {
            return "Error: ID inválido. " + e.getMessage();
        }
    }

    public static String findAll() {
        BPago bPago = new BPago();
        List<String[]> pagos = bPago.findAll();

        if (pagos == null || pagos.isEmpty()) return "No hay pagos registrados.";

        StringBuilder sb = new StringBuilder("=== LISTA DE PAGOS ===\n");
        for (String[] p : pagos) {
            sb.append("ID: ").append(p[0])
                    .append(" | Preinscripción ID: ").append(p[1])
                    .append(" | Monto: $").append(p[2])
                    .append(" | Recibo: ").append(p[3])
                    .append("\n");
        }
        return sb.toString();
    }

    public static String findById(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPago bPago = new BPago();
            String[] pago = bPago.findOneById(id);
            if (pago == null) return "No se encontró el pago con ID: " + id;

            return "ID: " + pago[0] +
                    " | Preinscripción ID: " + pago[1] +
                    " | Monto: $" + pago[2] +
                    " | Recibo: " + pago[3];
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }

    public static String findByPreinscripcion(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPago bPago = new BPago();
            String[] pago = bPago.findByPreinscripcion(id);
            if (pago == null) return "No hay pago registrado para preinscripción ID: " + id;

            return "ID: " + pago[0] +
                    " | Monto: $" + pago[2] +
                    " | Recibo: " + pago[3];
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }

    public static String findByRecibo(String params) {
        BPago bPago = new BPago();
        String[] pago = bPago.findByRecibo(params.trim());

        if (pago == null) return "No se encontró pago con recibo: " + params;

        return "ID: " + pago[0] +
                " | Preinscripción ID: " + pago[1] +
                " | Monto: $" + pago[2];
    }

    public static String findByRangoFecha(String params) {
        String[] fechas = params.split(",\s*");

        if (fechas.length < 2) return "Error: Se requieren 2 fechas: fechaInicio, fechaFin (formato yyyy-mm-dd)";

        try {
            Date inicio = Date.valueOf(fechas[0].trim());
            Date fin = Date.valueOf(fechas[1].trim());
            BPago bPago = new BPago();
            List<String[]> pagos = bPago.findByRangoFecha(inicio, fin);

            if (pagos == null || pagos.isEmpty()) return "No hay pagos en el rango indicado.";

            StringBuilder sb = new StringBuilder("=== PAGOS ENTRE " + inicio + " Y " + fin + " ===\n");
            for (String[] p : pagos) {
                sb.append("ID: ").append(p[0])
                        .append(" | Monto: $").append(p[2])
                        .append(" | Recibo: ").append(p[3])
                        .append("\n");
            }
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "Error: Formato de fecha inválido. Debe ser yyyy-mm-dd";
        }
    }

    public static String ingresosPorMes(String params) {
        String[] valores = params.split(",\s*");
        if (valores.length < 2) return "Error: Se requieren 2 parámetros: año, mes";

        try {
            int anio = Integer.parseInt(valores[0].trim());
            int mes = Integer.parseInt(valores[1].trim());

            BPago bPago = new BPago();
            String[] ingreso = bPago.getIngresosPorMes(anio, mes);

            if (ingreso == null) return "No se encontraron ingresos para el mes indicado";

            return "Ingresos en " + anio + "/" + mes + ": $" + ingreso[0];
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }

    public static String ingresosPorGestion(String params) {
        try {
            int id = Integer.parseInt(params.trim());
            BPago bPago = new BPago();
            String[] ingreso = bPago.getIngresosPorGestion(id);

            if (ingreso == null) return "No se encontraron ingresos para la gestión ID: " + id;

            return "Ingresos para gestión " + id + ": $" + ingreso[0];
        } catch (NumberFormatException e) {
            return "Error de formato: " + e.getMessage();
        }
    }
}