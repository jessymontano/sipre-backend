/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipre_backend.repositorio;

import com.example.sipre_backend.modelo.TipoFormatoPreimpreso;
import com.example.sipre_backend.repositorio.db.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jessica
 */
public class TipoDAO {
    
    public boolean agregarTipo(String nombre) {
        String sql = "INSERT INTO tipos_documento (Nombre) VALUES (?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TipoFormatoPreimpreso> obtenerTiposDocumento() {
        List<TipoFormatoPreimpreso> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipos_documento";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoFormatoPreimpreso tipo = new TipoFormatoPreimpreso();
                tipo.setIdTipo(rs.getInt("ID_Tipo"));
                tipo.setNombre(rs.getString("Nombre"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }
    
    public boolean modificarTipo(int id, String nombre) {
        String sql = "UPDATE tipos_documento SET Nombre = ? WHERE ID_Tipo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, id);
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarTipo(int id) {
        String sql = "DELETE FROM tipos_documento WHERE ID_Tipo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
