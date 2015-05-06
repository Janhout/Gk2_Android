package es.gk2.janhout.gk2_android.Util;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Producto implements Parcelable, Serializable {

    private int id_a;
    private String articulo;
    private String titulo;
    private String p_coste;
    private String familia;
    private String precio_venta;
    private String control_stock;
    private String lotes;
    private String disponibilidad;
    private String descuento;
    private String modo;
    private String modo_txt;
    private String tarifa;
    private String tarifa_titulo;
    private String tarifa_iva_incluido;
    private String tipo_iva;
    private String notas;
    private String unidades;
    private String p_iva;

    public static final Parcelable.Creator<Producto> CREATOR = new Parcelable.Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel parcel) {
            return new Producto(parcel);
        }
        @Override
        public Producto[] newArray(int i) {
            return new Producto[i];
        }
    };

    public Producto(Parcel parcel) {
        this.id_a = parcel.readInt();
        this.articulo = parcel.readString();
        this.titulo = parcel.readString();
        this.p_coste = parcel.readString();
        this.familia = parcel.readString();
        this.precio_venta = parcel.readString();
        this.control_stock = parcel.readString();
        this.lotes = parcel.readString();
        this.disponibilidad = parcel.readString();
        this.descuento = parcel.readString();
        this.modo = parcel.readString();
        this.modo_txt = parcel.readString();
        this.tarifa = parcel.readString();
        this.tarifa_titulo = parcel.readString();
        this.tarifa_iva_incluido = parcel.readString();
        this.tipo_iva = parcel.readString();
        this.notas = parcel.readString();
        this.unidades = parcel.readString();
        this.p_iva = parcel.readString();
    }

    public Producto(){

    }

    public Producto(JSONObject obj){
        try {
            this.titulo = obj.getString("TITULO");
        } catch (JSONException e) {
        }
    }

    public Producto(int id_a, String articulo, String titulo, String p_coste, String familia,
                    String precio_venta, String control_stock, String lotes, String disponibilidad,
                    String descuento, String modo, String modo_txt, String tarifa, String tarifa_titulo,
                    String tarifa_iva_incluido, String tipo_iva, String notas, String unidades, String p_iva) {
        this.id_a = id_a;
        this.articulo = articulo;
        this.titulo = titulo;
        this.p_coste = p_coste;
        this.familia = familia;
        this.precio_venta = precio_venta;
        this.control_stock = control_stock;
        this.lotes = lotes;
        this.disponibilidad = disponibilidad;
        this.descuento = descuento;
        this.modo = modo;
        this.modo_txt = modo_txt;
        this.tarifa = tarifa;
        this.tarifa_titulo = tarifa_titulo;
        this.tarifa_iva_incluido = tarifa_iva_incluido;
        this.tipo_iva = tipo_iva;
        this.notas = notas;
        this.unidades = unidades;
        this.p_iva = p_iva;
    }

    public int getId_a() {
        return id_a;
    }

    public void setId_a(int id_a) {
        this.id_a = id_a;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getP_coste() {
        return p_coste;
    }

    public void setP_coste(String p_coste) {
        this.p_coste = p_coste;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(String precio_venta) {
        this.precio_venta = precio_venta;
    }

    public String getControl_stock() {
        return control_stock;
    }

    public void setControl_stock(String control_stock) {
        this.control_stock = control_stock;
    }

    public String getLotes() {
        return lotes;
    }

    public void setLotes(String lotes) {
        this.lotes = lotes;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getModo_txt() {
        return modo_txt;
    }

    public void setModo_txt(String modo_txt) {
        this.modo_txt = modo_txt;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getTarifa_titulo() {
        return tarifa_titulo;
    }

    public void setTarifa_titulo(String tarifa_titulo) {
        this.tarifa_titulo = tarifa_titulo;
    }

    public String getTarifa_iva_incluido() {
        return tarifa_iva_incluido;
    }

    public void setTarifa_iva_incluido(String tarifa_iva_incluido) {
        this.tarifa_iva_incluido = tarifa_iva_incluido;
    }

    public String getTipo_iva() {
        return tipo_iva;
    }

    public void setTipo_iva(String tipo_iva) {
        this.tipo_iva = tipo_iva;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getP_iva() {
        return p_iva;
    }

    public void setP_iva(String p_iva) {
        this.p_iva = p_iva;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id_a);
        parcel.writeString(articulo);
        parcel.writeString(titulo);
        parcel.writeString(p_coste);
        parcel.writeString(familia);
        parcel.writeString(precio_venta);
        parcel.writeString(control_stock);
        parcel.writeString(lotes);
        parcel.writeString(disponibilidad);
        parcel.writeString(descuento);
        parcel.writeString(modo);
        parcel.writeString(modo_txt);
        parcel.writeString(tarifa);
        parcel.writeString(tarifa_titulo);
        parcel.writeString(tarifa_iva_incluido);
        parcel.writeString(tipo_iva);
        parcel.writeString(notas);
        parcel.writeString(unidades);
        parcel.writeString(p_iva);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;

        if (id_a != producto.id_a) return false;
        if (articulo != null ? !articulo.equals(producto.articulo) : producto.articulo != null)
            return false;
        if (control_stock != null ? !control_stock.equals(producto.control_stock) : producto.control_stock != null)
            return false;
        if (descuento != null ? !descuento.equals(producto.descuento) : producto.descuento != null)
            return false;
        if (disponibilidad != null ? !disponibilidad.equals(producto.disponibilidad) : producto.disponibilidad != null)
            return false;
        if (familia != null ? !familia.equals(producto.familia) : producto.familia != null)
            return false;
        if (lotes != null ? !lotes.equals(producto.lotes) : producto.lotes != null) return false;
        if (modo != null ? !modo.equals(producto.modo) : producto.modo != null) return false;
        if (modo_txt != null ? !modo_txt.equals(producto.modo_txt) : producto.modo_txt != null)
            return false;
        if (notas != null ? !notas.equals(producto.notas) : producto.notas != null) return false;
        if (p_coste != null ? !p_coste.equals(producto.p_coste) : producto.p_coste != null)
            return false;
        if (p_iva != null ? !p_iva.equals(producto.p_iva) : producto.p_iva != null) return false;
        if (precio_venta != null ? !precio_venta.equals(producto.precio_venta) : producto.precio_venta != null)
            return false;
        if (tarifa != null ? !tarifa.equals(producto.tarifa) : producto.tarifa != null)
            return false;
        if (tarifa_iva_incluido != null ? !tarifa_iva_incluido.equals(producto.tarifa_iva_incluido) : producto.tarifa_iva_incluido != null)
            return false;
        if (tarifa_titulo != null ? !tarifa_titulo.equals(producto.tarifa_titulo) : producto.tarifa_titulo != null)
            return false;
        if (tipo_iva != null ? !tipo_iva.equals(producto.tipo_iva) : producto.tipo_iva != null)
            return false;
        if (titulo != null ? !titulo.equals(producto.titulo) : producto.titulo != null)
            return false;
        if (unidades != null ? !unidades.equals(producto.unidades) : producto.unidades != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id_a;
        result = 31 * result + (articulo != null ? articulo.hashCode() : 0);
        result = 31 * result + (titulo != null ? titulo.hashCode() : 0);
        result = 31 * result + (p_coste != null ? p_coste.hashCode() : 0);
        result = 31 * result + (familia != null ? familia.hashCode() : 0);
        result = 31 * result + (precio_venta != null ? precio_venta.hashCode() : 0);
        result = 31 * result + (control_stock != null ? control_stock.hashCode() : 0);
        result = 31 * result + (lotes != null ? lotes.hashCode() : 0);
        result = 31 * result + (disponibilidad != null ? disponibilidad.hashCode() : 0);
        result = 31 * result + (descuento != null ? descuento.hashCode() : 0);
        result = 31 * result + (modo != null ? modo.hashCode() : 0);
        result = 31 * result + (modo_txt != null ? modo_txt.hashCode() : 0);
        result = 31 * result + (tarifa != null ? tarifa.hashCode() : 0);
        result = 31 * result + (tarifa_titulo != null ? tarifa_titulo.hashCode() : 0);
        result = 31 * result + (tarifa_iva_incluido != null ? tarifa_iva_incluido.hashCode() : 0);
        result = 31 * result + (tipo_iva != null ? tipo_iva.hashCode() : 0);
        result = 31 * result + (notas != null ? notas.hashCode() : 0);
        result = 31 * result + (unidades != null ? unidades.hashCode() : 0);
        result = 31 * result + (p_iva != null ? p_iva.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id_a=" + id_a +
                ", articulo='" + articulo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", p_coste='" + p_coste + '\'' +
                ", familia='" + familia + '\'' +
                ", precio_venta='" + precio_venta + '\'' +
                ", control_stock='" + control_stock + '\'' +
                ", lotes='" + lotes + '\'' +
                ", disponibilidad='" + disponibilidad + '\'' +
                ", descuento='" + descuento + '\'' +
                ", modo='" + modo + '\'' +
                ", modo_txt='" + modo_txt + '\'' +
                ", tarifa='" + tarifa + '\'' +
                ", tarifa_titulo='" + tarifa_titulo + '\'' +
                ", tarifa_iva_incluido='" + tarifa_iva_incluido + '\'' +
                ", tipo_iva='" + tipo_iva + '\'' +
                ", notas='" + notas + '\'' +
                ", unidades='" + unidades + '\'' +
                ", p_iva='" + p_iva + '\'' +
                '}';
    }

    /* {"PRECIO":121
        "DESCUENTO":0
        "MODO":0
        "MODO_TXT":"Precio y descuento de ficha"
        "TARIFA":
            {"TARIFA":"NOR"
            "TITULO":"Tarifa normal"
            "IVA_INCLUIDO":1}}
    {"TITULO":"CARTUCHO TINTA IMAX C6578A N"
        "PRECIO_VENTA":121
        "P_COSTE":11.148
        "IVA_INCLUIDO":1
        "TIPO_IVA":1
        "NOTAS":""
        "ARTICULO":"PRUEBA"
        "ID_A":23
        "CONTROL_STOCK":1
        "UNIDADES":"-"
        "LOTES":0
        "DISPONIBILIDAD":1
        "P_IVA":21}
    {"ID_A":23
        "ARTICULO":"PRUEBA"
        "TITULO":"CARTUCHO TINTA IMAX C6578A N"
        "P_COSTE":11.148
        "FAMILIA":"VAR"
        "PRECIO_VENTA":121
        "CONTROL_STOCK":1
        "LOTES":0
        "DISPONIBILIDAD":1
        "IMAGENES":0
        "BARRAS":0}*/
}
