package es.gk2.janhout.gk2_android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Factura;

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
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.filaFactura = (LinearLayout) convertView.findViewById(R.id.filaFactura);
            vh.numeroFactura = (TextView) convertView.findViewById(R.id.numeroFactura);
            vh.fechaFactura = (TextView) convertView.findViewById(R.id.fechaFactura);
            vh.importeFactura = (TextView) convertView.findViewById(R.id.importeFactura);
            vh.importePagado = (TextView) convertView.findViewById(R.id.importePagado);
            vh.iconoEnviado = (TextView) convertView.findViewById(R.id.detalle_factura_iconoEnviado);
            vh.iconoImpreso = (TextView) convertView.findViewById(R.id.detalle_factura_iconoImpreso);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (datos.get(position).getEstadoFactura() == 0)
            vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.rojo));
        else if (datos.get(position).getEstadoFactura() == 1)
            vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.verde));
        else if (datos.get(position).getEstadoFactura() == 2)
            vh.filaFactura.setBackgroundColor(contexto.getResources().getColor(R.color.amarillo));

        if (datos.get(position).getEnviado() > 0)
            Metodos.textViewAwesomeComponente(contexto, vh.iconoEnviado, contexto.getString(R.string.icono_enviado));
        else
            vh.iconoEnviado.setText("");
        if (datos.get(position).getImpreso() > 0)
            Metodos.textViewAwesomeComponente(contexto, vh.iconoImpreso, contexto.getString(R.string.icono_impreso));
        else
            vh.iconoImpreso.setText("");

        vh.numeroFactura.setText(position+" - "+datos.get(position).getNumeroFactura());
        vh.fechaFactura.setText(datos.get(position).getFechaFactura());
        vh.importeFactura.setText(contexto.getString(R.string.s_facturas_importe) + Float.toString(datos.get(position).getImporteFactura())+contexto.getString(R.string.moneda));
        if (datos.get(position).getEstadoFactura() == 0)
            vh.importePagado.setText(contexto.getString(R.string.s_facturas_pendiente) + Float.toString(datos.get(position).getImportePagado())+contexto.getString(R.string.moneda));
        else
            vh.importePagado.setText("");

        return convertView;
    }

    public static class ViewHolder {
        public LinearLayout filaFactura;
        public TextView numeroFactura;
        public TextView fechaFactura;
        public TextView importeFactura;
        public TextView importePagado;
        public TextView iconoEnviado;
        public TextView iconoImpreso;
    }
}
