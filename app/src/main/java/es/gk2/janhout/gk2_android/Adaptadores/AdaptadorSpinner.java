package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Localidad;
import es.gk2.janhout.gk2_android.Util.Provincia;
import es.gk2.janhout.gk2_android.Util.TipoDireccion;

public class AdaptadorSpinner extends ArrayAdapter {

    private Context contexto;
    private int recurso;
    private List<Object> lista;
    ViewHolder holder;

    public AdaptadorSpinner(Context context, int resource, int tv, List objects) {
        super(context, tv, objects);
        this.contexto = context;
        this.recurso = resource;
        this.lista = objects;
        this.holder = new ViewHolder();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(recurso, parent, false);

        holder.etiquetaSpinner = (TextView) convertView.findViewById(R.id.tvSpinner);

        //Mira qué tipo de objeto contiene la lista. Como la lista al hacer un get(position) devuelve un Object, hay que hacer un casting
        //para identificar qué tipo de objeto devuelve.
        if(lista.get(position).getClass().getName().contains("Provincia"))
            holder.etiquetaSpinner.setText(((Provincia) lista.get(position)).getTituloProvincia());
        else if(lista.get(position).getClass().getName().contains("Localidad"))
            holder.etiquetaSpinner.setText(((Localidad) lista.get(position)).getTituloLocalidad());
        else if(lista.get(position).getClass().getName().contains("TipoDireccion"))
            holder.etiquetaSpinner.setText(((TipoDireccion) lista.get(position)).getTituloTipoDireccion());
        holder.etiquetaSpinner.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(recurso, parent, false);

        holder.etiquetaSpinner = (TextView) convertView.findViewById(R.id.tvSpinner);

        //Mira qué tipo de objeto contiene la lista. Como la lista al hacer un get(position) devuelve un Object, hay que hacer un casting
        //para identificar qué tipo de objeto devuelve.
        if(lista.get(position).getClass().getName().contains("Provincia")) {
            holder.etiquetaSpinner.setText(((Provincia) lista.get(position)).getTituloProvincia());
            holder.etiquetaSpinner.setTag(((Provincia) lista.get(position)).getIdProvincia());
        }
        else if(lista.get(position).getClass().getName().contains("Localidad"))
            holder.etiquetaSpinner.setText(((Localidad) lista.get(position)).getTituloLocalidad());
        else if(lista.get(position).getClass().getName().contains("TipoDireccion"))
            holder.etiquetaSpinner.setText(((TipoDireccion) lista.get(position)).getTituloTipoDireccion());
        holder.etiquetaSpinner.setVisibility(View.VISIBLE);

        return convertView;
    }

    private static class ViewHolder{
        TextView etiquetaSpinner;
    }
}
