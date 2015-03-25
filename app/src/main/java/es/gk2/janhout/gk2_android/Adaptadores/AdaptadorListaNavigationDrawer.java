package es.gk2.janhout.gk2_android.Adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.gk2.janhout.gk2_android.ItemNavigationDrawer;
import es.gk2.janhout.gk2_android.R;

public class AdaptadorListaNavigationDrawer extends ArrayAdapter {

    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaNavigationDrawer(Context contexto, int recurso, List objects) {
        super(contexto, recurso, objects);
        this.recurso = recurso;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.icono = (ImageView) convertView.findViewById(R.id.iconoElemento);
            vh.titulo = (TextView) convertView.findViewById(R.id.tituloElemento);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ItemNavigationDrawer item = (ItemNavigationDrawer)getItem(position);
        vh.icono.setImageResource(item.getIcono());
        vh.titulo.setText(item.getNombre());

        return convertView;
    }
    public static class ViewHolder {
        public TextView titulo;
        public ImageView icono;
    }
}
