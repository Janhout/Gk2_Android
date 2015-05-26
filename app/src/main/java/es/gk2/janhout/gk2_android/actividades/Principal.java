package es.gk2.janhout.gk2_android.actividades;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.adaptadores.AdaptadorListaNavigationDrawer;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoContenedorListaFacturas;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoListaClientes;
import es.gk2.janhout.gk2_android.fragmentos.FragmentoListaCompras;
import es.gk2.janhout.gk2_android.modelos.ItemNavigationDrawer;
import es.gk2.janhout.gk2_android.util.Metodos;

public class Principal extends AppCompatActivityBusqueda {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] titulos;
    private boolean inicio;
    private SearchView mSearchView;
    private static Dialog dialogo;
    private static boolean mostrarDialogo;

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
            case facturas:
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
        } else if(id == R.id.action_nueva_factura){
            nuevaFactura();
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
        if(drawerOpen) {
            menu.findItem(R.id.action_nuevo_cliente).setVisible(!drawerOpen);
            menu.findItem(R.id.action_nuevoGasto).setVisible(!drawerOpen);
            menu.findItem(R.id.action_search).setVisible(!drawerOpen);
            menu.findItem(R.id.action_nueva_factura).setVisible(!drawerOpen);
        } else {
            if (fragmentoActual == ListaFragmentosPrincipal.clientes) {
                menu.findItem(R.id.action_nuevo_cliente).setVisible(true);
            } else {
                menu.findItem(R.id.action_nuevo_cliente).setVisible(false);
            }
            if (fragmentoActual == ListaFragmentosPrincipal.facturas) {
                menu.findItem(R.id.action_nueva_factura).setVisible(true);
            } else {
                menu.findItem(R.id.action_nueva_factura).setVisible(false);
            }
            if (fragmentoActual == ListaFragmentosPrincipal.facturas ||
                    fragmentoActual == ListaFragmentosPrincipal.clientes) {
                menu.findItem(R.id.action_search).setVisible(true);
            } else {
                menu.findItem(R.id.action_search).setVisible(false);
            }
            if (fragmentoActual == ListaFragmentosPrincipal.compras) {
                menu.findItem(R.id.action_nuevoGasto).setVisible(true);
            } else {
                menu.findItem(R.id.action_nuevoGasto).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mostrarDialogo = savedInstanceState.getBoolean("mostrarDialogo");
        tituloActividad = savedInstanceState.getString("tituloActividad");
        fragmentoActual = (ListaFragmentosPrincipal)savedInstanceState.getSerializable("actual");
        getSupportActionBar().setTitle(tituloActividad);
        if(mostrarDialogo){
            mostrarDialogo(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mostrarDialogo", mostrarDialogo);
        outState.putString("tituloActividad", tituloActividad);
        outState.putBoolean("fav", inicio);
        outState.putSerializable("actual", fragmentoActual);
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
            Fragment fragment = fragmentoClientes(true, "");
            getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();
            drawerList.setItemChecked(1, true);
            setTituloActividad(tituloActividad + " - " + titulos[1]);
        }
    }

    public void cerrarDialogo(){
        if(dialogo != null) {
            dialogo.dismiss();
        }
        mostrarDialogo = false;
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
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(getString(R.string.titulo_navigation_drawer));
                invalidateOptionsMenu();
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

    public void mostrarDialogo(Context contexto){
        dialogo = new Dialog(contexto, android.R.style.Theme_Panel);
        dialogo.setCancelable(false);
        mostrarDialogo = true;
        dialogo.show();
    }

    public void setTituloActividad(String tituloActividad){
        this.tituloActividad = tituloActividad;
        getSupportActionBar().setTitle(this.tituloActividad);
    }

    /* *************************************************************************
     ************************ Override Busqueda ********************************
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
        } else if (fragmentoActual == ListaFragmentosPrincipal.facturas) {
            /*f = new FragmentoListaFacturas();
            Bundle bundle = new Bundle();
            bundle.putBoolean("t odo", true);
            bundle.putString("query", textoBusqueda);
            f.setArguments(bundle);*/
            f = new FragmentoContenedorListaFacturas();
            Bundle bundle = new Bundle();
            bundle.putBoolean("todas", true);
            bundle.putString("query", textoBusqueda);
            f.setArguments(bundle);
            fragmentoActual = ListaFragmentosPrincipal.facturas;
        }
        if (f != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, f).commit();
        }
    }

    /* *************************************************************************
     ******************** Métodos items menú ***********************************
     *************************************************************************** */

    private void nuevaFactura() {
        startActivity(new Intent(this, NuevaFactura.class));
    }

    private void nuevoCliente() {
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
        Intent i;
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
                /*fragment = new FragmentoListaFacturas();
                Bundle bundle = new Bundle();
                bundle.putBoolean("t odo", true);
                bundle.putString("query", "");
                fragment.setArguments(bundle);*/
                fragmentoActual = ListaFragmentosPrincipal.facturas;
                fragment = new FragmentoContenedorListaFacturas();
                Bundle bundle = new Bundle();
                bundle.putBoolean("todas", true);
                bundle.putString("query", "");
                fragment.setArguments(bundle);
                break;

            case 3:
                fragment = new FragmentoListaCompras();
                fragmentoActual = ListaFragmentosPrincipal.compras;
                break;

            case 4:
                i = new Intent(this, NuevoProducto.class);
                startActivity(i);
                break;

            case 5:
                Metodos.borrarPreferenciasCompartidas(this);
                i = new Intent(this, Login.class);
                startActivity(i);
                this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_in_left);
                break;
        }

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();

            drawerList.setItemChecked(position, true);
            setTituloActividad(titulos[position]);
            drawerLayout.closeDrawer(drawerList);
        }
    }
}