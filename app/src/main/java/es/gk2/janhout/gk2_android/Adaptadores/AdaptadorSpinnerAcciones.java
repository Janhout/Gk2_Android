package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import es.gk2.janhout.gk2_android.R;

public class AdaptadorSpinnerAcciones extends ArrayAdapter<String> {

    private Context contexto;
    private int recurso;
    private String[] lista;
    ViewHolder holder;

    public AdaptadorSpinnerAcciones(Context context, int resource, int tv, String[] objects) {
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

        holder.accion = (TextView) convertView.findViewById(R.id.tvSpinner);
        holder.accion.setText(lista[position]);

        if(position==0){
            holder.accion.setVisibility(View.GONE);
        }
        else{
            holder.accion.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(recurso, parent, false);

        holder.accion = (TextView) convertView.findViewById(R.id.tvSpinner);
        holder.accion.setText(lista[position]);

        holder.accion.setVisibility(View.VISIBLE);

        return convertView;
    }

    private static class ViewHolder{
        TextView accion;
    }
}
