package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DParticipante;
import com.tecnoweb.grupo7sa.data.DTipoParticipante;

import java.util.List;

public class BParticipante {

    private final DParticipante dParticipante;
    private final DTipoParticipante dTipoParticipante;

    public BParticipante() {
        this.dParticipante = new DParticipante();
        this.dTipoParticipante = new DTipoParticipante();
    }

    // CU1 - LÓGICA DE NEGOCIO PARA PARTICIPANTES

    /**
     * Crear nuevo participante con validaciones de negocio
     */
    public String save(String carnet, String nombre, String apellido, String email, String telefono,
                       String universidad, int tipoParticipanteId, String registro) {

        // Validaciones básicas obligatorias
        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Error: El email es obligatorio";
        }

        if (!isValidEmail(email)) {
            return "Error: El formato del email no es válido";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        // Verificar que el tipo de participante existe
        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        // Verificar unicidad de carnet
        if (dParticipante.findByCarnet(carnet.trim()) != null) {
            return "Error: Ya existe un participante con ese carnet";
        }

        // Verificar unicidad de email
        if (dParticipante.findByEmail(email.trim().toLowerCase()) != null) {
            return "Error: Ya existe un participante con ese email";
        }

        // Verificar unicidad de registro si se proporciona
        if (registro != null && !registro.trim().isEmpty()) {
            if (dParticipante.findByRegistro(registro.trim()) != null) {
                return "Error: Ya existe un participante con ese registro";
            }
        }

        try {
            String result = dParticipante.save(
                    carnet.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim().toLowerCase(),
                    telefono,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId,
                    registro != null && !registro.trim().isEmpty() ? registro.trim() : null
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Actualizar participante existente con validaciones
     */
    public String update(int id, String carnet, String nombre, String apellido, String email, String telefono,
                         String universidad, int tipoParticipanteId, String registro) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validaciones básicas
        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Error: El email es obligatorio";
        }

        if (!isValidEmail(email)) {
            return "Error: El formato del email no es válido";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        // Verificar que el participante existe
        if (dParticipante.findOneById(id) == null) {
            return "Error: No se encontró el participante con ID: " + id;
        }

        // Verificar que el tipo de participante existe
        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        // Verificar unicidad de carnet (excluyendo el participante actual)
        String[] existingByCarnet = dParticipante.findByCarnet(carnet.trim());
        if (existingByCarnet != null && !existingByCarnet[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro participante con ese carnet";
        }

        // Verificar unicidad de email (excluyendo el participante actual)
        String[] existingByEmail = dParticipante.findByEmail(email.trim().toLowerCase());
        if (existingByEmail != null && !existingByEmail[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro participante con ese email";
        }

        // Verificar unicidad de registro si se proporciona (excluyendo el participante actual)
        if (registro != null && !registro.trim().isEmpty()) {
            String[] existingByRegistro = dParticipante.findByRegistro(registro.trim());
            if (existingByRegistro != null && !existingByRegistro[0].equals(String.valueOf(id))) {
                return "Error: Ya existe otro participante con ese registro";
            }
        }

        try {
            String result = dParticipante.update(
                    id,
                    carnet.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim().toLowerCase(),
                    telefono,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId,
                    registro != null && !registro.trim().isEmpty() ? registro.trim() : null
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Desactivar participante
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el participante existe
        if (dParticipante.findOneById(id) == null) {
            return "Error: No se encontró el participante con ID: " + id;
        }

        try {
            String result = dParticipante.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Reactivar participante
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el participante existe
        if (dParticipante.findOneById(id) == null) {
            return "Error: No se encontró el participante con ID: " + id;
        }

        try {
            String result = dParticipante.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Listar todos los participantes activos
     */
    public List<String[]> findAllParticipantes() {
        try {
            List<String[]> result = dParticipante.findAllParticipantes();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Buscar participante por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dParticipante.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Buscar participante por carnet
     */
    public String[] findByCarnet(String carnet) {
        if (carnet == null || carnet.trim().isEmpty()) {
            System.out.println("Error: El carnet es obligatorio");
            return null;
        }

        try {
            String[] result = dParticipante.findByCarnet(carnet.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Buscar participante por email
     */
    public String[] findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: El email es obligatorio");
            return null;
        }

        if (!isValidEmail(email)) {
            System.out.println("Error: El formato del email no es válido");
            return null;
        }

        try {
            String[] result = dParticipante.findByEmail(email.trim().toLowerCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Buscar participantes por tipo
     */
    public List<String[]> findByTipo(int tipoParticipanteId) {
        if (tipoParticipanteId <= 0) {
            System.out.println("Error: El ID del tipo de participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dParticipante.findByTipo(tipoParticipanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    /**
     * Buscar participante por registro
     */
    public String[] findByRegistro(String registro) {
        if (registro == null || registro.trim().isEmpty()) {
            System.out.println("Error: El registro es obligatorio");
            return null;
        }

        try {
            String[] result = dParticipante.findByRegistro(registro.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dParticipante.disconnect();
        }
    }

    // Métodos auxiliares de validación
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean existeTipoParticipante(int tipoParticipanteId) {
        try {
            String[] tipo = dTipoParticipante.findOneById(tipoParticipanteId);
            return tipo != null && tipo[3].equals("true"); // Verificar que esté activo
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dParticipante != null) {
            dParticipante.disconnect();
        }
        if (dTipoParticipante != null) {
            dTipoParticipante.disconnect();
        }
    }
}