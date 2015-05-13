package es.gk2.janhout.gk2_android.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.util.AsyncTaskPost;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.R;

public class LectorPDF extends AppCompatActivity implements OnPageChangeListener,
        AsyncTaskPost.OnProcessCompleteListener {

    private Integer pageNumber;
    private Intent intentCompartir;
    private String fichero;

    private static final String PARAMENTRO_MAILTO = "mailto";
    private static final String PARAMENTRO_ASUNTO = "asunto";
    private static final String PARAMENTRO_MENSAJE = "mensaje";

    private static final int CODIGO_ENVIAR_MAIL = 1;

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        fichero = intent.getStringExtra("pdf");

        File f = new File(fichero);
        pageNumber = 1;
        PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
        pdfView.fromFile(f)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .showMinimap(true)
                .enableSwipe(true)
                .load();

        intentCompartir = new Intent();
        intentCompartir.setAction(Intent.ACTION_SEND);
        intentCompartir.setType("application/pdf");
        intentCompartir.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lector_pdf, menu);

        MenuItem item = menu.findItem(R.id.action_compartir);

        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(intentCompartir);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_guardar) {
            guardarFichero();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        } else if(id == R.id.action_enviar_cliente){
            mandarCorreo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* *************************************************************************
     ******************* Interfaz OnPageChangedListener ************************
     *************************************************************************** */

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    /* *************************************************************************
     **************** Interfaz OnProcessCompleteListener ***********************
     *************************************************************************** */

    @Override
    public void resultadoPost(String respuesta, int codigo_peticion) {
        if(respuesta != null){
            Log.v("mio", respuesta);
            switch (codigo_peticion){
                case CODIGO_ENVIAR_MAIL:
                    break;
            }
        }
    }

    /* *************************************************************************
     ******************************** Auxiliares *******************************
     *************************************************************************** */

    private void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst, false);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            Log.v("mio", e.toString());
        }
    }

    private void guardarFichero() {
        File from = new File(fichero);
        File to = new File(Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + "/factura.pdf");
        copy(from, to);
    }

    private void mandarCorreo(){
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put(PARAMENTRO_MAILTO, "");
        parametros.put(PARAMENTRO_ASUNTO, "");
        parametros.put(PARAMENTRO_MENSAJE, "");
        AsyncTaskPost post = new AsyncTaskPost(this, this, Constantes.FACTURAS, CODIGO_ENVIAR_MAIL);
        post.execute(parametros);
    }
}
/*
void CreateDirectoryRecursively(string path)
{
    string[] pathParts = path.Split('\\');

    for (int i = 0; i < pathParts.Length; i++)
    {
        if (i > 0)
            pathParts[i] = Path.Combine(pathParts[i - 1], pathParts[i]);

        if (!Directory.Exists(pathParts[i]))
            Directory.CreateDirectory(pathParts[i]);
    }
}


Intent intentCompartir = new Intent(this, LectorPDF.class);
        intentCompartir.putExtra("pdf", Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath()+"/about.pdf");
        startActivity(intentCompartir);
 */