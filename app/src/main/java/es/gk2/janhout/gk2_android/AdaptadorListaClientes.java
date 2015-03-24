package es.gk2.janhout.gk2_android;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListaClientes extends ArrayAdapter<Cliente> implements Spinner.OnItemSelectedListener {

    private Context contexto;
    private ArrayList<Cliente> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaClientes(Context contexto, int recurso, ArrayList<Cliente> datos) {
        super(contexto, recurso, datos);
        this.contexto = contexto;
        this.recurso = recurso;
        this.datos = datos;
        this.inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.nombreComercial = (TextView) convertView.findViewById(R.id.nombre_cliente);
            vh.acciones = (Spinner) convertView.findViewById(R.id.sp_acciones);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.nombreComercial.setText(datos.get(position).getNombre_comercial());
        String[] list = contexto.getResources().getStringArray(R.array.lista_acciones);
        AdaptadorSpinnerAcciones ad = new AdaptadorSpinnerAcciones(contexto, R.layout.detalle_spinner, list);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contexto, R.array.lista_acciones, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vh.acciones.setAdapter(ad);
        vh.acciones.setOnItemSelectedListener(this);
        vh.acciones.setTag(position);

        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] accion = contexto.getResources().getStringArray(R.array.lista_acciones);
        if(accion[position].equalsIgnoreCase("Ver facturas")){
            Fragment newFragment = new FragmentoListaFacturas();
            FragmentTransaction transaction = ((ActionBarActivity)contexto).getFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayoutPrincipal, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if(accion[position].compareTo("Acciones")!=0) {
            Log.v("mio", parent.getTag() +  " " +accion[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class ViewHolder {
        public TextView nombreComercial;
        public Spinner acciones;
    }
}