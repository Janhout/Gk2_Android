package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class AdaptadorSeleccionarCliente extends ArrayAdapter<Cliente>{

    private Context contexto;
    private ArrayList<Cliente> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorSeleccionarCliente(Context contexto, int recurso, ArrayList<Cliente> datos) {
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
            vh.titulo = (TextView) convertView.findViewById(R.id.tvTitulo_detalle);
            vh.subTitulo = (TextView)convertView.findViewById(R.id.tvSubtitulo_detalle);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.titulo.setText(datos.get(position).getNombre_comercial());
        vh.subTitulo.setText(datos.get(position).getEmail());
        return convertView;
    }

    private static class ViewHolder {
        private TextView titulo;
        private TextView subTitulo;
    }
}
