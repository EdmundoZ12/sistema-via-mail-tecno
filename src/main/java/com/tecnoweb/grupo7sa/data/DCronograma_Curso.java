package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DCronograma_Curso {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DCronograma_Curso() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    /**
     * Crear un nuevo cronograma de curso para un período académico
     * @param cursoGestionId ID de la relación curso-gestión
     * @param descripcion Descripción del cronograma (ej: "Cronograma Matemáticas I - Semestre II-2024")
     * @param fase Fase del curso (ej: "Inscripciones", "Clases", "Exámenes")
     * @param fechaInicio Fecha de inicio de la fase
     * @param fechaFin Fecha de finalización de la fase
     * @return Mensaje de resultado
     */
    public String save(int cursoGestionId, String descripcion, String fase, Date fechaInicio, Date fechaFin) {
        String query = "INSERT INTO cronograma_curso (curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoGestionId);
            ps.setString(2, descripcion);
            ps.setString(3, fase);
            ps.setDate(4, fechaInicio);
            ps.setDate(5, fechaFin);
            ps.setBoolean(6, true); // Cronograma activo por defecto

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Cronograma de curso creado exitosamente";
            } else {
                return "Error: No se pudo crear el cronograma de curso";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar un cronograma de curso existente
     * @param id ID del cronograma
     * @param cursoGestionId ID de la relación curso-gestión
     * @param descripcion Nueva descripción
     * @param fase Nueva fase
     * @param fechaInicio Nueva fecha de inicio
     * @param fechaFin Nueva fecha de finalización
     * @return Mensaje de resultado
     */
    public String update(int id, int cursoGestionId, String descripcion, String fase, Date fechaInicio, Date fechaFin) {
        String query = "UPDATE cronograma_curso SET curso_gestion_id = ?, descripcion = ?, fase = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoGestionId);
            ps.setString(2, descripcion);
            ps.setString(3, fase);
            ps.setDate(4, fechaInicio);
            ps.setDate(5, fechaFin);
            ps.setInt(6, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Cronograma de curso actualizado exitosamente";
            } else {
                return "Error: No se pudo actualizar el cronograma de curso";
            }

        } catch (SQLException e) {
            return "Error en el Sistema: " + e.getMessage();
        }
    }

    /**
     * Desactivar un cronograma de curso (soft delete)
     * @param id ID del cronograma
     * @return Mensaje de resultado
     */
    public String delete(int id) {
        String query = "UPDATE cronograma_curso SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Cronograma de curso desactivado exitosamente";
            } else {
                return "Error: No se pudo desactivar el cronograma de curso";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Obtener todos los cronogramas de cursos activos
     * @return Lista de cronogramas
     */
    public List<String[]> findAllCronogramas() {
        String query = "SELECT id, curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo FROM cronograma_curso WHERE activo = true ORDER BY fecha_inicio";
        List<String[]> cronogramas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] cronograma = new String[7];
                cronograma[0] = String.valueOf(rs.getInt("id"));
                cronograma[1] = String.valueOf(rs.getInt("curso_gestion_id"));
                cronograma[2] = rs.getString("descripcion");
                cronograma[3] = rs.getString("fase");
                cronograma[4] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                cronograma[5] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                cronograma[6] = String.valueOf(rs.getBoolean("activo"));
                cronogramas.add(cronograma);

                // Mostrar cada cronograma en consola
                System.out.println("Cronograma: ID=" + cronograma[0] +
                        ", Curso-Gestión ID=" + cronograma[1] +
                        ", Descripción=" + cronograma[2] +
                        ", Fase=" + cronograma[3] +
                        ", Fecha Inicio=" + cronograma[4] +
                        ", Fecha Fin=" + cronograma[5] +
                        ", Activo=" + (cronograma[6].equals("true") ? "Sí" : "No"));
            }

            rs.close();
            ps.close();

            if (cronogramas.isEmpty()) {
                System.out.println("No se encontraron cronogramas de cursos activos en el sistema");
            } else {
                System.out.println("Total cronogramas encontrados: " + cronogramas.size());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return cronogramas;
    }

    /**
     * Obtener un cronograma específico por ID
     * @param id ID del cronograma
     * @return Array con datos del cronograma o null si no existe
     */
    public String[] findOneById(int id) {
        String query = "SELECT id, curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo FROM cronograma_curso WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] cronograma = new String[7];
                cronograma[0] = String.valueOf(rs.getInt("id"));
                cronograma[1] = String.valueOf(rs.getInt("curso_gestion_id"));
                cronograma[2] = rs.getString("descripcion");
                cronograma[3] = rs.getString("fase");
                cronograma[4] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                cronograma[5] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                cronograma[6] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Cronograma encontrado por ID: ID=" + cronograma[0] +
                        ", Curso-Gestión ID=" + cronograma[1] +
                        ", Descripción=" + cronograma[2] +
                        ", Fase=" + cronograma[3] +
                        ", Fecha Inicio=" + cronograma[4] +
                        ", Fecha Fin=" + cronograma[5] +
                        ", Activo=" + (cronograma[6].equals("true") ? "Sí" : "No"));

                rs.close();
                ps.close();

                return cronograma;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró el cronograma con ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cronograma por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtener cronogramas por curso-gestión específico
     * @param cursoGestionId ID de la relación curso-gestión
     * @return Lista de cronogramas para ese curso en esa gestión
     */
    public List<String[]> findByCursoGestion(int cursoGestionId) {
        String query = "SELECT id, curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo FROM cronograma_curso WHERE curso_gestion_id = ? AND activo = true ORDER BY fecha_inicio";
        List<String[]> cronogramas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoGestionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] cronograma = new String[7];
                cronograma[0] = String.valueOf(rs.getInt("id"));
                cronograma[1] = String.valueOf(rs.getInt("curso_gestion_id"));
                cronograma[2] = rs.getString("descripcion");
                cronograma[3] = rs.getString("fase");
                cronograma[4] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                cronograma[5] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                cronograma[6] = String.valueOf(rs.getBoolean("activo"));
                cronogramas.add(cronograma);
            }

            rs.close();
            ps.close();

            System.out.println("Cronogramas encontrados para curso-gestión " + cursoGestionId + ": " + cronogramas.size());

        } catch (SQLException e) {
            System.out.println("Error al buscar cronogramas por curso-gestión: " + e.getMessage());
        }

        return cronogramas;
    }

    /**
     * Obtener cronogramas por fase específica
     * @param fase Fase a buscar (ej: "Inscripciones", "Clases", "Exámenes")
     * @return Lista de cronogramas en esa fase
     */
    public List<String[]> findByFase(String fase) {
        String query = "SELECT id, curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo FROM cronograma_curso WHERE fase = ? AND activo = true ORDER BY fecha_inicio";
        List<String[]> cronogramas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, fase);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] cronograma = new String[7];
                cronograma[0] = String.valueOf(rs.getInt("id"));
                cronograma[1] = String.valueOf(rs.getInt("curso_gestion_id"));
                cronograma[2] = rs.getString("descripcion");
                cronograma[3] = rs.getString("fase");
                cronograma[4] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                cronograma[5] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                cronograma[6] = String.valueOf(rs.getBoolean("activo"));
                cronogramas.add(cronograma);
            }

            rs.close();
            ps.close();

            System.out.println("Cronogramas encontrados en fase '" + fase + "': " + cronogramas.size());

        } catch (SQLException e) {
            System.out.println("Error al buscar cronogramas por fase: " + e.getMessage());
        }

        return cronogramas;
    }

    /**
     * Obtener cronogramas por rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de cronogramas en el rango de fechas
     */
    public List<String[]> findByFechaRange(Date fechaInicio, Date fechaFin) {
        String query = "SELECT id, curso_gestion_id, descripcion, fase, fecha_inicio, fecha_fin, activo FROM cronograma_curso " +
                "WHERE activo = true AND fecha_inicio >= ? AND fecha_fin <= ? ORDER BY fecha_inicio";
        List<String[]> cronogramas = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] cronograma = new String[7];
                cronograma[0] = String.valueOf(rs.getInt("id"));
                cronograma[1] = String.valueOf(rs.getInt("curso_gestion_id"));
                cronograma[2] = rs.getString("descripcion");
                cronograma[3] = rs.getString("fase");
                cronograma[4] = rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "";
                cronograma[5] = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : "";
                cronograma[6] = String.valueOf(rs.getBoolean("activo"));
                cronogramas.add(cronograma);
            }

            rs.close();
            ps.close();

            System.out.println("Cronogramas encontrados en el rango de fechas: " + cronogramas.size());

        } catch (SQLException e) {
            System.out.println("Error al buscar cronogramas por rango de fechas: " + e.getMessage());
        }

        return cronogramas;
    }

    /**
     * Reactivar un cronograma desactivado
     * @param id ID del cronograma
     * @return Mensaje de resultado
     */
    public String reactivate(int id) {
        String query = "UPDATE cronograma_curso SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Cronograma de curso reactivado exitosamente";
            } else {
                return "Error: No se pudo reactivar el cronograma de curso";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}