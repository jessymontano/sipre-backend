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
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("ID_Usuario"));
                usuario.setNombreUsuario(resultSet.getString("NombreUsuario"));
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

    public boolean agregarUsuario(String nombreUsuario, String Nombre, String Apellido, String Email, String Contrasena, String Rol) {
        String query = "INSERT INTO sipre.usuarios (NombreUsuario, Nombre, Apellido, Email, Contrasena, Rol) VALUES (?, ?, ?, ?, ?, ?)";

        //hashear la contraseña antes de insertarla
        String hash = encoder.encode(Contrasena);
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nombreUsuario);
            statement.setString(2, Nombre);
            statement.setString(3, Apellido);
            statement.setString(4, Email);
            statement.setString(5, hash);
            statement.setString(6, Rol);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean autenticar(String nombre, String contrasena) {

        String query = "SELECT Contrasena FROM usuarios WHERE NombreUsuario = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
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

    public Usuario getUsuario(String nombreUsuario) {
        String query = "SELECT * FROM usuarios WHERE NombreUsuario = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nombreUsuario);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Usuario(
                        resultSet.getInt("ID_Usuario"),
                        resultSet.getString("NombreUsuario"),
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

    public boolean actualizarUsuario(Usuario usuario, int idUsuario) {
        String query = "UPDATE usuarios SET NombreUsuario = ?, Nombre = ?, Apellido = ?, Email = ? "
                + (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()
                ? ", Contrasena = ? " : "")
                + "WHERE ID_Usuario = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            int paramIndex = 1;
            statement.setString(paramIndex++, usuario.getNombreUsuario());
            statement.setString(paramIndex++, usuario.getNombre());
            statement.setString(paramIndex++, usuario.getApellido());
            statement.setString(paramIndex++, usuario.getEmail());

            // Solo actualizar contraseña si no está vacía
            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                statement.setString(paramIndex++, encoder.encode(usuario.getContrasena()));
            }

            statement.setInt(paramIndex, idUsuario);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        String query = "DELETE FROM sipre.usuarios WHERE ID_Usuario = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificarRol(int id, String nuevoRol) {
        String query = "UPDATE sipre.usuarios SET Rol = ? WHERE ID_Usuario = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nuevoRol);
            statement.setInt(2, id);

            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
