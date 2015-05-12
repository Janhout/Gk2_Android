package es.gk2.janhout.gk2_android.modelos;


public class ItemNavigationDrawer {

    private String nombre;
    private String icono;

    public ItemNavigationDrawer(String nombre, String icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}