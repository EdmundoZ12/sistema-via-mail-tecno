package com.tecnoweb.grupo7sa.business;

import com.tecnoweb.grupo7sa.data.DPrecioCurso;
import com.tecnoweb.grupo7sa.data.DCurso;
import com.tecnoweb.grupo7sa.data.DTipoParticipante;

import java.util.List;

public class BPrecioCurso {

    private final DPrecioCurso dPrecioCurso;
    private final DCurso dCurso;
    private final DTipoParticipante dTipoParticipante;

    public BPrecioCurso() {
        this.dPrecioCurso = new DPrecioCurso();
        this.dCurso = new DCurso();
        this.dTipoParticipante = new DTipoParticipante();
    }

    // CU3 - LÓGICA DE NEGOCIO PARA PRECIOS DE CURSOS

    /**
     * Crear precio diferenciado con validaciones
     */
    public String save(int cursoId, int tipoParticipanteId, double precio) {

        // Validaciones básicas
        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: El ID del tipo de participante debe ser mayor a 0";
        }

        if (precio <= 0) {
            return "Error: El precio debe ser mayor a 0";
        }

        if (precio > 10000) {
            return "Error: El precio no puede exceder $10,000";
        }

        // Verificar que el curso existe y está activo
        if (!existsCurso(cursoId)) {
            return "Error: El curso seleccionado no existe o no está activo";
        }

        // Verificar que el tipo de participante existe y está activo
        if (!existsTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe o no está activo";
        }

        // Verificar que no existe ya un precio para esta combinación
        if (existsPrecioEspecifico(cursoId, tipoParticipanteId)) {
            return "Error: Ya existe un precio definido para este curso y tipo de participante";
        }

        try {
            String result = dPrecioCurso.save(cursoId, tipoParticipanteId, precio);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPrecioCurso.disconnect();
            dCurso.disconnect();
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Actualizar precio existente
     */
    public String update(int id, int cursoId, int tipoParticipanteId, double precio) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Validaciones básicas
        if (cursoId <= 0) {
            return "Error: El ID del curso debe ser mayor a 0";
        }

        if (tipoParticipanteId <= 0) {
            return "Error: El ID del tipo de participante debe ser mayor a 0";
        }

        if (precio <= 0) {
            return "Error: El precio debe ser mayor a 0";
        }

        if (precio > 10000) {
            return "Error: El precio no puede exceder $10,000";
        }

        // Verificar que el precio existe
        if (dPrecioCurso.findOneById(id) == null) {
            return "Error: No se encontró el precio con ID: " + id;
        }

        // Verificar que el curso existe y está activo
        if (!existsCurso(cursoId)) {
            return "Error: El curso seleccionado no existe o no está activo";
        }

        // Verificar que el tipo de participante existe y está activo
        if (!existsTipoParticipante(tipoParticipanteId)) {
            return "Error: El tipo de participante seleccionado no existe o no está activo";
        }

        // Verificar que no existe conflicto con otro precio (excluyendo el actual)
        if (existsPrecioEspecificoExcluding(id, cursoId, tipoParticipanteId)) {
            return "Error: Ya existe otro precio definido para este curso y tipo de participante";
        }

        try {
            String result = dPrecioCurso.update(id, cursoId, tipoParticipanteId, precio);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPrecioCurso.disconnect();
            dCurso.disconnect();
            dTipoParticipante.disconnect();
        }
    }

    /**
     * Actualizar solo el monto del precio ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updatePrecio(int id, double nuevoPrecio) {

        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        if (nuevoPrecio <= 0) {
            return "Error: El precio debe ser mayor a 0";
        }

        if (nuevoPrecio > 10000) {
            return "Error: El precio no puede exceder $10,000";
        }

        // Verificar que el precio existe
        if (dPrecioCurso.findOneById(id) == null) {
            return "Error: No se encontró el precio con ID: " + id;
        }

        try {
            String result = dPrecioCurso.updatePrecio(id, nuevoPrecio);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Desactivar precio
     */
    public String delete(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el precio existe
        if (dPrecioCurso.findOneById(id) == null) {
            return "Error: No se encontró el precio con ID: " + id;
        }

        try {
            String result = dPrecioCurso.delete(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Reactivar precio
     */
    public String reactivate(int id) {
        if (id <= 0) {
            return "Error: El ID debe ser mayor a 0";
        }

        // Verificar que el precio existe
        if (dPrecioCurso.findOneById(id) == null) {
            return "Error: No se encontró el precio con ID: " + id;
        }

        try {
            String result = dPrecioCurso.reactivate(id);
            return result;
        } catch (Exception e) {
            return "Error en la capa de negocio: " + e.getMessage();
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Listar todos los precios activos
     */
    public List<String[]> findAll() {
        try {
            List<String[]> result = dPrecioCurso.findAll();
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Buscar precio por ID
     */
    public String[] findOneById(int id) {
        if (id <= 0) {
            System.out.println("Error: El ID debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPrecioCurso.findOneById(id);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Obtener precios de un curso específico ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dPrecioCurso.findByCurso(cursoId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Buscar precios por tipo de participante
     */
    public List<String[]> findByTipo(int tipoParticipanteId) {
        if (tipoParticipanteId <= 0) {
            System.out.println("Error: El ID del tipo de participante debe ser mayor a 0");
            return null;
        }

        try {
            List<String[]> result = dPrecioCurso.findByTipo(tipoParticipanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    /**
     * Obtener precio específico para un curso y tipo de participante ⭐ ENDPOINT PRINCIPAL
     * Para calcular costo exacto de inscripción
     */
    public String[] findEspecifico(int cursoId, int tipoParticipanteId) {
        if (cursoId <= 0) {
            System.out.println("Error: El ID del curso debe ser mayor a 0");
            return null;
        }

        if (tipoParticipanteId <= 0) {
            System.out.println("Error: El ID del tipo de participante debe ser mayor a 0");
            return null;
        }

        try {
            String[] result = dPrecioCurso.findEspecifico(cursoId, tipoParticipanteId);
            return result;
        } catch (Exception e) {
            System.out.println("Error en la capa de negocio: " + e.getMessage());
            return null;
        } finally {
            dPrecioCurso.disconnect();
        }
    }

    // Métodos auxiliares de validación

    /**
     * Verificar si existe el curso y está activo
     */
    private boolean existsCurso(int cursoId) {
        try {
            String[] curso = dCurso.findOneById(cursoId);
            return curso != null && curso[11].equals("true"); // activo = true
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si existe el tipo de participante y está activo
     */
    private boolean existsTipoParticipante(int tipoParticipanteId) {
        try {
            String[] tipo = dTipoParticipante.findOneById(tipoParticipanteId);
            return tipo != null && tipo[3].equals("true"); // activo = true
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar si ya existe un precio específico para la combinación curso-tipo
     */
    private boolean existsPrecioEspecifico(int cursoId, int tipoParticipanteId) {
        try {
            String[] precio = dPrecioCurso.findEspecifico(cursoId, tipoParticipanteId);
            return precio != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verificar conflicto de precio específico excluyendo un ID
     */
    private boolean existsPrecioEspecificoExcluding(int excludeId, int cursoId, int tipoParticipanteId) {
        try {
            String[] precio = dPrecioCurso.findEspecifico(cursoId, tipoParticipanteId);
            if (precio == null) {
                return false;
            }

            int precioId = Integer.parseInt(precio[0]);
            return precioId != excludeId; // Existe otro precio diferente al que estamos actualizando
        } catch (Exception e) {
            return false;
        }
    }

    public void disconnect() {
        if (dPrecioCurso != null) {
            dPrecioCurso.disconnect();
        }
        if (dCurso != null) {
            dCurso.disconnect();
        }
        if (dTipoParticipante != null) {
            dTipoParticipante.disconnect();
        }
    }
}