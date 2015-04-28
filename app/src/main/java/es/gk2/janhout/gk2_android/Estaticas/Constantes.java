package es.gk2.janhout.gk2_android.Estaticas;

public class Constantes {

    public final static String PROTOCOLO = "http://";
    public static String USUARIO = "dev.";
    private final static String URL_BASE = "gk2web.com/";

    private final static String MI_URL = PROTOCOLO + USUARIO + URL_BASE;
    private final static String URL_API = MI_URL + "api/";

    public final static String URL_LOGIN = MI_URL + "login/process/";
    public final static String FACTURAS = MI_URL + "facturas/";
    public final static String CLIENTES = MI_URL + "clientes/";
    public final static String COMPRAS = MI_URL + "compras/facturas/";
    public final static String PDF_URL = FACTURAS + "pdf/";

    public final static String CLIENTES_LISTAR = CLIENTES + "listar/";
    public final static String CLIENTES_LISTAR_FAVORITOS = CLIENTES + "listar_favoritos/";
    public final static String CLIENTES_ALTA_CLIENTE = CLIENTES + "guardar/";
    public final static String CLIENTES_DETALLE = CLIENTES + "detalle/";
    public final static String CLIENTES_CONSULTA_NIF = URL_API + "checknif/";

    public final static String SET_FAVORITO = CLIENTES + "favorito_set/";
    public final static String UNSET_FAVORITO = CLIENTES + "favorito_unset/";

    //Datos
    public final static String LOCALIDADES = CLIENTES + "get_localidades/ESP/";
    public final static String PROVINCIAS = CLIENTES + "get_provincias/ESP/";
    public final static String TIPOS_DIRECCION = CLIENTES + "get_tipo_direccion/";

}
