package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DUsuario;

import java.util.List;

public class BUsuario {

    private final DUsuario dUsuario;

    public BUsuario() {
        this.dUsuario = new DUsuario();
    }

    public String save(String nombre, String apellido, String email, String registro, String telefono, String carnet, String password, String rol) {

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

        if (registro == null || registro.trim().isEmpty()) {
            return "Error: El registro es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Error: La contraseña es obligatoria";
        }

        if (password.length() < 3) {
            return "Error: La contraseña debe tener al menos 3 caracteres";
        }

        if (rol == null || rol.trim().isEmpty()) {
            return "Error: El rol es obligatorio";
        }

        if (!isValidRol(rol)) {
            return "Error: El rol debe ser RESPONSABLE, ADMINISTRATIVO o TUTOR";
        }

        try {
            String result = dUsuario.save(nombre.trim(), apellido.trim(), email.trim().toLowerCase(),
                    registro.trim(), telefono, carnet.trim(), password, rol.toUpperCase());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public String update(int id, String nombre, String apellido, String email, String registro, String telefono, String carnet, String password, String rol) {

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

        if (registro == null || registro.trim().isEmpty()) {
            return "Error: El registro es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Error: La contraseña es obligatoria";
        }

        if (password.length() < 3) {
            return "Error: La contraseña debe tener al menos 3 caracteres";
        }

        if (rol == null || rol.trim().isEmpty()) {
            return "Error: El rol es obligatorio";
        }

        if (!isValidRol(rol)) {
            return "Error: El rol debe ser RESPONSABLE, ADMINISTRATIVO o TUTOR";
        }

        try {
            String result = dUsuario.update(id, nombre.trim(), apellido.trim(), email.trim().toLowerCase(),
                    registro.trim(), telefono, carnet.trim(), password, rol.toUpperCase());
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
            String result = dUsuario.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public List<String[]> findAllUsers() {

        try {
            List<String[]> result = dUsuario.findAllUsers();
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
            String[] result = dUsuario.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    public String[] findOneByRegister(String registro) {

        if (registro == null || registro.trim().isEmpty()) {
            System.out.println("Error: El registro es obligatorio");
            return null;
        }

        try {
            String[] result = dUsuario.findOneByRegister(registro.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    // ⭐ NUEVO: Buscar todos los usuarios RESPONSABLES activos
    public List<String[]> findAllResponsables() {

        try {
            List<String[]> result = dUsuario.findAllResponsables();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    // ⭐ NUEVO: Buscar todos los usuarios ADMINISTRATIVOS activos
    public List<String[]> findAllAdministrativos() {

        try {
            List<String[]> result = dUsuario.findAllAdministrativos();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        }
        // ⭐ ELIMINADO: finally con disconnect
    }

    // ⭐ NUEVO: Buscar todos los usuarios TUTORES activos
    public List<String[]> findAllTutores() {

        try {
            List<String[]> result = dUsuario.findAllTutores();
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

    private boolean isValidRol(String rol) {
        if (rol == null || rol.trim().isEmpty()) {
            return false;
        }

        String rolUpper = rol.toUpperCase();
        return rolUpper.equals("RESPONSABLE") || rolUpper.equals("ADMINISTRATIVO") || rolUpper.equals("TUTOR");
    }

    public void disconnect() {
        if (dUsuario != null) {
            dUsuario.disconnect();
        }
    }

}