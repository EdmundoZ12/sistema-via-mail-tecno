package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DCurso {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DCurso() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(), configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

 public String save(String nombre, String descripcion, String logoUrl, int duracionHoras, String modalidad, String nivel, String requisitos) {
    String query = "INSERT INTO curso (nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try {
        PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setString(3, logoUrl);
        ps.setInt(4, duracionHoras);
        ps.setString(5, modalidad);
        ps.setString(6, nivel);
        ps.setString(7, requisitos);

        int result = ps.executeUpdate();
        ps.close();

        if (result > 0) {
            return "Curso creado exitosamente";
        } else {
            return "Error: No se pudo crear el curso";
        }

    } catch (SQLException e) {
        return "Error: " + e.getMessage();
    }
}

   public String update(int id, String nombre, String descripcion, String logoUrl, int duracionHoras, String modalidad, String nivel, String requisitos, boolean activo) {
    String query = "UPDATE curso SET nombre = ?, descripcion = ?, logo_url = ?, duracion_horas = ?, modalidad = ?, nivel = ?, requisitos = ?, activo = ? WHERE id = ?";

    try {
        PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setString(3, logoUrl);
        ps.setInt(4, duracionHoras);
        ps.setString(5, modalidad);
        ps.setString(6, nivel);
        ps.setString(7, requisitos);
        ps.setBoolean(8, activo);
        ps.setInt(9, id);

        int result = ps.executeUpdate();
        ps.close();

        if (result > 0) {
            return "Curso actualizado exitosamente";
        } else {
            return "Error: No se pudo actualizar el curso";
        }

    } catch (SQLException e) {
        return "Error: " + e.getMessage();
    }
}

    public String delete(int id) {
        String query = "UPDATE curso SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Curso eliminado exitosamente";
            } else {
                return "Error: No se pudo eliminar el curso";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<String[]> findAllCursos() {
    String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos FROM curso WHERE activo = true ORDER BY nombre";
    List<String[]> cursos = new ArrayList<>();

    try {
        PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String[] curso = new String[8]; // Solo 8 campos
            curso[0] = String.valueOf(rs.getInt("id"));
            curso[1] = rs.getString("nombre");
            curso[2] = rs.getString("descripcion");
            curso[3] = rs.getString("logo_url");
            curso[4] = String.valueOf(rs.getInt("duracion_horas"));
            curso[5] = rs.getString("modalidad");
            curso[6] = rs.getString("nivel");
            curso[7] = rs.getString("requisitos");
            cursos.add(curso);
        }

        rs.close();
        ps.close();

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    return cursos;
}

   public String[] findOneById(int id) {
    String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos, activo FROM curso WHERE id = ? AND activo = true";

    try {
        PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String[] curso = new String[9];
            curso[0] = String.valueOf(rs.getInt("id"));
            curso[1] = rs.getString("nombre");
            curso[2] = rs.getString("descripcion");
            curso[3] = rs.getString("logo_url");
            curso[4] = String.valueOf(rs.getInt("duracion_horas"));
            curso[5] = rs.getString("modalidad");
            curso[6] = rs.getString("nivel");
            curso[7] = rs.getString("requisitos");
            curso[8] = String.valueOf(rs.getBoolean("activo")); // Siempre será "true"

            System.out.println("Curso encontrado: ID=" + curso[0] +
                    ", Nombre=" + curso[1] +
                    ", Duración=" + curso[4] + " horas" +
                    ", Modalidad=" + curso[5] +
                    ", Nivel=" + curso[6] +
                    ", Activo=" + curso[8]);

            rs.close();
            ps.close();
            return curso;
        } else {
            rs.close();
            ps.close();
            System.out.println("No se encontró un curso activo con ID: " + id);
            return null;
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar curso por ID: " + e.getMessage());
        return null;
    }
}
    public String[] findOneByNombre(String nombre) {
        String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos, activo FROM curso WHERE nombre = ? AND activo = true";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] curso = new String[9];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = rs.getString("logo_url");
                curso[4] = String.valueOf(rs.getInt("duracion_horas"));
                curso[5] = rs.getString("modalidad");
                curso[6] = rs.getString("nivel");
                curso[7] = rs.getString("requisitos");
                curso[8] = String.valueOf(rs.getBoolean("activo"));

                System.out.println("Curso encontrado por nombre: ID=" + curso[0] +
                        ", Nombre=" + curso[1] +
                        ", Duración=" + curso[4] + " horas" +
                        ", Modalidad=" + curso[5] +
                        ", Nivel=" + curso[6]);

                rs.close();
                ps.close();
                return curso;
            } else {
                rs.close();
                ps.close();
                System.out.println("No se encontró el curso con nombre: " + nombre);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar curso por nombre: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> findByModalidad(String modalidad) {
        String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos FROM curso WHERE modalidad = ? AND activo = true ORDER BY nombre";
        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, modalidad);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[8];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = rs.getString("logo_url");
                curso[4] = String.valueOf(rs.getInt("duracion_horas"));
                curso[5] = rs.getString("modalidad");
                curso[6] = rs.getString("nivel");
                curso[7] = rs.getString("requisitos");
                cursos.add(curso);

                System.out.println("Curso " + modalidad + ": ID=" + curso[0] +
                        ", Nombre=" + curso[1] +
                        ", Duración=" + curso[4] + " horas" +
                        ", Nivel=" + curso[6]);
            }

            rs.close();
            ps.close();

            if (cursos.isEmpty()) {
                System.out.println("No se encontraron cursos con modalidad: " + modalidad);
            } else {
                System.out.println("Total cursos " + modalidad + " encontrados: " + cursos.size());
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cursos por modalidad: " + e.getMessage());
        }

        return cursos;
    }

    public List<String[]> findByNivel(String nivel) {
        String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos FROM curso WHERE nivel = ? AND activo = true ORDER BY nombre";
        List<String[]> cursos = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nivel);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] curso = new String[8];
                curso[0] = String.valueOf(rs.getInt("id"));
                curso[1] = rs.getString("nombre");
                curso[2] = rs.getString("descripcion");
                curso[3] = rs.getString("logo_url");
                curso[4] = String.valueOf(rs.getInt("duracion_horas"));
                curso[5] = rs.getString("modalidad");
                curso[6] = rs.getString("nivel");
                curso[7] = rs.getString("requisitos");
                cursos.add(curso);

                System.out.println("Curso " + nivel + ": ID=" + curso[0] +
                        ", Nombre=" + curso[1] +
                        ", Duración=" + curso[4] + " horas" +
                        ", Modalidad=" + curso[5]);
            }

            rs.close();
            ps.close();

            if (cursos.isEmpty()) {
                System.out.println("No se encontraron cursos de nivel: " + nivel);
            } else {
                System.out.println("Total cursos nivel " + nivel + " encontrados: " + cursos.size());
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cursos por nivel: " + e.getMessage());
        }

        return cursos;
    }

    public List<String[]> findByDuracion(int duracionMinima, int duracionMaxima) {
    String query = "SELECT id, nombre, descripcion, logo_url, duracion_horas, modalidad, nivel, requisitos FROM curso WHERE duracion_horas BETWEEN ? AND ? AND activo = true ORDER BY duracion_horas";
    List<String[]> cursos = new ArrayList<>();

    try {
        PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
        ps.setInt(1, duracionMinima);
        ps.setInt(2, duracionMaxima);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String[] curso = new String[8];
            curso[0] = String.valueOf(rs.getInt("id"));
            curso[1] = rs.getString("nombre");
            curso[2] = rs.getString("descripcion");
            curso[3] = rs.getString("logo_url");
            curso[4] = String.valueOf(rs.getInt("duracion_horas"));
            curso[5] = rs.getString("modalidad");
            curso[6] = rs.getString("nivel");
            curso[7] = rs.getString("requisitos");
            cursos.add(curso);

            System.out.println("Curso: " + curso[1] +
                    ", Duración=" + curso[4] + " horas" +
                    ", Modalidad=" + curso[5] +
                    ", Nivel=" + curso[6]);
        }

        rs.close();
        ps.close();

        if (cursos.isEmpty()) {
            System.out.println("No se encontraron cursos con duración entre " + duracionMinima + " y " + duracionMaxima + " horas");
        } else {
            System.out.println("Total cursos encontrados: " + cursos.size() + " (entre " + duracionMinima + "-" + duracionMaxima + " horas)");
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar cursos por duración: " + e.getMessage());
    }

    return cursos;
}
}