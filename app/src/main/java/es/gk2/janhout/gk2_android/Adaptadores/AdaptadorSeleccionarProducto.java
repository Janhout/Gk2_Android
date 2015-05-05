package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Producto;

public class AdaptadorSeleccionarProducto extends ArrayAdapter<Producto>{

    private Context contexto;
    private ArrayList<Producto> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorSeleccionarProducto(Context contexto, int recurso, ArrayList<Producto> datos) {
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
            vh.articulo = (TextView) convertView.findViewById(R.id.seleccionar_producto_articulo);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.articulo.setText(datos.get(position).toString());
        return convertView;
    }

    private static class ViewHolder {
        private TextView articulo;
    }
}