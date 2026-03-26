package tda;

/**
 * Lista simplemente enlazada generica implementada sin usar java.util.
 * Provee operaciones basicas de insercion, eliminacion, busqueda
 * y acceso por indice.
 *
 * @param <T> Tipo de dato que almacena la lista.
 * @author Equipo
 */
public class ListaSimple<T> {

    /** Primer nodo de la lista. */
    private NodoLista<T> cabeza;

    /** Cantidad de elementos en la lista. */
    private int tamanio;

    /**
     * Construye una lista vacia.
     */
    public ListaSimple() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param dato El elemento a agregar.
     */
    public void agregar(T dato) {
        NodoLista<T> nuevo = new NodoLista<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            // Recorrer hasta el ultimo nodo
            NodoLista<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamanio++;
    }

    /**
     * Agrega un elemento al inicio de la lista.
     *
     * @param dato El elemento a agregar al inicio.
     */
    public void agregarAlInicio(T dato) {
        NodoLista<T> nuevo = new NodoLista<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamanio++;
    }

    /**
     * Obtiene el elemento en la posicion indicada.
     *
     * @param indice Posicion del elemento (base 0).
     * @return El elemento en esa posicion.
     * @throws IndexOutOfBoundsException Si el indice esta fuera de rango.
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException(
                "Indice " + indice + " fuera de rango. Tamanio: " + tamanio);
        }
        NodoLista<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * Elimina la primera ocurrencia del elemento indicado.
     * Usa el metodo equals() para comparar.
     *
     * @param dato El elemento a eliminar.
     * @return true si se elimino, false si no se encontro.
     */
    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }
        // Caso especial: el elemento esta en la cabeza
        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }
        // Buscar el nodo anterior al que contiene el dato
        NodoLista<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Elimina el elemento en la posicion indicada.
     *
     * @param indice Posicion del elemento a eliminar (base 0).
     * @return El elemento eliminado.
     * @throws IndexOutOfBoundsException Si el indice esta fuera de rango.
     */
    public T eliminarEnIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException(
                "Indice " + indice + " fuera de rango. Tamanio: " + tamanio);
        }
        T eliminado;
        if (indice == 0) {
            eliminado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
        } else {
            NodoLista<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            eliminado = actual.getSiguiente().getDato();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
        tamanio--;
        return eliminado;
    }

    /**
     * Elimina una ocurrencia por referencia exacta (==), no por equals.
     * Util cuando se necesita eliminar un objeto especifico.
     *
     * @param dato La referencia exacta del objeto a eliminar.
     * @return true si se encontro y elimino, false en caso contrario.
     */
    public boolean eliminarPorReferencia(T dato) {
        if (cabeza == null) {
            return false;
        }
        if (cabeza.getDato() == dato) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }
        NodoLista<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato() == dato) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Verifica si la lista contiene el elemento (por equals).
     *
     * @param dato Elemento a buscar.
     * @return true si se encuentra, false si no.
     */
    public boolean contiene(T dato) {
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Retorna la cantidad de elementos en la lista.
     *
     * @return El tamanio de la lista.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Indica si la lista esta vacia.
     *
     * @return true si no hay elementos, false si hay al menos uno.
     */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Vacia la lista eliminando todos sus elementos.
     */
    public void vaciar() {
        cabeza = null;
        tamanio = 0;
    }

    /**
     * Retorna la cabeza de la lista (primer nodo).
     * Util para recorridos externos.
     *
     * @return El primer nodo de la lista, o null si esta vacia.
     */
    public NodoLista<T> getCabeza() {
        return cabeza;
    }
}