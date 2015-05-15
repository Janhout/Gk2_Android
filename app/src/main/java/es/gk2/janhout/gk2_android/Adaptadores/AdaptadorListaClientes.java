package es.gk2.janhout.gk2_android.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Cliente;

public class AdaptadorListaClientes extends ArrayAdapter<Cliente> implements AsyncTaskGet.OnProcessCompleteListener{

    private Context contexto;
    private ArrayList<Cliente> datos;
    private int recurso;
    private static LayoutInflater inflador;
    private int posicion_favorito;

    private final static int CODIGO_SET_FAVORITO = 1;

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
            vh.btFavorito = (Button)convertView.findViewById(R.id.bt_favorito);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Metodos.botonAwesomeComponente(contexto, vh.btEmail, contexto.getString(R.string.icono_email));
        Metodos.botonAwesomeComponente(contexto, vh.btTelefono, contexto.getString(R.string.icono_telefono));

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

        vh.btFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicion_favorito = position;
                clickFavorito();
            }
        });

        if(datos.get(position).isFavorito()){
            Metodos.botonAwesomeComponente(contexto, vh.btFavorito, contexto.getString(R.string.icono_clientes_favoritos));
        } else {
            Metodos.botonAwesomeComponente(contexto, vh.btFavorito, contexto.getString(R.string.icono_clientes_no_favoritos));
        }

        String telefono1 = datos.get(position).getTelefono01().trim();
        String telefono2 = datos.get(position).getTelefono02().trim();
        telefono1 = telefono1.replaceAll(" ", "");
        telefono2 = telefono2.replaceAll(" ", "");
        if(!TextUtils.isDigitsOnly(telefono1)){
            telefono1 = "";
        }
        if(!TextUtils.isDigitsOnly(telefono2)){
            telefono2 = "";
        }

        if(telefono1.equals("") && telefono2.equals("")){
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

    private void clickFavorito(){
        String url;
        if(datos.get(posicion_favorito).isFavorito()) {
            url = Constantes.UNSET_FAVORITO + datos.get(posicion_favorito).getId();
        }else{
            url = Constantes.SET_FAVORITO + datos.get(posicion_favorito).getId();
        }
        AsyncTaskGet h = new AsyncTaskGet(contexto, this, url, false, CODIGO_SET_FAVORITO);
        h.execute(new Hashtable<String, String>());
    }

    private void clickTelefono(int position){
        String telefono01 = datos.get(position).getTelefono01().trim();
        String telefono02 = datos.get(position).getTelefono02().trim();
        telefono01 = telefono01.replaceAll(" ", "");
        telefono02 = telefono02.replaceAll(" ", "");

        String uri = "";
        if(!telefono01.equals("") && !telefono02.equals("")){
            seleccionarTelefono(telefono01, telefono02);
        } else if(!telefono01.equals("")) {
            uri = "tel:" + telefono01;
        } else if(!telefono02.equals("")) {
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
        builder.setTitle(contexto.getString(R.string.selecciona_telefono));
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

    @Override
    public void resultadoGet(String respuesta, int codigo_peticion) {
        if(respuesta != null) {
            switch (codigo_peticion) {
                case CODIGO_SET_FAVORITO:
                    datos.get(posicion_favorito).setFavorito(!datos.get(posicion_favorito).isFavorito());
                    this.notifyDataSetChanged();
                    break;
            }
        } else {
            Metodos.redireccionarLogin(contexto);
        }
    }

    private static class ViewHolder {
        private TextView nombreComercial;
        private Button btTelefono;
        private Button btEmail;
        private Button btFavorito;
    }
}