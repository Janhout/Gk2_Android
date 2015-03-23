package es.gk2.janhout.gk2_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListaClientes extends ArrayAdapter<Cliente> {

    private Context contexto;
    private ArrayList<Cliente> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaClientes(Context contexto, int recurso, ArrayList<Cliente> datos) {
        super(contexto, recurso, datos);
        this.contexto = contexto;
        this.recurso = recurso;
        this.datos = datos;
        this.inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.nombreComercial = (TextView) convertView.findViewById(R.id.nombre_cliente);
            vh.acciones = (Spinner) convertView.findViewById(R.id.sp_acciones);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.nombreComercial.setText(datos.get(position).getNombre_comercial());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contexto, R.array.lista_acciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vh.acciones.setAdapter(adapter);
        return convertView;
    }

    public static class ViewHolder {
        public TextView nombreComercial;
        public Spinner acciones;
    }
}