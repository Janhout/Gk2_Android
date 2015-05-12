package es.gk2.janhout.gk2_android.modelos;

import org.json.JSONException;
import org.json.JSONObject;

public class TipoDireccion {
    private String idTipoDireccion;
    private String tituloTipoDireccion;

    public TipoDireccion(JSONObject datos) {
        try {
            this.idTipoDireccion = datos.getString("TIPO");
            this.tituloTipoDireccion = datos.getString("TITULO");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTituloTipoDireccion() {
        return tituloTipoDireccion;
    }

    public String getIdTipoDireccion() {
        return idTipoDireccion;
    }
}
