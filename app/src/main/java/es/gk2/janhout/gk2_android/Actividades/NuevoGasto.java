package es.gk2.janhout.gk2_android.actividades;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

import es.gk2.janhout.gk2_android.R;

public class NuevoGasto extends ActionBarActivity {

    private ImageView fotoTomada;
    private final int ACTIVIDAD_CAMARA = 0;

    /* *************************************************************************
    **************************** Métodos on... ********************************
    *************************************************************************** */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case ACTIVIDAD_CAMARA:
                    Log.v("Ruta: ", getExternalFilesDir("/")+"/fotoprueba.jpg");
                    fotoTomada.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir("/")+"/fotoprueba.jpg"));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nuevoGasto_concepto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner nuevoGasto_spinner = (Spinner) findViewById(R.id.nuevoGasto_concepto);
        nuevoGasto_spinner.setAdapter(adapter);
        fotoTomada = (ImageView) findViewById(R.id.nuevoGasto_imagen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_gasto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /* *************************************************************************
    **************************** Auxiliares ************************************
    *************************************************************************** */

    public void nuevaFoto(View v){
        Intent tomaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tomaFoto.resolveActivity(getPackageManager()) != null) {
            File fichero = new File(getExternalFilesDir("/"), "fotoprueba.jpg");
            if (fichero != null) {
                tomaFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fichero));
                startActivityForResult(tomaFoto, ACTIVIDAD_CAMARA);
            }
        }
    }
}
