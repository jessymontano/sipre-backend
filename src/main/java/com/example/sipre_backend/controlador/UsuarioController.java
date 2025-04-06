/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipre_backend.controlador;

import com.example.sipre_backend.repositorio.UsuarioDAO;
import com.example.sipre_backend.modelo.Usuario;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        boolean resultado = usuarioDAO.agregarUsuario(usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getContrasena(), usuario.getRol());
        if (resultado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar usuario");
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@RequestParam String nombre, @RequestParam String contrasena) {
        boolean autenticado = usuarioDAO.autenticar(nombre, contrasena);
        if (autenticado) {
            return ResponseEntity.ok("Usuario autenticado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
    
    @GetMapping("/{nombreUsuario}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String nombreUsuario) {
        Usuario usuario = usuarioDAO.getUsuario(nombreUsuario);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PutMapping("/{nombreUsuario}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable String nombreUsuario, @RequestBody Usuario usuario) {
        boolean actualizado = usuarioDAO.actualizarUsuario(usuario, nombreUsuario);
        if (actualizado) {
            return ResponseEntity.ok("Usuario actualizado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar usuario");
    }
    
    @DeleteMapping("/{nombreUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String nombreUsuario) {
        boolean eliminado = usuarioDAO.eliminarUsuario(nombreUsuario);
        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar usuario");
    }

    @PutMapping("/{nombreUsuario}/rol")
    public ResponseEntity<String> modificarRol(
            @PathVariable String nombreUsuario,
            @RequestParam String nuevoRol) {

        boolean actualizado = usuarioDAO.modificarRol(nombreUsuario, nuevoRol);
        if (actualizado) {
            return ResponseEntity.ok("Rol actualizado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el rol");
    }
}
