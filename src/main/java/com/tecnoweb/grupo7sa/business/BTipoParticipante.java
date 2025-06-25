package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DTipoParticipante;

import java.util.List;

public class BTipoParticipante {

    private final DTipoParticipante dTipoParticipante;

    public BTipoParticipante() {
        this.dTipoParticipante = new DTipoParticipante();
    }

    // CU1 - LÓGICA DE NEGOCIO PARA TIPOS DE PARTICIPANTE

    /**
     * Crear nuevo tipo de participante con validaciones de negocio
     */
    public String save(String codigo, String descripcion) {

        // Validaciones básicas
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }

        if (codigo.length() > 20) {
            return "Error: El código no puede tener más de 20 caracteres";
        }

        if (!isValidCodigo(codigo)) {
            return "Error: El código solo puede contener letras, números y guiones bajos";
        }

        // Verificar unicidad del código
        if (dTipoParticipante.findByCodigo(codigo.trim().toUpperCase()) != null) {
            return "Error: Ya existe un tipo de participante con ese código";
        }

        try {
            String result = dTipoParticipante.save(codigo.trim().toUpperCase(), descripcion.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Actualizar tipo de participante existente con validaciones
     */
    public String update(int id, String codigo, String descripcion) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validaciones básicas
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }

        if (codigo.length() > 20) {
            return "Error: El código no puede tener más de 20 caracteres";
        }

        if (!isValidCodigo(codigo)) {
            return "Error: El código solo puede contener letras, números y guiones bajos";
        }

        // Verificar que el tipo existe
        if (dTipoParticipante.findOneById(id) == null) {
            return "Error: No se encontró el tipo de participante con ID: " + id;
        }

        // Verificar unicidad del código (excluyendo el tipo actual)
        String[] existingByCodigo = dTipoParticipante.findByCodigo(codigo.trim().toUpperCase());
        if (existingByCodigo != null && !existingByCodigo[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro tipo de participante con ese código";
        }

        try {
            String result = dTipoParticipante.update(id, codigo.trim().toUpperCase(), descripcion.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Desactivar tipo de participante
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el tipo existe
        if (dTipoParticipante.findOneById(id) == null) {
            return "Error: No se encontró el tipo de participante con ID: " + id;
        }

        try {
            String result = dTipoParticipante.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Reactivar tipo de participante
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el tipo existe
        if (dTipoParticipante.findOneById(id) == null) {
            return "Error: No se encontró el tipo de participante con ID: " + id;
        }

        try {
            String result = dTipoParticipante.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Listar todos los tipos de participante activos
     */
    public List<String[]> findAllTipos() {
        try {
            List<String[]> result = dTipoParticipante.findAllTipos();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Buscar tipo por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dTipoParticipante.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Buscar tipo por código
     */
    public String[] findByCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("Error: El código es obligatorio");
            return null;
        }

        try {
            String[] result = dTipoParticipante.findByCodigo(codigo.trim().toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    // Métodos auxiliares de validación
    private boolean isValidCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        // Solo letras, números y guiones bajos
        String codigoRegex = "^[A-Za-z0-9_]+$";
        return codigo.matches(codigoRegex);
    }

    public void disconnect() {
        if (dTipoParticipante != null) {
            dTipoParticipante.disconnect();
        }
    }
}