package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DCurso;

import java.util.ArrayList;
import java.util.List;

public class BCurso {

    private final DCurso dCurso;

    public BCurso() {
        this.dCurso = new DCurso();
    }

    public String save(String nombre, String descripcion, String logoUrl, int duracionHoras, String modalidad, String nivel, String requisitos) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre del curso es obligatorio";
        }

        if (nombre.length() > 200) {
            return "Error: El nombre no puede tener más de 200 caracteres";
        }

        if (duracionHoras <= 0) {
            return "Error: La duración en horas debe ser mayor a 0";
        }

        if (duracionHoras > 1000) {
            return "Error: La duración no puede ser mayor a 1000 horas";
        }

        if (logoUrl != null && logoUrl.length() > 500) {
            return "Error: La URL del logo no puede tener más de 500 caracteres";
        }

        if (modalidad != null && !modalidad.trim().isEmpty() && !isValidModalidad(modalidad)) {
            return "Error: La modalidad debe ser PRESENCIAL, VIRTUAL o HIBRIDO";
        }

        if (nivel != null && !nivel.trim().isEmpty() && !isValidNivel(nivel)) {
            return "Error: El nivel debe ser BASICO, INTERMEDIO o AVANZADO";
        }

        try {
            String result = dCurso.save(
                    nombre.trim(),
                    descripcion != null ? descripcion.trim() : null,
                    logoUrl != null && !logoUrl.trim().isEmpty() ? logoUrl.trim() : null,
                    duracionHoras,
                    modalidad != null && !modalidad.trim().isEmpty() ? modalidad.trim().toUpperCase() : null,
                    nivel != null && !nivel.trim().isEmpty() ? nivel.trim().toUpperCase() : null,
                    requisitos != null ? requisitos.trim() : null
            );
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

  public String update(int id, String nombre, String descripcion, String logoUrl, int duracionHoras, String modalidad, String nivel, String requisitos, boolean activo) {

    if (id <= 0) {
        return "Error: El ID debe ser mayor a 0";
    }

    if (nombre == null || nombre.trim().isEmpty()) {
        return "Error: El nombre del curso es obligatorio";
    }

    if (nombre.length() > 200) {
        return "Error: El nombre no puede tener más de 200 caracteres";
    }

    if (duracionHoras <= 0) {
        return "Error: La duración en horas debe ser mayor a 0";
    }

    if (duracionHoras > 1000) {
        return "Error: La duración no puede ser mayor a 1000 horas";
    }

    if (logoUrl != null && logoUrl.length() > 500) {
        return "Error: La URL del logo no puede tener más de 500 caracteres";
    }

    if (modalidad != null && !modalidad.trim().isEmpty() && !isValidModalidad(modalidad)) {
        return "Error: La modalidad debe ser PRESENCIAL, VIRTUAL o HIBRIDO";
    }

    if (nivel != null && !nivel.trim().isEmpty() && !isValidNivel(nivel)) {
        return "Error: El nivel debe ser BASICO, INTERMEDIO o AVANZADO";
    }

    // 🔍 VALIDACIÓN DEL ACTIVO
    System.out.println("🔍 DEBUG - Valor recibido para 'activo': " + activo);
    
    // Opcionalmente puedes validar si el boolean viene como esperado
    if (activo) {
        System.out.println("✅ El curso se actualizará como ACTIVO");
    } else {
        System.out.println("⚠️ El curso se actualizará como INACTIVO");
    }

    try {
        String result = dCurso.update(
                id,
                nombre.trim(),
                descripcion != null ? descripcion.trim() : null,
                logoUrl != null && !logoUrl.trim().isEmpty() ? logoUrl.trim() : null,
                duracionHoras,
                modalidad != null && !modalidad.trim().isEmpty() ? modalidad.trim().toUpperCase() : null,
                nivel != null && !nivel.trim().isEmpty() ? nivel.trim().toUpperCase() : null,
                requisitos != null ? requisitos.trim() : null,
                activo
        );
        return result;
    } catch (Exception e) {
        return "Error en la capa de negocio: " + e.getMessage();
    } finally {
        dCurso.disconnect();
    }
}

    public String delete(int id) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        try {
            String result = dCurso.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dCurso.disconnect();
        }
    }

   public List<String[]> findAllCursos() {
    try {
        List<String[]> result = dCurso.findAllCursos();
        return result != null ? result : new ArrayList<>(); // Lista vacía en lugar de null
    } catch (Exception e) {
        System.out.println("Error en la capa de negocio: " + e.getMessage());
        return new ArrayList<>(); // Lista vacía en lugar de null
    } finally {
        dCurso.disconnect();
    }
}

    public String[] findOneById(int id) {

        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dCurso.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    public String[] findOneByNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return null;
        }

        try {
            String[] result = dCurso.findOneByNombre(nombre.trim());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    public List<String[]> findByModalidad(String modalidad) {

        if (modalidad == null || modalidad.trim().isEmpty()) {
            System.out.println("Error: La modalidad es obligatoria");
            return null;
        }

        if (!isValidModalidad(modalidad)) {
            System.out.println("Error: La modalidad debe ser PRESENCIAL, VIRTUAL o HIBRIDO");
            return null;
        }

        try {
            List<String[]> result = dCurso.findByModalidad(modalidad.trim().toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    public List<String[]> findByNivel(String nivel) {

        if (nivel == null || nivel.trim().isEmpty()) {
            System.out.println("Error: El nivel es obligatorio");
            return null;
        }

        if (!isValidNivel(nivel)) {
            System.out.println("Error: El nivel debe ser BASICO, INTERMEDIO o AVANZADO");
            return null;
        }

        try {
            List<String[]> result = dCurso.findByNivel(nivel.trim().toUpperCase());
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    public List<String[]> findByDuracion(int duracionMinima, int duracionMaxima) {

        if (duracionMinima <= 0) {
            System.out.println("Error: La duración mínima debe ser mayor a 0");
            return null;
        }

        if (duracionMaxima <= 0) {
            System.out.println("Error: La duración máxima debe ser mayor a 0");
            return null;
        }

        if (duracionMinima > duracionMaxima) {
            System.out.println("Error: La duración mínima no puede ser mayor a la máxima");
            return null;
        }

        try {
            List<String[]> result = dCurso.findByDuracion(duracionMinima, duracionMaxima);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dCurso.disconnect();
        }
    }

    private boolean isValidModalidad(String modalidad) {
        if (modalidad == null || modalidad.trim().isEmpty()) {
            return false;
        }

        String modalidadUpper = modalidad.trim().toUpperCase();
        return modalidadUpper.equals("PRESENCIAL") || 
               modalidadUpper.equals("VIRTUAL") || 
               modalidadUpper.equals("HIBRIDO");
    }

    private boolean isValidNivel(String nivel) {
        if (nivel == null || nivel.trim().isEmpty()) {
            return false;
        }

        String nivelUpper = nivel.trim().toUpperCase();
        return nivelUpper.equals("BASICO") || 
               nivelUpper.equals("INTERMEDIO") || 
               nivelUpper.equals("AVANZADO");
    }
}