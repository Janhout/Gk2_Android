package es.gk2.janhout.gk2_android.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Actividades.LectorPDF;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.GetAsyncTask;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Factura;

public class AdaptadorListaFacturas extends ArrayAdapter<Factura> implements GetAsyncTask.OnProcessCompleteListener{

    private Context contexto;
    private ArrayList<Factura> datos;
    private int recurso;
    private static LayoutInflater inflador;

    private static final int CODIGO_PEDIR_PDF = 1;

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
            vh.verFactura = (Button) convertView.findViewById(R.id.detalle_factura_botonVerFactura);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.verFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verFactura(position);
            }
        });
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

        Metodos.botonAwesomeComponente(contexto, vh.verFactura, contexto.getString(R.string.icono_ver));

        vh.numeroFactura.setText(position+" - "+datos.get(position).getNumeroFactura());
        vh.fechaFactura.setText(datos.get(position).getFechaFactura());
        vh.importeFactura.setText(contexto.getString(R.string.s_facturas_importe) + Float.toString(datos.get(position).getImporteFactura())+contexto.getString(R.string.moneda));
        if (datos.get(position).getEstadoFactura() == 0)
            vh.importePagado.setText(contexto.getString(R.string.s_facturas_pendiente) + Float.toString(datos.get(position).getImportePagado())+contexto.getString(R.string.moneda));
        else
            vh.importePagado.setText("");

        return convertView;
    }

    private void verFactura(int position){
        Hashtable<String, String> parametros = null;
        GetAsyncTask a = new GetAsyncTask(contexto, this, Constantes.PDF_URL +String.valueOf(datos.get(position).getIdImpresion()), true, CODIGO_PEDIR_PDF);
        a.execute(parametros);
    }

    @Override
    public void resultadoGet(String respuesta, int codigo){
        if(respuesta!=null) {
            Intent intentCompartir = new Intent(contexto, LectorPDF.class);
            intentCompartir.putExtra("pdf", respuesta);
            contexto.startActivity(intentCompartir);
        }
    }


    public static class ViewHolder {
        public LinearLayout filaFactura;
        public TextView numeroFactura;
        public TextView fechaFactura;
        public TextView importeFactura;
        public TextView importePagado;
        public TextView iconoEnviado;
        public TextView iconoImpreso;
        public Button verFactura;
    }
}
