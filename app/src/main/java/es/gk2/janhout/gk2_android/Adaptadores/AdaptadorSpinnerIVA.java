package es.gk2.janhout.gk2_android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.IVA;

public class AdaptadorSpinnerIVA extends ArrayAdapter {

    private Context contexto;
    private int recurso;
    private List<IVA> lista;
    private static LayoutInflater inflador;

    public AdaptadorSpinnerIVA(Context context, int resource, List<IVA> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.lista = objects;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.etiquetaSpinner = (TextView) convertView.findViewById(R.id.tvSpinner);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.etiquetaSpinner.setText(lista.get(position).getTitulo() + " (" + lista.get(position).getP_iva() + "%)");
        return convertView;
    }

    private static class ViewHolder{
        TextView etiquetaSpinner;
    }
}
