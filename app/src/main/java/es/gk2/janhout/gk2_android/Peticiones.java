package es.gk2.janhout.gk2_android;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Peticiones {

    public static String peticionGetJSON(Context contexto, String url){
        String linea, resultado = "";

        try {
            URL u = new URL(url);
            URLConnection conexion = u.openConnection();
            conexion.getContent();
            u = new URL(url);
            conexion = u.openConnection();
            conexion.setDoOutput(false);
            String token = Metodos.leerPreferenciasCompartidasString(contexto, contexto.getString(R.string.token_session));
            conexion.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            while ((linea = in.readLine()) != null) {
                resultado += linea + "\n";
            }
            in.close();

            return resultado;
        }catch (IOException e){
            Log.e("error GET", e.toString());
            return null;
        }
    }
}
