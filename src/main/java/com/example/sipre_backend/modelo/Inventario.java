package com.example.sipre_backend.modelo;

import java.util.Date;

public class Inventario {
    public int cantidad;
    public Inventario(String tipoDocumento, int Cantidad) {

        this.cantidad = Cantidad;
    }
    public int  getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}