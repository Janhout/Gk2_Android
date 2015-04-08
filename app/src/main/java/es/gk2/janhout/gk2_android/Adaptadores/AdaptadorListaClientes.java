package es.gk2.janhout.gk2_android.Adaptadores;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.nombreComercial = (TextView) convertView.findViewById(R.id.nombre_cliente);
            vh.btEmail = (Button)convertView.findViewById(R.id.bt_enviar_email);
            vh.btTelefono = (Button)convertView.findViewById(R.id.bt_telefono);
            vh.btFacturas = (Button)convertView.findViewById(R.id.bt_ver_facturas);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }



        Metodos.botonAwesomeComponente(contexto, vh.btEmail, contexto.getString(R.string.icono_email));
        Metodos.botonAwesomeComponente(contexto, vh.btFacturas, contexto.getString(R.string.icono_facturas));
        Metodos.botonAwesomeComponente(contexto, vh.btTelefono, contexto.getString(R.string.icono_telefono));
        vh.btFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFacturas(position);
            }
        });

        vh.btTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTelefono(position);
            }
        });

        vh.btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEmail(position);
            }
        });

        if(datos.get(position).getTelefono01().trim().equals("") && datos.get(position).getTelefono02().trim().equals("")){
            vh.btTelefono.setEnabled(false);
        }else{
            vh.btTelefono.setEnabled(true);
        }
        if(datos.get(position).getEmail().trim().equals("")){
            vh.btEmail.setEnabled(false);
        }else{
            vh.btEmail.setEnabled(true);
        }
        vh.nombreComercial.setText(datos.get(position).getNombre_comercial());
        return convertView;
    }

    private void clickEmail(int position){
        if(!datos.get(position).getEmail().trim().equals("")){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{datos.get(position).getEmail().trim()});
            try {
                contexto.startActivity(Intent.createChooser(i, contexto.getString(R.string.enviar_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(contexto, contexto.getString(R.string.no_app_disponible), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clickFacturas(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("idCliente", datos.get(position).getId());
        Fragment fragmento = new FragmentoListaFacturas();
        fragmento.setArguments(bundle);
        FragmentTransaction transaction = ((ActionBarActivity)contexto).getFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayoutPrincipal, fragmento);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void clickTelefono(int position){
        String telefono01 = datos.get(position).getTelefono01().trim();
        String telefono02 = datos.get(position).getTelefono02().trim();

        Log.v("mio", telefono01 + " - " + telefono02);

        String uri = "";
        if(!telefono01.equals("") && !telefono02.equals("")){
            seleccionarTelefono(telefono01, telefono02);
        } else if(!telefono01.equals("")) {
            uri = "tel:" + telefono01;
        } else if(!telefono01.equals("")) {
            uri = "tel:" + telefono02;
        }

        if(!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            contexto.startActivity(intent);
        }
    }

    private void seleccionarTelefono(String telefono1, String telefono2){
        final CharSequence telfs[] = new CharSequence[] {telefono1, telefono2};
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Selecciona telefono");
        builder.setItems(telfs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uri = "tel:" + telfs[which].toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                contexto.startActivity(intent);
            }
        });
        builder.show();
    }

    private static class ViewHolder {
        private TextView nombreComercial;
        private Button btTelefono;
        private Button btEmail;
        private Button btFacturas;
    }
}