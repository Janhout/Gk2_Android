package es.gk2.janhout.gk2_android.actividades;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.gk2.janhout.gk2_android.fragmentos.FragmentoNuevaFactura;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoNuevaLinea;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoSeleccionarCliente;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoSeleccionarProducto;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Cliente;
import es.gk2.janhout.gk2_android.modelos.Producto;

public class NuevaFactura extends AppCompatActivityBusqueda implements FragmentoSeleccionarCliente.OnClienteSelectedListener,
        FragmentoSeleccionarProducto.OnProductoListaSelectedListener, FragmentoNuevaLinea.OnProductoSelectedListener {

    private FragmentoNuevaFactura fragmentoPrincipal;
    private FragmentoNuevaLinea fragmentoNuevaLinea;
    private String tituloActividad;
    private SearchView searchView;
    private boolean inicio;
    private Cliente clienteFactura;

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
                finish();
                break;
            case seleccionarProducto:
                FragmentoNuevaFactura.productoModificar = -1;
                mostrarFragmentoNuevaLinea(false, null);
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
            clienteFactura = savedInstanceState.getParcelable("clienteFactura");
        }
        inicializarToolbar();
        if(inicio) {
            Bundle b = getIntent().getExtras();
            if(b != null) {
                clienteFactura = b.getParcelable("cliente");
            }
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
            } else if (fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura ||
                    fragmentoActual == ListaFragmentosNuevaFactura.ninguno){
                finish();
            } else if (fragmentoActual == ListaFragmentosNuevaFactura.seleccionarProducto){
                FragmentoNuevaFactura.productoModificar = -1;
                mostrarFragmentoNuevaLinea(false, null);
            }
            invalidateOptionsMenu();
            return true;
        } else if(id == R.id.action_guardar_factura){
            if(fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura){
                return false;
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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragmentoActual = (ListaFragmentosNuevaFactura)savedInstanceState.getSerializable("fragmentoActual");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ini", inicio);
        outState.putParcelable("clienteFactura", clienteFactura);
        outState.putSerializable("fragmentoActual", fragmentoActual);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, f).commit();
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

    public Cliente getClienteFactura(){
        return clienteFactura;
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
        FragmentManager fm = getSupportFragmentManager();
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragmentoPrincipal, TAG_FRAGMENTO_PRINCIPAL);
        ft.addToBackStack(TAG_FRAGMENTO_PRINCIPAL);
        ft.commit();
        fm.executePendingTransactions();
    }

    public void mostrarFragmentoNuevaLinea(boolean nuevo, Producto productoModificar){
        FragmentManager fm = getSupportFragmentManager();
        fragmentoNuevaLinea = (FragmentoNuevaLinea)fm.findFragmentByTag(TAG_FRAGMENTO_NUEVO_PRODUCTO);
        FragmentTransaction transaction = fm.beginTransaction();
        if(fragmentoNuevaLinea == null || nuevo){
            fragmentoNuevaLinea = new FragmentoNuevaLinea();
            Bundle b = new Bundle();
            fragmentoNuevaLinea.setArguments(null);
            b.putParcelable("cliente", clienteFactura);
            b.putParcelable("productoModificar", productoModificar);
            fragmentoNuevaLinea.setArguments(b);
        }
        invalidateOptionsMenu();
        transaction.replace(R.id.relativeLayoutFactura, fragmentoNuevaLinea, TAG_FRAGMENTO_NUEVO_PRODUCTO);
        fragmentoActual = ListaFragmentosNuevaFactura.nuevaLinea;
        transaction.addToBackStack(TAG_FRAGMENTO_NUEVO_PRODUCTO);
        transaction.commit();
        fm.executePendingTransactions();
    }

    public void setInicio(boolean inicio){
        this.inicio = inicio;
    }

    /* *************************************************************************
     **************** Interfaz OnClienteSelectedListener ***********************
     *************************************************************************** */

    @Override
    public void devolverCliente(Cliente cliente) {
        this.clienteFactura = cliente;
        mostrarFragmentoNuevaFactura();
        fragmentoPrincipal.setCliente();
    }

    /* *************************************************************************
     **************** Interfaz OnProductoSelectedListener **********************
     *************************************************************************** */

    @Override
    public void devolverProductoLista(Producto producto) {
        FragmentoNuevaFactura.productoModificar = -1;
        mostrarFragmentoNuevaLinea(false, null);
        fragmentoNuevaLinea.setProducto(producto);
    }

    @Override
    public void devolverProducto(Producto producto) {
        mostrarFragmentoNuevaFactura();
        fragmentoPrincipal.setProducto(producto);
    }
}

/*Fragment fragment = new FragmentoNuevaLinea();
                NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.nuevaLinea;
                Bundle bundle = new Bundle();
                bundle.putParcelable("cliente", clienteSeleccionado);
                fragment.setArguments(bundle);
                FragmentoNuevaFactura.this.getActivity().invalidateOptionsMenu();
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                ft.addToBackStack(null);
                ft.commit();
                getFragmentManager().executePendingTransactions();*/