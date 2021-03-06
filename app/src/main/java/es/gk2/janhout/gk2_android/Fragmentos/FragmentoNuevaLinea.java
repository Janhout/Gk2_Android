package es.gk2.janhout.gk2_android.fragmentos;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.actividades.NuevaFactura;
import es.gk2.janhout.gk2_android.adaptadores.AdaptadorSpinnerIVA;
import es.gk2.janhout.gk2_android.modelos.Cliente;
import es.gk2.janhout.gk2_android.modelos.IVA;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Producto;
import es.gk2.janhout.gk2_android.util.Metodos;

public class FragmentoNuevaLinea extends Fragment implements AsyncTaskGet.OnProcessCompleteListener{

    private NuevaFactura actividad;
    private Cliente cliente;
    private Producto producto;
    private String iva_incluido;

    private EditText etNombreProducto;
    private EditText etCantidadProducto;
    private EditText etPrecioProducto;
    private EditText etDescripcionProducto;
    private EditText etUnidad;

    private Spinner spIva;

    private OnProductoSelectedListener listener;

    private ArrayList<IVA> listaIva;
    private AdaptadorSpinnerIVA adaptador;

    private static final int CODIGO_DATOS_ADICIONALES = 1;
    private static final int CODIGO_OTROS_DATOS = 2;
    private static final int CODIGO_IVA = 3;

    public FragmentoNuevaLinea() {
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actividad = (NuevaFactura)activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = (NuevaFactura)getActivity();
        cliente = this.getArguments().getParcelable("cliente");
        producto = this.getArguments().getParcelable("productoModificar");
        iva_incluido = this.getArguments().getInt("iva_incluido")+"";
        cargarIva();
        cargarView();
        if(producto != null){
            mostrarDatosProducto();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nueva_linea, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_guardar_factura){
            if(NuevaFactura.fragmentoActual == NuevaFactura.ListaFragmentosNuevaFactura.nuevaLinea){
                if(producto == null ||
                        !producto.getArticulo().equals(etNombreProducto.getText().toString())) {
                    producto = new Producto();
                    producto.setArticulo(".");
                    producto.setUnidades(etUnidad.getText().toString());
                    producto.setNotas("");
                    producto.setTarifa_iva_incluido(iva_incluido+"");
                    producto.setDescuento("");
                }
                String precio = etPrecioProducto.getText().toString().equals("") ? "0":etPrecioProducto.getText().toString();
                producto.setPrecio_venta_final(Metodos.stringToDouble(precio)+"");
                String cantidad = etCantidadProducto.getText().toString();
                producto.setCantidad(cantidad);
                producto.setTitulo(etDescripcionProducto.getText().toString());
                if(listaIva != null) {
                    producto.setP_iva(listaIva.get(spIva.getSelectedItemPosition()).getP_iva());
                    producto.setTipo_iva(listaIva.get(spIva.getSelectedItemPosition()).getTipo());
                }
                listener.devolverProducto(producto);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* *************************************************************************
     **************************** Auxiliares... ********************************
     *************************************************************************** */

    private void cargarIva(){
        AsyncTaskGet h = new AsyncTaskGet(actividad, this, Constantes.TIPOS_IVA, false, CODIGO_IVA);
        h.execute(new Hashtable<String, String>());
    }

    private void cargarView(){
        if(getView()!= null) {
            cargarViewProducto();
            cargarViewInfoLinea();
            cargarViewDescripcion();
        }
    }

    private void cargarViewDescripcion(){
        etDescripcionProducto = (EditText)getView().findViewById(R.id.nueva_factura_et_descripcion_producto);
    }

    private void cargarViewInfoLinea(){
        etCantidadProducto = (EditText)getView().findViewById(R.id.nueva_factura_et_cantidad_producto);
        etPrecioProducto = (EditText)getView().findViewById(R.id.nueva_factura_et_precio_producto);
        etUnidad = (EditText)getView().findViewById(R.id.nueva_factura_et_unidad_producto);
        spIva = (Spinner)getView().findViewById(R.id.nueva_factura_sp_iva_producto);
        etPrecioProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && ((EditText)v).getText().toString().equals("")){
                    etPrecioProducto.setText(Metodos.doubleToString(0.00));
                } else if (hasFocus && ((EditText)v).getText().toString().equals(Metodos.doubleToString(0.00))){
                    etPrecioProducto.setText("");
                }
            }
        });
        if(listaIva != null && listaIva.size()>0) {
            if(adaptador == null) {
                adaptador = new AdaptadorSpinnerIVA(actividad, R.layout.detalle_spinner, listaIva);
                adaptador.setDropDownViewResource( R.layout.detalle_spinner);
                spIva.setAdapter(adaptador);
            }
        }
        etPrecioProducto.setText(Metodos.doubleToString(0.00));
        etCantidadProducto.setText("1");
    }

    private void cargarViewProducto(){
        EditText etSeleccionarProducto = (EditText) getView().findViewById(R.id.nueva_factura_et_producto);
        etNombreProducto = (EditText)getView().findViewById(R.id.nueva_factura_et_nombre_producto);
        etSeleccionarProducto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Fragment fragment = new FragmentoSeleccionarProducto();
                    NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.seleccionarProducto;
                    Bundle bundle = new Bundle();
                    bundle.putString("query", "");
                    bundle.putBoolean("listener", true);
                    fragment.setArguments(bundle);
                    FragmentoNuevaLinea.this.getActivity().invalidateOptionsMenu();
                    FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    getFragmentManager().executePendingTransactions();
                }
                return true;
            }
        });
    }

    private void mostrarDatosProducto(){
        etNombreProducto.setText(producto.getArticulo());
        etCantidadProducto.setText(producto.getCantidad());
        etPrecioProducto.setText(producto.getPrecio_venta_final());
        etUnidad.setText(producto.getUnidades());
        etDescripcionProducto.setText(producto.getTitulo());
        boolean encontrado = false;
        String iva = producto.getTipo_iva();
        for (int i = 0; i < listaIva.size() && !encontrado; i++) {
            if(listaIva.get(i).getTipo().equals(iva)){
                encontrado = true;
                if(spIva.getAdapter() == null) {
                    if(adaptador == null) {
                        adaptador = new AdaptadorSpinnerIVA(actividad, R.layout.detalle_spinner, listaIva);
                        adaptador.setDropDownViewResource(R.layout.detalle_spinner);
                    }
                    spIva.setAdapter(adaptador);
                }
                spIva.setSelection(i);
            }
        }
    }

    private void pedirOtrosDatos(){
        AsyncTaskGet otrosDatos = new AsyncTaskGet(actividad, this, Constantes.PRODUCTOS_OTROS_DETALLES, false, CODIGO_OTROS_DATOS);
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("articulo", producto.getArticulo());
        String tarifa = (cliente==null) ? "NOR":cliente.getTarifa();
        parametros.put("tarifa", tarifa);
        parametros.put("unidades", producto.getCantidad());
        String idCliente = (cliente==null) ? "":cliente.getId()+"";
        parametros.put("cliente", idCliente);
        otrosDatos.execute(parametros);
    }

    public void setProductoSeleccionado(Producto producto){
        this.producto = new Producto();
        this.producto.setCantidad(producto.getCantidad());
        this.producto.setArticulo(producto.getArticulo());
        this.producto.setTitulo(producto.getTitulo());
        this.producto.setP_coste(producto.getP_coste());
        this.producto.setId_a(producto.getId_a());
        this.producto.setFamilia(producto.getFamilia());
        this.producto.setPrecio_venta(producto.getPrecio_venta());
        this.producto.setControl_stock(producto.getControl_stock());
        this.producto.setLotes(producto.getLotes());
        this.producto.setDisponibilidad(producto.getDisponibilidad());

        AsyncTaskGet datosAdicionales = new AsyncTaskGet(actividad, this, Constantes.PRODUCTOS_DETALLE, false, CODIGO_DATOS_ADICIONALES);
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("q", producto.getArticulo());
        parametros.put("status", "1");
        datosAdicionales.execute(parametros);
    }

    /* *************************************************************************
     ***************** Interfaz OnProcessCompleteListener **********************
     *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo_peticion) {
        if(respuesta != null){
            JSONObject obj;
            switch (codigo_peticion){
                case CODIGO_DATOS_ADICIONALES:
                    try {
                        obj = new JSONObject(respuesta);
                        producto.datosAdicionales(obj);
                        pedirOtrosDatos();
                    } catch (JSONException ignored) {
                    }
                    break;
                case CODIGO_OTROS_DATOS:
                    try {
                        obj = new JSONObject(respuesta);
                        producto.otrosDetalles(obj);
                        mostrarDatosProducto();
                    } catch (JSONException ignored) {
                    }
                    break;
                case CODIGO_IVA:
                    JSONTokener token = new JSONTokener(respuesta);
                    JSONArray array;
                    try {
                        listaIva = new ArrayList<>();
                        array = new JSONArray(token);
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            IVA f = new IVA(obj);
                            if(!listaIva.contains(f)) {
                                listaIva.add(f);
                            }
                            if(listaIva != null && listaIva.size()>0) {
                                if(adaptador == null) {
                                    adaptador = new AdaptadorSpinnerIVA(actividad,  R.layout.detalle_spinner, listaIva);
                                    adaptador.setDropDownViewResource(R.layout.detalle_spinner);
                                    spIva.setAdapter(adaptador);
                                } else {
                                    adaptador.notifyDataSetChanged();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        listaIva = null;
                    }
                    break;
            }
        }
    }

    /* *************************************************************************
     ******************** Interfaz producto seleccionado ***********************
     *************************************************************************** */

    public interface OnProductoSelectedListener{
        public void devolverProducto(Producto producto);
    }
}