/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.Documento;
import com.example.sipre_backend.repositorio.DocumentoDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @PostMapping
    public ResponseEntity<String> agregarDocumento(@RequestBody Documento documento) {
        boolean resultado = documentoDAO.agregarDocumento(
                documento.getFolio(),
                documento.getTipoDocumento(),
                documento.getEstatus(),
                documento.getCantidadDocumentos()
        );
        if (resultado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Documento creado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear documento");
    }

    @GetMapping
    public ResponseEntity<List<Documento>> obtenerTodosLosDocumentos() {
        List<Documento> documentos = documentoDAO.obtenerDocumentos();
        return ResponseEntity.ok(documentos);

    }

    @GetMapping("/{folio}")
    public ResponseEntity<Documento> buscarPorFolio(@PathVariable int folio) {
        Documento documento = documentoDAO.buscarDocumentoPorFolio(folio);
        if (documento != null) {
            return ResponseEntity.ok(documento);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Documento>> buscarPorEstatus(@PathVariable String estatus) {
        List<Documento> documentos = documentoDAO.buscarDocumentosPorEstatus(estatus);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Documento>> buscarPorTipo(@PathVariable String tipo) {
        List<Documento> documentos = documentoDAO.buscarDocumentosPorTipo(tipo);
        return ResponseEntity.ok(documentos);
    }

    @PutMapping("/{folio}")
    public ResponseEntity<String> actualizarDocumento(@PathVariable int folio, @RequestBody Documento documento) {
        boolean resultado = documentoDAO.actualizarDocumento(documento, folio);
        if (resultado) {
            return ResponseEntity.ok("Documento actualizado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar documento");
    }

    @DeleteMapping("/{folio}")
    public ResponseEntity<String> eliminarDocumento(@PathVariable int folio) {
        boolean resultado = documentoDAO.eliminarDocumento(folio);
        if (resultado) {
            return ResponseEntity.ok("Documento eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar documento");
    }
}
