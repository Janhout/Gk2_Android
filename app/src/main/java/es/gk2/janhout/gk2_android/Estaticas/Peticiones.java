package es.gk2.janhout.gk2_android.Estaticas;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.R;

public class Peticiones {

    public static String peticionGetJSON(Context contexto, String url){
        String linea;
        StringBuilder resultado = new StringBuilder("");
        try {
            URL u = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection) u.openConnection();
            conexion.setDoOutput(false);

            String token = Metodos.leerPreferenciasCompartidasString(contexto, contexto.getString(R.string.token_session));
            conexion.addRequestProperty("Authorization", "Bearer " + token);

            String cookieSesion = Metodos.leerPreferenciasCompartidasString(contexto, "cookieSesion");
            conexion.setRequestProperty("Cookie", cookieSesion);

            conexion.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            while ((linea = in.readLine()) != null) {
                resultado.append(linea);
            }
            in.close();

            return resultado.toString();
        }catch (IOException e){
            Log.e("error GET", e.toString());
            return null;
        }
    }

    public static String peticionGetFichero(Context contexto, String url){
        try {
            URL u = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection) u.openConnection();
            conexion.setDoOutput(false);

            String token = Metodos.leerPreferenciasCompartidasString(contexto, contexto.getString(R.string.token_session));
            conexion.addRequestProperty("Authorization", "Bearer " + token);

            String cookieSesion = Metodos.leerPreferenciasCompartidasString(contexto, "cookieSesion");
            conexion.setRequestProperty("Cookie", cookieSesion);

            conexion.connect();

            InputStream input = conexion.getInputStream();

            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath() + "/factura.tmp");

            byte data[] = new byte[1024];

            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/factura.tmp";
        } catch (Exception e) {
            Log.e("error GET fichero", e.toString());
            return null;
        }
    }

    public static String peticionPostJSON(Context contexto, String url, Hashtable<String, String> params) {
        String linea;
        StringBuilder resultado = new StringBuilder("");
        try {
            URL u = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection)u.openConnection();

            conexion.setInstanceFollowRedirects(false);

            String cookieSesion = Metodos.leerPreferenciasCompartidasString(contexto, "cookieSesion");

            if (cookieSesion != null && !cookieSesion.equals("")) {
                conexion.setRequestProperty("Cookie", cookieSesion);
            }

            String parametros = crearParametros(params);

            conexion.setRequestProperty("Content-size", Integer.toString(parametros.length()));

            conexion.setRequestMethod("POST");

            conexion.setDoInput(true);
            conexion.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(conexion.getOutputStream());
            out.write(parametros);
            out.flush();

            boolean redireccionar = leerCabeceras(conexion);

            if(redireccionar) {
                leerCookies(contexto, conexion);

                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                while ((linea = in.readLine()) != null) {
                    resultado.append(linea);
                }

                in.close();
                return resultado.toString();
            }
            return null;
        }catch (IOException e){
            Log.e("error POST", e.toString());
            return null;
        }
    }

    private static String crearParametros(Hashtable<String, String> params){
        if(params.size() == 0) {
            return "";
        }
        StringBuilder resultado = new StringBuilder();
        Enumeration<String> keys = params.keys();
        while(keys.hasMoreElements()) {
            resultado.append(resultado.length() == 0 ? "" : "&");
            String key = keys.nextElement();
            resultado.append(key).append("=").append(params.get(key));
        }
        return resultado.toString();
    }

    private static boolean leerCabeceras(HttpURLConnection con){
        String cabecera;
        String nombreCabecera;
        for (int i = 1; (nombreCabecera = con.getHeaderFieldKey(i)) != null; i++) {
            if (nombreCabecera.equals("Location")) {
                cabecera = con.getHeaderField(i);
                return !cabecera.contains("login");
            }
        }
        return false;
    }

    private static void leerCookies(Context contexto, HttpURLConnection con) {
        StringBuilder cookieBuffer = null;
        String cabecera;
        String nombreCabecera;
        for (int i = 1; (nombreCabecera = con.getHeaderFieldKey(i)) != null; i++) {
            if (nombreCabecera.toLowerCase().equals("set-cookie")) {
                cabecera = con.getHeaderField(i);
                cabecera = cabecera.substring(0, cabecera.indexOf(";"));
                if (cookieBuffer != null && cabecera.contains("PHP")) {
                    cookieBuffer.append("; ");
                } else {
                    cookieBuffer = new StringBuilder();
                }
                cookieBuffer.append(cabecera);
            }
        }
        if (cookieBuffer != null) {
            Metodos.escribirPreferenciasCompartidasString(contexto, "cookieSesion", cookieBuffer.toString());
        }
    }
}
