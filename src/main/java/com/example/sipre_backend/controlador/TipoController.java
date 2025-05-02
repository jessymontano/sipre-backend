package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.TipoFormatoPreimpreso;
import com.example.sipre_backend.repositorio.TipoDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tipos")
public class TipoController {
    private final TipoDAO tipoDAO = new TipoDAO();

    // Agregar nuevo tipo
    @PostMapping
    public ResponseEntity<String> agregarTipo(@RequestBody TipoFormatoPreimpreso tipo) {
        boolean resultado = tipoDAO.agregarTipo(tipo.getNombre());
        if (resultado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Tipo creado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear tipo");
    }

    // Obtener todos los tipos
    @GetMapping
    public ResponseEntity<List<TipoFormatoPreimpreso>> obtenerTipos() {
        List<TipoFormatoPreimpreso> tipos = tipoDAO.obtenerTiposDocumento();
        return ResponseEntity.ok(tipos);
    }

    // Modificar tipo existente
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTipo(@PathVariable int id, @RequestBody TipoFormatoPreimpreso tipoActualizado) {
        boolean resultado = tipoDAO.modificarTipo(id, tipoActualizado.getNombre());
        if (resultado) {
            return ResponseEntity.ok("Tipo actualizado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar tipo");
    }

    // Eliminar tipo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipo(@PathVariable int id) {
        boolean eliminado = tipoDAO.eliminarTipo(id);
        if (eliminado) {
            return ResponseEntity.ok("Tipo eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo no encontrado");
    }
}

