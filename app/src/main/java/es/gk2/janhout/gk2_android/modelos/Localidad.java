package es.gk2.janhout.gk2_android.modelos;

import org.json.JSONException;
import org.json.JSONObject;

public class Localidad {
    private String idLocalidad;
    private String cpLocalidad;
    private String tituloLocalidad;

    public Localidad(JSONObject datos) {
        try {
            this.idLocalidad = datos.getString("LOCALIDAD");
            this.cpLocalidad = datos.getString("CODIGO_POSTAL");
            this.tituloLocalidad = datos.getString("TITULO");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public String getCpLocalidad() {
        return cpLocalidad;
    }

    public String getTituloLocalidad() {
        return tituloLocalidad;
    }
}
