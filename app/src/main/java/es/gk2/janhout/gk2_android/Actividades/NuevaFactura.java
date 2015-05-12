package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.gk2.janhout.gk2_android.ActionBarActivityBusqueda;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoNuevaFactura;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoNuevoProducto;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoSeleccionarCliente;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoSeleccionarProducto;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;
import es.gk2.janhout.gk2_android.Util.Producto;

public class NuevaFactura extends ActionBarActivityBusqueda implements FragmentoSeleccionarCliente.OnClienteSelectedListener,
        FragmentoSeleccionarProducto.OnProductoListaSelectedListener, FragmentoNuevoProducto.OnProductoSelectedListener {

    private FragmentoNuevaFactura fragmentoPrincipal;
    private FragmentoNuevoProducto fragmentoNuevoProducto;
    private String tituloActividad;
    private SearchView searchView;
    private boolean inicio;

    private final static String TAG_FRAGMENTO_PRINCIPAL = "fragmento_principal";
    private final static String TAG_FRAGMENTO_NUEVO_PRODUCTO = "fragmento_nuevo_producto";

    public static enum ListaFragmentosNuevaFactura {
        ninguno,
        seleccionCliente,
        nuevaFactura,
        seleccionarProducto,
        nuevaLinea
    }

    public static ListaFragmentosNuevaFactura fragmentoActual;

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onBackPressed() {
        switch (fragmentoActual){
            case ninguno:
            case nuevaFactura:
                super.onBackPressed();
                break;
            case seleccionarProducto:
                mostrarFragmentoNuevaLinea(false);
                break;
            case seleccionCliente:
            case nuevaLinea:
                mostrarFragmentoNuevaFactura();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);
        inicio = true;
        if(savedInstanceState != null) {
            inicio = savedInstanceState.getBoolean("ini");
        }
        inicializarToolbar();
        if(inicio) {
            mostrarFragmentoNuevaFactura();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nueva_factura, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search) {
            searchView.setIconified(false);
            return true;
        } else if (id == android.R.id.home) {
            if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente ||
                    fragmentoActual == ListaFragmentosNuevaFactura.nuevaLinea){
                mostrarFragmentoNuevaFactura();
            } else if (fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura){
                finish();
            } else if (fragmentoActual == ListaFragmentosNuevaFactura.seleccionarProducto){
                mostrarFragmentoNuevaLinea(false);
            }
            invalidateOptionsMenu();
            return true;
        } else if(id == R.id.action_guardar_factura){
            if(fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura){
                guardarFactura();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente ||
                fragmentoActual == ListaFragmentosNuevaFactura.seleccionarProducto) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionarProducto){
            searchView.setQueryHint(getString(R.string.hint_busqueda_producto));
        }
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente){
            searchView.setQueryHint(getString(R.string.hint_busqueda_cliente));
        }
        if(fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura ||
                fragmentoActual == ListaFragmentosNuevaFactura.nuevaLinea) {
            menu.findItem(R.id.action_guardar_factura).setVisible(true);
        }else{
            menu.findItem(R.id.action_guardar_factura).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("ini", inicio);
    }

    /* *************************************************************************
     ************************ Override Busqueda ********************************
     *************************************************************************** */

    @Override
    protected void busqueda(String textoBusqueda) {
        Fragment f = null;
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente){
            f = fragmentoClientes(textoBusqueda);
        } else if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionarProducto){
            f = fragmentoProductos(textoBusqueda);
        }
        if (f != null) {
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, f).commit();
        }
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private Fragment fragmentoClientes(String query){
        Fragment fragment = new FragmentoSeleccionarCliente();
        fragmentoActual = ListaFragmentosNuevaFactura.seleccionCliente;
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Fragment fragmentoProductos(String query){
        Fragment fragment = new FragmentoSeleccionarProducto();
        fragmentoActual = ListaFragmentosNuevaFactura.seleccionarProducto;
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void guardarFactura() {
        //TODO
    }

    private void inicializarToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentoActual = ListaFragmentosNuevaFactura.ninguno;
        if(tituloActividad == null) {
            tituloActividad = getTitle().toString();
        }
    }

    private void mostrarFragmentoNuevaFactura(){
        android.app.FragmentManager fm = getFragmentManager();
        fragmentoPrincipal = (FragmentoNuevaFactura)fm.findFragmentByTag(TAG_FRAGMENTO_PRINCIPAL);
        if(fragmentoPrincipal == null){
            fragmentoPrincipal = new FragmentoNuevaFactura();
            FragmentTransaction ft = fm.beginTransaction().add(fragmentoPrincipal, TAG_FRAGMENTO_PRINCIPAL);
            ft.addToBackStack(TAG_FRAGMENTO_PRINCIPAL);
            ft.commit();
            fm.executePendingTransactions();
        }
        fragmentoActual = ListaFragmentosNuevaFactura.nuevaFactura;
        invalidateOptionsMenu();
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragmentoPrincipal, TAG_FRAGMENTO_PRINCIPAL);
        ft.addToBackStack(TAG_FRAGMENTO_PRINCIPAL);
        ft.commit();
        fm.executePendingTransactions();
    }

    public void mostrarFragmentoNuevaLinea(boolean nuevo){
        android.app.FragmentManager fm = getFragmentManager();
        fragmentoNuevoProducto = (FragmentoNuevoProducto)fm.findFragmentByTag(TAG_FRAGMENTO_NUEVO_PRODUCTO);
        FragmentTransaction transaction = fm.beginTransaction();
        if(fragmentoNuevoProducto == null || nuevo){
            fragmentoNuevoProducto = new FragmentoNuevoProducto();
            Bundle b = new Bundle();
            fragmentoNuevoProducto.setArguments(null);
            b.putString("idCliente", "-1");
            fragmentoNuevoProducto.setArguments(b);
        }
        invalidateOptionsMenu();
        transaction.replace(R.id.relativeLayoutFactura, fragmentoNuevoProducto, TAG_FRAGMENTO_NUEVO_PRODUCTO);
        fragmentoActual = ListaFragmentosNuevaFactura.nuevaLinea;
        transaction.addToBackStack(TAG_FRAGMENTO_NUEVO_PRODUCTO);
        transaction.commit();
        fm.executePendingTransactions();
    }

    /* *************************************************************************
     **************** Interfaz OnClienteSelectedListener ***********************
     *************************************************************************** */

    @Override
    public void devolverCliente(Cliente cliente) {
        mostrarFragmentoNuevaFactura();
        fragmentoPrincipal.setCliente(cliente);
    }

    /* *************************************************************************
     **************** Interfaz OnProductoSelectedListener **********************
     *************************************************************************** */

    @Override
    public void devolverProductoLista(Producto producto) {
        mostrarFragmentoNuevaLinea(false);
        fragmentoNuevoProducto.setProducto(producto);
    }

    @Override
    public void devolverProducto(Producto producto) {
        mostrarFragmentoNuevaFactura();
        fragmentoPrincipal.setProducto(producto);
    }
}

/*Fragment fragment = new FragmentoNuevoProducto();
                NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.nuevaLinea;
                Bundle bundle = new Bundle();
                bundle.putParcelable("cliente", clienteSeleccionado);
                fragment.setArguments(bundle);
                FragmentoNuevaFactura.this.getActivity().invalidateOptionsMenu();
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                ft.addToBackStack(null);
                ft.commit();
                getFragmentManager().executePendingTransactions();*/