package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaNavigationDrawer;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaClientes;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaCompras;
import es.gk2.janhout.gk2_android.ItemNavigationDrawer;
import es.gk2.janhout.gk2_android.R;

public class Principal extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] titulos;

    private ActionBarDrawerToggle drawerToggle;
    private String tituloActividad;

    public static enum ListaFragmentos {
        ninguno,
        clientes,
        compras,
        facturas,
        gastos
    }
    public static ListaFragmentos fragmentoActual;

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onBackPressed() {
        switch (fragmentoActual){
            case ninguno:
                super.onBackPressed();
                break;
            case clientes:
                super.onBackPressed();
                break;
            case compras:
            case facturas:
            case gastos:
                seleccionarItem(0);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        fragmentoActual = ListaFragmentos.ninguno;
        if(tituloActividad == null) {
            tituloActividad = getTitle().toString();
        }
        inicializarDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Metodos.borrarPreferenciasCompartidas(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_nuevoGasto) {
            nuevoGasto();
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
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        if(fragmentoActual == ListaFragmentos.compras){
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
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

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

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.open_drawer, R.string.close_drawer) {
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

    /* *************************************************************************
     ******************** Métodos items menú ***********************************
     *************************************************************************** */

    public void nuevoGasto() {
        Intent intent = new Intent(this, NuevoGasto.class);
        startActivity(intent);
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
        switch (position){
            case -1:
                break;
            case 0:
                fragment = new FragmentoListaClientes();
                fragmentoActual = ListaFragmentos.clientes;
                break;
            case 1:
                fragment = new FragmentoListaCompras();
                fragmentoActual = ListaFragmentos.compras;
                break;
        }

        getFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();

        drawerList.setItemChecked(position, true);
        tituloActividad = titulos[position];
        getSupportActionBar().setTitle(tituloActividad);
        drawerLayout.closeDrawer(drawerList);
    }
}




/*
    Paginador
    Navegación
    Nuevo gasto?
    Acceso a nuevo gasto
    TimerOut consultas
    Base de datos????
    ActionBar, estilos titulos e iconos
    menus - eliminar los innecesarios
    Compras
 */