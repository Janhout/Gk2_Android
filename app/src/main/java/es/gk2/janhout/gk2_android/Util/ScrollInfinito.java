package es.gk2.janhout.gk2_android.util;

import android.widget.AbsListView;

public abstract class ScrollInfinito implements AbsListView.OnScrollListener {

    private int bajoLista = 5;
    private int paginaActual = 0;
    private int cargadosAnterior = 0;
    private boolean cargando = true;

    public ScrollInfinito(int bajoLista) {
        this.bajoLista = bajoLista;
    }

    public abstract void cargaMas(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < cargadosAnterior) {
            this.paginaActual = 0;
            this.cargadosAnterior = totalItemCount;
            if (totalItemCount == 0) { this.cargando = true; }
        }

        // si el estado es cargando y el número de elementos actual es mayor que el número de elementos anterior,
        // es que ya hemos terminado la carga, por lo que cargando pasaría a false.
        if (cargando && (totalItemCount > cargadosAnterior)) {
            cargando = false;
            cargadosAnterior = totalItemCount;
            paginaActual++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!cargando && (totalItemCount - visibleItemCount)<=(firstVisibleItem + bajoLista)) {
            //cargaMas(paginaActual+1, totalItemCount);
            cargaMas(paginaActual, totalItemCount);
            cargando = true;
        }
    }
}
