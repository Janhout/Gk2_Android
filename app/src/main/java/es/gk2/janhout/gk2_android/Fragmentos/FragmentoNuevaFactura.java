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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.gk2.janhout.gk2_android.Actividades.NuevaFactura;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;
import es.gk2.janhout.gk2_android.Util.Producto;

public class FragmentoNuevaFactura extends Fragment implements OnDateSetListener {

    private NuevaFactura actividad;

    private EditText etCliente;
    private Cliente clienteSeleccionado;
    private EditText etFechaFactura;
    private EditText etFechaVenciminetoFactura;
    private Switch formatoPrecio;
    private LinearLayout llLineas;
    private ArrayList<Producto> listaProductos;
    private Spinner spCondiciones_pago;
    private EditText etNotas;
    private TextView tvSubtotal;
    private TextView tvSubtotalEtiqueta;
    private TextView tvTotal;

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
        if(savedInstanceState != null) {
            listaProductos = (ArrayList) savedInstanceState.getParcelableArrayList("listaProductos");
        }
        if(listaProductos == null) {
            listaProductos = new ArrayList<>();
        }
        cargarListaProductos();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listaProductos", listaProductos);
    }

    private void cargarView(){
        cargarViewCliente();
        cargarViewInfoFactura();
        cargarViewLineasFactura();
        cargarViewOpciones();
        cargarViewTotales();
    }

    private void cargarViewCliente(){
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
    }

    private void cargarViewInfoFactura(){
        spCondiciones_pago = (Spinner)getView().findViewById(R.id.nueva_factura_sp_condiciones_pago);
        etFechaFactura = (EditText)getView().findViewById(R.id.nueva_factura_et_fecha_factura);
        etFechaVenciminetoFactura = (EditText)getView().findViewById(R.id.nueva_factura_et_fecha_vencimiento);
        etNotas = (EditText)getView().findViewById(R.id.nueva_factura_et_notas);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.lista_condiciones_pago_factura, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCondiciones_pago.setAdapter(adapter);

        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), false);

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
        etFechaFactura.setText(formatearFecha(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.YEAR)));

        spCondiciones_pago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cambioFechaVencimiento(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spCondiciones_pago.setSelection(2);
        cambioFechaVencimiento(2);

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
    }

    private void cambioFechaVencimiento(int position){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatoFecha.parse(etFechaFactura.getText().toString());
            cal.setTime(date);
        } catch (ParseException ignored) {
        }
        cal.setTime(date);
        int dias = 0;
        switch (position){
            case 0:
                break;
            case 1:
                dias = 8;
                break;
            case 2:
                dias = 14;
                break;
            case 3:
                dias = 30;
                break;
            case 4:
                dias = 0;
                break;
        }
        if(position != 0) {
            cal.add(Calendar.DAY_OF_MONTH, dias);
            etFechaVenciminetoFactura.setText(formatearFecha(cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH)+1,
                    cal.get(Calendar.YEAR)));
        }
    }

    private void cargarViewLineasFactura(){
        TextView et_nuevaLinea = (TextView)getView().findViewById(R.id.nueva_factura_bt_nueva_linea);
        Metodos.textViewAwesomeComponente(getActivity(), et_nuevaLinea, getString(R.string.icono_nueva_linea));
        LinearLayout llNuevaLinea = (LinearLayout) getView().findViewById(R.id.nueva_factura_layout_nueva_linea);
        llLineas = (LinearLayout)getView().findViewById(R.id.nueva_factura_layout_lineas);
        llNuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentoSeleccionarProducto();
                NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.seleccionarProducto;
                Bundle bundle = new Bundle();
                bundle.putString("query", "");
                fragment.setArguments(bundle);
                FragmentoNuevaFactura.this.getActivity().invalidateOptionsMenu();
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                ft.addToBackStack(null);
                ft.commit();
                getFragmentManager().executePendingTransactions();
            }
        });
    }

    private void cargarViewTotales(){
        tvSubtotal = (TextView)getView().findViewById(R.id.nueva_factura_tv_subtotal);
        tvSubtotalEtiqueta = (TextView)getView().findViewById(R.id.nueva_factura_tv_subtotal_etiqueta);
        tvTotal = (TextView)getView().findViewById(R.id.nueva_factura_tv_total);
        cambiosNeto();
    }

    private void cargarViewOpciones(){
        formatoPrecio = (Switch) getView().findViewById(R.id.nueva_factura_sw_formato_precio);
        formatoPrecio.setChecked(true);
        formatoPrecio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cambiosNeto();
                } else {
                    cambiosBruto();
                }
            }
        });
    }

    private void cambiosNeto(){
        tvSubtotalEtiqueta.setText(getString(R.string.nueva_factura_subtotal));
    }

    private void cambiosBruto(){
        tvSubtotalEtiqueta.setText(getString(R.string.nueva_factura_subtotal_sin_iva));
    }

    public void setCliente(Cliente cliente){
        clienteSeleccionado = cliente;
        etCliente.setText(clienteSeleccionado.getNombre_comercial());
    }

    public void setProducto(Producto producto){
        listaProductos.add(producto);
        cargarListaProductos();
    }

    private void cargarListaProductos(){
        llLineas.removeAllViewsInLayout();
        llLineas.invalidate();
        TextView tv = (TextView)getView().findViewById(R.id.nueva_factura_tv_sin_producto);
        if (listaProductos.size()>0) {
            tv.setVisibility(View.GONE);
            for (Producto producto : listaProductos) {
                crearViewLinea(producto);
            }
        } else {
            tv.setVisibility(View.VISIBLE);
        }
    }

    private void crearViewLinea(Producto producto){
        LayoutInflater inflador = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View detalle = inflador.inflate(R.layout.detalle_linea_factura, null);
        Button delete = (Button)detalle.findViewById(R.id.detalle_linea_bt_borrar);
        TextView tv_descripcion = (TextView)detalle.findViewById(R.id.detalle_linea_tv_descripcion);
        TextView tv_producto = (TextView)detalle.findViewById(R.id.detalle_linea_tv_producto);
        tv_descripcion.setText(producto.getTitulo());
        tv_producto.setText(producto.getTitulo());
        delete.setTag(producto);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                Producto pos = (Producto) b.getTag();
                listaProductos.remove(pos);
                cargarListaProductos();
            }
        });
        llLineas.addView(detalle);
    }

    private String formatearFecha(int d, int m, int y){
        String dia = ("0" + d).substring(("0" + d).length()-2);
        String mes = ("0" + m).substring(("0" + m).length()-2);
        String anio = "" + y;
        return dia + "/" + mes + "/" + anio;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(datePickerDialog.getTag().equals(FECHA_FACTURA_TAG)) {
            etFechaFactura.setText(formatearFecha(day, month + 1, year));
            cambioFechaVencimiento(spCondiciones_pago.getSelectedItemPosition());
        } else if(datePickerDialog.getTag().equals(FECHA_VENCIMIENTO_TAG)){
            etFechaVenciminetoFactura.setText(day + "/" + (month + 1) + "/" + year);
            spCondiciones_pago.setSelection(0);
        }
    }
}