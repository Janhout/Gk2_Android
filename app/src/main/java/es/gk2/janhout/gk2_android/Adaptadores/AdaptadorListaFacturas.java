package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Util.Factura;
import es.gk2.janhout.gk2_android.R;

/**
 * Created by usuario on 23/03/2015.
 */
public class AdaptadorListaFacturas extends ArrayAdapter<Factura> {
    private Context contexto;
    private ArrayList<Factura> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaFacturas(Context contexto, int recurso, ArrayList<Factura> datos) {
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
            vh.filaFactura = (RelativeLayout) convertView.findViewById(R.id.filaFactura);
            vh.numeroFactura = (TextView) convertView.findViewById(R.id.numeroFactura);
            vh.fechaFactura = (TextView) convertView.findViewById(R.id.fechaFactura);
            vh.importeFactura = (TextView) convertView.findViewById(R.id.importeFactura);
            vh.importePagado = (TextView) convertView.findViewById(R.id.importePagado);

            if(datos.get(position).getEstadoFactura() == 0)
                vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.rojo));
            else if(datos.get(position).getEstadoFactura() == 1)
                vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.verde));
            else if(datos.get(position).getEstadoFactura() == 2)
                vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.amarillo));
            vh.acciones = (Spinner) convertView.findViewById(R.id.sp_acciones_facturas);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.numeroFactura.setText(datos.get(position).getNumeroFactura());
        vh.fechaFactura.setText(datos.get(position).getFechaFactura());
        vh.importeFactura.setText("Importe: "+Float.toString(datos.get(position).getImporteFactura()));
        if(datos.get(position).getEstadoFactura() == 0)
            vh.importePagado.setText("Pagado: "+Float.toString(datos.get(position).getImportePagado()));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contexto, R.array.lista_acciones_facturas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vh.acciones.setAdapter(adapter);
        return convertView;
    }

    public static class ViewHolder {
        public Integer estadoFactura;
        public RelativeLayout filaFactura;
        public TextView numeroFactura;
        public TextView fechaFactura;
        public TextView importeFactura;
        public TextView importePagado;
        public Spinner acciones;
    }
}
