package vista;

import controlador.SistemaOperativo;
import modelo.RegistroImpresion;
import tda.ListaSimple;
import tda.MonticuloBinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Panel que muestra la cola de impresion en vista secuencial (tabla)
 * y provee controles para liberar la impresora y eliminar documentos.
 *
 * @author Equipo
 */
public class PanelColaImpresion extends JPanel {

    /** Referencia al sistema operativo. */
    private SistemaOperativo sistema;

    /** Referencia a la ventana principal. */
    private VentanaPrincipal ventana;

    /** Modelo de la tabla de la cola. */
    private DefaultTableModel modeloTablaCola;

    /** Tabla que muestra los registros en la cola. */
    private JTable tablaCola;

    /** Etiqueta que muestra info del ultimo documento impreso. */
    private JLabel lblUltimoImpreso;

    /** Etiqueta que muestra el tamanio de la cola. */
    private JLabel lblTamanioCola;

    /**
     * Construye el panel de la cola de impresion.
     *
     * @param sistema Referencia al sistema operativo.
     * @param ventana Referencia a la ventana principal.
     */
    public PanelColaImpresion(SistemaOperativo sistema, VentanaPrincipal ventana) {
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
        // ===== ENCABEZADO =====
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Cola de Impresion - Vista Secuencial");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTamanioCola = new JLabel("Elementos en cola: 0");
        panelEncabezado.add(lblTitulo, BorderLayout.WEST);
        panelEncabezado.add(lblTamanioCola, BorderLayout.EAST);
        add(panelEncabezado, BorderLayout.NORTH);

        // ===== TABLA DE LA COLA =====
        String[] columnas = {"Pos.", "ID", "Documento", "Paginas", "Tipo", "Etiqueta Tiempo"};
        modeloTablaCola = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCola = new JTable(modeloTablaCola);
        tablaCola.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCola.setRowHeight(22);
        tablaCola.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaCola.getColumnModel().getColumn(1).setPreferredWidth(40);
        add(new JScrollPane(tablaCola), BorderLayout.CENTER);

        // ===== PANEL INFERIOR: Botones y ultimo impreso =====
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnLiberar = new JButton("Liberar Impresora (Imprimir)");
        JButton btnEliminarDeCola = new JButton("Eliminar de Cola");

        btnLiberar.setBackground(new Color(76, 175, 80));
        btnLiberar.setForeground(Color.WHITE);

        btnLiberar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionLiberarImpresora();
            }
        });
        btnEliminarDeCola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionEliminarDeCola();
            }
        });

        panelBotones.add(btnLiberar);
        panelBotones.add(btnEliminarDeCola);

        lblUltimoImpreso = new JLabel("Ultimo impreso: Ninguno");
        lblUltimoImpreso.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblUltimoImpreso.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(lblUltimoImpreso, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Ejecuta la operacion de liberar impresora (eliminar_min).
     */
    private void accionLiberarImpresora() {
        String resultado = sistema.liberarImpresora();
        JOptionPane.showMessageDialog(this, resultado);
        ventana.refrescarTodo();
    }

    /**
     * Ejecuta la operacion de eliminar un documento especifico de la cola.
     * Solicita el nombre del usuario y muestra sus documentos en cola.
     */
    private void accionEliminarDeCola() {
        if (sistema.getColaImpresion().estaVacio()) {
            JOptionPane.showMessageDialog(this,
                "La cola de impresion esta vacia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Solicitar nombre de usuario
        String nombreUsuario = JOptionPane.showInputDialog(this,
            "Ingrese el nombre del usuario cuyos documentos desea eliminar de la cola:");
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) return;
        nombreUsuario = nombreUsuario.trim();

        // Buscar en la tabla hash
        ListaSimple<RegistroImpresion> registros = sistema.getTablaHash().buscar(nombreUsuario);
        if (registros == null || registros.estaVacia()) {
            JOptionPane.showMessageDialog(this,
                "El usuario '" + nombreUsuario + "' no tiene documentos en la cola.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Construir opciones para que el usuario elija cual eliminar
        String[] opciones = new String[registros.tamanio()];
        for (int i = 0; i < registros.tamanio(); i++) {
            RegistroImpresion r = registros.obtener(i);
            opciones[i] = r.getNombreDocumento() + " (t=" +
                String.format("%.4f", r.getEtiquetaTiempo()) + ")";
        }
        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el documento a eliminar de la cola:",
            "Eliminar de Cola",
            JOptionPane.QUESTION_MESSAGE, null, opciones,
            opciones[0]);
        if (seleccion == null) return;

        // Encontrar cual fue seleccionado
        int indiceSeleccion = -1;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                indiceSeleccion = i;
                break;
            }
        }
        if (indiceSeleccion == -1) return;

        RegistroImpresion registroAEliminar = registros.obtener(indiceSeleccion);
        String resultado = sistema.eliminarDocumentoDeCola(registroAEliminar);
        JOptionPane.showMessageDialog(this, resultado);
        ventana.refrescarTodo();
    }

    /**
     * Refresca la tabla de la cola y las etiquetas.
     */
    public void actualizar() {
        modeloTablaCola.setRowCount(0);
        MonticuloBinario cola = sistema.getColaImpresion();
        for (int i = 1; i <= cola.getTamanioActual(); i++) {
            RegistroImpresion r = cola.obtenerEnIndice(i);
            if (r != null) {
                modeloTablaCola.addRow(new Object[]{
                    i, r.getId(), r.getNombreDocumento(),
                    r.getTamanioPaginas(), r.getTipoDocumento(),
                    String.format("%.4f", r.getEtiquetaTiempo())
                });
            }
        }
        lblTamanioCola.setText("Elementos en cola: " + cola.getTamanioActual());
        lblUltimoImpreso.setText("Ultimo impreso: " + sistema.getUltimoImpreso());
    }
}