package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.Documento;
import com.example.sipre_backend.repositorio.DocumentoDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ExportExcel {
    private final DocumentoDAO documentoDAO = new DocumentoDAO();
    private final ExcelExportService excelExportService = new ExcelExportService();

    @GetMapping("/descargarexcel")
    public ResponseEntity<byte[]> descargarExcel() {
                List<Documento> documentos = documentoDAO.obtenerDocumentos();
        return excelExportService.exportarDocumentos(documentos);
    }
}
