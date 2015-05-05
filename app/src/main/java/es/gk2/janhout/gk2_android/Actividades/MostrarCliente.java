package es.gk2.janhout.gk2_android.Actividades;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
    private int idCliente;
    private SearchView searchView;
    private static Dialog dialogo;
    private static boolean mostrarDialogo;
    private static boolean inicio;
    public static ItemMenuPulsado escuchadorMenu;

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
    public void onBackPressed() {
        invalidateOptionsMenu();
        switch (fragmentoActual){
            case ninguno:
            case clienteActual:
                super.onBackPressed();
                break;
            case facturas:
                inicio = true;
                cargarFragmentoInicial();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);
        inicio = true;
        if(savedInstanceState != null) {
            inicio = savedInstanceState.getBoolean("ini");
        }
        idCliente = getIntent().getExtras().getInt("cliente");
        inicializarToolbar();
        cargarFragmentoInicial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostrar_cliente, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
            if(fragmentoActual == ListaFragmentosCliente.clienteActual) {
                finish();
            } else if (fragmentoActual == ListaFragmentosCliente.facturas){
                inicio = true;
                cargarFragmentoInicial();
            }
            invalidateOptionsMenu();
            return true;
        } else if (id == R.id.action_llamar){
            escuchadorMenu.itemMenuPulsado(R.id.action_llamar, null);
            invalidateOptionsMenu();
            return true;
        } else if (id == R.id.action_email) {
            escuchadorMenu.itemMenuPulsado(R.id.action_email, null);
            invalidateOptionsMenu();
            return true;
        } else if (id == R.id.action_facturas) {
            escuchadorMenu.itemMenuPulsado(R.id.action_facturas, "");
            invalidateOptionsMenu();
            return true;
        } else if(id == R.id.action_nueva_factura){
            //TODO: navagacion... ver a donde volvemos despues de añadir facturas(cancelar o aceptar)
            Intent i = new Intent(this, NuevaFactura.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(fragmentoActual == ListaFragmentosCliente.facturas) {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_nueva_factura).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.action_nueva_factura).setVisible(false);
        }
        if(fragmentoActual == ListaFragmentosCliente.clienteActual){
            menu.findItem(R.id.action_llamar).setVisible(true);
            menu.findItem(R.id.action_email).setVisible(true);
            menu.findItem(R.id.action_facturas).setVisible(true);
        } else {
            menu.findItem(R.id.action_llamar).setVisible(false);
            menu.findItem(R.id.action_email).setVisible(false);
            menu.findItem(R.id.action_facturas).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mostrarDialogo = savedInstanceState.getBoolean("mostrarDialogo");
        fragmentoActual = (ListaFragmentosCliente)savedInstanceState.getSerializable("actual");
        if(mostrarDialogo){
            mostrarDialogo(this);
        }
        setTituloActividad(savedInstanceState.getString("tituloActividad"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mostrarDialogo", mostrarDialogo);
        outState.putBoolean("ini", inicio);
        outState.putSerializable("actual", fragmentoActual);
        outState.putString("tituloActividad", tituloActividad);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cerrarDialogo();
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarFragmentoInicial(){
        if(inicio) {
            Fragment fragment = new FragmentoDatosCliente();
            fragmentoActual = ListaFragmentosCliente.clienteActual;
            setTituloActividad(getString(R.string.title_activity_mostrar_cliente));
            Bundle bundle = new Bundle();
            bundle.putInt("idCliente", idCliente);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutCliente, fragment).commit();
        }
    }

    public static void setInicio(boolean inicio){
        MostrarCliente.inicio = inicio;
    }

    private void inicializarToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        escuchadorMenu.itemMenuPulsado(R.id.action_facturas, textoBusqueda);
    }

    public void setTituloActividad(String tituloActividad){
        this.tituloActividad = tituloActividad;
        getSupportActionBar().setTitle(this.tituloActividad);
    }

    public void mostrarDialogo(Context contexto){
        dialogo = new Dialog(contexto, android.R.style.Theme_Panel);
        dialogo.setCancelable(false);
        mostrarDialogo = true;
        dialogo.show();
    }

    public void cerrarDialogo(){
        if(dialogo != null) {
            dialogo.dismiss();
        }
        mostrarDialogo = false;
    }

    /* *************************************************************************
     ******************** Interfaz Menú Pulsado ********************************
     *************************************************************************** */

    public interface ItemMenuPulsado {
        public void itemMenuPulsado(int itemMenu, String query);
    }
}