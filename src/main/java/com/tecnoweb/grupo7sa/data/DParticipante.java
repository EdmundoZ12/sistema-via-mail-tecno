package com.tecnoweb.grupo7sa.data;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DParticipante {

    private final DatabaseConection databaseConection;
    ConfigDB configDB = new ConfigDB();

    public DParticipante() {
        this.databaseConection = new DatabaseConection(configDB.getUser(), configDB.getPassword(), configDB.getHost(), configDB.getPort(), configDB.getDbName());
    }

    public void disconnect() {
        if (databaseConection != null) {
            databaseConection.closeConnection();
        }
    }

    public String save(String nombre, String apellido, String email, String carnet, String telefono, String carrera, String facultad, String universidad, int tipoParticipanteId) {
        String query = "INSERT INTO participante (nombre, apellido, email, carnet, telefono, carrera, facultad, universidad, tipo_participante_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, carnet);
            ps.setString(5, telefono);
            ps.setString(6, carrera);
            ps.setString(7, facultad);
            ps.setString(8, universidad);
            ps.setInt(9, tipoParticipanteId);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Participante creado exitosamente";
            } else {
                return "Error: No se pudo crear el participante";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String update(int id, String nombre, String apellido, String email, String carnet, String telefono, String carrera, String facultad, String universidad, int tipoParticipanteId) {
        String query = "UPDATE participante SET nombre = ?, apellido = ?, email = ?, carnet = ?, telefono = ?, carrera = ?, facultad = ?, universidad = ?, tipo_participante_id = ? WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, carnet);
            ps.setString(5, telefono);
            ps.setString(6, carrera);
            ps.setString(7, facultad);
            ps.setString(8, universidad);
            ps.setInt(9, tipoParticipanteId);
            ps.setInt(10, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Participante actualizado exitosamente";
            } else {
                return "Error: No se pudo actualizar el participante";
            }

        } catch (SQLException e) {
            return "Error: en el Sistema " + e.getMessage();
        }
    }

    public String delete(int id) {
        String query = "DELETE FROM participante WHERE id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                return "Participante eliminado exitosamente";
            } else {
                return "Error: No se pudo eliminar el participante";
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<String[]> findAllParticipantes() {
        String query = "SELECT p.id, p.nombre, p.apellido, p.email, p.carnet, p.telefono, p.carrera, p.facultad, p.universidad, p.tipo_participante_id, tp.nombre as tipo_nombre FROM participante p INNER JOIN tipo_participante tp ON p.tipo_participante_id = tp.id";
        List<String[]> participantes = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("nombre");
                participante[2] = rs.getString("apellido");
                participante[3] = rs.getString("email");
                participante[4] = rs.getString("carnet");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("carrera");
                participante[7] = rs.getString("facultad");
                participante[8] = rs.getString("universidad");
                participante[9] = String.valueOf(rs.getInt("tipo_participante_id"));
                participante[10] = rs.getString("tipo_nombre");
                participantes.add(participante);

                // Mostrar cada participante en consola
                System.out.println("Participante: ID=" + participante[0] +
                        ", Nombre=" + participante[1] +
                        ", Apellido=" + participante[2] +
                        ", Email=" + participante[3] +
                        ", Carnet=" + participante[4] +
                        ", Telefono=" + participante[5] +
                        ", Carrera=" + participante[6] +
                        ", Facultad=" + participante[7] +
                        ", Universidad=" + participante[8] +
                        ", Tipo=" + participante[10]);
            }

            rs.close();
            ps.close();

            if (participantes.isEmpty()) {
                System.out.println("No se encontraron participantes en el sistema");
            } else {
                System.out.println("Total participantes encontrados: " + participantes.size());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return participantes;
    }

    public String[] findOneById(int id) {
        String query = "SELECT p.id, p.nombre, p.apellido, p.email, p.carnet, p.telefono, p.carrera, p.facultad, p.universidad, p.tipo_participante_id, tp.nombre as tipo_nombre FROM participante p INNER JOIN tipo_participante tp ON p.tipo_participante_id = tp.id WHERE p.id = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("nombre");
                participante[2] = rs.getString("apellido");
                participante[3] = rs.getString("email");
                participante[4] = rs.getString("carnet");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("carrera");
                participante[7] = rs.getString("facultad");
                participante[8] = rs.getString("universidad");
                participante[9] = String.valueOf(rs.getInt("tipo_participante_id"));
                participante[10] = rs.getString("tipo_nombre");

                System.out.println("Participante encontrado por ID: ID=" + participante[0] +
                        ", Nombre=" + participante[1] +
                        ", Apellido=" + participante[2] +
                        ", Email=" + participante[3] +
                        ", Carnet=" + participante[4] +
                        ", Telefono=" + participante[5] +
                        ", Carrera=" + participante[6] +
                        ", Facultad=" + participante[7] +
                        ", Universidad=" + participante[8] +
                        ", Tipo=" + participante[10]);

                rs.close();
                ps.close();

                return participante;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró el participante con ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar participante por ID: " + e.getMessage());
            return null;
        }
    }

    public String[] findOneByCarnet(String carnet) {
        String query = "SELECT p.id, p.nombre, p.apellido, p.email, p.carnet, p.telefono, p.carrera, p.facultad, p.universidad, p.tipo_participante_id, tp.nombre as tipo_nombre FROM participante p INNER JOIN tipo_participante tp ON p.tipo_participante_id = tp.id WHERE p.carnet = ?";

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("nombre");
                participante[2] = rs.getString("apellido");
                participante[3] = rs.getString("email");
                participante[4] = rs.getString("carnet");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("carrera");
                participante[7] = rs.getString("facultad");
                participante[8] = rs.getString("universidad");
                participante[9] = String.valueOf(rs.getInt("tipo_participante_id"));
                participante[10] = rs.getString("tipo_nombre");

                System.out.println("Participante encontrado por carnet: ID=" + participante[0] +
                        ", Nombre=" + participante[1] +
                        ", Apellido=" + participante[2] +
                        ", Email=" + participante[3] +
                        ", Carnet=" + participante[4] +
                        ", Telefono=" + participante[5] +
                        ", Carrera=" + participante[6] +
                        ", Facultad=" + participante[7] +
                        ", Universidad=" + participante[8] +
                        ", Tipo=" + participante[10]);

                rs.close();
                ps.close();

                return participante;
            } else {
                rs.close();
                ps.close();

                System.out.println("No se encontró el participante con carnet: " + carnet);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar participante por carnet: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> findByTipoParticipante(int tipoParticipanteId) {
        String query = "SELECT p.id, p.nombre, p.apellido, p.email, p.carnet, p.telefono, p.carrera, p.facultad, p.universidad, p.tipo_participante_id, tp.nombre as tipo_nombre FROM participante p INNER JOIN tipo_participante tp ON p.tipo_participante_id = tp.id WHERE p.tipo_participante_id = ?";
        List<String[]> participantes = new ArrayList<>();

        try {
            PreparedStatement ps = databaseConection.openConnection().prepareStatement(query);
            ps.setInt(1, tipoParticipanteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] participante = new String[11];
                participante[0] = String.valueOf(rs.getInt("id"));
                participante[1] = rs.getString("nombre");
                participante[2] = rs.getString("apellido");
                participante[3] = rs.getString("email");
                participante[4] = rs.getString("carnet");
                participante[5] = rs.getString("telefono");
                participante[6] = rs.getString("carrera");
                participante[7] = rs.getString("facultad");
                participante[8] = rs.getString("universidad");
                participante[9] = String.valueOf(rs.getInt("tipo_participante_id"));
                participante[10] = rs.getString("tipo_nombre");
                participantes.add(participante);

                System.out.println("Participante por tipo: ID=" + participante[0] +
                        ", Nombre=" + participante[1] +
                        ", Apellido=" + participante[2] +
                        ", Email=" + participante[3] +
                        ", Carnet=" + participante[4] +
                        ", Tipo=" + participante[10]);
            }

            rs.close();
            ps.close();

            if (participantes.isEmpty()) {
                System.out.println("No se encontraron participantes del tipo especificado");
            } else {
                System.out.println("Total participantes del tipo encontrados: " + participantes.size());
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar participantes por tipo: " + e.getMessage());
        }

        return participantes;
    }
}