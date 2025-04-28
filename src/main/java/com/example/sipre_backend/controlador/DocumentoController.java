package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.Documento;
import com.example.sipre_backend.modelo.TipoDocumento;
import com.example.sipre_backend.repositorio.DocumentoDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @PostMapping
    public ResponseEntity<String> agregarDocumento(@RequestBody Documento documento) {
        // obtener el id del tipo
        int idTipo = documentoDAO.obtenerIdTipoPorNombre(documento.getTipoDocumento());
        if (idTipo == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de documento no válido");
        }

        boolean resultado = documentoDAO.agregarDocumento(
                documento.getFolio(),
                idTipo,
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Documento>> buscarPorEstatus(@PathVariable String estatus) {
        List<Documento> documentos = documentoDAO.buscarDocumentosPorEstatus(estatus);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/tipo/{idTipo}")
    public ResponseEntity<List<Documento>> buscarPorTipo(@PathVariable int idTipo) {
        List<Documento> documentos = documentoDAO.buscarDocumentosPorTipo(idTipo);
        return ResponseEntity.ok(documentos);
    }

    @PutMapping("/{folio}")
    public ResponseEntity<String> actualizarDocumento(@PathVariable int folio, @RequestBody Documento documento) {
        // obtener el id del tipo de documento
        int idTipo = documentoDAO.obtenerIdTipoPorNombre(documento.getTipoDocumento());
        if (idTipo == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de documento no válido");
        }

        Documento documentoActualizado = new Documento();
        documentoActualizado.setFolio(documento.getFolio());
        documentoActualizado.setTipoDocumento(documento.getTipoDocumento());
        documentoActualizado.setEstatus(documento.getEstatus());
        documentoActualizado.setCantidadDocumentos(documento.getCantidadDocumentos());

        boolean resultado = documentoDAO.actualizarDocumento(documentoActualizado, folio);
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
    
    @GetMapping("/tipos-documento")
    public ResponseEntity<List<TipoDocumento>> obtenerTiposDocumento() {
        List<TipoDocumento> tipos = documentoDAO.obtenerTiposDocumento();
        return ResponseEntity.ok(tipos);
    }
}