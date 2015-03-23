package es.gk2.janhout.gk2_android;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by usuario on 23/03/2015.
 */
public class Factura {
    private String numeroFactura;
    private Date fechaFactura;
    // 0: impagada | 1: pagada | 2: borrador
    private Integer estadoFactura;
    private Float importeFactura;
    private Float importePagado;

    public Factura(String numeroFactura, Date fechaFactura, Integer estadoFactura, Float importeFactura, Float importePagado) {
        this.numeroFactura = numeroFactura;
        this.fechaFactura = fechaFactura;
        this.estadoFactura = estadoFactura;
        this.importeFactura = importeFactura;
        this.importePagado = importePagado;
    }

    public Factura(JSONObject facturaJSON){
        try {
            this.numeroFactura = facturaJSON.getString("NUMERO");
            this.fechaFactura = new SimpleDateFormat("YYYY-NN-DD HH:MM:SS").parse(facturaJSON.getString("FECHA"));
            String estadoFacturaTemp = facturaJSON.getString("ESTADO");
            if(estadoFacturaTemp == "Impagada")
                this.estadoFactura = 0;
            else if(estadoFacturaTemp == "Pagada")
                this.estadoFactura = 1;
            else if(estadoFacturaTemp == "Borrador")
                this.estadoFactura = 2;
            this.importeFactura = Float.parseFloat(facturaJSON.getString("LIQUIDO"));
            this.importePagado = Float.parseFloat(facturaJSON.getString("PENDIENTE"));
        } catch (JSONException e) {
            Log.e("error mio", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Integer getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(Integer estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public Float getImporteFactura() {
        return importeFactura;
    }

    public void setImporteFactura(Float importeFactura) {
        this.importeFactura = importeFactura;
    }

    public Float getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(Float importePagado) {
        this.importePagado = importePagado;
    }
}
