package tda;

import modelo.RegistroImpresion;

/**
 * Tabla de dispersion (Hash Table) con encadenamiento separado.
 * Implementada desde cero sin usar java.util.
 *
 * La clave es el nombre de usuario y el valor es una lista de
 * registros de impresion que ese usuario tiene en la cola.
 * Permite busqueda en tiempo cercano a O(1).
 *
 * Funcion hash: polinomial con base 31.
 * Resolucion de colisiones: encadenamiento separado (listas enlazadas).
 *
 * @author Equipo
 */
public class TablaHash {

    /** Arreglo de cubetas (buckets). Cada posicion es la cabeza de una cadena. */
    private NodoHash[] tabla;

    /** Capacidad actual de la tabla (numero de cubetas). */
    private int capacidad;

    /** Cantidad de claves distintas almacenadas. */
    private int tamanio;

    /** Factor de carga maximo antes de redimensionar. */
    private static final double FACTOR_CARGA_MAX = 0.75;

    /**
     * Construye una tabla hash con capacidad inicial.
     * La capacidad se ajusta al primo mas cercano.
     *
     * @param capacidadInicial Capacidad inicial deseada.
     */
    public TablaHash(int capacidadInicial) {
        this.capacidad = siguientePrimo(capacidadInicial);
        this.tabla = new NodoHash[capacidad];
        this.tamanio = 0;
    }

    /**
     * Funcion de dispersion (hash). Calcula un indice en el arreglo
     * a partir de la clave usando el metodo polinomial con base 31.
     *
     * @param clave La cadena a dispersar.
     * @return Indice en el arreglo de cubetas (0 a capacidad-1).
     */
    private int funcionHash(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash = 31 * hash + clave.charAt(i);
        }
        // Asegurar que el indice sea positivo
        return Math.abs(hash) % capacidad;
    }

    /**
     * Inserta un registro de impresion bajo la clave del usuario.
     * Si el usuario ya tiene una entrada, agrega el registro a su lista.
     * Si no, crea una nueva entrada.
     *
     * @param clave    Nombre del usuario.
     * @param registro Registro de impresion a asociar.
     */
    public void insertar(String clave, RegistroImpresion registro) {
        // Verificar factor de carga
        if ((double)(tamanio + 1) / capacidad > FACTOR_CARGA_MAX) {
            redimensionar();
        }
        int indice = funcionHash(clave);
        // Buscar si ya existe un nodo con esa clave en la cadena
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.getClave().equals(clave)) {
                // Ya existe, agregar registro a su lista
                actual.getRegistros().agregar(registro);
                return;
            }
            actual = actual.getSiguiente();
        }
        // No existe, crear nuevo nodo y agregarlo al inicio de la cadena
        NodoHash nuevo = new NodoHash(clave);
        nuevo.getRegistros().agregar(registro);
        nuevo.setSiguiente(tabla[indice]);
        tabla[indice] = nuevo;
        tamanio++;
    }

    /**
     * Busca la lista de registros de impresion de un usuario.
     * Complejidad promedio: O(1).
     *
     * @param clave Nombre del usuario a buscar.
     * @return Lista de registros del usuario, o null si no tiene documentos en cola.
     */
    public ListaSimple<RegistroImpresion> buscar(String clave) {
        int indice = funcionHash(clave);
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.getClave().equals(clave)) {
                return actual.getRegistros();
            }
            actual = actual.getSiguiente();
        }
        return null; // No se encontro
    }

    /**
     * Elimina un registro de impresion especifico de la lista de un usuario.
     * Si la lista del usuario queda vacia, se elimina la entrada completa.
     *
     * @param clave    Nombre del usuario.
     * @param registro Registro especifico a eliminar (por referencia).
     * @return true si se elimino exitosamente, false si no se encontro.
     */
    public boolean eliminarRegistro(String clave, RegistroImpresion registro) {
        int indice = funcionHash(clave);
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.getClave().equals(clave)) {
                boolean eliminado = actual.getRegistros().eliminarPorReferencia(registro);
                // Si la lista quedo vacia, eliminar el nodo completo
                if (eliminado && actual.getRegistros().estaVacia()) {
                    eliminarNodoDeCubeta(indice, clave);
                }
                return eliminado;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Elimina un nodo completo de una cubeta (cuando su lista queda vacia).
     *
     * @param indiceCubeta Indice de la cubeta en el arreglo.
     * @param clave        Clave del nodo a eliminar.
     */
    private void eliminarNodoDeCubeta(int indiceCubeta, String clave) {
        NodoHash actual = tabla[indiceCubeta];
        if (actual == null) return;
        // Si es el primer nodo de la cadena
        if (actual.getClave().equals(clave)) {
            tabla[indiceCubeta] = actual.getSiguiente();
            tamanio--;
            return;
        }
        // Buscar en el resto de la cadena
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getClave().equals(clave)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    /**
     * Redimensiona la tabla al doble de capacidad (primo mas cercano)
     * y reinserta todos los elementos.
     */
    private void redimensionar() {
        int nuevaCapacidad = siguientePrimo(capacidad * 2);
        NodoHash[] tablaVieja = tabla;
        int capacidadVieja = capacidad;
        tabla = new NodoHash[nuevaCapacidad];
        capacidad = nuevaCapacidad;
        tamanio = 0;
        // Reinsertar todos los nodos
        for (int i = 0; i < capacidadVieja; i++) {
            NodoHash actual = tablaVieja[i];
            while (actual != null) {
                // Reinsertar cada registro de la lista del nodo
                ListaSimple<RegistroImpresion> regs = actual.getRegistros();
                for (int j = 0; j < regs.tamanio(); j++) {
                    insertar(actual.getClave(), regs.obtener(j));
                }
                actual = actual.getSiguiente();
            }
        }
    }

    /**
     * Encuentra el siguiente numero primo mayor o igual al dado.
     * Se usa para la capacidad de la tabla.
     *
     * @param n Numero a partir del cual buscar.
     * @return El primo mas cercano mayor o igual a n.
     */
    private int siguientePrimo(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++; // Empezar con impar
        while (!esPrimo(n)) {
            n += 2;
        }
        return n;
    }

    /**
     * Verifica si un numero es primo.
     *
     * @param n Numero a verificar.
     * @return true si es primo, false si no.
     */
    private boolean esPrimo(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Retorna la cantidad de claves (usuarios) en la tabla.
     *
     * @return Numero de usuarios con documentos en cola.
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Retorna la capacidad actual de la tabla.
     *
     * @return Numero de cubetas.
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Retorna el arreglo interno de cubetas.
     * Uso exclusivo para depuracion o visualizacion.
     *
     * @return El arreglo de NodoHash.
     */
    public NodoHash[] getTabla() {
        return tabla;
    }
}