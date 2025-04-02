/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipre_backend.controlador;

import com.example.sipre_backend.repositorio.SolicitudDAO;
import com.example.sipre_backend.modelo.Solicitud;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {
    private final SolicitudDAO solicitudDAO;
    public SolicitudController(SolicitudDAO solicitudDAO) {
        this.solicitudDAO = solicitudDAO;
    }
    
    @PostMapping
    public ResponseEntity<String> agregarSolicitud(@RequestBody Solicitud solicitud) {
        boolean resultado = solicitudDAO.agregarSolicitud(solicitud.getFolio(), solicitud.tipoDocumento, solicitud.getFecha(), solicitud.getMotivo());
        if (resultado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear solicitud");
    }
    
    @DeleteMapping("/cancelar/{folio}")
    public ResponseEntity<String> cancelarSolicitud(@PathVariable int folio) {
        boolean resultado = solicitudDAO.cancelarSolicitud(folio);
        if (resultado) {
            return ResponseEntity.ok("Solicitud cancelada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al cancelar solicitud");
    }
    
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarSolicitud(@RequestBody Solicitud solicitud) {
        boolean resultado = solicitudDAO.actualizarSolicitud(solicitud);
        if (resultado) {
            return ResponseEntity.ok("Solicitud actualizada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar solicitud");
    }
    
    @GetMapping("/{folio}")
    public ResponseEntity<Solicitud> buscarSolicitud(@PathVariable int folio) {
        Solicitud solicitud = solicitudDAO.buscarSolicitud(folio);
        if (solicitud != null) {
            return ResponseEntity.ok(solicitud);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    @GetMapping("/buscar/tipo/{tipoDocumento}")
    public ResponseEntity<List<Solicitud>> buscarSolicitudesPorTipo(@PathVariable String tipoDocumento) {
        List<Solicitud> solicitudes = solicitudDAO.buscarSolicitudesPorTipo(tipoDocumento);
        return ResponseEntity.ok(solicitudes);
    }
    
    @GetMapping("/buscar/fecha")
    public ResponseEntity<List<Solicitud>> buscarSolicitudesPorFecha(
            @RequestParam int anio,
            @RequestParam int mes) {
        List<Solicitud> solicitudes = solicitudDAO.buscarSolicitudPorMesAnio(anio, mes);
        return ResponseEntity.ok(solicitudes);
    }
    
     @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerTodasLasSolicitudes() {
        List<Solicitud> solicitudes = solicitudDAO.obtenerSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }
}
