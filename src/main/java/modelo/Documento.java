

package modelo;

/**
 * Representa un documento creado por un usuario.
 * Cada documento tiene nombre, tamanio en paginas, tipo de archivo
 * y un indicador de si ya fue enviado a la cola de impresion.
 *
 * @author Equipo
 */
public class Documento {

    /** Nombre del documento. */
    private String nombre;

    /** Tamanio del documento en numero de paginas. */
    private int tamanioPaginas;

    /** Tipo de archivo (por ejemplo: pdf, txt, docx). */
    private String tipo;

    /** Indica si el documento ya fue enviado a la cola de impresion. */
    private boolean enColaImpresion;

    /**
     * Construye un documento con los datos indicados.
     *
     * @param nombre         Nombre del documento.
     * @param tamanioPaginas Tamanio en paginas.
     * @param tipo           Tipo de archivo.
     */
    public Documento(String nombre, int tamanioPaginas, String tipo) {
        this.nombre = nombre;
        this.tamanioPaginas = tamanioPaginas;
        this.tipo = tipo;
        this.enColaImpresion = false;
    }

    /**
     * Obtiene el nombre del documento.
     *
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del documento.
     *
     * @param nombre Nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tamanio en paginas.
     *
     * @return Numero de paginas.
     */
    public int getTamanioPaginas() {
        return tamanioPaginas;
    }

    /**
     * Establece el tamanio en paginas.
     *
     * @param tamanioPaginas Nuevo tamanio.
     */
    public void setTamanioPaginas(int tamanioPaginas) {
        this.tamanioPaginas = tamanioPaginas;
    }

    /**
     * Obtiene el tipo de archivo.
     *
     * @return El tipo (pdf, txt, etc.).
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de archivo.
     *
     * @param tipo Nuevo tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Indica si el documento esta en la cola de impresion.
     *
     * @return true si esta en cola, false si no.
     */
    public boolean isEnColaImpresion() {
        return enColaImpresion;
    }

    /**
     * Establece el estado de cola de impresion.
     *
     * @param enColaImpresion true si esta en cola, false si no.
     */
    public void setEnColaImpresion(boolean enColaImpresion) {
        this.enColaImpresion = enColaImpresion;
    }

    /**
     * Representacion en texto del documento.
     *
     * @return Cadena con la informacion del documento.
     */
    @Override
    public String toString() {
        return nombre + " (" + tamanioPaginas + " pags, " + tipo + ")";
    }
}
