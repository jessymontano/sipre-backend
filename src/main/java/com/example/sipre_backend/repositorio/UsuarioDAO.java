package com.example.sipre_backend.repositorio;


import com.example.sipre_backend.modelo.Usuario;
import com.example.sipre_backend.repositorio.db.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        String query = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(resultSet.getString("Nombre"));
                usuario.setApellido(resultSet.getString("Apellido"));
                usuario.setEmail(resultSet.getString("Email"));
                usuario.setContrasena(resultSet.getString("Contrasena"));
                usuario.setRol(resultSet.getString("Rol"));
                
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean agregarUsuario(String Nombre, String Apellido, String Email, String Contrasena, String Rol) {
        String query = "INSERT INTO sipre.usuarios (Nombre, Apellido, Email, Contrasena, Rol) VALUES (?, ?, ?, ?, ?)";
        
        //hashear la contraseña antes de insertarla
        String hash = encoder.encode(Contrasena);
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, Nombre);
            statement.setString(2, Apellido); 
            statement.setString(3, Email);
            statement.setString(4, hash);
            statement.setString(5, Rol);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   public boolean autenticar(String nombre, String contrasena) {
    String query = "SELECT Contrasena FROM usuarios WHERE Nombre = ?";

    try (Connection connection = MySQLConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, nombre);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String contrasenaAlmacenada = resultSet.getString("Contrasena");
            return encoder.matches(contrasena, contrasenaAlmacenada);
        } else {
            return false; 
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public Usuario getUsuario(String Usuario) {
        String query = "SELECT * FROM usuarios WHERE Nombre = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, Usuario);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Usuario(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Apellido"),
                        resultSet.getString("Email"),
                        resultSet.getString("Contrasena"),
                        resultSet.getString("Rol")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no se encuentra el usuario
    }
    
    public boolean actualizarUsuario(Usuario usuario, String nombreUsuario) {
        String query = "UPDATE sipre.usuario SET Nombre = ?, Apellido = ?, Email = ?, Contrasena = ? WHERE Nombre = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, encoder.encode(usuario.getContrasena()));
            statement.setString(5, nombreUsuario);

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarUsuario(String nombreUsuario) {
        String query = "DELETE FROM sipre.usuarios WHERE Nombre = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nombreUsuario);

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0; 

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


