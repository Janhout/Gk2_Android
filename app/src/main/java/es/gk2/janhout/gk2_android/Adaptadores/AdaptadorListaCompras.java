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

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Compra;

/**
 * Created by usuario on 25/03/2015.
 */
public class AdaptadorListaCompras extends ArrayAdapter<Compra> {
    private Context contexto;
    private ArrayList<Compra> datos;
    private int recurso;
    private static LayoutInflater inflador;

    public AdaptadorListaCompras(Context contexto, int recurso, ArrayList<Compra> datos) {
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
            vh.compra_fila = (RelativeLayout) convertView.findViewById(R.id.compra_fila);
            vh.compra_id = (TextView) convertView.findViewById(R.id.compra_id);
            vh.compra_proveedor = (TextView) convertView.findViewById(R.id.compra_proveedor);
            vh.compra_fecha = (TextView) convertView.findViewById(R.id.compra_fecha);
            vh.compra_importe = (TextView) convertView.findViewById(R.id.compra_importe);
            vh.compra_pendiente = (TextView) convertView.findViewById(R.id.compra_pendiente);

            if(datos.get(position).getCompra_estado() == 0) //Sin contabilizar
                vh.compra_fila.setBackgroundColor(contexto.getResources().getColor(R.color.amarillo));
            else if(datos.get(position).getCompra_estado() == 5) //Contabilizada
                vh.compra_fila.setBackgroundColor(contexto.getResources().getColor(R.color.verde));
            vh.compra_acciones = (Spinner) convertView.findViewById(R.id.sp_acciones_compras);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.compra_id.setText(datos.get(position).getCompra_id());
        vh.compra_proveedor.setText(datos.get(position).getCompra_nombreProveedor());
        vh.compra_fecha.setText(datos.get(position).getCompra_fecha());
        vh.compra_importe.setText(datos.get(position).getCompra_importe().toString());

        //if(datos.get(position).getCompra_pendiente() != 0)
            vh.compra_pendiente.setText("Pendiente: "+Float.toString(datos.get(position).getCompra_pendiente()));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contexto, R.array.lista_acciones_facturas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vh.compra_acciones.setAdapter(adapter);
        return convertView;
    }

    public static class ViewHolder {
        public Integer compra_estadoFactura;
        public RelativeLayout compra_fila;
        public TextView compra_id;
        public TextView compra_proveedor;
        public TextView compra_fecha;
        public TextView compra_importe;
        public TextView compra_pendiente;
        public Spinner compra_acciones;
    }
}
