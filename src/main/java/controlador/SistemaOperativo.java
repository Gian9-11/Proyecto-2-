package controlador;

import modelo.Documento;
import modelo.RegistroImpresion;
import modelo.Reloj;
import modelo.Usuario;
import tda.ListaSimple;
import tda.MonticuloBinario;
import tda.TablaHash;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controlador principal de la simulacion. Actua como el sistema operativo
 * que administra usuarios, documentos, la cola de impresion (monticulo binario)
 * y la tabla de dispersion para busqueda rapida.
 *
 * Coordina la interaccion entre los TDA y provee la logica de negocio
 * para todas las operaciones requeridas.
 *
 * @author Equipo
 */
public class SistemaOperativo {

    /** Lista de usuarios registrados en el sistema. */
    private ListaSimple<Usuario> usuarios;

    /** Cola de impresion implementada como monticulo binario minimo. */
    private MonticuloBinario colaImpresion;

    /** Tabla de dispersion que mapea usuario a sus registros en la cola. */
    private TablaHash tablaHash;

    /** Reloj de la simulacion. */
    private Reloj reloj;

    /** Informacion del ultimo documento impreso. */
    private String ultimoImpreso;

    /**
     * Construye el sistema operativo e inicializa todos los componentes.
     */
    public SistemaOperativo() {
        usuarios = new ListaSimple<>();
        colaImpresion = new MonticuloBinario(20);
        tablaHash = new TablaHash(17);
        reloj = new Reloj();
        ultimoImpreso = "Ninguno";
    }

    // ============================================================
    //                  GESTION DE USUARIOS
    // ============================================================

    /**
     * Agrega un nuevo usuario al sistema.
     *
     * @param nombre Nombre identificador unico del usuario.
     * @param tipo   Tipo de prioridad (prioridad_alta, prioridad_media, prioridad_baja).
     * @return Mensaje indicando el resultado de la operacion.
     */
    public String agregarUsuario(String nombre, String tipo) {
        // Validar que no exista un usuario con ese nombre
        if (buscarUsuario(nombre) != null) {
            return "Error: Ya existe un usuario con el nombre '" + nombre + "'.";
        }
        // Validar tipo
        if (!tipo.equals("prioridad_alta") && !tipo.equals("prioridad_media")
            && !tipo.equals("prioridad_baja")) {
            return "Error: Tipo de usuario invalido. Use: prioridad_alta, prioridad_media o prioridad_baja.";
        }
        Usuario nuevo = new Usuario(nombre, tipo);
        usuarios.agregar(nuevo);
        return "Usuario '" + nombre + "' agregado exitosamente.";
    }

    /**
     * Carga usuarios desde un archivo CSV.
     * Formato esperado: primera linea es encabezado, las siguientes: usuario, tipo
     *
     * @param archivo Archivo CSV a cargar.
     * @return Mensaje con el resultado de la carga.
     */
    public String cargarUsuariosCSV(File archivo) {
        int cargados = 0;
        int errores = 0;
        StringBuilder detalle = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                // Saltar la primera linea (encabezado)
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                // Separar por coma
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    String nombre = partes[0].trim();
                    String tipo = partes[1].trim();
                    String resultado = agregarUsuario(nombre, tipo);
                    if (resultado.startsWith("Error")) {
                        errores++;
                        detalle.append("  - ").append(resultado).append("\n");
                    } else {
                        cargados++;
                    }
                } else {
                    errores++;
                    detalle.append("  - Linea invalida: ").append(linea).append("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            return "Error al leer el archivo: " + e.getMessage();
        }
        String msg = "Carga completada. Usuarios cargados: " + cargados + ". Errores: " + errores + ".";
        if (errores > 0) {
            msg += "\nDetalles:\n" + detalle.toString();
        }
        return msg;
    }

    /**
     * Elimina un usuario del sistema. Elimina sus documentos creados
     * pero NO los que ya estan en la cola de impresion.
     *
     * @param nombre Nombre del usuario a eliminar.
     * @return Mensaje con el resultado.
     */
    public String eliminarUsuario(String nombre) {
        Usuario usuario = buscarUsuario(nombre);
        if (usuario == null) {
            return "Error: No se encontro al usuario '" + nombre + "'.";
        }
        usuarios.eliminar(usuario);
        return "Usuario '" + nombre + "' eliminado. Sus documentos en cola permanecen.";
    }

    /**
     * Busca un usuario por nombre.
     *
     * @param nombre Nombre del usuario a buscar.
     * @return El usuario encontrado, o null si no existe.
     */
    public Usuario buscarUsuario(String nombre) {
        for (int i = 0; i < usuarios.tamanio(); i++) {
            if (usuarios.obtener(i).getNombre().equals(nombre)) {
                return usuarios.obtener(i);
            }
        }
        return null;
    }

    // ============================================================
    //                  GESTION DE DOCUMENTOS
    // ============================================================

    /**
     * Crea un documento para un usuario.
     *
     * @param nombreUsuario  Nombre del usuario propietario.
     * @param nombreDoc      Nombre del documento.
     * @param tamanioPaginas Tamanio en paginas.
     * @param tipoDoc        Tipo de archivo (pdf, txt, docx, etc.).
     * @return Mensaje con el resultado.
     */
    public String crearDocumento(String nombreUsuario, String nombreDoc,
                                  int tamanioPaginas, String tipoDoc) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) {
            return "Error: Usuario no encontrado.";
        }
        if (nombreDoc == null || nombreDoc.trim().isEmpty()) {
            return "Error: El nombre del documento no puede estar vacio.";
        }
        if (tamanioPaginas <= 0) {
            return "Error: El tamanio debe ser mayor a 0 paginas.";
        }
        Documento doc = new Documento(nombreDoc.trim(), tamanioPaginas, tipoDoc);
        usuario.agregarDocumento(doc);
        return "Documento '" + nombreDoc + "' creado para " + nombreUsuario + ".";
    }

    /**
     * Elimina un documento de un usuario (solo si no esta en la cola).
     *
     * @param usuario   El usuario propietario.
     * @param documento El documento a eliminar.
     * @return Mensaje con el resultado.
     */
    public String eliminarDocumento(Usuario usuario, Documento documento) {
        if (documento.isEnColaImpresion()) {
            return "Error: No se puede eliminar un documento que esta en la cola de impresion.";
        }
        boolean eliminado = usuario.eliminarDocumento(documento);
        if (eliminado) {
            return "Documento '" + documento.getNombre() + "' eliminado.";
        }
        return "Error: No se pudo eliminar el documento.";
    }

    // ============================================================
    //                  COLA DE IMPRESION
    // ============================================================

    /**
     * Envia un documento a la cola de impresion.
     * Crea un registro de impresion con la etiqueta de tiempo,
     * lo inserta en el monticulo binario y registra en la tabla hash.
     *
     * @param usuario      El usuario que envia el documento.
     * @param documento    El documento a imprimir.
     * @param esPrioritario true si se debe aplicar prioridad del usuario.
     * @return Mensaje con el resultado.
     */
    public String enviarAImprimir(Usuario usuario, Documento documento,
                                   boolean esPrioritario) {
        if (documento.isEnColaImpresion()) {
            return "Error: Este documento ya esta en la cola de impresion.";
        }
        // Obtener tiempo actual del reloj
        double tiempoActual = reloj.tiempoTranscurrido();
        // Calcular etiqueta de tiempo segun prioridad
        double etiqueta;
        if (esPrioritario) {
            etiqueta = tiempoActual * usuario.getFactorPrioridad();
        } else {
            etiqueta = tiempoActual;
        }
        // Crear registro de impresion
        RegistroImpresion registro = new RegistroImpresion(
            documento.getNombre(),
            documento.getTamanioPaginas(),
            documento.getTipo(),
            etiqueta,
            usuario.getNombre(),
            documento
        );
        // Insertar en el monticulo binario (cola de prioridad)
        colaImpresion.insertar(registro);
        // Registrar en la tabla de dispersion
        tablaHash.insertar(usuario.getNombre(), registro);
        // Marcar documento como en cola
        documento.setEnColaImpresion(true);
        String prioMsg = esPrioritario ? " (PRIORITARIO, factor=" + usuario.getFactorPrioridad() + ")" : "";
        return "Documento enviado a imprimir" + prioMsg +
               ". Etiqueta de tiempo: " + String.format("%.4f", etiqueta);
    }

    /**
     * Libera la impresora: extrae e "imprime" el documento con menor
     * etiqueta de tiempo. Equivale a eliminar_min del monticulo.
     *
     * @return Mensaje con informacion del documento impreso.
     */
    public String liberarImpresora() {
        if (colaImpresion.estaVacio()) {
            return "Error: La cola de impresion esta vacia.";
        }
        // Extraer el minimo del monticulo
        RegistroImpresion impreso = colaImpresion.eliminarMin();
        // Eliminar de la tabla hash
        tablaHash.eliminarRegistro(impreso.getPropietario(), impreso);
        // Actualizar estado del documento original si aun existe
        if (impreso.getDocumentoOriginal() != null) {
            impreso.getDocumentoOriginal().setEnColaImpresion(false);
        }
        // Guardar info del ultimo impreso
        ultimoImpreso = impreso.getNombreDocumento() + " (" +
            impreso.getTamanioPaginas() + " pags, " + impreso.getTipoDocumento() +
            ") | t=" + String.format("%.4f", impreso.getEtiquetaTiempo());
        return "Documento impreso: " + ultimoImpreso;
    }

    /**
     * Elimina un registro especifico de la cola de impresion.
     * Proceso: se disminuye su etiqueta de tiempo al minimo posible,
     * se percola hasta la raiz, y se extrae con eliminar_min (sin imprimir).
     *
     * @param registro El registro a eliminar de la cola.
     * @return Mensaje con el resultado.
     */
    public String eliminarDocumentoDeCola(RegistroImpresion registro) {
        if (colaImpresion.estaVacio()) {
            return "Error: La cola de impresion esta vacia.";
        }
        int indice = registro.getIndiceEnMonticulo();
        if (indice < 1) {
            return "Error: El registro no esta en la cola.";
        }
        // Calcular una etiqueta menor que el minimo actual
        double minActual = colaImpresion.verMin().getEtiquetaTiempo();
        double nuevaEtiqueta = minActual - 1.0;
        // Disminuir la clave para que suba a la raiz
        colaImpresion.disminuirClave(indice, nuevaEtiqueta);
        // Extraer de la raiz (sin imprimir)
        RegistroImpresion eliminado = colaImpresion.eliminarMin();
        // Eliminar de la tabla hash
        tablaHash.eliminarRegistro(eliminado.getPropietario(), eliminado);
        // Restaurar estado del documento original
        if (eliminado.getDocumentoOriginal() != null) {
            eliminado.getDocumentoOriginal().setEnColaImpresion(false);
        }
        return "Documento '" + eliminado.getNombreDocumento() +
               "' eliminado de la cola (no impreso).";
    }

    // ============================================================
    //                  GETTERS
    // ============================================================

    /**
     * Obtiene la lista de usuarios.
     *
     * @return La lista de usuarios registrados.
     */
    public ListaSimple<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Obtiene la cola de impresion (monticulo binario).
     *
     * @return El monticulo binario.
     */
    public MonticuloBinario getColaImpresion() {
        return colaImpresion;
    }

    /**
     * Obtiene la tabla de dispersion.
     *
     * @return La tabla hash.
     */
    public TablaHash getTablaHash() {
        return tablaHash;
    }

    /**
     * Obtiene el reloj de la simulacion.
     *
     * @return El reloj.
     */
    public Reloj getReloj() {
        return reloj;
    }

    /**
     * Obtiene la informacion del ultimo documento impreso.
     *
     * @return Cadena con la informacion del ultimo impreso.
     */
    public String getUltimoImpreso() {
        return ultimoImpreso;
    }
}