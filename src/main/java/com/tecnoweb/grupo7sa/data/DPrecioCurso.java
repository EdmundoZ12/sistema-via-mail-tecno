package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DPrecioCurso {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DPrecioCurso() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(),
                configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    // CU3 - MÉTODOS PARA GESTIÓN DE PRECIOS DE CURSOS

    /**
     * Crear precio diferenciado para un curso
     *
     * @param cursoId            ID del curso
     * @param tipoParticipanteId ID del tipo de participante
     * @param precio             Precio para este tipo de participante
     * @return Mensaje de resultado
     */
    public String save(int cursoId, int tipoParticipanteId, double precio) {
        String query = "INSERT INTO PRECIO_CURSO (curso_id, tipo_participante_id, precio, activo) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ps.setInt(2, tipoParticipanteId);
            ps.setDouble(3, precio);
            ps.setBoolean(4, true);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Precio creado exitosamente" : "Error: No se pudo crear el precio";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar precio existente
     */
    public String update(int id, int cursoId, int tipoParticipanteId, double precio) {
        String query = "UPDATE PRECIO_CURSO SET curso_id = ?, tipo_participante_id = ?, precio = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ps.setInt(2, tipoParticipanteId);
            ps.setDouble(3, precio);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Precio actualizado exitosamente" : "Error: No se pudo actualizar el precio";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Actualizar solo el monto del precio ⭐ FUNCIONALIDAD ESPECIAL
     */
    public String updatePrecio(int id, double nuevoPrecio) {
        String query = "UPDATE PRECIO_CURSO SET precio = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setDouble(1, nuevoPrecio);
            ps.setInt(2, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Precio actualizado exitosamente" : "Error: No se pudo actualizar el precio";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Desactivar precio (soft delete)
     */
    public String delete(int id) {
        String query = "UPDATE PRECIO_CURSO SET activo = false WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Precio desactivado exitosamente" : "Error: No se pudo desactivar el precio";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Reactivar precio
     */
    public String reactivate(int id) {
        String query = "UPDATE PRECIO_CURSO SET activo = true WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0 ? "Precio reactivado exitosamente" : "Error: No se pudo reactivar el precio";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Listar todos los precios activos
     */
    public List<String[]> findAll() {
        String query = "SELECT pc.id, pc.curso_id, pc.tipo_participante_id, pc.precio, pc.activo, " +
                "c.nombre AS curso_nombre, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PRECIO_CURSO pc " +
                "JOIN CURSO c ON pc.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON pc.tipo_participante_id = tp.id " +
                "WHERE pc.activo = true ORDER BY c.nombre, tp.codigo";

        List<String[]> precios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] precio = new String[8];
                precio[0] = String.valueOf(rs.getInt("id"));
                precio[1] = String.valueOf(rs.getInt("curso_id"));
                precio[2] = String.valueOf(rs.getInt("tipo_participante_id"));
                precio[3] = String.valueOf(rs.getDouble("precio"));
                precio[4] = String.valueOf(rs.getBoolean("activo"));
                precio[5] = rs.getString("curso_nombre");
                precio[6] = rs.getString("tipo_codigo");
                precio[7] = rs.getString("tipo_descripcion");
                precios.add(precio);

                System.out.println("Precio: " + precio[5] + " - " + precio[6] + " - $" + precio[3]);
            }

            rs.close();
            ps.close();
            System.out.println("Total precios activos: " + precios.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return precios;
    }

    /**
     * Buscar precio por ID
     */
    public String[] findOneById(int id) {
        String query = "SELECT pc.id, pc.curso_id, pc.tipo_participante_id, pc.precio, pc.activo, " +
                "c.nombre AS curso_nombre, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PRECIO_CURSO pc " +
                "JOIN CURSO c ON pc.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON pc.tipo_participante_id = tp.id " +
                "WHERE pc.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] precio = new String[8];
                precio[0] = String.valueOf(rs.getInt("id"));
                precio[1] = String.valueOf(rs.getInt("curso_id"));
                precio[2] = String.valueOf(rs.getInt("tipo_participante_id"));
                precio[3] = String.valueOf(rs.getDouble("precio"));
                precio[4] = String.valueOf(rs.getBoolean("activo"));
                precio[5] = rs.getString("curso_nombre");
                precio[6] = rs.getString("tipo_codigo");
                precio[7] = rs.getString("tipo_descripcion");

                rs.close();
                ps.close();
                return precio;
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
     * Obtener precios de un curso específico ⭐ ENDPOINT PRINCIPAL
     */
    public List<String[]> findByCurso(int cursoId) {
        String query = "SELECT pc.id, pc.curso_id, pc.tipo_participante_id, pc.precio, pc.activo, " +
                "c.nombre AS curso_nombre, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PRECIO_CURSO pc " +
                "JOIN CURSO c ON pc.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON pc.tipo_participante_id = tp.id " +
                "WHERE pc.curso_id = ? AND pc.activo = true ORDER BY tp.codigo";

        List<String[]> precios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] precio = new String[8];
                precio[0] = String.valueOf(rs.getInt("id"));
                precio[1] = String.valueOf(rs.getInt("curso_id"));
                precio[2] = String.valueOf(rs.getInt("tipo_participante_id"));
                precio[3] = String.valueOf(rs.getDouble("precio"));
                precio[4] = String.valueOf(rs.getBoolean("activo"));
                precio[5] = rs.getString("curso_nombre");
                precio[6] = rs.getString("tipo_codigo");
                precio[7] = rs.getString("tipo_descripcion");
                precios.add(precio);

                System.out.println("Precio curso " + cursoId + ": " + precio[6] + " - $" + precio[3]);
            }

            rs.close();
            ps.close();
            System.out.println("Precios para curso " + cursoId + ": " + precios.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return precios;
    }

    /**
     * Buscar precios por tipo de participante
     */
    public List<String[]> findByTipo(int tipoParticipanteId) {
        String query = "SELECT pc.id, pc.curso_id, pc.tipo_participante_id, pc.precio, pc.activo, " +
                "c.nombre AS curso_nombre, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PRECIO_CURSO pc " +
                "JOIN CURSO c ON pc.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON pc.tipo_participante_id = tp.id " +
                "WHERE pc.tipo_participante_id = ? AND pc.activo = true ORDER BY c.nombre";

        List<String[]> precios = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tipoParticipanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] precio = new String[8];
                precio[0] = String.valueOf(rs.getInt("id"));
                precio[1] = String.valueOf(rs.getInt("curso_id"));
                precio[2] = String.valueOf(rs.getInt("tipo_participante_id"));
                precio[3] = String.valueOf(rs.getDouble("precio"));
                precio[4] = String.valueOf(rs.getBoolean("activo"));
                precio[5] = rs.getString("curso_nombre");
                precio[6] = rs.getString("tipo_codigo");
                precio[7] = rs.getString("tipo_descripcion");
                precios.add(precio);
            }

            rs.close();
            ps.close();
            System.out.println("Precios para tipo " + tipoParticipanteId + ": " + precios.size());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return precios;
    }

    /**
     * Obtener precio específico para un curso y tipo de participante ⭐ ENDPOINT PRINCIPAL
     * Para calcular costo exacto de inscripción
     */
    public String[] findEspecifico(int cursoId, int tipoParticipanteId) {
        String query = "SELECT pc.id, pc.curso_id, pc.tipo_participante_id, pc.precio, pc.activo, " +
                "c.nombre AS curso_nombre, tp.codigo AS tipo_codigo, tp.descripcion AS tipo_descripcion " +
                "FROM PRECIO_CURSO pc " +
                "JOIN CURSO c ON pc.curso_id = c.id " +
                "JOIN TIPO_PARTICIPANTE tp ON pc.tipo_participante_id = tp.id " +
                "WHERE pc.curso_id = ? AND pc.tipo_participante_id = ? AND pc.activo = true";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, cursoId);
            ps.setInt(2, tipoParticipanteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] precio = new String[8];
                precio[0] = String.valueOf(rs.getInt("id"));
                precio[1] = String.valueOf(rs.getInt("curso_id"));
                precio[2] = String.valueOf(rs.getInt("tipo_participante_id"));
                precio[3] = String.valueOf(rs.getDouble("precio"));
                precio[4] = String.valueOf(rs.getBoolean("activo"));
                precio[5] = rs.getString("curso_nombre");
                precio[6] = rs.getString("tipo_codigo");
                precio[7] = rs.getString("tipo_descripcion");

                System.out.println("Precio específico encontrado: " + precio[5] + " - " + precio[6] + " - $" + precio[3]);

                rs.close();
                ps.close();
                return precio;
            }

            rs.close();
            ps.close();
            System.out.println("No se encontró precio específico para curso " + cursoId + " y tipo " + tipoParticipanteId);
            return null;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}