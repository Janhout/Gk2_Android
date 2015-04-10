package es.gk2.janhout.gk2_android.Actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
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

import es.gk2.janhout.gk2_android.R;

public class LectorPDF extends ActionBarActivity implements OnPageChangeListener {

    private Integer pageNumber;
    private Intent intentCompartir;
    private String fichero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_pdf);

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

        intentCompartir = new Intent();
        intentCompartir.setAction(Intent.ACTION_SEND);
        intentCompartir.setType("application/pdf");
        intentCompartir.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarFichero() {
        File from = new File(fichero);
        File to = new File(Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + "/factura.pdf");
        copy(from, to);
    }

    public void copy(File src, File dst) {
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