package es.gk2.janhout.gk2_android.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Cliente implements Parcelable, Serializable {

    private int id;
    private String nombre_comercial;
    private String nif;
    private String telefono01;
    private String telefono02;
    private String email;
    private boolean favorito;
    private String tarifa;

    public static final Parcelable.Creator<Cliente> CREATOR = new Parcelable.Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel parcel) {
            return new Cliente(parcel);
        }
        @Override
        public Cliente[] newArray(int i) {
            return new Cliente[i];
        }
    };

    public Cliente(Parcel parcel) {
        this.id = parcel.readInt();
        this.nombre_comercial = parcel.readString();
        this.nif = parcel.readString();
        this.telefono01 = parcel.readString();
        this.telefono02 = parcel.readString();
        this.email = parcel.readString();
        this.favorito = parcel.readByte() == 1;
        this.tarifa = parcel.readString();
    }

    public Cliente() {
    }

    public Cliente(int id, String nombre_comercial, String nif, String telefono01, String telefono02,
                   String email, boolean favorito, String tarifa) {
        this.id = id;
        this.nombre_comercial = nombre_comercial;
        this.nif = nif;
        this.telefono01 = telefono01;
        this.telefono02 = telefono02;
        this.email = email;
        this.favorito = favorito;
        this.tarifa = tarifa;
    }

    public Cliente(JSONObject clienteJSON){
        try {
            this.id = clienteJSON.getInt("CLIENTE");
            this.nombre_comercial = clienteJSON.getString("NOMBRE_COMERCIAL");
            this.nif = clienteJSON.getString("NIF");
            this.telefono01 = clienteJSON.getString("TELEFONO01");
            this.telefono02 = clienteJSON.getString("TELEFONO02");
            this.email = clienteJSON.getString("EMAIL");
            this.favorito = clienteJSON.getInt("FAVORITO") == 1;
            this.tarifa = clienteJSON.getString("TARIFA");
            if(tarifa.equals("") || tarifa == null){
                tarifa = "NOR";
            }
        } catch (JSONException ignore) {
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

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(nombre_comercial);
        parcel.writeString(nif);
        parcel.writeString(telefono01);
        parcel.writeString(telefono02);
        parcel.writeString(email);
        parcel.writeByte((byte) (favorito ? 1 : 0));
        parcel.writeString(tarifa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        if (favorito != cliente.favorito) return false;
        if (id != cliente.id) return false;
        if (email != null ? !email.equals(cliente.email) : cliente.email != null) return false;
        if (nif != null ? !nif.equals(cliente.nif) : cliente.nif != null) return false;
        if (nombre_comercial != null ? !nombre_comercial.equals(cliente.nombre_comercial) : cliente.nombre_comercial != null)
            return false;
        if (tarifa != null ? !tarifa.equals(cliente.tarifa) : cliente.tarifa != null) return false;
        if (telefono01 != null ? !telefono01.equals(cliente.telefono01) : cliente.telefono01 != null)
            return false;
        if (telefono02 != null ? !telefono02.equals(cliente.telefono02) : cliente.telefono02 != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre_comercial != null ? nombre_comercial.hashCode() : 0);
        result = 31 * result + (nif != null ? nif.hashCode() : 0);
        result = 31 * result + (telefono01 != null ? telefono01.hashCode() : 0);
        result = 31 * result + (telefono02 != null ? telefono02.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (favorito ? 1 : 0);
        result = 31 * result + (tarifa != null ? tarifa.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre_comercial='" + nombre_comercial + '\'' +
                ", nif='" + nif + '\'' +
                ", telefono01='" + telefono01 + '\'' +
                ", telefono02='" + telefono02 + '\'' +
                ", email='" + email + '\'' +
                ", favorito=" + favorito +
                ", tarifa='" + tarifa + '\'' +
                '}';
    }
}
