package com.example.sipre_backend.repositorio;

import com.example.sipre_backend.repositorio.db.MySQLConnection;
import com.example.sipre_backend.modelo.Documento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoDAO {

    public boolean agregarDocumento(int Folio, int ID_Tipo, String Estatus, int CantidadDocumentos) {
        String query = "INSERT INTO sipre.documentos (Folio, ID_Tipo, Estatus, CantidadDocumentos) VALUES (?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, Folio);
            statement.setInt(2, ID_Tipo);
            statement.setString(3, Estatus);
            statement.setInt(4, CantidadDocumentos);

            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Documento> buscarDocumentosPorEstatus(String estatus) {
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos "
                + "FROM sipre.documentos d "
                + "LEFT JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo "
                + "WHERE d.Estatus = ?";
        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, estatus);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Documento documento = new Documento();
                documento.setFolio(rs.getInt("Folio"));
                documento.setTipoDocumento(rs.getString("TipoDocumento"));
                documento.setIdTipo(rs.getInt("IdTipo"));
                documento.setEstatus(rs.getString("Estatus"));
                documento.setCantidadDocumentos(rs.getInt("CantidadDocumentos"));

                documentos.add(documento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documentos;
    }

    public Documento buscarDocumentoPorFolio(int folio) {
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos "
                + "FROM sipre.documentos d "
                + "LEFT JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo "
                + "WHERE d.Folio = ?";
        Documento documento = null;

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, folio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                documento = new Documento();
                documento.setFolio(rs.getInt("Folio"));
                documento.setTipoDocumento(rs.getString("TipoDocumento"));
                documento.setIdTipo(rs.getInt("IdTipo"));
                documento.setEstatus(rs.getString("Estatus"));
                documento.setCantidadDocumentos(rs.getInt("CantidadDocumentos"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documento;
    }

    public List<Documento> buscarDocumentosPorTipo(int idTipo) {
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos "
                + "FROM sipre.documentos d "
                + "JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo "
                + "WHERE d.ID_Tipo = ?";
        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Documento documento = new Documento();
                documento.setFolio(rs.getInt("Folio"));
                documento.setTipoDocumento(rs.getString("TipoDocumento"));
                documento.setIdTipo(rs.getInt("IdTipo"));
                documento.setEstatus(rs.getString("Estatus"));
                documento.setCantidadDocumentos(rs.getInt("CantidadDocumentos"));

                documentos.add(documento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentos;
    }

    public List<Documento> obtenerDocumentos() {
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos "
                + "FROM sipre.documentos d "
                + "JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo";
        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Documento documento = new Documento();
                documento.setFolio(rs.getInt("Folio"));
                documento.setTipoDocumento(rs.getString("TipoDocumento"));
                documento.setIdTipo(rs.getInt("IdTipo"));
                documento.setEstatus(rs.getString("Estatus"));
                documento.setCantidadDocumentos(rs.getInt("CantidadDocumentos"));

                documentos.add(documento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentos;
    }

    public boolean actualizarDocumento(Documento documento, int folioAnterior) {
        // obtener el id del tipo de documento
        String tipoQuery = "SELECT ID_Tipo FROM sipre.tipos_documento WHERE Nombre = ?";
        int idTipo = -1;

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement tipoStmt = connection.prepareStatement(tipoQuery)) {

            tipoStmt.setString(1, documento.getTipoDocumento());
            ResultSet rs = tipoStmt.executeQuery();

            if (rs.next()) {
                idTipo = rs.getInt("ID_Tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (idTipo == -1) {
            return false;
        }

        String sql = "UPDATE sipre.documentos SET Folio = ?, ID_Tipo = ?, CantidadDocumentos = ? WHERE Folio = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, documento.getFolio());
            stmt.setInt(2, idTipo);
            stmt.setInt(3, documento.getCantidadDocumentos());
            stmt.setInt(4, folioAnterior);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarDocumento(int folio) {
        String sql = "DELETE FROM sipre.documentos WHERE Folio = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, folio);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdTipoPorNombre(String nombreTipo) {
        String sql = "SELECT ID_Tipo FROM tipos_documento WHERE Nombre = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, nombreTipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_Tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // alta de documentos
    public boolean agregarDocumentosPorRango(String tipoDocumento, int cantidad, int folioInicial, Integer folioFinal, String fechaIngreso, int idUsuario) {
        // Validaciones iniciales
        if (cantidad <= 0) {
            System.err.println("La cantidad debe ser mayor que cero");
            return false;
        }

        if (folioInicial <= 0) {
            System.err.println("El folio inicial debe ser mayor que cero");
            return false;
        }

        if (folioFinal != null && folioFinal <= 0) {
            System.err.println("El folio final debe ser mayor que cero");
            return false;
        }

        if (folioFinal != null && folioFinal < folioInicial) {
            System.err.println("El folio final no puede ser menor que el folio inicial");
            return false;
        }

        Connection connection = null;
        PreparedStatement insertDocumento = null;
        PreparedStatement insertMovimiento = null;
        PreparedStatement checkFolio = null;

        try {
            connection = MySQLConnection.getConnection();
            connection.setAutoCommit(false);

            int idTipo = obtenerIdTipoPorNombre(tipoDocumento);
            if (idTipo == -1) {
                System.err.println("Tipo de documento no válido: " + tipoDocumento);
                return false;
            }

            // Calcular folioFinal si no se proporcionó
            if (folioFinal == null) {
                folioFinal = folioInicial + cantidad - 1;
            } else {
                // Validar que la cantidad coincida con el rango
                int rangoCalculado = folioFinal - folioInicial + 1;
                if (rangoCalculado != cantidad) {
                    System.err.println("La cantidad no coincide con el rango de folios");
                    return false;
                }
            }

            // Verificar si los folios ya existen
            String checkSql = "SELECT COUNT(*) FROM sipre.documentos WHERE Folio BETWEEN ? AND ?";
            checkFolio = connection.prepareStatement(checkSql);
            checkFolio.setInt(1, folioInicial);
            checkFolio.setInt(2, folioFinal);
            ResultSet rs = checkFolio.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("Algunos folios en el rango ya existen");
                return false;
            }

            String sqlDocumento = "INSERT INTO sipre.documentos (Folio, ID_Tipo, Estatus, CantidadDocumentos) VALUES (?, ?, ?, ?)";
            String sqlMovimiento = "INSERT INTO sipre.movimientos_inventario (Fecha, Tipo_Movimiento, Cantidad, ID_Usuario, ID_Solicitud, ID_Documentos) VALUES (?, ?, ?, ?, NULL, ?)";

            insertDocumento = connection.prepareStatement(sqlDocumento, Statement.RETURN_GENERATED_KEYS);
            insertMovimiento = connection.prepareStatement(sqlMovimiento);

            for (int folio = folioInicial; folio <= folioFinal; folio++) {
                // Insertar documento
                insertDocumento.setInt(1, folio);
                insertDocumento.setInt(2, idTipo);
                insertDocumento.setString(3, "En bodega");
                insertDocumento.setInt(4, 1);
                insertDocumento.executeUpdate();

                ResultSet generatedKeys = insertDocumento.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idDocumentoGenerado = generatedKeys.getInt(1);
                    insertMovimiento.setString(1, fechaIngreso);
                    insertMovimiento.setString(2, "entrada");
                    insertMovimiento.setInt(3, 1);
                    insertMovimiento.setInt(4, idUsuario);
                    insertMovimiento.setInt(5, idDocumentoGenerado); 
                    insertMovimiento.executeUpdate();
                } else {
                    throw new SQLException("No se pudo obtener el ID del documento insertado.");
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error en la transacción: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (checkFolio != null) {
                    checkFolio.close();
                }
                if (insertDocumento != null) {
                    insertDocumento.close();
                }
                if (insertMovimiento != null) {
                    insertMovimiento.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

}
