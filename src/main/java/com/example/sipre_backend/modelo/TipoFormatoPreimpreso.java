package com.example.sipre_backend.modelo;

public class TipoFormatoPreimpreso {
    public int ID_Tipo;
    public String Nombre;

    public TipoFormatoPreimpreso(int ID_Tipo, String Nombre) {

        this.ID_Tipo = ID_Tipo;
        this.Nombre = Nombre;
    }

    public TipoFormatoPreimpreso() {

    }
    public int getID_Tipo(){
        return ID_Tipo;
    }

    public void setID_Tipo(int ID_Tipo) {
        this.ID_Tipo = ID_Tipo;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    public String getNombre() {
        return Nombre;
    }
}
