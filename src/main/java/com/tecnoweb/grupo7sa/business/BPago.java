package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DPago;
import com.tecnoweb.grupo7sa.data.DPreinscripcion;

import java.sql.Date;
import java.util.List;

public class BPago {

    private final DPago dPago;
    private final DPreinscripcion dPreinscripcion;

    public BPago() {
        this.dPago = new DPago();
        this.dPreinscripcion = new DPreinscripcion();
    }

    // CU6 - LÓGICA DE NEGOCIO PARA PAGOS

    /**
     * Registrar nuevo pago con validaciones
     */
    public String save(int preinscripcionId, double monto, String recibo) {

        // Validaciones básicas
        if (preinscripcionId <= 0) {
            return "Error: El ID de preinscripción debe ser mayor a 0";
        }

        if (monto <= 0) {
            return "Error: El monto debe ser mayor a 0";
        }

        if (monto > 10000) {
            return "Error: El monto no puede exceder $10,000";
        }

        if (recibo == null || recibo.trim().isEmpty()) {
            return "Error: El número de recibo es obligatorio";
        }

        if (recibo.trim().length() > 50) {
            return "Error: El número de recibo no puede exceder 50 caracteres";
        }

        // Verificar que la preinscripción existe
        String[] preinscripcion = dPreinscripcion.findOneById(preinscripcionId);
        if (preinscripcion == null) {
            return "Error: No se encontró la preinscripción con ID: " + preinscripcionId;
        }

        // Verificar que la preinscripción está aprobada
        if (!"APROBADA".equals(preinscripcion[4])) {
            return "Error: Solo se pueden registrar pagos para preinscripciones aprobadas";
        }

        // Verificar que no existe ya un pago para esta preinscripción
        if (dPago.findByPreinscripcion(preinscripcionId) != null) {
            return "Error: Ya existe un pago registrado para esta preinscripción";
        }

        // Verificar que el número de recibo no esté duplicado
        if (dPago.findByRecibo(recibo.trim()) != null) {
            return "Error: Ya existe un pago con el número de recibo: " + recibo.trim();
        }

        try {
            String result = dPago.save(preinscripcionId, monto, recibo.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPago.disconnect();
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Actualizar pago existente
     */
    public String update(int id, int preinscripcionId, double monto, String recibo) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (preinscripcionId <= 0) {
            return "Error: El ID de preinscripción debe ser mayor a 0";
        }

        if (monto <= 0) {
            return "Error: El monto debe ser mayor a 0";
        }

        if (monto > 10000) {
            return "Error: El monto no puede exceder $10,000";
        }

        if (recibo == null || recibo.trim().isEmpty()) {
            return "Error: El número de recibo es obligatorio";
        }

        // Verificar que el pago existe
        if (dPago.findOneById(id) == null) {
            return "Error: No se encontró el pago con ID: " + id;
        }

        // Verificar que la preinscripción existe
        if (dPreinscripcion.findOneById(preinscripcionId) == null) {
            return "Error: No se encontró la preinscripción con ID: " + preinscripcionId;
        }

        // Verificar unicidad del recibo (excluyendo el pago actual)
        String[] existingByRecibo = dPago.findByRecibo(recibo.trim());
        if (existingByRecibo != null && !existingByRecibo[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro pago con el número de recibo: " + recibo.trim();
        }

        try {
            String result = dPago.update(id, preinscripcionId, monto, recibo.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPago.disconnect();
            dPreinscripcion.disconnect();
        }
    }

    /**
     * Anular pago
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el pago existe
        if (dPago.findOneById(id) == null) {
            return "Error: No se encontró el pago con ID: " + id;
        }

        try {
            String result = dPago.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Listar todos los pagos ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dPago.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Buscar pago por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPago.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Verificar si ya se pagó una preinscripción ⭐ ENDPOINT PRINCIPAL
     */
    public String[] findByPreinscripcion(int preinscripcionId) {
        if (preinscripcionId <= 0) {
            System.out.println("Error: El ID de preinscripción debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPago.findByPreinscripcion(preinscripcionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Buscar pago por número de recibo
     */
    public String[] findByRecibo(String recibo) {
        if (recibo == null || recibo.trim().isEmpty()) {
            System.out.println("Error: El número de recibo es obligatorio");
            return null;
        }

        try {
            String[] result = dPago.findByRecibo(recibo.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Buscar pagos por rango de fechas ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByRangoFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null) {
            System.out.println("Error: La fecha de inicio es obligatoria");
            return null;
        }

        if (fechaFin == null) {
            System.out.println("Error: La fecha de fin es obligatoria");
            return null;
        }

        if (fechaFin.before(fechaInicio)) {
            System.out.println("Error: La fecha de fin debe ser posterior a la fecha de inicio");
            return null;
        }

        try {
            List<String[]> result = dPago.findByRangoFecha(fechaInicio, fechaFin);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Obtener ingresos por mes ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getIngresosPorMes(int año, int mes) {
        if (año < 2020 || año > 2030) {
            System.out.println("Error: El año debe estar entre 2020 y 2030");
            return null;
        }

        if (mes < 1 || mes > 12) {
            System.out.println("Error: El mes debe estar entre 1 y 12");
            return null;
        }

        try {
            String[] result = dPago.getIngresosPorMes(año, mes);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    /**
     * Obtener ingresos por gestión ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String[] getIngresosPorGestion(int gestionId) {
        if (gestionId <= 0) {
            System.out.println("Error: El ID de gestión debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPago.getIngresosPorGestion(gestionId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPago.disconnect();
        }
    }

    public void disconnect() {
        if (dPago != null) {
            dPago.disconnect();
        }
        if (dPreinscripcion != null) {
            dPreinscripcion.disconnect();
        }
    }
}