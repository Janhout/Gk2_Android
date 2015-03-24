package es.gk2.janhout.gk2_android;

import android.app.Fragment;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class Principal extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] titulos;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        titulos = getResources().getStringArray(R.array.lista_navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.lista_drawer);

        ArrayList<ItemNavigationDrawer> items = new ArrayList<>();
        items.add(new ItemNavigationDrawer(titulos[0],R.mipmap.ic_launcher));
        items.add(new ItemNavigationDrawer(titulos[1],R.mipmap.ic_launcher));

        drawerList.setAdapter(new AdaptadorListaNavigationDrawer(this, R.layout.detelle_elemento_drawer, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.bienvenido,
                R.string.usuario
        ) {
            public void onDrawerClosed(View view) {
                toolbar.setTitle("item");
            }

            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle("hola");
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new FragmentoListaClientes();
                    break;
                case 1:
                    fragment = new FragmentoListaFacturas();
                    break;
            }

            getFragmentManager().beginTransaction().replace(R.id.relativeLayoutPrincipal, fragment).commit();

            drawerList.setItemChecked(position, true);
            setTitle(titulos[position]);
            drawerLayout.closeDrawer(drawerList);
        }
    }
}
