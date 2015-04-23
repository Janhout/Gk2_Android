package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.ActionBarActivityBusqueda;
import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaNavigationDrawer;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaClientes;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaCompras;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaFacturas;
import es.gk2.janhout.gk2_android.ItemNavigationDrawer;
import es.gk2.janhout.gk2_android.R;

public class Principal extends ActionBarActivityBusqueda {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] titulos;
    private boolean inicio;
    private SearchView mSearchView;

    private ActionBarDrawerToggle drawerToggle;
    private String tituloActividad;

    private static enum ListaFragmentosPrincipal {
        ninguno,
        clientes,
        compras,
        gastos,
        facturas,
        clientes_favoritos
    }
    public static ListaFragmentosPrincipal fragmentoActual;

    private Toolbar toolbar;

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onBackPressed() {
        switch (fragmentoActual){
            case ninguno:
            case clientes_favoritos:
            case clientes:
                super.onBackPressed();
                break;
            case compras:
            case gastos:
                seleccionarItem(0);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        inicio = true;
        if(savedInstanceState != null) {
            inicio = savedInstanceState.getBoolean("fav");
        }
        inicializarToolbar();
        inicializarDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        cargarFragmentoInicial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_nuevo_cliente) {
            nuevoCliente();
            return true;
        } else if(id == R.id.action_nuevoGasto) {
            nuevoGasto();
            return true;
        } else if(id == R.id.action_search) {
            mSearchView.setIconified(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //ocultar todas las opocines de menu o mostrarlas
        menu.findItem(R.id.action_nuevo_cliente).setVisible(!drawerOpen);
        if(fragmentoActual == ListaFragmentosPrincipal.clientes) {
            menu.findItem(R.id.action_nuevo_cliente).setVisible(true);
        } else {
            menu.findItem(R.id.action_nuevo_cliente).setVisible(false);
        }

        if(fragmentoActual == ListaFragmentosPrincipal.facturas ||
                fragmentoActual == ListaFragmentosPrincipal.clientes) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }

        if(fragmentoActual == ListaFragmentosPrincipal.compras){
            menu.findItem(R.id.action_nuevoGasto).setVisible(true);
        }else{
            menu.findItem(R.id.action_nuevoGasto).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tituloActividad = savedInstanceState.getString("tituloActividad");
        getSupportActionBar().setTitle(tituloActividad);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tituloActividad", tituloActividad);
        outState.putBoolean("fav", inicio);
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    @Override
    protected void busqueda(String textoBusqueda) {
        Fragment f = null;
        if (fragmentoActual == ListaFragmentosPrincipal.clientes) {
            f = fragmentoClientes(false, textoBusqueda);
        } else if (fragmentoActual == ListaFragmentosPrincipal.compras) {
            f = null;
        } else if (fragmentoActual == ListaFragmentosPrincipal.gastos) {
            f = null;
        }
        if (f != null) {
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, f).commit();
        }
    }

    private void cargarFragmentoInicial(){
        if(inicio) {
            Fragment fragment = fragmentoClientes(true, "");
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();
            drawerList.setItemChecked(1, true);
            setTituloActividad(tituloActividad + " - " + titulos[1]);
        }
    }

    private Fragment fragmentoClientes(boolean favorito, String query){
        Fragment fragment = new FragmentoListaClientes();
        if(favorito){
            fragmentoActual = ListaFragmentosPrincipal.clientes_favoritos;
        } else {
            fragmentoActual = ListaFragmentosPrincipal.clientes;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("favorito", favorito);
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void inicializarDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        titulos = getResources().getStringArray(R.array.lista_titulos_navigation_drawer);
        String[] iconos = getResources().getStringArray(R.array.lista_iconos_navigation_drawer);

        drawerList = (ListView) findViewById(R.id.lista_drawer);

        ArrayList<ItemNavigationDrawer> items = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++) {
            items.add(new ItemNavigationDrawer(titulos[i], iconos[i]));
        }
        drawerList.setAdapter(new AdaptadorListaNavigationDrawer(this, R.layout.detelle_elemento_drawer, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(tituloActividad);
                supportInvalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(getString(R.string.titulo_navigation_drawer));
                supportInvalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void inicializarToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        fragmentoActual = ListaFragmentosPrincipal.ninguno;
        if(tituloActividad == null) {
            tituloActividad = getTitle().toString();
        }
    }

    public void setTituloActividad(String tituloActividad){
        this.tituloActividad = tituloActividad;
        getSupportActionBar().setTitle(this.tituloActividad);
    }

    /* *************************************************************************
     ******************** Métodos items menú ***********************************
     *************************************************************************** */
    private void nuevoCliente() {
        Log.v("mio", "metodo nuevo cliente");
        startActivity(new Intent(this, NuevoCliente.class));
    }


    private void nuevoGasto() {
        startActivity(new Intent(this, NuevoGasto.class));
    }

    /* *************************************************************************
     ******************** Gestion Navigation Drawer... *************************
     *************************************************************************** */

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            seleccionarItem(position);
        }
    }

    private void seleccionarItem(int position) {
        Fragment fragment = null;
        inicio = false;
        switch (position){
            case 0:
                fragment = fragmentoClientes(false, "");
                fragmentoActual = ListaFragmentosPrincipal.clientes;
                break;
            case 1:
                fragment = fragmentoClientes(true, "");
                fragmentoActual = ListaFragmentosPrincipal.clientes_favoritos;
                break;
            case 2:
                fragment = new FragmentoListaFacturas();
                Bundle bundle = new Bundle();
                bundle.putBoolean("todo", true);
                fragment.setArguments(bundle);
                fragmentoActual = ListaFragmentosPrincipal.facturas;
                break;
            case 3:
                fragment = new FragmentoListaCompras();
                fragmentoActual = ListaFragmentosPrincipal.compras;
                break;
            case 4:
                Metodos.borrarPreferenciasCompartidas(this);
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                this.finish();
                break;
        }

        if(position != 4) {
            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();

            drawerList.setItemChecked(position, true);
            setTituloActividad(titulos[position]);
            drawerLayout.closeDrawer(drawerList);
        }
    }
}




/*
    Navegación
    Nuevo gasto?                                        NO SE SABE QUE HACER
    Acceso a nuevo gasto                                NO SE SABE QUE HACER
    TimerOut consultas
    Base de datos????                                   NO SE SABE QUE HACER
    ActionBar, estilos titulos e iconos                 OK?
    menus - eliminar los innecesarios                   CUANDO SE TENGA TODO LO QUE VAMOS A HACER
    Compras                                             NO SE SABE QUE HACER
    buscar a la toolbar                                 OK - afinar resultados
    dialogo progreso, casca la aplicaccion al perder referencia.
 */