package es.gk2.janhout.gk2_android.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Provincia {
    private String idProvincia;
    private String tituloProvincia;

    public Provincia(String idProvincia, String tituloProvincia) {
        this.idProvincia = idProvincia;
        this.tituloProvincia = tituloProvincia;
    }

    public Provincia(JSONObject datos) {
        try {
            this.idProvincia = datos.getString("PROVINCIA");
            this.tituloProvincia = datos.getString("TITULO");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public String getTituloProvincia() {
        return tituloProvincia;
    }
}
