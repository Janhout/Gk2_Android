package es.gk2.janhout.gk2_android.Estaticas;

public class Constantes {

    public final static String protocolo = "http://";
    public static String usuario = "dev.";
    private final static String urlBase = "gk2web.com/";

    private final static String miUrl = protocolo + usuario + urlBase;
    private final static String URL_API = miUrl + "api/";

    public final static String urlLogin = miUrl + "login/process/";
    public final static String facturas = miUrl + "facturas/";
    public final static String clientes = miUrl + "clientes/";
    public final static String compras = miUrl + "compras/facturas/";
    public final static String pdfUrl = facturas + "pdf/";
    public final static String tiposDireccion = clientes + "get_tipo_direccion/";

    public final static String clientesListar = clientes + "listar/";
    public final static String clientesListarFavoritos = clientes + "listar_favoritos/";
    public final static String clientesAltaCliente = clientes + "guardar/";
    public final static String clientesDetalle = clientes + "detalle/";
    public final static String CLIENTES_CONSULTA_NIF = URL_API + "checknif/";
}
