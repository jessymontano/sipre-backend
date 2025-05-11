package com.example.sipre_backend.controlador;

import com.example.sipre_backend.modelo.Documento;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import com.itextpdf.text.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportePDFService {

    public ResponseEntity<byte[]> generarReporte(List<Documento> documentos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            document.add(new Paragraph("Reporte de Documentos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(Chunk.NEWLINE);

            // Gráfico de barras por tipo
            document.add(new Paragraph("Gráfico de Barras - Documentos por Tipo"));
            Image graficoBarras = Image.getInstance(generarGraficoBarras(documentos));
            graficoBarras.scaleToFit(500, 300);
            document.add(graficoBarras);
            document.newPage();

            // Gráfico de pastel por estatus
            document.add(new Paragraph("Gráfico de Pastel - Documentos por Estatus"));
            Image graficoPastel = Image.getInstance(generarGraficoPastel(documentos));
            graficoPastel.scaleToFit(500, 300);
            document.add(graficoPastel);

            document.newPage();

            // Gráfico de líneas
            document.add(new Paragraph("Gráfico de Líneas - Documentos por Tipo"));
            Image graficoLineas = Image.getInstance(generarGraficoLineas(documentos));
            graficoLineas.scaleToFit(500, 300);
            document.add(graficoLineas);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm"));
            String nombreArchivo = "ReporteDocumentos_" + timestamp + ".pdf";

            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private byte[] generarGraficoBarras(List<Documento> documentos) throws IOException {
        Map<String, Long> agrupado = documentos.stream()
                .collect(Collectors.groupingBy(Documento::getTipoDocumento, Collectors.counting()));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        agrupado.forEach((tipo, cantidad) -> dataset.addValue(cantidad, "Documentos", tipo));

        JFreeChart chart = ChartFactory.createBarChart("Documentos por Tipo", "Tipo", "Cantidad", dataset);

        // Obtener el eje del dominio (eje X)
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();

        // Configurar la rotación de las etiquetas del eje X
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotación de 45 grados

        chart.setBackgroundPaint(Color.white);
        return EncoderUtil.encode(chart.createBufferedImage(600, 400), ImageFormat.PNG);
    }

    private byte[] generarGraficoPastel(List<Documento> documentos) throws IOException {
        Map<String, Long> agrupado = documentos.stream()
                .collect(Collectors.groupingBy(Documento::getEstatus, Collectors.counting()));
        DefaultPieDataset dataset = new DefaultPieDataset();
        agrupado.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart("Documentos por Estatus", dataset, true, true, false);
        chart.setBackgroundPaint(Color.white);
        return EncoderUtil.encode(chart.createBufferedImage(600, 400), ImageFormat.PNG);
    }

    private byte[] generarGraficoLineas(List<Documento> documentos) throws IOException {
        Map<String, Long> agrupado = documentos.stream()
                .collect(Collectors.groupingBy(Documento::getTipoDocumento, Collectors.counting()));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        agrupado.forEach((tipo, cantidad) -> dataset.addValue(cantidad, "Documentos", tipo));

        JFreeChart chart = ChartFactory.createLineChart("Documentos por Tipo", "Tipo", "Cantidad", dataset);

        // Rotar etiquetas del eje X (dominio)
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);


        chart.setBackgroundPaint(Color.white);
        return EncoderUtil.encode(chart.createBufferedImage(600, 400), ImageFormat.PNG);
    }
}