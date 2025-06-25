package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DUsuario;

import java.util.List;

public class BUsuario {

    private final DUsuario dUsuario;

    public BUsuario() {
        this.dUsuario = new DUsuario();
    }

    // CU1 - LÓGICA DE NEGOCIO PARA USUARIOS

    /**
     * Crear nuevo usuario con validaciones de negocio
     */
    public String save(String nombre, String apellido, String carnet, String email, String telefono,
                       String password, String rol, String registro) {

        // Validaciones de negocio
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Error: El email es obligatorio";
        }

        if (!isValidEmail(email)) {
            return "Error: El formato del email no es válido";
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

        if (registro == null || registro.trim().isEmpty()) {
            return "Error: El registro es obligatorio";
        }

        // Verificar unicidad de carnet
        if (dUsuario.findByCarnet(carnet.trim()) != null) {
            return "Error: Ya existe un usuario con ese carnet";
        }

        // Verificar unicidad de email
        if (dUsuario.findByEmail(email.trim().toLowerCase()) != null) {
            return "Error: Ya existe un usuario con ese email";
        }

        try {
            String result = dUsuario.save(nombre.trim(), apellido.trim(), carnet.trim(),
                    email.trim().toLowerCase(), telefono, password,
                    rol.toUpperCase(), registro.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Actualizar usuario existente con validaciones
     */
    public String update(int id, String nombre, String apellido, String carnet, String email,
                         String telefono, String password, String rol, String registro) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return "Error: El apellido es obligatorio";
        }

        if (carnet == null || carnet.trim().isEmpty()) {
            return "Error: El carnet es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Error: El email es obligatorio";
        }

        if (!isValidEmail(email)) {
            return "Error: El formato del email no es válido";
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

        if (registro == null || registro.trim().isEmpty()) {
            return "Error: El registro es obligatorio";
        }

        // Verificar que el usuario existe
        if (dUsuario.findOneById(id) == null) {
            return "Error: No se encontró el usuario con ID: " + id;
        }

        // Verificar unicidad de carnet (excluyendo el usuario actual)
        String[] existingByCarnet = dUsuario.findByCarnet(carnet.trim());
        if (existingByCarnet != null && !existingByCarnet[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro usuario con ese carnet";
        }

        // Verificar unicidad de email (excluyendo el usuario actual)
        String[] existingByEmail = dUsuario.findByEmail(email.trim().toLowerCase());
        if (existingByEmail != null && !existingByEmail[0].equals(String.valueOf(id))) {
            return "Error: Ya existe otro usuario con ese email";
        }

        try {
            String result = dUsuario.update(id, nombre.trim(), apellido.trim(), carnet.trim(),
                    email.trim().toLowerCase(), telefono, password,
                    rol.toUpperCase(), registro.trim());
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Desactivar usuario
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el usuario existe
        if (dUsuario.findOneById(id) == null) {
            return "Error: No se encontró el usuario con ID: " + id;
        }

        try {
            String result = dUsuario.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Reactivar usuario
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el usuario existe
        if (dUsuario.findOneById(id) == null) {
            return "Error: No se encontró el usuario con ID: " + id;
        }

        try {
            String result = dUsuario.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Listar todos los usuarios activos
     */
    public List<String[]> findAllUsers() {
        try {
            List<String[]> result = dUsuario.findAllUsers();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Buscar usuario por ID
     */
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
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Buscar usuarios por rol
     */
    public List<String[]> findByRole(String rol) {
        if (rol == null || rol.trim().isEmpty()) {
            System.out.println("Error: El rol es obligatorio");
            return null;
        }

        if (!isValidRol(rol)) {
            System.out.println("Error: El rol debe ser RESPONSABLE, ADMINISTRATIVO o TUTOR");
            return null;
        }

        try {
            List<String[]> result = dUsuario.findByRole(rol.toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Buscar usuario por carnet
     */
    public String[] findByCarnet(String carnet) {
        if (carnet == null || carnet.trim().isEmpty()) {
            System.out.println("Error: El carnet es obligatorio");
            return null;
        }

        try {
            String[] result = dUsuario.findByCarnet(carnet.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dUsuario.disconnect();
        }
    }

    /**
     * Buscar usuario por email
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
            String[] result = dUsuario.findByEmail(email.trim().toLowerCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dUsuario.disconnect();
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