package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.TipoFormatoPreimpreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tipos")
public class TipoController {
    private final List<TipoFormatoPreimpreso> tipos = new ArrayList<>();

    // Agregar nuevo tipo
    @PostMapping
    public ResponseEntity<String> agregarTipo(@RequestBody TipoFormatoPreimpreso tipo) {
        tipos.add(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tipo creado exitosamente");
    }

    // Obtener todos los tipos
    @GetMapping
    public ResponseEntity<List<TipoFormatoPreimpreso>> obtenerTipos() {
        return ResponseEntity.ok(tipos);
    }

    // Modificar tipo existente
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTipo(@PathVariable int id, @RequestBody TipoFormatoPreimpreso tipoActualizado) {
        for (TipoFormatoPreimpreso tipo : tipos) {
            if (tipo.getID_Tipo() == id) {
                tipo.setNombre(tipoActualizado.getNombre());
                return ResponseEntity.ok("Tipo actualizado exitosamente");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo no encontrado");
    }

    // Eliminar tipo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipo(@PathVariable int id) {
        boolean eliminado = tipos.removeIf(tipo -> tipo.getID_Tipo() == id);
        if (eliminado) {
            return ResponseEntity.ok("Tipo eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo no encontrado");
    }
}

