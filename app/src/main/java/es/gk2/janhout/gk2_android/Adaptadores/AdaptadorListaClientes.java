package es.gk2.janhout.gk2_android.Adaptadores;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.Util.Cliente;
import es.gk2.janhout.gk2_android.Fragmentos.FragmentoListaFacturas;
import es.gk2.janhout.gk2_android.R;

public class AdaptadorListaClientes extends ArrayAdapter<Cliente> {

    private Context contexto;
    private ArrayList<Cliente> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaClientes(Context contexto, int recurso, ArrayList<Cliente> datos) {
        super(contexto, recurso, datos);
        this.contexto = contexto;
        this.recurso = recurso;
        this.datos = datos;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.nombreComercial = (TextView) convertView.findViewById(R.id.nombre_cliente);
            vh.btEmail = (Button)convertView.findViewById(R.id.bt_enviar_email);
            vh.btTelefono = (Button)convertView.findViewById(R.id.bt_telefono);
            vh.btFacturas = (Button)convertView.findViewById(R.id.bt_ver_facturas);

            Metodos.botonAwesomeComponente(contexto, vh.btEmail, contexto.getString(R.string.icono_email));
            Metodos.botonAwesomeComponente(contexto, vh.btFacturas, contexto.getString(R.string.icono_facturas));
            Metodos.botonAwesomeComponente(contexto, vh.btTelefono, contexto.getString(R.string.icono_telefono));

            vh.btFacturas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("idCliente", (int)v.getTag());
                    Fragment fragmento = new FragmentoListaFacturas();
                    fragmento.setArguments(bundle);
                    FragmentTransaction transaction = ((ActionBarActivity)contexto).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.relativeLayoutPrincipal, fragmento);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            vh.btTelefono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("mio", (int)v.getTag()+" telefono");
                }
            });

            vh.btEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("mio", (int)v.getTag()+" email");
                }
            });
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.nombreComercial.setText(datos.get(position).getNombre_comercial());

        vh.btFacturas.setTag(datos.get(position).getId());
        vh.btTelefono.setTag(datos.get(position).getId());
        vh.btEmail.setTag(datos.get(position).getId());

        return convertView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] accion = contexto.getResources().getStringArray(R.array.lista_acciones);
        if(accion[position].equalsIgnoreCase("Ver facturas")){
            Bundle bundle = new Bundle();
            bundle.putInt("idCliente", (int)parent.getTag());
            Fragment fragmento = new FragmentoListaFacturas();
            fragmento.setArguments(bundle);
            FragmentTransaction transaction = ((ActionBarActivity)contexto).getFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayoutPrincipal, fragmento);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if(accion[position].compareTo("Acciones")!=0) {
            Log.v("mio", parent.getTag() +  " " +accion[position]);
        }
    }

    public void llamarTelefono() {

    }

    public void enviarEmail(){

    }
    private static class ViewHolder {
        public TextView nombreComercial;
        public Button btTelefono;
        public Button btEmail;
        public Button btFacturas;
    }
}

/* String[] list = contexto.getResources().getStringArray(R.array.lista_acciones);
        AdaptadorSpinnerAcciones ad = new AdaptadorSpinnerAcciones(contexto, R.layout.detalle_spinner, R.id.tvSpinner, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vh.acciones.setAdapter(ad);
        vh.acciones.setOnItemSelectedListener(this);
        vh.acciones.setTag(datos.get(position).getId());*/