package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DTipoParticipante;

import java.util.List;

public class BTipoParticipante {

    private final DTipoParticipante dTipoParticipante;

    public BTipoParticipante() {
        this.dTipoParticipante = new DTipoParticipante();
    }

    public String save(String nombre, String codigo, String descripcion) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }

        if (codigo.length() > 10) {
            return "Error: El código no puede tener más de 10 caracteres";
        }

        if (!isValidCodigo(codigo)) {
            return "Error: El código solo puede contener letras, números y guiones bajos";
        }


        try {
            String result = dTipoParticipante.save(nombre.trim(), codigo.trim().toUpperCase(),
                    descripcion != null ? descripcion.trim() : null);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    public String update(int id, String nombre, String codigo, String descripcion) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }

        if (codigo.length() > 10) {
            return "Error: El código no puede tener más de 10 caracteres";
        }

        if (!isValidCodigo(codigo)) {
            return "Error: El código solo puede contener letras, números y guiones bajos";
        }

        try {
            String result = dTipoParticipante.update(id, nombre.trim(), codigo.trim().toUpperCase(),
                    descripcion != null ? descripcion.trim() : null);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    public String delete(int id) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
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

    public String[] findOneByCodigo(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("Error: El código es obligatorio");
            return null;
        }

        try {
            String[] result = dTipoParticipante.findOneByCodigo(codigo.trim().toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dTipoParticipante.disconnect();
        }
    }

    private boolean isValidCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        // Solo letras, números y guiones bajos
        String codigoRegex = "^[A-Za-z0-9_]+$";
        return codigo.matches(codigoRegex);
    }

}