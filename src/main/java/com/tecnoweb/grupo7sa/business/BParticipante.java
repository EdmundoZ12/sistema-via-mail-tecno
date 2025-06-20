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

    public String save(String nombre, String apellido, String email, String carnet, String telefono,
                       String carrera, String facultad, String universidad, int tipoParticipanteId) {

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

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        try {
            String result = dParticipante.save(nombre.trim(), apellido.trim(), email.trim().toLowerCase(),
                    carnet.trim(), telefono,
                    carrera != null ? carrera.trim() : null,
                    facultad != null ? facultad.trim() : null,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
        }
    }

    public String update(int id, String nombre, String apellido, String email, String carnet, String telefono,
                         String carrera, String facultad, String universidad, int tipoParticipanteId) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
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

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: Debe seleccionar un tipo de participante válido";
        }

        if (!existeTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe";
        }

        try {
            String result = dParticipante.update(id, nombre.trim(), apellido.trim(), email.trim().toLowerCase(),
                    carnet.trim(), telefono,
                    carrera != null ? carrera.trim() : null,
                    facultad != null ? facultad.trim() : null,
                    universidad != null ? universidad.trim() : null,
                    tipoParticipanteId);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dParticipante.disconnect();
        }
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
        } finally {
            dParticipante.disconnect();
        }
    }

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
        } finally {
            dParticipante.disconnect();
        }
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
        } finally {
            dParticipante.disconnect();
        }
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