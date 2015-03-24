package es.gk2.janhout.gk2_android;


public class ItemNavigationDrawer {

    private String nombre;
    private int icono;

    public ItemNavigationDrawer(String nombre, int icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}