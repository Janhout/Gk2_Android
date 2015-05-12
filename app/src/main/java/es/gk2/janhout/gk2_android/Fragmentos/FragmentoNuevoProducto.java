package es.gk2.janhout.gk2_android.Fragmentos;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Actividades.NuevaFactura;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Producto;

public class FragmentoNuevoProducto extends Fragment implements AsyncTaskGet.OnProcessCompleteListener{

    private NuevaFactura actividad;
    private String idCliente;
    private Producto producto;

    private EditText etNombreProducto;
    private EditText etCantidadProducto;
    private EditText etPrecioProducto;
    private EditText etDescripcionProducto;
    private EditText etUnidad;

    private Spinner spIva;

    private OnProductoSelectedListener listener;

    private static final int CODIGO_DATOS_ADICIONALES = 1;
    private static final int CODIGO_OTROS_DATOS = 2;

    public FragmentoNuevoProducto() {
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
        idCliente = this.getArguments().getString("idCliente");
        if(idCliente == null){
            idCliente = "";
        }
        cargarView();
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
        return inflater.inflate(R.layout.fragment_nuevo_producto, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_guardar_factura){
            if(NuevaFactura.fragmentoActual == NuevaFactura.ListaFragmentosNuevaFactura.nuevaLinea){
                if(producto == null) {
                    producto = new Producto();
                }
                producto.setPrecio_venta_final(etPrecioProducto.getText().toString());
                producto.setCantidad(etCantidadProducto.getText().toString());
                producto.setArticulo(etNombreProducto.getText().toString());
                producto.setUnidades(etUnidad.getText().toString());
                producto.setNotas(etDescripcionProducto.getText().toString());
                producto.setP_iva(spIva.getSelectedItem().toString());
                producto.setTarifa_iva_incluido("1");
                listener.devolverProducto(producto);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

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

        etPrecioProducto.setText("0.00");
        etCantidadProducto.setText("1");
        spIva.setSelection(1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.lista_iva, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIva.setAdapter(adapter);
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
                    fragment.setArguments(bundle);
                    FragmentoNuevoProducto.this.getActivity().invalidateOptionsMenu();
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
        etDescripcionProducto.setText(producto.getNotas());
        //TODO poner iva prodcuto
    }

    private void pedirOtrosDatos(){
        AsyncTaskGet otrosDatos = new AsyncTaskGet(actividad, this, Constantes.PRODUCTOS_OTROS_DETALLES, false, CODIGO_OTROS_DATOS);
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("articulo", producto.getArticulo());
        parametros.put("tarifa", "NOR");
        parametros.put("unidades", producto.getCantidad());
        parametros.put("cliente", idCliente);
        otrosDatos.execute(parametros);
    }

    public void setProducto(Producto producto){
        this.producto = producto;
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