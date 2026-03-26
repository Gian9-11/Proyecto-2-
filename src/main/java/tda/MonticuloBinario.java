package tda;
import modelo.RegistroImpresion;

/**
 * Monticulo Binario Minimo (Min-Heap) implementado con un arreglo.
 * El elemento con la menor etiqueta de tiempo siempre esta en la raiz.
 * Se usa como cola de prioridad para la simulacion de impresion.
 *
 * Propiedades mantenidas:
 * - Propiedad de orden: cada padre es menor o igual que sus hijos.
 * - Propiedad de forma: arbol binario completo (se llena de izq a der).
 *
 * El arreglo comienza en el indice 1. Para un nodo en la posicion i:
 * - Padre: i / 2
 * - Hijo izquierdo: 2 * i
 * - Hijo derecho: 2 * i + 1
 *
 * @author Equipo
 */
public class MonticuloBinario {

    /** Arreglo que almacena los registros del monticulo. Posicion 0 no se usa. */
    private RegistroImpresion[] arreglo;

    /** Cantidad actual de elementos en el monticulo. */
    private int tamanioActual;

    /** Capacidad inicial por defecto. */
    private static final int CAPACIDAD_INICIAL = 20;

    /**
     * Construye un monticulo binario vacio con capacidad inicial por defecto.
     */
    public MonticuloBinario() {
        arreglo = new RegistroImpresion[CAPACIDAD_INICIAL + 1];
        tamanioActual = 0;
    }

    /**
     * Construye un monticulo binario vacio con la capacidad indicada.
     *
     * @param capacidad Capacidad inicial del arreglo interno.
     */
    public MonticuloBinario(int capacidad) {
        arreglo = new RegistroImpresion[capacidad + 1];
        tamanioActual = 0;
    }

    /**
     * Inserta un registro en el monticulo. El registro se coloca al final
     * del arreglo y luego se percola hacia arriba para mantener la propiedad
     * de orden. Complejidad: O(log n).
     *
     * @param registro El registro de impresion a insertar.
     */
    public void insertar(RegistroImpresion registro) {
        // Verificar si hay espacio, si no, redimensionar
        if (tamanioActual + 1 >= arreglo.length) {
            redimensionar();
        }
        // Colocar al final del arreglo
        tamanioActual++;
        arreglo[tamanioActual] = registro;
        registro.setIndiceEnMonticulo(tamanioActual);
        // Restaurar propiedad de orden subiendo el elemento
        percolarArriba(tamanioActual);
    }

    /**
     * Elimina y retorna el registro con la menor etiqueta de tiempo (la raiz).
     * Se reemplaza la raiz con el ultimo elemento y se percola hacia abajo.
     * Complejidad: O(log n).
     *
     * @return El registro con menor etiqueta de tiempo, o null si esta vacio.
     */
    public RegistroImpresion eliminarMin() {
        if (estaVacio()) {
            return null;
        }
        // Guardar el minimo (la raiz)
        RegistroImpresion minimo = arreglo[1];
        // Mover el ultimo elemento a la raiz
        arreglo[1] = arreglo[tamanioActual];
        arreglo[tamanioActual] = null;
        tamanioActual--;
        // Actualizar el indice del nuevo elemento en la raiz
        if (tamanioActual > 0) {
            arreglo[1].setIndiceEnMonticulo(1);
            // Restaurar propiedad de orden bajando el elemento
            percolarAbajo(1);
        }
        // Marcar que el registro eliminado ya no esta en el monticulo
        minimo.setIndiceEnMonticulo(-1);
        return minimo;
    }

    /**
     * Consulta el registro con la menor etiqueta de tiempo sin eliminarlo.
     *
     * @return El registro minimo, o null si esta vacio.
     */
    public RegistroImpresion verMin() {
        if (estaVacio()) {
            return null;
        }
        return arreglo[1];
    }

    /**
     * Disminuye la etiqueta de tiempo de un registro en la posicion dada.
     * Luego percola hacia arriba para restaurar la propiedad de orden.
     * Se usa para la operacion de eliminar un registro especifico de la cola:
     * se disminuye su clave al minimo, sube a la raiz, y luego se extrae.
     *
     * @param indice        Posicion en el arreglo del registro a modificar.
     * @param nuevaEtiqueta Nueva etiqueta de tiempo (debe ser menor que la actual).
     */
    public void disminuirClave(int indice, double nuevaEtiqueta) {
        if (indice < 1 || indice > tamanioActual) {
            return;
        }
        // Solo permitir disminuir, no aumentar
        if (nuevaEtiqueta >= arreglo[indice].getEtiquetaTiempo()) {
            return;
        }
        arreglo[indice].setEtiquetaTiempo(nuevaEtiqueta);
        // Subir el elemento para restaurar la propiedad de orden
        percolarArriba(indice);
    }

    /**
     * Percola un elemento hacia arriba (swim/bubble up).
     * Compara con su padre y sube mientras sea menor.
     *
     * @param indice Posicion del elemento a percolar.
     */
    private void percolarArriba(int indice) {
        // Mientras no sea la raiz y sea menor que su padre
        while (indice > 1) {
            int indicePadre = indice / 2;
            double tiempoHijo = arreglo[indice].getEtiquetaTiempo();
            double tiempoPadre = arreglo[indicePadre].getEtiquetaTiempo();
            if (tiempoHijo < tiempoPadre) {
                intercambiar(indice, indicePadre);
                indice = indicePadre;
            } else {
                break; // Ya esta en la posicion correcta
            }
        }
    }

    /**
     * Percola un elemento hacia abajo (sink/bubble down).
     * Compara con sus hijos y baja hacia el menor de ellos
     * mientras sea mayor.
     *
     * @param indice Posicion del elemento a percolar.
     */
    private void percolarAbajo(int indice) {
        // Mientras tenga al menos un hijo izquierdo
        while (2 * indice <= tamanioActual) {
            int hijoIzq = 2 * indice;
            int hijoDer = hijoIzq + 1;
            int hijoMenor = hijoIzq; // Asumir que el izquierdo es el menor
            // Si existe el hijo derecho y es menor que el izquierdo
            if (hijoDer <= tamanioActual &&
                arreglo[hijoDer].getEtiquetaTiempo() < arreglo[hijoIzq].getEtiquetaTiempo()) {
                hijoMenor = hijoDer;
            }
            // Si el hijo menor es mas pequeño que el padre, intercambiar
            if (arreglo[hijoMenor].getEtiquetaTiempo() < arreglo[indice].getEtiquetaTiempo()) {
                intercambiar(indice, hijoMenor);
                indice = hijoMenor;
            } else {
                break; // Ya esta en la posicion correcta
            }
        }
    }

    /**
     * Intercambia dos elementos en el arreglo y actualiza sus indices.
     *
     * @param i Posicion del primer elemento.
     * @param j Posicion del segundo elemento.
     */
    private void intercambiar(int i, int j) {
        RegistroImpresion temp = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temp;
        // Actualizar los indices almacenados en cada registro
        arreglo[i].setIndiceEnMonticulo(i);
        arreglo[j].setIndiceEnMonticulo(j);
    }

    /**
     * Duplica la capacidad del arreglo interno cuando se llena.
     */
    private void redimensionar() {
        int nuevaCapacidad = arreglo.length * 2;
        RegistroImpresion[] nuevo = new RegistroImpresion[nuevaCapacidad];
        // Copiar todos los elementos existentes
        for (int i = 0; i <= tamanioActual; i++) {
            nuevo[i] = arreglo[i];
        }
        arreglo = nuevo;
    }

    /**
     * Indica si el monticulo esta vacio.
     *
     * @return true si no hay elementos, false si hay al menos uno.
     */
    public boolean estaVacio() {
        return tamanioActual == 0;
    }

    /**
     * Retorna la cantidad de elementos en el monticulo.
     *
     * @return El numero de elementos.
     */
    public int tamanio() {
        return tamanioActual;
    }

    /**
     * Obtiene el registro en la posicion indicada del arreglo.
     * Util para visualizar el monticulo.
     *
     * @param indice Posicion en el arreglo (1 a tamanioActual).
     * @return El registro en esa posicion, o null si el indice es invalido.
     */
    public RegistroImpresion obtenerEnIndice(int indice) {
        if (indice < 1 || indice > tamanioActual) {
            return null;
        }
        return arreglo[indice];
    }

    /**
     * Retorna una referencia al arreglo interno. Uso exclusivo
     * para la representacion grafica del monticulo.
     *
     * @return El arreglo de registros.
     */
    public RegistroImpresion[] getArreglo() {
        return arreglo;
    }

    /**
     * Retorna el tamanio actual del monticulo.
     *
     * @return Cantidad de elementos.
     */
    public int getTamanioActual() {
        return tamanioActual;
    }
}