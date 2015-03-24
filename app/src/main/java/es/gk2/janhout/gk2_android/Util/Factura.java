package es.gk2.janhout.gk2_android.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by usuario on 23/03/2015.
 */
public class Factura {
    private String numeroFactura;
    private String fechaFactura;
    // 0: impagada | 1: pagada | 2: borrador
    private Integer estadoFactura;
    private Float importeFactura;
    private Float importePagado;

    public Factura(String numeroFactura, String fechaFactura, Integer estadoFactura, Float importeFactura, Float importePagado) {
        this.numeroFactura = numeroFactura;
        this.fechaFactura = fechaFactura;
        this.estadoFactura = estadoFactura;
        this.importeFactura = importeFactura;
        this.importePagado = importePagado;
    }

    public Factura(JSONObject facturaJSON) {
        try {
            this.numeroFactura = facturaJSON.getString("NUMERO");
            String stringFecha = facturaJSON.getString("FECHA");
            this.fechaFactura = stringFecha.substring(8, 10)+"/"+stringFecha.substring(5, 7)+"/"+stringFecha.substring(0, 4);
            String estadoFacturaTemp = facturaJSON.getString("ESTADO");
            if (estadoFacturaTemp.equals("Impagada"))
                this.estadoFactura = 0;
            else if (estadoFacturaTemp.equals("Pagada"))
                this.estadoFactura = 1;
            else if (estadoFacturaTemp.equals("Borrador"))
                this.estadoFactura = 2;
            this.importeFactura = Float.parseFloat(facturaJSON.getString("LIQUIDO"));
            this.importePagado = Float.parseFloat(facturaJSON.getString("PENDIENTE"));
        } catch (JSONException e) {
            Log.e("error mio", e.toString());
        }
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
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
