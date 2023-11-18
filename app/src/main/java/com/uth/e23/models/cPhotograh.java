package com.uth.e23.models;

import android.graphics.Bitmap;

public class cPhotograh {
    private Bitmap foto;
    private String descripcion;

    public cPhotograh(){}

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap firma_digital) {
        this.foto = firma_digital;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}