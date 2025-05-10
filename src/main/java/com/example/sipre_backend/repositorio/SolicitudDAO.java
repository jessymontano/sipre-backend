package com.example.sipre_backend.repositorio;

import com.example.sipre_backend.repositorio.db.MySQLConnection;
import com.example.sipre_backend.modelo.Solicitud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudDAO {

    public boolean agregarSolicitud(int folio, int idTipo, java.util.Date fechaSolicitud, String motivo, int idUsuario) {
        // Consulta para verificar si el folio existe en la tabla de documentos
        String queryBusquedaFolio = "SELECT COUNT(*) FROM sipre.documentos WHERE Folio = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmtBusqueda = connection.prepareStatement(queryBusquedaFolio)) {

            // Establecer el valor del folio en la consulta de búsqueda
            stmtBusqueda.setInt(1, folio);
            ResultSet rs = stmtBusqueda.executeQuery();

            // Verificar si el folio existe
            if (rs.next() && rs.getInt(1) > 0) {
                // El folio existe, primero verificamos el estatus actual
                String queryEstatus = "SELECT Estatus FROM sipre.documentos WHERE Folio = ?";
                try (PreparedStatement stmtEstatus = connection.prepareStatement(queryEstatus)) {
                    stmtEstatus.setInt(1, folio);
                    ResultSet rsEstatus = stmtEstatus.executeQuery();

                    if (rsEstatus.next()) {
                        String estatusActual = rsEstatus.getString("Estatus");
                        if ("Solicitado".equals(estatusActual)) {
                            System.out.println("El folio ya tiene el estatus 'Solicitado'.");
                            return false; // No se actualiza si ya está solicitado
                        }
                    }

                    // El folio no tiene el estatus 'Solicitado', ahora podemos insertar en la tabla solicitud
                    String queryInsert = "INSERT INTO sipre.solicitud (Folio, ID_Tipo, Fecha_Solicitud, Motivo, ID_Usuario) VALUES (?, ?, ?, ?, ?)";

                    try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
                        // Convertir java.util.Date a java.sql.Date si es necesario
                        java.sql.Date sqlDate = new java.sql.Date(fechaSolicitud.getTime());

                        stmtInsert.setInt(1, folio);
                        stmtInsert.setInt(2, idTipo);
                        stmtInsert.setDate(3, sqlDate);
                        stmtInsert.setString(4, motivo);
                        stmtInsert.setInt(5, idUsuario);

                        int filasInsertadas = stmtInsert.executeUpdate();
                        if (filasInsertadas > 0) {
                            // Si la inserción fue exitosa, actualizar el estatus del folio en documentos
                            String queryUpdateEstatus = "UPDATE sipre.documentos SET Estatus = 'Solicitado' WHERE Folio = ?";

                            try (PreparedStatement stmtUpdate = connection.prepareStatement(queryUpdateEstatus)) {
                                stmtUpdate.setInt(1, folio);
                                int filasActualizadas = stmtUpdate.executeUpdate();
                                return filasActualizadas > 0;
                            }
                        }
                        return false;
                    }
                }

            } else {
                // El folio no existe en la tabla documentos, manejar el error
                System.out.println("El folio no existe en la tabla documentos.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelarSolicitud(int folio) {
        String sqlEliminarSolicitud = "DELETE FROM sipre.solicitud WHERE Folio = ?";
        String sqlActualizarEstatus = "UPDATE sipre.documentos SET Estatus = 'En bodega' WHERE Folio = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmtEliminar = connection.prepareStatement(sqlEliminarSolicitud);
             PreparedStatement stmtActualizarEstatus = connection.prepareStatement(sqlActualizarEstatus)) {

            // Eliminar la solicitud
            stmtEliminar.setInt(1, folio);
            int filasAfectadas = stmtEliminar.executeUpdate();

            if (filasAfectadas > 0) {
                // Si se eliminó la solicitud, actualizar el estatus del documento
                stmtActualizarEstatus.setInt(1, folio);
                int filasActualizadas = stmtActualizarEstatus.executeUpdate();
                return filasActualizadas > 0;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Solicitud buscarSolicitud(int folio) {
        String sql = "SELECT s.*, td.Nombre AS TipoDocumento FROM sipre.solicitud s " +
                     "JOIN sipre.tipos_documento td ON s.ID_Tipo = td.ID_Tipo " +
                     "WHERE s.Folio = ?";
        Solicitud solicitud = null;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, folio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setFolio(rs.getInt("Folio"));
                solicitud.setIdTipo(rs.getInt("ID_Tipo"));
                solicitud.setTipoDocumento(rs.getString("TipoDocumento")); // Para compatibilidad
                solicitud.setFecha(rs.getDate("Fecha_Solicitud"));
                solicitud.setMotivo(rs.getString("Motivo"));
                solicitud.setIdUsuario(rs.getInt("ID_Usuario"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitud;
    }

    public List<Solicitud> buscarSolicitudesPorTipo(int idTipo) {
        String sql = "SELECT s.*, td.Nombre AS TipoDocumento FROM sipre.solicitud s " +
                     "JOIN sipre.tipos_documento td ON s.ID_Tipo = td.ID_Tipo " +
                     "WHERE s.ID_Tipo = ?";
        List<Solicitud> solicitudes = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setFolio(rs.getInt("Folio"));
                solicitud.setIdTipo(rs.getInt("ID_Tipo"));
                solicitud.setTipoDocumento(rs.getString("TipoDocumento"));
                solicitud.setFecha(rs.getDate("Fecha_Solicitud"));
                solicitud.setMotivo(rs.getString("Motivo"));
                solicitud.setIdUsuario(rs.getInt("ID_Usuario"));

                solicitudes.add(solicitud);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    public List<Solicitud> buscarSolicitudPorMesAnio(int anio, int mes) {
        String sql = "SELECT s.*, td.Nombre AS TipoDocumento FROM sipre.solicitud s " +
                     "JOIN sipre.tipos_documento td ON s.ID_Tipo = td.ID_Tipo " +
                     "WHERE YEAR(Fecha_Solicitud) = ? AND MONTH(Fecha_Solicitud) = ?";
        List<Solicitud> solicitudes = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, anio);
            stmt.setInt(2, mes);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setFolio(rs.getInt("Folio"));
                solicitud.setIdTipo(rs.getInt("ID_Tipo"));
                solicitud.setTipoDocumento(rs.getString("TipoDocumento"));
                solicitud.setFecha(rs.getDate("Fecha_Solicitud"));
                solicitud.setMotivo(rs.getString("Motivo"));
                solicitud.setIdUsuario(rs.getInt("ID_Usuario"));
                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    public List<Solicitud> obtenerSolicitudes() {
        String sql = "SELECT s.*, td.Nombre AS TipoDocumento FROM sipre.solicitud s " +
                     "JOIN sipre.tipos_documento td ON s.ID_Tipo = td.ID_Tipo";
        List<Solicitud> solicitudes = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setFolio(rs.getInt("Folio"));
                solicitud.setIdTipo(rs.getInt("ID_Tipo"));
                solicitud.setTipoDocumento(rs.getString("TipoDocumento"));
                solicitud.setFecha(rs.getDate("Fecha_Solicitud"));
                solicitud.setMotivo(rs.getString("Motivo"));
                solicitud.setIdUsuario(rs.getInt("ID_Usuario"));
                solicitudes.add(solicitud);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    public boolean actualizarSolicitud(Solicitud solicitud) {
        String sql = "UPDATE sipre.solicitud SET ID_Tipo = ?, Fecha_Solicitud = ?, Motivo = ? WHERE Folio = ?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            java.sql.Date sqlDate = new java.sql.Date(solicitud.getFecha().getTime());

            stmt.setInt(1, solicitud.getIdTipo());
            stmt.setDate(2, sqlDate);
            stmt.setString(3, solicitud.getMotivo());
            stmt.setInt(4, solicitud.getFolio());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdTipoPorNombre(String nombreTipo) {
        String query = "SELECT ID_Tipo FROM sipre.tipos_documento WHERE Nombre = ?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nombreTipo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("ID_Tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}