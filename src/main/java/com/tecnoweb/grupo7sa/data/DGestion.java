package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DGestion {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DGestion() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    /**
     * Crear una nueva gestión académica (período/semestre)
     * @param descripcion Información adicional del período (ej: "Semestre con modalidad híbrida")
     * @param fechaInicio Fecha de inicio del período académico
     * @param fechaFin Fecha de finalización del período académico
     * @param nombre Nombre del período (ej: "Semestre I-2024", "Gestión 2024")
     * @return Mensaje de resultado
     */
    public String crearGestion(String descripcion, Date fechaInicio, Date fechaFin, String nombre) {
        String query = "INSERT INTO gestion (descripcion, estado, fecha_inicio, fecha_fin, nombre) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setBoolean(2, true); // Estado activo por defecto
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);
            ps.setString(5, nombre);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Gestión creada exitosamente";
            } else {
                return "Error: No se pudo crear la gestión";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar un período académico existente
     * @param id ID del período académico
     * @param descripcion Nueva información adicional
     * @param fechaInicio Nueva fecha de inicio del período
     * @param fechaFin Nueva fecha de finalización del período
     * @param nombre Nuevo nombre del período
     * @return Mensaje de resultado
     */
    public String actualizarGestion(int id, String descripcion, Date fechaInicio, Date fechaFin, String nombre) {
        String query = "UPDATE gestion SET descripcion = ?, fecha_inicio = ?, fecha_fin = ?, nombre = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setDate(2, fechaInicio);
            ps.setDate(3, fechaFin);
            ps.setString(4, nombre);
            ps.setInt(5, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Período académico actualizado exitosamente";
            } else {
                return "Error: No se pudo actualizar el período académico";
            }

        } catch (SQLException e) {
            return "Error en el Sistema: " + e.getMessage();
        }
    }

    /**
     * Desactivar un período académico (marcar como inactivo)
     * @param id ID del período académico
     * @return Mensaje de resultado
     */
    public String desactivarGestion(int id) {
        String query = "UPDATE gestion SET estado = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Período académico desactivado exitosamente";
            } else {
                return "Error: No se pudo desactivar el período académico";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Obtener todos los períodos académicos activos
     * @return Lista de períodos académicos
     */
    public List<String[]> obtenerGestiones() {
        String query = "SELECT id, descripcion, estado, fecha_inicio, fecha_fin, nombre FROM gestion WHERE estado = true ORDER BY fecha_inicio DESC";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("descripcion");
                gestion[2] = String.valueOf(rs.getBoolean("estado"));
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = rs.getString("nombre");
                gestiones.add(gestion);

                // Mostrar cada período académico en consola
                System.out.println("Período Académico: ID=" + gestion[0] +
                        ", Descripción=" + gestion[1] +
                        ", Estado=" + (gestion[2].equals("true") ? "Activo" : "Inactivo") +
                        ", Fecha Inicio=" + gestion[3] +
                        ", Fecha Fin=" + gestion[4] +
                        ", Nombre=" + gestion[5]);
            }

            rs.close();
            ps.close();

            if (gestiones.isEmpty()) {
                System.out.println("No se encontraron períodos académicos activos en el sistema");
            } else {
                System.out.println("Total períodos académicos encontrados: " + gestiones.size());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Obtener un período académico específico por ID
     * @param id ID del período académico
     * @return Array con datos del período o null si no existe
     */
    public String[] obtenerUnaGestion(int id) {
        String query = "SELECT id, descripcion, estado, fecha_inicio, fecha_fin, nombre FROM gestion WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("descripcion");
                gestion[2] = String.valueOf(rs.getBoolean("estado"));
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = rs.getString("nombre");

                System.out.println("Gestión encontrada por ID: ID=" + gestion[0] +
                        ", Descripción=" + gestion[1] +
                        ", Estado=" + gestion[2] +
                        ", Fecha Inicio=" + gestion[3] +
                        ", Fecha Fin=" + gestion[4] +
                        ", Nombre=" + gestion[5]);

                rs.close();
                ps.close();

                return gestion;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró la gestión con ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar gestión por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtener gestiones por rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de gestiones en el rango
     */
    public List<String[]> obtenerGestionesPorFecha(Date fechaInicio, Date fechaFin) {
        String query = "SELECT id, descripcion, estado, fecha_inicio, fecha_fin, nombre FROM gestion " +
                "WHERE estado = true AND fecha_inicio >= ? AND fecha_fin <= ? ORDER BY fecha_inicio";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("descripcion");
                gestion[2] = String.valueOf(rs.getBoolean("estado"));
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = rs.getString("nombre");
                gestiones.add(gestion);
            }

            rs.close();
            ps.close();

            System.out.println("Gestiones encontradas en el rango: " + gestiones.size());

        } catch (SQLException e) {
            System.out.println("Error al buscar gestiones por fecha: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Buscar gestiones por nombre (búsqueda parcial)
     * @param nombre Nombre a buscar
     * @return Lista de gestiones que coinciden
     */
    public List<String[]> buscarGestionesPorNombre(String nombre) {
        String query = "SELECT id, descripcion, estado, fecha_inicio, fecha_fin, nombre FROM gestion " +
                "WHERE estado = true AND nombre LIKE ? ORDER BY nombre";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("descripcion");
                gestion[2] = String.valueOf(rs.getBoolean("estado"));
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = rs.getString("nombre");
                gestiones.add(gestion);
            }

            rs.close();
            ps.close();

            System.out.println("Gestiones encontradas con nombre '" + nombre + "': " + gestiones.size());

        } catch (SQLException e) {
            System.out.println("Error al buscar gestiones por nombre: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Reactivar una gestión desactivada
     * @param id ID de la gestión
     * @return Mensaje de resultado
     */
    public String reactivarGestion(int id) {
        String query = "UPDATE gestion SET estado = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Gestión reactivada exitosamente";
            } else {
                return "Error: No se pudo reactivar la gestión";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}

