package es.gk2.janhout.gk2_android.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Cliente {

    private int id;
    private String nombre_comercial;
    private String nif;
    private String telefono01;
    private String telefono02;
    private String email;

    public Cliente() {
    }

    public Cliente(int id, String nombre_comercial, String nif, String telefono01, String telefono02, String email) {
        this.id = id;
        this.nombre_comercial = nombre_comercial;
        this.nif = nif;
        this.telefono01 = telefono01;
        this.telefono02 = telefono02;
        this.email = email;
    }

    public Cliente(JSONObject clienteJSON){
        try {
            this.id = clienteJSON.getInt("CLIENTE");
            this.nombre_comercial = clienteJSON.getString("NOMBRE_COMERCIAL");
            this.nif = clienteJSON.getString("NIF");
            this.telefono01 = clienteJSON.getString("TELEFONO01");
            this.telefono02 = clienteJSON.getString("TELEFONO02");
            this.email = clienteJSON.getString("EMAIL");
        } catch (JSONException e) {
            Log.e("error mio", e.toString());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getTelefono01() {
        return telefono01;
    }

    public void setTelefono01(String telefono01) {
        this.telefono01 = telefono01;
    }

    public String getTelefono02() {
        return telefono02;
    }

    public void setTelefono02(String telefono02) {
        this.telefono02 = telefono02;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
