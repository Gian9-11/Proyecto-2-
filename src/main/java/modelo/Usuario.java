package modelo;

import tda.ListaSimple;

/**
 * Representa un usuario del sistema de impresion.
 * Cada usuario tiene un nombre unico, un tipo de prioridad
 * y una lista de documentos creados.
 *
 * @author Equipo
 */
public class Usuario {

    /** Nombre identificador unico del usuario. */
    private String nombre;

    /** Tipo de usuario que define su nivel de prioridad. */
    private String tipo;

    /** Lista de documentos creados por este usuario. */
    private ListaSimple<Documento> documentos;

    /**
     * Construye un usuario con nombre y tipo.
     * Los tipos validos son: prioridad_alta, prioridad_media, prioridad_baja.
     *
     * @param nombre Nombre identificador del usuario.
     * @param tipo   Tipo de prioridad del usuario.
     */
    public Usuario(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.documentos = new ListaSimple<>();
    }

    /**
     * Agrega un documento a la lista de documentos del usuario.
     *
     * @param doc El documento a agregar.
     */
    public void agregarDocumento(Documento doc) {
        documentos.agregar(doc);
    }

    /**
     * Elimina un documento que no este en la cola de impresion.
     *
     * @param doc El documento a eliminar.
     * @return true si se pudo eliminar, false si no se encontro
     *         o si esta en la cola de impresion.
     */
    public boolean eliminarDocumento(Documento doc) {
        if (doc.isEnColaImpresion()) {
            return false; // No se puede eliminar si esta en cola
        }
        return documentos.eliminarPorReferencia(doc);
    }

    /**
     * Obtiene el factor de prioridad asociado al tipo de usuario.
     * Este factor multiplica la etiqueta de tiempo para alterar la prioridad.
     * Un factor menor significa mayor prioridad en el monticulo min.
     *
     * @return Factor de prioridad: 0.25 para alta, 0.50 para media, 1.0 para baja.
     */
    public double getFactorPrioridad() {
        switch (tipo) {
            case "prioridad_alta":
                return 0.25;
            case "prioridad_media":
                return 0.50;
            case "prioridad_baja":
                return 1.0;
            default:
                return 1.0;
        }
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el tipo de prioridad del usuario.
     *
     * @return El tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene la lista de documentos del usuario.
     *
     * @return La lista de documentos.
     */
    public ListaSimple<Documento> getDocumentos() {
        return documentos;
    }

    /**
     * Representacion en texto del usuario.
     *
     * @return Cadena con nombre y tipo.
     */
    @Override
    public String toString() {
        return nombre + " [" + tipo + "]";
    }

    /**
     * Compara usuarios por nombre.
     *
     * @param obj Objeto a comparar.
     * @return true si tienen el mismo nombre.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario otro = (Usuario) obj;
        return nombre.equals(otro.nombre);
    }
}
