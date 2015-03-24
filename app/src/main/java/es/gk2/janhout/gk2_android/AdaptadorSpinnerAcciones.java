package es.gk2.janhout.gk2_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorSpinnerAcciones extends ArrayAdapter<String> {

    private Context contexto;
    private int recurso;
    private String[] lista;

    public AdaptadorSpinnerAcciones(Context contexto, int resource, String[] lista) {
        super(contexto, resource, lista);
        this.contexto = contexto;
        this.recurso = resource;
        this.lista = lista;
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sp = inflater.inflate(recurso, parent, false);
        TextView ts = (TextView) sp.findViewById(R.id.tvSpinner);
        ts.setText(lista[position]);
        return sp;
    }
}
