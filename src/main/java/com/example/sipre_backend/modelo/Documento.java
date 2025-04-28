package com.example.sipre_backend.modelo;

import java.util.Date;

public class Documento {
    private int folio;
    private String tipoDocumento;
    private int idTipo;
    private String estatus;
    private int cantidadDocumentos;



    public Documento(int Folio, String TipoDocumento, String Estatus, int CantidadDocumentos) {
        this.folio = Folio;
        this.tipoDocumento = TipoDocumento;
        this.estatus = Estatus;
        this.cantidadDocumentos = CantidadDocumentos;
    }

    public Documento() {
    }


    public int getFolio() {
        return  folio;
    }

    public void setFolio(int Folio) {
        this.folio = Folio;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String TipoDocumento) {
        this.tipoDocumento = TipoDocumento;
    }

    public String getEstatus(){
        return estatus;
    }
    public void setEstatus(String Estatus) {
        this.estatus = Estatus;
    }
    public int getCantidadDocumentos(){
        return cantidadDocumentos;
    }

    public void setCantidadDocumentos(int CantidadDocumentos) {
        this.cantidadDocumentos = CantidadDocumentos;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int id) {
        this.idTipo = id;
    }
    }



