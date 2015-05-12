package es.gk2.janhout.gk2_android.modelos;

import org.json.JSONException;
import org.json.JSONObject;

public class Tarifa {

    private String tarifa;
    private String titulo;
    private int iva_incluido;

    public Tarifa() {
    }

    public Tarifa(String tarifa, String titulo, int iva_incluido) {
        this.tarifa = tarifa;
        this.titulo = titulo;
        this.iva_incluido = iva_incluido;
    }

    public Tarifa(JSONObject obj) {
        try {
            this.tarifa = obj.getString("TARIFA");
            this.titulo = obj.getString("TITULO");
            this.iva_incluido = obj.getInt("IVA_INCLUIDO");
        } catch (JSONException ignored) {
        }
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIva_incluido() {
        return iva_incluido;
    }

    public void setIva_incluido(int iva_incluido) {
        this.iva_incluido = iva_incluido;
    }
}