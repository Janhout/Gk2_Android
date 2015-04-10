package es.gk2.janhout.gk2_android.Util;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Factura implements Parcelable, Serializable {

    private String numeroFactura;
    private Date fechaFactura;
    private int estadoFactura; // 0: impagada | 1: pagada | 2: borrador
    private float importeFactura;
    private float importePagado;
    private int impreso; //0: no impresa | 1: impresa
    private int enviado; //0: no enviada | 1: enviada
    private int idImpresion;

    public static final Parcelable.Creator<Factura> CREATOR = new Parcelable.Creator<Factura>() {
        @Override
        public Factura createFromParcel(Parcel parcel) {
            return new Factura(parcel);
        }
        @Override
        public Factura[] newArray(int i) {
            return new Factura[i];
        }
    };

    public Factura(Parcel parcel) {
        this.numeroFactura = parcel.readString();
        this.fechaFactura = stringToDate(parcel.readString());
        this.estadoFactura = parcel.readInt();
        this.importeFactura = parcel.readFloat();
        this.importePagado = parcel.readFloat();
        this.impreso = parcel.readInt();
        this.enviado = parcel.readInt();
        this.idImpresion = parcel.readInt();
    }

    public Factura(){
    }

    public Factura(String numeroFactura, String fechaFactura, int estadoFactura, float importeFactura, float importePagado, int impreso, int enviado, int idImpresion) {
        this.numeroFactura = numeroFactura;
        this.fechaFactura = stringToDate(fechaFactura);
        this.estadoFactura = estadoFactura;
        this.importeFactura = importeFactura;
        this.importePagado = importePagado;
        this.impreso = impreso;
        this.enviado = enviado;
        this.idImpresion = idImpresion;
    }

    public Factura(JSONObject facturaJSON) {
        try {
            this.numeroFactura = facturaJSON.getString("NUMERO");
            String stringFecha = facturaJSON.getString("FECHA");
            SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.fechaFactura = toDateFormatter.parse(stringFecha);
            String estadoFacturaTemp = facturaJSON.getString("ESTADO");
            switch (estadoFacturaTemp) {
                case "Impagada":
                    this.estadoFactura = 0;
                    break;
                case "Pagada":
                    this.estadoFactura = 1;
                    break;
                case "Borrador":
                    this.estadoFactura = 2;
                    break;
            }
            this.importeFactura = Float.parseFloat(facturaJSON.getString("LIQUIDO"));
            this.importePagado = Float.parseFloat(facturaJSON.getString("PENDIENTE"));
            this.impreso = facturaJSON.getInt("PRINTED");
            this.enviado = facturaJSON.getInt("SENT");
            this.idImpresion = facturaJSON.getInt("ID_S");
        } catch (JSONException | ParseException e) {
            e.getMessage();
        }
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaFactura() {
        return dateToString();
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = stringToDate(fechaFactura);
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public float getImporteFactura() {
        return importeFactura;
    }

    public void setImporteFactura(float importeFactura) {
        this.importeFactura = importeFactura;
    }

    public float getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(float importePagado) {
        this.importePagado = importePagado;
    }

    public int getImpreso() {
        return impreso;
    }

    public void setImpreso(int impreso) {
        this.impreso = impreso;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado){
        this.enviado = enviado;
    }

    public int getIdImpresion() {
        return idImpresion;
    }

    public void setIdImpresion(int idImpresion) {
        this.idImpresion = idImpresion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(numeroFactura);
        parcel.writeString(dateToString());
        parcel.writeInt(estadoFactura);
        parcel.writeFloat(importeFactura);
        parcel.writeFloat(importePagado);
        parcel.writeInt(impreso);
        parcel.writeInt(enviado);
        parcel.writeInt(idImpresion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factura factura = (Factura) o;

        if (enviado != factura.enviado) return false;
        if (estadoFactura != factura.estadoFactura) return false;
        if (idImpresion != factura.idImpresion) return false;
        if (Float.compare(factura.importeFactura, importeFactura) != 0) return false;
        if (Float.compare(factura.importePagado, importePagado) != 0) return false;
        if (impreso != factura.impreso) return false;
        if (fechaFactura != null ? !fechaFactura.equals(factura.fechaFactura) : factura.fechaFactura != null)
            return false;
        if (numeroFactura != null ? !numeroFactura.equals(factura.numeroFactura) : factura.numeroFactura != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numeroFactura != null ? numeroFactura.hashCode() : 0;
        result = 31 * result + (fechaFactura != null ? fechaFactura.hashCode() : 0);
        result = 31 * result + estadoFactura;
        result = 31 * result + (importeFactura != +0.0f ? Float.floatToIntBits(importeFactura) : 0);
        result = 31 * result + (importePagado != +0.0f ? Float.floatToIntBits(importePagado) : 0);
        result = 31 * result + impreso;
        result = 31 * result + enviado;
        result = 31 * result + idImpresion;
        return result;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "numeroFactura='" + numeroFactura + '\'' +
                ", fechaFactura=" + fechaFactura +
                ", estadoFactura=" + estadoFactura +
                ", importeFactura=" + importeFactura +
                ", importePagado=" + importePagado +
                ", impreso=" + impreso +
                ", enviado=" + enviado +
                ", idImpresion=" + idImpresion +
                '}';
    }


    private String dateToString(){
        SimpleDateFormat toStringFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return toStringFormatter.format(fechaFactura);
    }

    private Date stringToDate(String s){
        SimpleDateFormat toStringFormatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return toStringFormatter.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
