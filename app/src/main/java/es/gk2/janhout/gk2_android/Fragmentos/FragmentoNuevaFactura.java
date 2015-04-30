package es.gk2.janhout.gk2_android.Fragmentos;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.util.Calendar;

import es.gk2.janhout.gk2_android.Actividades.NuevaFactura;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class FragmentoNuevaFactura extends Fragment implements OnDateSetListener {

    private NuevaFactura actividad;

    private EditText etCliente;
    private Cliente clienteSeleccionado;
    private EditText etFechaFactura;
    private EditText etFechaVenciminetoFactura;
    private LinearLayout llNuevaLinea;
    private LinearLayout llLineas;

    public static final String FECHA_FACTURA_TAG = "fecha_factura";
    public static final String FECHA_VENCIMIENTO_TAG = "fecha_vencimiento";

    public FragmentoNuevaFactura() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actividad = (NuevaFactura)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nueva_factura, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cargarView();
    }

    private void cargarView(){
        etCliente = (EditText)getView().findViewById(R.id.nueva_factura_et_cliente);
        etCliente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Fragment fragment = new FragmentoSeleccionarCliente();
                    NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.seleccionCliente;
                    Bundle bundle = new Bundle();
                    bundle.putString("query", "");
                    fragment.setArguments(bundle);
                    FragmentoNuevaFactura.this.getActivity().invalidateOptionsMenu();
                    FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    getFragmentManager().executePendingTransactions();
                }
                return true;
            }
        });

        etFechaFactura = (EditText)getView().findViewById(R.id.nueva_factura_et_fecha_factura);
        etFechaVenciminetoFactura = (EditText)getView().findViewById(R.id.nueva_factura_et_fecha_vencimiento);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

        etFechaFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actividad.getSupportFragmentManager().findFragmentByTag(FECHA_FACTURA_TAG) == null &&
                        actividad.getSupportFragmentManager().findFragmentByTag(FECHA_VENCIMIENTO_TAG) == null) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(1990, 2037);
                    datePickerDialog.show(actividad.getSupportFragmentManager(), FECHA_FACTURA_TAG);
                }
            }
        });
        etFechaFactura.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        etFechaVenciminetoFactura.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        etFechaVenciminetoFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actividad.getSupportFragmentManager().findFragmentByTag(FECHA_VENCIMIENTO_TAG) == null
                        && actividad.getSupportFragmentManager().findFragmentByTag(FECHA_FACTURA_TAG) == null) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(1990, 2037);
                    datePickerDialog.show(actividad.getSupportFragmentManager(), FECHA_VENCIMIENTO_TAG);
                }
            }
        });

        llNuevaLinea = (LinearLayout)getView().findViewById(R.id.nueva_factura_layout_nueva_linea);
        llLineas = (LinearLayout)getView().findViewById(R.id.nueva_factura_layout_lineas);
        llNuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflador = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View detalle = inflador.inflate(R.layout.detalle_linea_factura, null);
                llLineas.addView(detalle);
            }
        });
    }

    public void setCliente(Cliente cliente){
        clienteSeleccionado = cliente;
        etCliente.setText(clienteSeleccionado.getNombre_comercial());
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(datePickerDialog.getTag().equals(FECHA_FACTURA_TAG)) {
            etFechaFactura.setText(day + "/" + (month + 1) + "/" + year);
        } else if(datePickerDialog.getTag().equals(FECHA_VENCIMIENTO_TAG)){
            etFechaVenciminetoFactura.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
}