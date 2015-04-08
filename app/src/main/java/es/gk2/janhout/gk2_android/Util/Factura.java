package es.gk2.janhout.gk2_android.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Factura {
    private String numeroFactura;
    private Date fechaFactura;
    // 0: impagada | 1: pagada | 2: borrador
    private int estadoFactura;
    private float importeFactura;
    private float importePagado;
    //0: no impresa | 1: impresa
    private int impreso;
    //0: no enviada | 1: enviada
    private int enviado;
    private int idImpresion;

    public Factura(JSONObject facturaJSON) {
        try {
            this.numeroFactura = facturaJSON.getString("NUMERO");
            String stringFecha = facturaJSON.getString("FECHA");
            SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.fechaFactura = toDateFormatter.parse(stringFecha);

            String estadoFacturaTemp = facturaJSON.getString("ESTADO");
            if(estadoFacturaTemp.equals("Impagada"))
                this.estadoFactura = 0;
            else if(estadoFacturaTemp.equals("Pagada"))
                this.estadoFactura = 1;
            else if(estadoFacturaTemp.equals("Borrador"))
                this.estadoFactura = 2;
            this.importeFactura = Float.parseFloat(facturaJSON.getString("LIQUIDO"));
            this.importePagado = Float.parseFloat(facturaJSON.getString("PENDIENTE"));
            this.impreso = facturaJSON.getInt("PRINTED");
            this.enviado = facturaJSON.getInt("SENT");
            this.idImpresion = facturaJSON.getInt("ID_S");

        } catch (JSONException e) {
            Log.e("error mio", e.toString());
        } catch (ParseException e) {
            Log.v("mio2", e.toString());
        }
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaFactura() {
        SimpleDateFormat toStringFormatter = new SimpleDateFormat("dd/MM/yyyy");

        return toStringFormatter.format(fechaFactura);
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(Integer estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public float getImporteFactura() {
        return importeFactura;
    }

    public void setImporteFactura(Float importeFactura) {
        this.importeFactura = importeFactura;
    }

    public float getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(Float importePagado) {
        this.importePagado = importePagado;
    }

    public int getImpreso() {
        return impreso;
    }

    public int getEnviado() {
        return enviado;
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
                '}';
    }

    public int getIdImpresion() {
        return idImpresion;
    }

    public void setIdImpresion(int idImpresion) {
        this.idImpresion = idImpresion;
    }
}
