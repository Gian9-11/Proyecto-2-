package vista;

import controlador.SistemaOperativo;
import modelo.Documento;
import modelo.Usuario;
import tda.ListaSimple;
import modelo.RegistroImpresion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de gestion de usuarios y sus documentos.
 * Permite agregar, cargar desde CSV y eliminar usuarios.
 * Para cada usuario, permite crear documentos, enviarlos a imprimir
 * y eliminar los que no esten en cola.
 *
 * @author Equipo
 */
public class PanelUsuarios extends JPanel {

    /** Referencia al sistema operativo (controlador). */
    private SistemaOperativo sistema;

    /** Referencia a la ventana principal para refrescar paneles. */
    private VentanaPrincipal ventana;

    /** Modelo de la lista de usuarios. */
    private DefaultListModel<String> modeloListaUsuarios;

    /** Lista visual de usuarios. */
    private JList<String> listaUsuarios;

    /** Modelo de la tabla de documentos. */
    private DefaultTableModel modeloTablaDocumentos;

    /** Tabla de documentos del usuario seleccionado. */
    private JTable tablaDocumentos;

    /** Etiqueta que muestra el nombre del usuario seleccionado. */
    private JLabel lblUsuarioSeleccionado;

    /**
     * Construye el panel de usuarios.
     *
     * @param sistema Referencia al sistema operativo.
     * @param ventana Referencia a la ventana principal.
     */
    public PanelUsuarios(SistemaOperativo sistema, VentanaPrincipal ventana) {
        this.sistema = sistema;
        this.ventana = ventana;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        crearInterfaz();
    }

    /**
     * Crea todos los componentes de la interfaz.
     */
    private void crearInterfaz() {
        // ===== PANEL IZQUIERDO: Lista de usuarios =====
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Usuarios"));
        panelIzquierdo.setPreferredSize(new Dimension(260, 0));

        modeloListaUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloListaUsuarios);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaUsuarios.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarDocumentos();
                }
            }
        });
        panelIzquierdo.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

        // Botones de usuarios
        JPanel panelBotonesUsuario = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnCargarCSV = new JButton("Cargar CSV");
        JButton btnEliminar = new JButton("Eliminar Usuario");

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionAgregarUsuario();
            }
        });
        btnCargarCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionCargarCSV();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionEliminarUsuario();
            }
        });

        panelBotonesUsuario.add(btnAgregar);
        panelBotonesUsuario.add(btnCargarCSV);
        panelBotonesUsuario.add(btnEliminar);
        panelIzquierdo.add(panelBotonesUsuario, BorderLayout.SOUTH);

        // ===== PANEL DERECHO: Documentos del usuario =====
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Documentos"));

        lblUsuarioSeleccionado = new JLabel("Seleccione un usuario");
        lblUsuarioSeleccionado.setFont(new Font("SansSerif", Font.BOLD, 13));
        panelDerecho.add(lblUsuarioSeleccionado, BorderLayout.NORTH);

        // Tabla de documentos
        String[] columnas = {"Nombre", "Paginas", "Tipo", "Estado"};
        modeloTablaDocumentos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };
        tablaDocumentos = new JTable(modeloTablaDocumentos);
        tablaDocumentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDocumentos.setRowHeight(22);
        panelDerecho.add(new JScrollPane(tablaDocumentos), BorderLayout.CENTER);

        // Botones de documentos
        JPanel panelBotonesDoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnCrearDoc = new JButton("Crear Documento");
        JButton btnEnviarImprimir = new JButton("Enviar a Imprimir");
        JButton btnEliminarDoc = new JButton("Eliminar Documento");

        btnCrearDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionCrearDocumento();
            }
        });
        btnEnviarImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionEnviarAImprimir();
            }
        });
        btnEliminarDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionEliminarDocumento();
            }
        });

        panelBotonesDoc.add(btnCrearDoc);
        panelBotonesDoc.add(btnEnviarImprimir);
        panelBotonesDoc.add(btnEliminarDoc);
        panelDerecho.add(panelBotonesDoc, BorderLayout.SOUTH);

        // Agregar ambos paneles al layout principal
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }

    // ============================================================
    //                  ACCIONES DE USUARIO
    // ============================================================

    /**
     * Abre un dialogo para agregar un nuevo usuario.
     */
    private void accionAgregarUsuario() {
        JTextField campoNombre = new JTextField(15);
        String[] tipos = {"prioridad_alta", "prioridad_media", "prioridad_baja"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Tipo de prioridad:"));
        panel.add(comboTipo);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "El nombre no puede estar vacio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String resultado = sistema.agregarUsuario(nombre, tipo);
            JOptionPane.showMessageDialog(this, resultado);
            actualizar();
        }
    }

    /**
     * Abre un JFileChooser para cargar usuarios desde un archivo CSV.
     */
    private void accionCargarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos CSV", "csv");
        fileChooser.setFileFilter(filtro);
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String mensaje = sistema.cargarUsuariosCSV(archivo);
            JOptionPane.showMessageDialog(this, mensaje);
            actualizar();
        }
    }

    /**
     * Elimina el usuario seleccionado en la lista.
     */
    private void accionEliminarUsuario() {
        int indice = listaUsuarios.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = listaUsuarios.getSelectedValue();
        // Extraer solo el nombre (antes del [tipo])
        nombre = nombre.split(" \\[")[0];
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al usuario '" + nombre + "'?\n" +
            "Sus documentos creados seran eliminados,\npero los de la cola permaneceran.",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            String resultado = sistema.eliminarUsuario(nombre);
            JOptionPane.showMessageDialog(this, resultado);
            actualizar();
        }
    }

    // ============================================================
    //                  ACCIONES DE DOCUMENTOS
    // ============================================================

    /**
     * Obtiene el usuario actualmente seleccionado.
     *
     * @return El Usuario seleccionado o null si no hay seleccion.
     */
    private Usuario obtenerUsuarioSeleccionado() {
        int indice = listaUsuarios.getSelectedIndex();
        if (indice == -1) return null;
        String texto = listaUsuarios.getSelectedValue();
        String nombre = texto.split(" \\[")[0];
        return sistema.buscarUsuario(nombre);
    }

    /**
     * Abre un dialogo para crear un nuevo documento.
     */
    private void accionCrearDocumento() {
        Usuario usuario = obtenerUsuarioSeleccionado();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField campoNombre = new JTextField(15);
        JTextField campoPaginas = new JTextField(5);
        String[] tiposDoc = {"pdf", "txt", "docx", "xlsx", "pptx", "jpg", "png"};
        JComboBox<String> comboTipo = new JComboBox<>(tiposDoc);
        comboTipo.setEditable(true);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nombre del documento:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Paginas:"));
        panel.add(campoPaginas);
        panel.add(new JLabel("Tipo de archivo:"));
        panel.add(comboTipo);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "Crear Documento", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();
            int paginas;
            try {
                paginas = Integer.parseInt(campoPaginas.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "El numero de paginas debe ser un entero valido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String resultado = sistema.crearDocumento(
                usuario.getNombre(), nombre, paginas, tipo);
            JOptionPane.showMessageDialog(this, resultado);
            actualizarDocumentos();
        }
    }

    /**
     * Envia el documento seleccionado a la cola de impresion.
     */
    private void accionEnviarAImprimir() {
        Usuario usuario = obtenerUsuarioSeleccionado();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = tablaDocumentos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un documento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Documento doc = usuario.getDocumentos().obtener(fila);
        if (doc.isEnColaImpresion()) {
            JOptionPane.showMessageDialog(this,
                "Este documento ya esta en la cola de impresion.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Preguntar si es prioritario
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Enviar como PRIORITARIO?\n" +
            "(Se aplicara el factor de prioridad del usuario: " +
            usuario.getFactorPrioridad() + ")",
            "Prioridad", JOptionPane.YES_NO_CANCEL_OPTION);
        if (respuesta == JOptionPane.CANCEL_OPTION) return;
        boolean esPrioritario = (respuesta == JOptionPane.YES_OPTION);
        String resultado = sistema.enviarAImprimir(usuario, doc, esPrioritario);
        JOptionPane.showMessageDialog(this, resultado);
        ventana.refrescarTodo();
    }

    /**
     * Elimina el documento seleccionado (solo si no esta en cola).
     */
    private void accionEliminarDocumento() {
        Usuario usuario = obtenerUsuarioSeleccionado();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = tablaDocumentos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un documento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Documento doc = usuario.getDocumentos().obtener(fila);
        String resultado = sistema.eliminarDocumento(usuario, doc);
        JOptionPane.showMessageDialog(this, resultado);
        actualizarDocumentos();
    }

    // ============================================================
    //              ACTUALIZACION DE INTERFAZ
    // ============================================================

    /**
     * Refresca la lista de usuarios y la tabla de documentos.
     */
    public void actualizar() {
        modeloListaUsuarios.clear();
        ListaSimple<Usuario> users = sistema.getUsuarios();
        for (int i = 0; i < users.tamanio(); i++) {
            Usuario u = users.obtener(i);
            modeloListaUsuarios.addElement(u.getNombre() + " [" + u.getTipo() + "]");
        }
        actualizarDocumentos();
    }

    /**
     * Refresca la tabla de documentos del usuario seleccionado.
     */
    private void actualizarDocumentos() {
        // Limpiar tabla
        modeloTablaDocumentos.setRowCount(0);
        Usuario usuario = obtenerUsuarioSeleccionado();
        if (usuario == null) {
            lblUsuarioSeleccionado.setText("Seleccione un usuario");
            return;
        }
        lblUsuarioSeleccionado.setText("Documentos de: " + usuario.getNombre() +
            " (" + usuario.getTipo() + ")");
        ListaSimple<Documento> docs = usuario.getDocumentos();
        for (int i = 0; i < docs.tamanio(); i++) {
            Documento d = docs.obtener(i);
            String estado = d.isEnColaImpresion() ? "EN COLA" : "Disponible";
            modeloTablaDocumentos.addRow(new Object[]{
                d.getNombre(), d.getTamanioPaginas(), d.getTipo(), estado
            });
        }
    }
}