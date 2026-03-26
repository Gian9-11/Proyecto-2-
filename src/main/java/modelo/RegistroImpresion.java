package modelo;

/**
 * Registro que se almacena en la cola de impresion (monticulo binario).
 * Contiene los datos del documento, una etiqueta de tiempo que determina
 * su prioridad en la cola, y metadatos internos para la gestion
 * por parte de la tabla hash y el monticulo.
 * 
 * Nota: la cola de impresion ordena exclusivamente por etiquetaTiempo.
 * El campo propietario se usa internamente para la tabla de dispersion,
 * pero no se muestra en la vista de la cola.
 *
 * @author Equipo
 */
public class RegistroImpresion {

    /** Contador estatico para generar IDs unicos. */
    private static int contadorId = 0;

    /** Identificador unico de este registro. */
    private int id;

    /** Nombre del documento. */
    private String nombreDocumento;

    /** Tamanio del documento en paginas. */
    private int tamanioPaginas;

    /** Tipo de archivo del documento. */
    private String tipoDocumento;

    /** Etiqueta de tiempo que define la prioridad en la cola (menor = mayor prioridad). */
    private double etiquetaTiempo;

    /** Indice actual de este registro dentro del arreglo del monticulo. */
    private int indiceEnMonticulo;

    /**
     * Nombre del usuario propietario. Se usa internamente para la
     * tabla de dispersion, no se muestra en la cola.
     */
    private String propietario;

    /** Referencia al documento original del usuario para actualizar su estado. */
    private Documento documentoOriginal;

    /**
     * Construye un registro de impresion.
     *
     * @param nombreDocumento   Nombre del documento.
     * @param tamanioPaginas    Tamanio en paginas.
     * @param tipoDocumento     Tipo de archivo.
     * @param etiquetaTiempo    Etiqueta de tiempo (prioridad).
     * @param propietario       Nombre del usuario propietario.
     * @param documentoOriginal Referencia al documento original.
     */
    public RegistroImpresion(String nombreDocumento, int tamanioPaginas,
                             String tipoDocumento, double etiquetaTiempo,
                             String propietario, Documento documentoOriginal) {
        this.id = ++contadorId;
        this.nombreDocumento = nombreDocumento;
        this.tamanioPaginas = tamanioPaginas;
        this.tipoDocumento = tipoDocumento;
        this.etiquetaTiempo = etiquetaTiempo;
        this.indiceEnMonticulo = -1; // Se asigna al insertar en el monticulo
        this.propietario = propietario;
        this.documentoOriginal = documentoOriginal;
    }

    // ==================== Getters y Setters ====================

    /**
     * Obtiene el ID unico del registro.
     *
     * @return El ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del documento.
     *
     * @return Nombre del documento.
     */
    public String getNombreDocumento() {
        return nombreDocumento;
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
     * Obtiene el tipo de archivo.
     *
     * @return Tipo del documento.
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Obtiene la etiqueta de tiempo (prioridad en la cola).
     *
     * @return La etiqueta de tiempo.
     */
    public double getEtiquetaTiempo() {
        return etiquetaTiempo;
    }

    /**
     * Establece la etiqueta de tiempo. Se usa en la operacion
     * de disminuir clave para eliminar un registro de la cola.
     *
     * @param etiquetaTiempo Nueva etiqueta de tiempo.
     */
    public void setEtiquetaTiempo(double etiquetaTiempo) {
        this.etiquetaTiempo = etiquetaTiempo;
    }

    /**
     * Obtiene el indice actual en el arreglo del monticulo.
     *
     * @return El indice en el monticulo.
     */
    public int getIndiceEnMonticulo() {
        return indiceEnMonticulo;
    }

    /**
     * Establece el indice en el monticulo. El monticulo lo actualiza
     * internamente cada vez que mueve este registro.
     *
     * @param indiceEnMonticulo Nuevo indice.
     */
    public void setIndiceEnMonticulo(int indiceEnMonticulo) {
        this.indiceEnMonticulo = indiceEnMonticulo;
    }

    /**
     * Obtiene el nombre del propietario.
     *
     * @return Nombre del usuario propietario.
     */
    public String getPropietario() {
        return propietario;
    }

    /**
     * Obtiene la referencia al documento original.
     *
     * @return El documento original.
     */
    public Documento getDocumentoOriginal() {
        return documentoOriginal;
    }

    /**
     * Representacion en texto del registro para la vista de cola.
     * No incluye informacion del propietario.
     *
     * @return Cadena con datos del registro.
     */
    @Override
    public String toString() {
        return "[ID:" + id + "] " + nombreDocumento + " | " +
               tamanioPaginas + " pags | " + tipoDocumento +
               " | t=" + String.format("%.2f", etiquetaTiempo);
    }
}