package tda;

/**
 * Nodo generico para una lista simplemente enlazada.
 * Almacena un dato de tipo T y una referencia al siguiente nodo.
 *
 * @param <T> Tipo de dato almacenado en el nodo.
 * @author Equipo
 */
public class NodoLista<T> {

    /** Dato almacenado en este nodo. */
    private T dato;

    /** Referencia al siguiente nodo en la lista. */
    private NodoLista<T> siguiente;

    /**
     * Construye un nodo con el dato indicado y sin siguiente.
     *
     * @param dato El dato a almacenar.
     */
    public NodoLista(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    /**
     * Obtiene el dato almacenado.
     *
     * @return El dato de tipo T.
     */
    public T getDato() {
        return dato;
    }

    /**
     * Establece el dato almacenado.
     *
     * @param dato Nuevo dato a almacenar.
     */
    public void setDato(T dato) {
        this.dato = dato;
    }

    /**
     * Obtiene la referencia al siguiente nodo.
     *
     * @return El siguiente nodo, o null si no existe.
     */
    public NodoLista<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece la referencia al siguiente nodo.
     *
     * @param siguiente El nuevo nodo siguiente.
     */
    public void setSiguiente(NodoLista<T> siguiente) {
        this.siguiente = siguiente;
    }
}