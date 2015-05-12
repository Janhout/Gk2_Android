package es.gk2.janhout.gk2_android.modelos;

/**
 * Created by usuario on 27/03/2015.
 */
public class Gasto {
    private String gasto_identificador;
    private String gasto_rutaFoto;
    private String gasto_concepto;
    private Float gasto_importe;
    private Integer gasto_iva;

    public Gasto(String gasto_identificador, String gasto_concepto, Float gasto_importe, Integer gasto_iva) {
        this.gasto_identificador = gasto_identificador;
        this.gasto_concepto = gasto_concepto;
        this.gasto_importe = gasto_importe;
        this.gasto_iva = gasto_iva;
    }

    public String getGasto_identificador() {
        return gasto_identificador;
    }

    public void setGasto_identificador(String gasto_identificador) {
        this.gasto_identificador = gasto_identificador;
    }

    public String getGasto_rutaFoto() {
        return gasto_rutaFoto;
    }

    public void setGasto_rutaFoto(String gasto_rutaFoto) {
        this.gasto_rutaFoto = gasto_rutaFoto;
    }

    public String getGasto_concepto() {
        return gasto_concepto;
    }

    public void setGasto_concepto(String gasto_concepto) {
        this.gasto_concepto = gasto_concepto;
    }

    public Float getGasto_importe() {
        return gasto_importe;
    }

    public void setGasto_importe(Float gasto_importe) {
        this.gasto_importe = gasto_importe;
    }

    public Integer getGasto_iva() {
        return gasto_iva;
    }

    public void setGasto_iva(Integer gasto_iva) {
        this.gasto_iva = gasto_iva;
    }
}
