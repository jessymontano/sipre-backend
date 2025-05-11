package com.example.sipre_backend.controlador;
import com.example.sipre_backend.modelo.Documento;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportService {
    public ResponseEntity<byte[]> exportarDocumentos(List<Documento> documentos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Documentos");

            // Encabezados
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Folio");
            headerRow.createCell(1).setCellValue("Tipo Documento");
            headerRow.createCell(2).setCellValue("Estatus");
            headerRow.createCell(3).setCellValue("Cantidad Documentos");

            // Contenido
            int rowNum = 1;
            for (Documento doc : documentos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(doc.getFolio());
                row.createCell(1).setCellValue(doc.getTipoDocumento());
                row.createCell(2).setCellValue(doc.getEstatus());
            }

            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm"));
            String nombreArchivo = "ExcelReporte_" + timestamp + ".xlsx";
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
