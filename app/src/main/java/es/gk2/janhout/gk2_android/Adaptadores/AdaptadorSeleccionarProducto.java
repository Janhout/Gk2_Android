package es.gk2.janhout.gk2_android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Producto;
import es.gk2.janhout.gk2_android.util.Metodos;

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
            vh.titulo = (TextView) convertView.findViewById(R.id.seleccionar_producto_titulo);
            vh.precio = (TextView) convertView.findViewById(R.id.seleccionar_producto_precio);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.articulo.setText(datos.get(position).getArticulo());
        vh.titulo.setText(datos.get(position).getTitulo());
        vh.precio.setText(Metodos.stringToMoney(datos.get(position).getPrecio_venta()));
        return convertView;
    }

    private static class ViewHolder {
        private TextView articulo;
        private TextView titulo;
        private TextView precio;
    }
}