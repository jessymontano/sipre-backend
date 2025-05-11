package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.Documento;
import com.example.sipre_backend.repositorio.DocumentoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportePDF {
    @Autowired
    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    private final ReportePDFService reportePDFService = new ReportePDFService();

    @GetMapping("/ReportePDF")
    public ResponseEntity<byte[]> generarReportePDF() {
            List<Documento> documentos = documentoDAO.obtenerDocumentos();
        return reportePDFService.generarReporte(documentos);
    }
}
