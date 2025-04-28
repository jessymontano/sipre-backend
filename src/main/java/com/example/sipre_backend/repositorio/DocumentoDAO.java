package com.example.sipre_backend.repositorio;

import com.example.sipre_backend.repositorio.db.MySQLConnection;
import com.example.sipre_backend.modelo.Documento;
import com.example.sipre_backend.modelo.TipoDocumento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoDAO {

    public boolean agregarDocumento(int Folio, int ID_Tipo, String Estatus, int CantidadDocumentos) {
        String query = "INSERT INTO sipre.documentos (Folio, ID_Tipo, Estatus, CantidadDocumentos) VALUES (?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos " +
                     "FROM sipre.documentos d " +
                     "LEFT JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo " +
                     "WHERE d.Estatus = ?";
        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos " +
                     "FROM sipre.documentos d " +
                     "LEFT JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo " +
                     "WHERE d.Folio = ?";
        Documento documento = null;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos " +
                     "FROM sipre.documentos d " +
                     "JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo " +
                     "WHERE d.ID_Tipo = ?";
        List<Documento> documentos = new ArrayList<>();

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
        String sql = "SELECT d.Folio, td.Nombre AS TipoDocumento, d.ID_Tipo AS IdTipo, d.Estatus, d.CantidadDocumentos " +
                     "FROM sipre.documentos d " +
                     "JOIN sipre.tipos_documento td ON d.ID_Tipo = td.ID_Tipo";
        List<Documento> documentos = new ArrayList<>();
        
        try (Connection connection = MySQLConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
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
        
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement tipoStmt = connection.prepareStatement(tipoQuery)) {
            
            tipoStmt.setString(1, documento.getTipoDocumento());
            ResultSet rs = tipoStmt.executeQuery();
            
            if (rs.next()) {
                idTipo = rs.getInt("ID_Tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        if (idTipo == -1) return false;

        String sql = "UPDATE sipre.documentos SET Folio = ?, ID_Tipo = ?, CantidadDocumentos = ? WHERE Folio = ?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

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
    try (Connection connection = MySQLConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
        
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
    public List<TipoDocumento> obtenerTiposDocumento() {
        List<TipoDocumento> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipos_documento";
        try (Connection connection = MySQLConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoDocumento tipo = new TipoDocumento();
                tipo.setId(rs.getInt("ID_Tipo"));
                tipo.setNombre(rs.getString("Nombre"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }
}