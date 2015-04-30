package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.gk2.janhout.gk2_android.ActionBarActivityBusqueda;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoNuevaFactura;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoSeleccionarCliente;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class NuevaFactura extends ActionBarActivityBusqueda implements FragmentoSeleccionarCliente.OnClienteSelectedListener{

    private FragmentoNuevaFactura fragmentoPrincipal;
    private String tituloActividad;
    private SearchView searchView;

    private final static String TAG_FRAGMENTO_PRINCIPAL = "fragmento_principal";

    public static enum ListaFragmentosNuevaFactura {
        ninguno,
        seleccionCliente,
        nuevaFactura,
        nuevaLinea,
        nuevoProducto
    }

    public static ListaFragmentosNuevaFactura fragmentoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);
        inicializarToolbar();
        mostrarFragmentoNuevaFactura();
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
        searchView.setQueryHint(getString(R.string.hint_busqueda_cliente));
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
                    fragmentoActual == ListaFragmentosNuevaFactura.nuevaLinea ||
                    fragmentoActual == ListaFragmentosNuevaFactura.nuevoProducto){
                mostrarFragmentoNuevaFactura();
            } else if (fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura){
                finish();
            }
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }
        if(fragmentoActual == ListaFragmentosNuevaFactura.nuevaFactura) {
            menu.findItem(R.id.action_guardar_factura).setVisible(true);
        }else{
            menu.findItem(R.id.action_guardar_factura).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void busqueda(String textoBusqueda) {
        Fragment f = null;
        if(fragmentoActual == ListaFragmentosNuevaFactura.seleccionCliente){
            f = fragmentoClientes(textoBusqueda);
        }
        if (f != null) {
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, f).commit();
        }
    }

    private Fragment fragmentoClientes(String query){
        Fragment fragment = new FragmentoSeleccionarCliente();
        fragmentoActual = ListaFragmentosNuevaFactura.seleccionCliente;
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
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

    @Override
    public void devolverCliente(Cliente cliente) {
        mostrarFragmentoNuevaFactura();
        fragmentoPrincipal.setCliente(cliente);
    }
}
