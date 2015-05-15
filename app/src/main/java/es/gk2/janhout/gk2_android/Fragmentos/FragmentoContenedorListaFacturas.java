package es.gk2.janhout.gk2_android.fragmentos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.util.SlidingTabLayout;

public class FragmentoContenedorListaFacturas extends Fragment {

    static class FragmentoListaFacturasItem {
        private boolean todas;
        private int idCliente;
        private int estadoFactura;
        private String query;

        FragmentoListaFacturasItem(boolean todas, int idCliente, int estadoFactura, String query) {
            this.todas = todas;
            this.idCliente = idCliente;
            this.estadoFactura = estadoFactura;
            this.query = query;
        }

        Fragment createFragment() {
            return FragmentoListaFacturas.newInstance(todas, query, idCliente, estadoFactura);
        }

        public boolean isTodas() {
            return todas;
        }

        public int getIdCliente() {
            return idCliente;
        }

        public int getEstadoFactura() {
            return estadoFactura;
        }

        public String getQuery() {
            return query;
        }
    }

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private List<FragmentoListaFacturasItem> mTabs = new ArrayList<FragmentoListaFacturasItem>();
    private int idCliente;
    private String query;
    private boolean todas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCliente = getArguments().getInt("idCliente");
        query = getArguments().getString("query");
        todas = getArguments().getBoolean("todas");

        mTabs.add(new FragmentoListaFacturasItem (todas, idCliente, 2, query));

        mTabs.add(new FragmentoListaFacturasItem (todas, idCliente, 0, query));

        mTabs.add(new FragmentoListaFacturasItem (todas, idCliente, 1, query));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contenedor_lista_facturas, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                switch (mTabs.get(position).getEstadoFactura()){
                    case 0:
                        return Color.RED;//getResources().getColor(R.color.rojo);
                    case 1:
                        return Color.GREEN;//getResources().getColor(R.color.verde);
                    case 2:
                        return Color.YELLOW;//getResources().getColor(R.color.amarillo);
                    default:
                        return Color.BLUE;
                }
            }
        });

    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String titulo = "";
            switch (mTabs.get(position).getEstadoFactura()){
                case 0:
                    titulo = "Impagadas";
                    break;
                case 1:
                    titulo = "Pagadas";
                    break;
                case 2:
                    titulo = "Borradores";
                    break;
            }
            return titulo;
        }
    }


}
