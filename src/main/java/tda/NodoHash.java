package tda;

import modelo.RegistroImpresion;

/**
 * Nodo para la tabla de dispersion con encadenamiento separado.
 * Cada nodo almacena la clave (nombre de usuario), una lista de
 * registros de impresion asociados a ese usuario, y una referencia
 * al siguiente nodo en la cadena (para resolver colisiones).
 *
 * @author Equipo
 */
public class NodoHash {

    /** Clave del nodo: nombre del usuario. */
    private String clave;

    /** Lista de registros de impresion de este usuario en la cola. */
    private ListaSimple<RegistroImpresion> registros;

    /** Referencia al siguiente nodo en la cadena (colision). */
    private NodoHash siguiente;

    /**
     * Construye un nodo hash con la clave indicada.
     * Inicializa la lista de registros vacia.
     *
     * @param clave Nombre del usuario (clave de la tabla).
     */
    public NodoHash(String clave) {
        this.clave = clave;
        this.registros = new ListaSimple<>();
        this.siguiente = null;
    }

    /**
     * Obtiene la clave del nodo.
     *
     * @return El nombre del usuario.
     */
    public String getClave() {
        return clave;
    }

    /**
     * Obtiene la lista de registros de impresion.
     *
     * @return La lista de registros.
     */
    public ListaSimple<RegistroImpresion> getRegistros() {
        return registros;
    }

    /**
     * Obtiene el siguiente nodo en la cadena.
     *
     * @return El siguiente nodo, o null si no hay.
     */
    public NodoHash getSiguiente() {
        return siguiente;
    }

    /**
     * Establece el siguiente nodo en la cadena.
     *
     * @param siguiente El nuevo nodo siguiente.
     */
    public void setSiguiente(NodoHash siguiente) {
        this.siguiente = siguiente;
    }
}