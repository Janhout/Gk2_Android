package es.gk2.janhout.gk2_android.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compra {
    private String compra_id;
    private String compra_idProveedor;
    private String compra_nombreProveedor;
    private Date compra_fecha;
    private Integer compra_estado; //0: Sin contabilizar, 5: Contabilizada
    private Float compra_importe;
    private Float compra_pendiente;

    //Este objeto representa una compra del autónomo usuario de GK2 a un proveedor
    public Compra(JSONObject obj) {
        try {
            this.compra_id = obj.getString("NUM_FACTURA");
            this.compra_idProveedor = obj.getString("PROVEEDOR");
            this.compra_nombreProveedor = obj.getString("TITULO");
            SimpleDateFormat toDateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            this.compra_fecha = toDateFormatter.parse(obj.getString("FECHA"));
            this.compra_importe = Float.parseFloat(obj.getString("LIQUIDO"));
            this.compra_estado = obj.getInt("ESTADO");
            this.compra_pendiente = Float.parseFloat(obj.getString("PENDIENTE"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getCompra_id() {
        return compra_id;
    }

    public void setCompra_id(String compra_id) {
        this.compra_id = compra_id;
    }

    public String getCompra_idProveedor() {
        return compra_idProveedor;
    }

    public void setCompra_idProveedor(String compra_idProveedor) {
        this.compra_idProveedor = compra_idProveedor;
    }

    public String getCompra_nombreProveedor() {
        return compra_nombreProveedor;
    }

    public void setCompra_nombreProveedor(String compra_nombreProveedor) {
        this.compra_nombreProveedor = compra_nombreProveedor;
    }

    public String getCompra_fecha() {
        SimpleDateFormat toStringFormatter = new SimpleDateFormat("dd/MM/yyyy");

        return toStringFormatter.format(compra_fecha);
    }

    public void setCompra_fecha(Date compra_fecha) {
        this.compra_fecha = compra_fecha;
    }

    public Integer getCompra_estado() {
        return compra_estado;
    }

    public void setCompra_estado(Integer compra_estado) {
        this.compra_estado = compra_estado;
    }

    public Float getCompra_importe() {
        return compra_importe;
    }

    public void setCompra_importe(Float compra_importe) {
        this.compra_importe = compra_importe;
    }

    public Float getCompra_pendiente() {
        return compra_pendiente;
    }

    public void setCompra_pendiente(Float compra_pendiente) {
        this.compra_pendiente = compra_pendiente;
    }
}


