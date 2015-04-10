package es.gk2.janhout.gk2_android.Actividades;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class MostrarCliente extends ActionBarActivity {

    private TextView tvCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);

        Cliente c = getIntent().getExtras().getParcelable("cliente");
        //tvCliente = (TextView)findViewById(R.id.tvCliente);

        //tvCliente.setText(c.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostrar_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
