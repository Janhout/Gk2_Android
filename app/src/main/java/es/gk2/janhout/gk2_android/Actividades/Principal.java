package es.gk2.janhout.gk2_android.Actividades;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaNavigationDrawer;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaClientes;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaFacturas;
import es.gk2.janhout.gk2_android.ItemNavigationDrawer;
import es.gk2.janhout.gk2_android.R;


public class Principal extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] titulos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        titulos = getResources().getStringArray(R.array.lista_navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.lista_drawer);

        ArrayList<ItemNavigationDrawer> items = new ArrayList<>();
        items.add(new ItemNavigationDrawer(titulos[0],R.mipmap.ic_launcher));
        items.add(new ItemNavigationDrawer(titulos[1],R.mipmap.ic_launcher));

        drawerList.setAdapter(new AdaptadorListaNavigationDrawer(this, R.layout.detelle_elemento_drawer, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
