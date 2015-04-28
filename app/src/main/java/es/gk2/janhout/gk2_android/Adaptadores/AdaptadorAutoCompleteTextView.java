package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Util.Provincia;

public class AdaptadorAutoCompleteTextView extends ArrayAdapter{

    private Context context;
    private int recurso;
    private ArrayList<Provincia> listaOriginal;
    private Filtro filter;


    private ArrayList<Provincia> suggestions;
    private static LayoutInflater inflador;


    public AdaptadorAutoCompleteTextView(Context context, int resource, ArrayList<Provincia> objects) {
        super(context, resource, objects);
        filter = new Filtro();
        this.context = context;
        this.recurso = resource;
        this.listaOriginal = new ArrayList<>(objects);
        this.suggestions = new ArrayList<>();
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

        Provincia provincia = suggestions.get(position);
        if (provincia != null) {
            if (holder.title != null) {
                holder.title.setText(provincia.getTituloProvincia());
            }
        }

       /* if(lista.get(position).getClass().getName().contains("Provincia"))
            holder.title.setText(((Provincia) lista.get(position)).getTituloProvincia());
        else if(lista.get(position).getClass().getName().contains("Localidad"))
            holder.title.setText(((Localidad) lista.get(position)).getTituloLocalidad());
        else if(lista.get(position).getClass().getName().contains("TipoDireccion"))
            holder.title.setText(((TipoDireccion) lista.get(position)).getTituloTipoDireccion());*/
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
            return ((Provincia)(resultValue)).getTituloProvincia();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Provincia> lista = new ArrayList<>(listaOriginal);
            if (constraint != null) {
                suggestions = new ArrayList<>();
                for (Provincia provincia : lista) {
                    if (provincia.getTituloProvincia().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(provincia);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Provincia> filteredList = (ArrayList<Provincia>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Provincia p : filteredList) {
                    add(p);
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