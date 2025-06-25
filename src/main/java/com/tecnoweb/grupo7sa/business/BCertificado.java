package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DCertificado;
import com.tecnoweb.grupo7sa.data.DInscripcion;

import java.util.List;

public class BCertificado {

    private final DCertificado dCertificado;
    private final DInscripcion dInscripcion;

    public BCertificado() {
        this.dCertificado = new DCertificado();
        this.dInscripcion = new DInscripcion();
    }

    // CU7 - LÓGICA DE NEGOCIO PARA CERTIFICADOS

    /**
     * Crear certificado manual
     */
    public String save(int inscripcionId, String tipo, String codigoVerificacion, String urlPdf) {

        // Validaciones básicas
        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (tipo == null || tipo.trim().isEmpty()) {
            return "Error: El tipo de certificado es obligatorio";
        }

        if (!isValidTipoCertificado(tipo)) {
            return "Error: El tipo debe ser PARTICIPACION, APROBACION o MENCION_HONOR";
        }

        if (codigoVerificacion == null || codigoVerificacion.trim().isEmpty()) {
            return "Error: El código de verificación es obligatorio";
        }

        if (codigoVerificacion.trim().length() > 100) {
            return "Error: El código de verificación no puede exceder 100 caracteres";
        }

        // Verificar que la inscripción existe
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        // Verificar que no existe ya un certificado para esta inscripción
        if (existeCertificadoParaInscripcion(inscripcionId)) {
            return "Error: Ya existe un certificado para esta inscripción";
        }

        try {
            String result = dCertificado.save(inscripcionId, tipo.toUpperCase(), codigoVerificacion.trim(), urlPdf);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCertificado.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Actualizar certificado
     */
    public String update(int id, int inscripcionId, String tipo, String codigoVerificacion, String urlPdf) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        if (tipo == null || tipo.trim().isEmpty()) {
            return "Error: El tipo de certificado es obligatorio";
        }

        if (!isValidTipoCertificado(tipo)) {
            return "Error: El tipo debe ser PARTICIPACION, APROBACION o MENCION_HONOR";
        }

        if (codigoVerificacion == null || codigoVerificacion.trim().isEmpty()) {
            return "Error: El código de verificación es obligatorio";
        }

        // Verificar que el certificado existe
        if (dCertificado.findOneById(id) == null) {
            return "Error: No se encontró el certificado con ID: " + id;
        }

        // Verificar que la inscripción existe
        if (dInscripcion.findOneById(inscripcionId) == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        try {
            String result = dCertificado.update(id, inscripcionId, tipo.toUpperCase(), codigoVerificacion.trim(), urlPdf);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCertificado.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Eliminar certificado
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el certificado existe
        if (dCertificado.findOneById(id) == null) {
            return "Error: No se encontró el certificado con ID: " + id;
        }

        try {
            String result = dCertificado.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Generar certificado automáticamente ⭐ FUNCIONALIDAD ESPECIAL
     * Evalúa el estado de la inscripción y genera el certificado apropiado
     */
    public String generar(int inscripcionId) {
        if (inscripcionId <= 0) {
            return "Error: El ID de inscripción debe ser mayor a 0";
        }

        // Verificar que la inscripción existe
        String[] inscripcion = dInscripcion.findOneById(inscripcionId);
        if (inscripcion == null) {
            return "Error: No se encontró la inscripción con ID: " + inscripcionId;
        }

        // Verificar que no existe ya un certificado
        if (existeCertificadoParaInscripcion(inscripcionId)) {
            return "Error: Ya existe un certificado para esta inscripción";
        }

        try {
            String result = dCertificado.generar(inscripcionId);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCertificado.disconnect();
            dInscripcion.disconnect();
        }
    }

    /**
     * Listar todos los certificados
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dCertificado.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Buscar certificado por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dCertificado.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Buscar certificados por curso ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dCertificado.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Buscar certificados por participante ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByParticipante(int participanteId) {
        if (participanteId <= 0) {
            System.out.println("Error: El ID del participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dCertificado.findByParticipante(participanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Buscar certificados por tipo
     */
    public List<String[]> findByTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            System.out.println("Error: El tipo es obligatorio");
            return null;
        }

        if (!isValidTipoCertificado(tipo)) {
            System.out.println("Error: El tipo debe ser PARTICIPACION, APROBACION o MENCION_HONOR");
            return null;
        }

        try {
            List<String[]> result = dCertificado.findByTipo(tipo.toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Verificar certificado por código ⭐ FUNCIONALIDAD ESPECIAL
     * Para validación pública
     */
    public String[] verificar(String codigoVerificacion) {
        if (codigoVerificacion == null || codigoVerificacion.trim().isEmpty()) {
            System.out.println("Error: El código de verificación es obligatorio");
            return null;
        }

        try {
            String[] result = dCertificado.verificar(codigoVerificacion.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    /**
     * Obtener estadísticas de certificados ⭐ FUNCIONALIDAD ESPECIAL
     */
    public List<String[]> getEstadisticas() {
        try {
            List<String[]> result = dCertificado.getEstadisticas();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCertificado.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Validar tipos de certificado válidos
     */
    private boolean isValidTipoCertificado(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return false;
        }

        String tipoUpper = tipo.toUpperCase();
        return tipoUpper.equals("PARTICIPACION") ||
                tipoUpper.equals("APROBACION") ||
                tipoUpper.equals("MENCION_HONOR");
    }

    /**
     * Verificar si ya existe un certificado para una inscripción
     */
    private boolean existeCertificadoParaInscripcion(int inscripcionId) {
        try {
            List<String[]> certificados = dCertificado.findAll();
            if (certificados == null) return false;

            for (String[] certificado : certificados) {
                int certInscripcionId = Integer.parseInt(certificado[1]);
                if (certInscripcionId == inscripcionId) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dCertificado != null) {
            dCertificado.disconnect();
        }
        if (dInscripcion != null) {
            dInscripcion.disconnect();
        }
    }
}