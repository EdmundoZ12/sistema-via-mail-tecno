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

    // ⭐ ACTUALIZADO: Ahora incluye parámetro 'registro'
    public String save(String nombre, String apellido, String carnet, String registro, String carrera,
                       String email, String facultad, String telefono, String universidad, int tipoParticipanteId) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        // ⭐ NUEVA VALIDACIÓN: registro es obligatorio solo si se proporciona
        if (registro != null && registro.trim().isEmpty()) {
            return "Error: El registro no puede estar vacío si se proporciona";
        }

        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            return "Error: El formato del email no es válido";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        try {
            // ⭐ ACTUALIZADO: Nuevo orden de parámetros según DParticipante
            String result = dParticipante.save(
                    nombre.trim(),
                    apellido.trim(),
                    carnet.trim(),
                    registro != null ? registro.trim() : null,
                    carrera != null ? carrera.trim() : null,
                    email != null ? email.trim().toLowerCase() : null,
                    facultad != null ? facultad.trim() : null,
                    telefono,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        }
        // ⭐ ELIMINADO: finally con disconnect (según tu corrección anterior)
    }

    // ⭐ ACTUALIZADO: Ahora incluye parámetro 'registro'
    public String update(int id, String nombre, String apellido, String carnet, String registro, String carrera,
                         String email, String facultad, String telefono, String universidad, int tipoParticipanteId) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        // ⭐ NUEVA VALIDACIÓN: registro
        if (registro != null && registro.trim().isEmpty()) {
            return "Error: El registro no puede estar vacío si se proporciona";
        }

        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            return "Error: El formato del email no es válido";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        try {
            // ⭐ ACTUALIZADO: Nuevo orden de parámetros según DParticipante
            String result = dParticipante.update(
                    id,
                    nombre.trim(),
                    apellido.trim(),
                    carnet.trim(),
                    registro != null ? registro.trim() : null,
                    carrera != null ? carrera.trim() : null,
                    email != null ? email.trim().toLowerCase() : null,
                    facultad != null ? facultad.trim() : null,
                    telefono,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public String delete(int id) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        try {
            String result = dParticipante.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public List<String[]> findAllParticipantes() {

        try {
            List<String[]> result = dParticipante.findAllParticipantes();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

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
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public String[] findOneByCarnet(String carnet) {

        if (carnet == null || carnet.trim().isEmpty()) {
            System.out.println("Error: El carnet es obligatorio");
            return null;
        }

        try {
            String[] result = dParticipante.findOneByCarnet(carnet.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    // ⭐ NUEVO: Método para buscar por registro
    public String[] findOneByRegistro(String registro) {

        if (registro == null || registro.trim().isEmpty()) {
            System.out.println("Error: El registro es obligatorio");
            return null;
        }

        try {
            String[] result = dParticipante.findOneByRegistro(registro.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public List<String[]> findByTipoParticipante(int tipoParticipanteId) {

        if (tipoParticipanteId <= 0) {
            System.out.println("Error: El ID del tipo de participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dParticipante.findByTipoParticipante(tipoParticipanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

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
            return tipo != null;
        } catch (Exception e) {
            return false;
        }
    }


}