package es.gk2.janhout.gk2_android.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class IVA implements Serializable, Parcelable{

    private String tipo;
    private String p_iva;
    private String titulo;
    private String defecto;

    public static final Parcelable.Creator<IVA> CREATOR = new Parcelable.Creator<IVA>() {
        @Override
        public IVA createFromParcel(Parcel parcel) {
            return new IVA(parcel);
        }
        @Override
        public IVA[] newArray(int i) {
            return new IVA[i];
        }
    };

    public IVA(Parcel parcel) {
        this.tipo = parcel.readString();
        this.p_iva = parcel.readString();
        this.titulo = parcel.readString();
        this.defecto = parcel.readString();
    }

    public IVA() {
    }

    public IVA(String tipo, String p_iva, String titulo, String defecto) {
        this.tipo = tipo;
        this.p_iva = p_iva;
        this.titulo = titulo;
        this.defecto = defecto;
    }

    public IVA(JSONObject obj){
        try {
            this.tipo = obj.getInt("TIPO")+"";
            this.p_iva = obj.getString("P_IVA");
            this.titulo = obj.getString("TITULO");
            this.defecto = obj.getInt("DEFECTO")+"";
        } catch (JSONException ignore) {
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getP_iva() {
        return p_iva;
    }

    public void setP_iva(String p_iva) {
        this.p_iva = p_iva;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDefecto() {
        return defecto;
    }

    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tipo);
        parcel.writeString(p_iva);
        parcel.writeString(titulo);
        parcel.writeString(defecto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IVA iva = (IVA) o;

        if (defecto != null ? !defecto.equals(iva.defecto) : iva.defecto != null) return false;
        if (!p_iva.equals(iva.p_iva)) return false;
        if (!tipo.equals(iva.tipo)) return false;
        if (!titulo.equals(iva.titulo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tipo.hashCode();
        result = 31 * result + p_iva.hashCode();
        result = 31 * result + titulo.hashCode();
        result = 31 * result + (defecto != null ? defecto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IVA{" +
                "tipo='" + tipo + '\'' +
                ", p_iva='" + p_iva + '\'' +
                ", titulo='" + titulo + '\'' +
                ", defecto='" + defecto + '\'' +
                '}';
    }
}