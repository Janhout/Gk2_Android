package es.gk2.janhout.gk2_android;

import java.util.Date;

/**
 * Created by usuario on 23/03/2015.
 */
public class Factura {
    private String numeroFactura;
    private Date fechaFactura;
    // 0 pagada, 1 pendiente pago, 2 borrador
    private Integer estadoFactura;
    private Integer importeFactura;
    private Integer importePagado;

    public Factura(String numeroFactura, Date fechaFactura, Integer estadoFactura, Integer importeFactura, Integer importePagado) {
        this.numeroFactura = numeroFactura;
        this.fechaFactura = fechaFactura;
        this.estadoFactura = estadoFactura;
        this.importeFactura = importeFactura;
        this.importePagado = importePagado;
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

    public Integer getImporteFactura() {
        return importeFactura;
    }

    public void setImporteFactura(Integer importeFactura) {
        this.importeFactura = importeFactura;
    }

    public Integer getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(Integer importePagado) {
        this.importePagado = importePagado;
    }
}
