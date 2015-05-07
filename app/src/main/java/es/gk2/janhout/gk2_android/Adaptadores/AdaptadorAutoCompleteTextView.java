package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Util.Localidad;
import es.gk2.janhout.gk2_android.Util.Provincia;
import es.gk2.janhout.gk2_android.Util.TipoDireccion;

public class AdaptadorAutoCompleteTextView extends ArrayAdapter {

    private int recurso;
    private ArrayList<Object> listaOriginal;
    private Filtro filter;


    private ArrayList<Object> sugerencias;
    private static LayoutInflater inflador;


    public AdaptadorAutoCompleteTextView(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        this.filter = new Filtro();
        this.recurso = resource;
        this.listaOriginal = new ArrayList<>(objects);
        this.sugerencias = new ArrayList<>();
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (sugerencias.get(position).getClass().getName().contains("Provincia"))
            holder.title.setText(((Provincia) sugerencias.get(position)).getTituloProvincia());
        else if (sugerencias.get(position).getClass().getName().contains("Localidad"))
            holder.title.setText(((Localidad) sugerencias.get(position)).getTituloLocalidad());
        else if (sugerencias.get(position).getClass().getName().contains("TipoDireccion"))
            holder.title.setText(((TipoDireccion) sugerencias.get(position)).getTituloTipoDireccion());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        public TextView title;
    }

    private class Filtro extends Filter {

        @Override
        public String convertResultToString(Object resultValue) {
            if (resultValue.getClass().getName().contains("Provincia")) {
                return ((Provincia) (resultValue)).getTituloProvincia();
            } else if (resultValue.getClass().getName().contains("Localidad")) {
                return ((Localidad) (resultValue)).getTituloLocalidad();
            } else if (resultValue.getClass().getName().contains("TipoDireccion")) {
                return ((TipoDireccion) (resultValue)).getTituloTipoDireccion();
            } else return "";
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Object> lista = new ArrayList<>(listaOriginal);
            if (constraint != null) {
                constraint = constraint.toString().trim();
                sugerencias = new ArrayList<>();
                for (Object object : lista) {
                    if (object.getClass().getName().contains("Provincia")) {
                        if (((Provincia) object).getTituloProvincia().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sugerencias.add(object);
                        }
                    } else if (object.getClass().getName().contains("Localidad")) {
                        if (((Localidad) object).getTituloLocalidad().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sugerencias.add(object);
                        }
                    } else if (object.getClass().getName().contains("TipoDireccion")) {
                        if (((TipoDireccion) object).getTituloTipoDireccion().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sugerencias.add(object);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sugerencias;
                filterResults.count = sugerencias.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Object> listaFinal = (ArrayList<Object>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Object object : listaFinal) {
                    add(object);
                }
                notifyDataSetChanged();
            }
        }
    }
}

/*
        <AutoCompleteTextView
            android:id="@+id/autocomplete_country"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />


    AdaptadorAutoCompleteTextView adapter;
    ArrayList<Provincia> countries;
    AutoCompleteTextView textView;

        // Get a reference to the AutoCompleteTextView in the layout
        textView = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_country);
        countries = new ArrayList();
        adapter = new AdaptadorAutoCompleteTextView(getActivity(), android.R.layout.simple_dropdown_item_1line, countries);
        textView.setAdapter(adapter);
 */