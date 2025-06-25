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

    // CU2 - MÉTODOS PARA GESTIÓN DE PERÍODOS ACADÉMICOS

    /**
     * Crear nueva gestión académica (período/semestre)
     *
     * @param nombre      Nombre del período (ej: "2025-1", "Semestre I-2025")
     * @param descripcion Información adicional del período
     * @param fechaInicio Fecha de inicio del período académico
     * @param fechaFin    Fecha de finalización del período académico
     * @return Mensaje de resultado
     */
    public String save(String nombre, String descripcion, Date fechaInicio, Date fechaFin) {
        String query = "INSERT INTO GESTION (nombre, descripcion, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);
            ps.setBoolean(5, true); // Gestión activa por defecto

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Gestión creada exitosamente" : "Error: No se pudo crear la gestión";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar gestión académica existente
     *
     * @param id          ID de la gestión
     * @param nombre      Nuevo nombre del período
     * @param descripcion Nueva descripción
     * @param fechaInicio Nueva fecha de inicio
     * @param fechaFin    Nueva fecha de fin
     * @return Mensaje de resultado
     */
    public String update(int id, String nombre, String descripcion, Date fechaInicio, Date fechaFin) {
        String query = "UPDATE GESTION SET nombre = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);
            ps.setInt(5, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Gestión actualizada exitosamente" : "Error: No se pudo actualizar la gestión";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar gestión (soft delete)
     *
     * @param id ID de la gestión
     * @return Mensaje de resultado
     */
    public String delete(int id) {
        String query = "UPDATE GESTION SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Gestión desactivada exitosamente" : "Error: No se pudo desactivar la gestión";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Reactivar gestión desactivada
     *
     * @param id ID de la gestión
     * @return Mensaje de resultado
     */
    public String reactivate(int id) {
        String query = "UPDATE GESTION SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Gestión reactivada exitosamente" : "Error: No se pudo reactivar la gestión";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todas las gestiones activas
     *
     * @return Lista de gestiones
     */
    public List<String[]> findAll() {
        String query = "SELECT id, nombre, descripcion, fecha_inicio, fecha_fin, activo FROM GESTION WHERE activo = true ORDER BY fecha_inicio DESC";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("nombre");
                gestion[2] = rs.getString("descripcion");
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = String.valueOf(rs.getBoolean("activo"));
                gestiones.add(gestion);

                System.out.println("Gestión: ID=" + gestion[0] +
                        ", Nombre=" + gestion[1] +
                        ", Inicio=" + gestion[3] +
                        ", Fin=" + gestion[4]);
            }

            rs.close();
            ps.close();

            if (gestiones.isEmpty()) {
                System.out.println("No se encontraron gestiones activas");
            } else {
                System.out.println("Total gestiones encontradas: " + gestiones.size());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Buscar gestión por ID
     *
     * @param id ID de la gestión
     * @return Array con datos de la gestión o null si no existe
     */
    public String[] findOneById(int id) {
        String query = "SELECT id, nombre, descripcion, fecha_inicio, fecha_fin, activo FROM GESTION WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("nombre");
                gestion[2] = rs.getString("descripcion");
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Gestión encontrada: " + gestion[1]);
                rs.close();
                ps.close();
                return gestion;
            }

            rs.close();
            ps.close();
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtener gestiones en un rango de fechas
     *
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin    Fecha de fin del rango
     * @return Lista de gestiones en el rango
     */
    public List<String[]> findByDateRange(Date fechaInicio, Date fechaFin) {
        String query = "SELECT id, nombre, descripcion, fecha_inicio, fecha_fin, activo FROM GESTION " +
                "WHERE activo = true AND fecha_inicio >= ? AND fecha_fin <= ? ORDER BY fecha_inicio";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("nombre");
                gestion[2] = rs.getString("descripcion");
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = String.valueOf(rs.getBoolean("activo"));
                gestiones.add(gestion);
            }

            rs.close();
            ps.close();
            System.out.println("Gestiones en rango: " + gestiones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Obtener gestiones activas en las fechas actuales (función findCurrent)
     *
     * @return Lista de gestiones vigentes
     */
    public List<String[]> findCurrent() {
        String query = "SELECT id, nombre, descripcion, fecha_inicio, fecha_fin, activo FROM GESTION " +
                "WHERE activo = true AND CURRENT_DATE BETWEEN fecha_inicio AND fecha_fin ORDER BY fecha_inicio";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("nombre");
                gestion[2] = rs.getString("descripcion");
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = String.valueOf(rs.getBoolean("activo"));
                gestiones.add(gestion);
            }

            rs.close();
            ps.close();
            System.out.println("Gestiones actuales vigentes: " + gestiones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return gestiones;
    }

    /**
     * Buscar gestiones por nombre (búsqueda parcial)
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de gestiones que coinciden
     */
    public List<String[]> findByName(String nombre) {
        String query = "SELECT id, nombre, descripcion, fecha_inicio, fecha_fin, activo FROM GESTION " +
                "WHERE activo = true AND LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";
        List<String[]> gestiones = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] gestion = new String[6];
                gestion[0] = String.valueOf(rs.getInt("id"));
                gestion[1] = rs.getString("nombre");
                gestion[2] = rs.getString("descripcion");
                gestion[3] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                gestion[4] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                gestion[5] = String.valueOf(rs.getBoolean("activo"));
                gestiones.add(gestion);
            }

            rs.close();
            ps.close();
            System.out.println("Gestiones encontradas con nombre '" + nombre + "': " + gestiones.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return gestiones;
    }
}