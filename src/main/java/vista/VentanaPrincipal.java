package vista;

import controlador.SistemaOperativo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Ventana principal de la aplicacion. Contiene un JTabbedPane con tres
 * pestanias: Usuarios, Cola de Impresion (secuencia) y Vista de Arbol.
 * Incluye una barra de estado inferior con el reloj de la simulacion.
 *
 * @author Equipo
 */
public class VentanaPrincipal extends JFrame {

    /** Controlador principal del sistema. */
    private SistemaOperativo sistema;

    /** Panel de gestion de usuarios y documentos. */
    private PanelUsuarios panelUsuarios;

    /** Panel de la cola de impresion (vista secuencial). */
    private PanelColaImpresion panelCola;

    /** Panel de visualizacion del arbol del monticulo. */
    private PanelArbolMonticulo panelArbol;

    /** Etiqueta del reloj en la barra de estado. */
    private JLabel lblReloj;

    /** Timer de Swing para actualizar el reloj cada segundo. */
    private Timer timerReloj;

    /**
     * Construye la ventana principal e inicializa todos los componentes.
     *
     * @param sistema Referencia al sistema operativo.
     */
    public VentanaPrincipal(SistemaOperativo sistema) {
        super("Simulador de Cola de Impresion - Estructuras de Datos");
        this.sistema = sistema;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        crearInterfaz();
        iniciarReloj();
    }

    /**
     * Crea la interfaz principal con pestanias y barra de estado.
     */
    private void crearInterfaz() {
        setLayout(new BorderLayout());

        // ===== BARRA SUPERIOR =====
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        barraSuperior.setBackground(new Color(60, 63, 65));

        JLabel lblTitulo = new JLabel("Simulador de Cola de Impresion");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        barraSuperior.add(lblTitulo, BorderLayout.WEST);

        lblReloj = new JLabel("Reloj: 0.0 s");
        lblReloj.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblReloj.setForeground(new Color(144, 238, 144));
        lblReloj.setHorizontalAlignment(SwingConstants.RIGHT);
        barraSuperior.add(lblReloj, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);

        // ===== PESTANIAS =====
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panelUsuarios = new PanelUsuarios(sistema, this);
        panelCola = new PanelColaImpresion(sistema, this);
        panelArbol = new PanelArbolMonticulo(sistema);

        pestanias.addTab("Usuarios y Documentos", panelUsuarios);
        pestanias.addTab("Cola de Impresion", panelCola);
        pestanias.addTab("Vista de Arbol", panelArbol);

        // Refrescar al cambiar de pestania
        pestanias.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                refrescarPanelActivo(pestanias.getSelectedIndex());
            }
        });

        add(pestanias, BorderLayout.CENTER);

        // ===== BARRA INFERIOR =====
        JPanel barraInferior = new JPanel(new BorderLayout());
        barraInferior.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
        barraInferior.setBackground(new Color(240, 240, 240));
        JLabel lblCreditos = new JLabel("Proyecto 2 - Colas de Prioridad y HashTables");
        lblCreditos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCreditos.setForeground(Color.GRAY);
        barraInferior.add(lblCreditos, BorderLayout.WEST);
        add(barraInferior, BorderLayout.SOUTH);
    }

    /**
     * Inicia el timer que actualiza el reloj cada segundo.
     */
    private void iniciarReloj() {
        timerReloj = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblReloj.setText("Reloj: " + sistema.getReloj().tiempoFormateado());
            }
        });
        timerReloj.start();
    }

    /**
     * Refresca todos los paneles. Se llama despues de cada operacion
     * que modifique el estado del sistema.
     */
    public void refrescarTodo() {
        panelUsuarios.actualizar();
        panelCola.actualizar();
        panelArbol.actualizar();
    }

    /**
     * Refresca solo el panel de la pestania seleccionada.
     *
     * @param indicePestania Indice de la pestania seleccionada.
     */
    private void refrescarPanelActivo(int indicePestania) {
        switch (indicePestania) {
            case 0:
                panelUsuarios.actualizar();
                break;
            case 1:
                panelCola.actualizar();
                break;
            case 2:
                panelArbol.actualizar();
                break;
        }
    }
}