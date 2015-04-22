package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.gk2.janhout.gk2_android.ActionBarActivityBusqueda;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoDatosCliente;
import es.gk2.janhout.gk2_android.R;

public class MostrarCliente extends ActionBarActivityBusqueda{

    private String tituloActividad;
    private Toolbar toolbar;
    private int idCliente;
    private SearchView mSearchView;


    public static enum ListaFragmentosCliente {
        ninguno,
        clienteActual,
        facturas
    }

    public static ListaFragmentosCliente fragmentoActual;


    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);

        idCliente = getIntent().getExtras().getInt("cliente");
        inicializarToolbar();
        cargarFragmentoInicial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostrar_cliente, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_search) {
            mSearchView.setIconified(false);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*if(fragmentoActual == ListaFragmentosCliente.facturas) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarFragmentoInicial(){
        Fragment fragment = new FragmentoDatosCliente();
        fragmentoActual = ListaFragmentosCliente.clienteActual;
        Bundle bundle = new Bundle();
        bundle.putInt("idCliente", idCliente);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.relativeLayoutCliente, fragment).commit();
    }

    private void inicializarToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        fragmentoActual = ListaFragmentosCliente.ninguno;
        if(tituloActividad == null) {
            tituloActividad = getTitle().toString();
        }
    }

    /* *************************************************************************
     ******************************* Auxiliares ********************************
     *************************************************************************** */

    @Override
    protected void busqueda(String textoBusqueda) {
        System.out.println(textoBusqueda);
        //TODO: hacer la busqueda de facturas
    }

    public void setTituloActividad(String tituloActividad){
        this.tituloActividad = tituloActividad;
        getSupportActionBar().setTitle(this.tituloActividad);
    }
}