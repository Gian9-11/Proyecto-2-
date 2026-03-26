package vista;

import controlador.SistemaOperativo;
import modelo.RegistroImpresion;
import tda.MonticuloBinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel que dibuja el monticulo binario como un arbol grafico
 * usando Graphics2D. Cada nodo muestra la etiqueta de tiempo
 * y el nombre del documento.
 *
 * @author Equipo
 */
public class PanelArbolMonticulo extends JPanel {

    /** Referencia al sistema operativo. */
    private SistemaOperativo sistema;

    /** Panel interno donde se dibuja el arbol. */
    private DibujoArbol dibujo;

    /** Etiqueta informativa. */
    private JLabel lblInfo;

    /**
     * Construye el panel de visualizacion del arbol.
     *
     * @param sistema Referencia al sistema operativo.
     */
    public PanelArbolMonticulo(SistemaOperativo sistema) {
        this.sistema = sistema;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblInfo = new JLabel("Vista de Arbol del Monticulo Binario");
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(lblInfo, BorderLayout.NORTH);

        dibujo = new DibujoArbol();
        JScrollPane scroll = new JScrollPane(dibujo);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Refresca el dibujo del arbol.
     */
    public void actualizar() {
        int n = sistema.getColaImpresion().getTamanioActual();
        lblInfo.setText("Vista de Arbol del Monticulo Binario (" + n + " elementos)");
        dibujo.repaint();
        // Recalcular tamanio preferido del panel de dibujo
        dibujo.calcularTamanio();
        dibujo.revalidate();
    }

    /**
     * Panel interno que realiza el dibujo del arbol con Graphics2D.
     */
    private class DibujoArbol extends JPanel {

        /** Radio de cada nodo. */
        private static final int RADIO_NODO = 35;

        /** Espacio vertical entre niveles. */
        private static final int ESPACIO_VERTICAL = 80;

        /** Margen superior. */
        private static final int MARGEN_SUPERIOR = 50;

        /** Margen lateral minimo. */
        private static final int MARGEN_LATERAL = 30;

        /**
         * Construye el panel de dibujo.
         */
        public DibujoArbol() {
            setBackground(Color.WHITE);
        }

        /**
         * Calcula el tamanio preferido del panel segun la profundidad del arbol.
         */
        public void calcularTamanio() {
            int n = sistema.getColaImpresion().getTamanioActual();
            if (n == 0) {
                setPreferredSize(new Dimension(600, 400));
                return;
            }
            int nivelMax = calcularNivel(n);
            int ancho = (1 << nivelMax) * (RADIO_NODO * 2 + 10) + MARGEN_LATERAL * 2;
            int alto = (nivelMax + 1) * ESPACIO_VERTICAL + MARGEN_SUPERIOR + 50;
            if (ancho < 600) ancho = 600;
            if (alto < 400) alto = 400;
            setPreferredSize(new Dimension(ancho, alto));
        }

        /**
         * Dibuja el arbol del monticulo.
         *
         * @param g Contexto grafico.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            MonticuloBinario cola = sistema.getColaImpresion();
            int n = cola.getTamanioActual();

            if (n == 0) {
                g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("La cola de impresion esta vacia", 200, 200);
                return;
            }

            int nivelMax = calcularNivel(n);
            int anchoPanelReal = getWidth();
            if (anchoPanelReal < 600) anchoPanelReal = 600;

            // Dibujar aristas primero (para que queden debajo de los nodos)
            for (int i = 2; i <= n; i++) {
                int padre = i / 2;
                int[] posPadre = calcularPosicion(padre, nivelMax, anchoPanelReal);
                int[] posHijo = calcularPosicion(i, nivelMax, anchoPanelReal);
                g2.setColor(new Color(150, 150, 150));
                g2.drawLine(posPadre[0], posPadre[1], posHijo[0], posHijo[1]);
            }

            // Dibujar nodos
            for (int i = 1; i <= n; i++) {
                RegistroImpresion reg = cola.obtenerEnIndice(i);
                int[] pos = calcularPosicion(i, nivelMax, anchoPanelReal);
                dibujarNodo(g2, pos[0], pos[1], reg, i);
            }
        }

        /**
         * Calcula la posicion (x, y) de un nodo segun su indice.
         *
         * @param indice         Indice del nodo (1-based).
         * @param nivelMax       Nivel maximo del arbol.
         * @param anchoPanelReal Ancho real del panel.
         * @return Arreglo con {x, y}.
         */
        private int[] calcularPosicion(int indice, int nivelMax, int anchoPanelReal) {
            int nivel = calcularNivel(indice);
            int posEnNivel = indice - (1 << nivel); // posicion dentro del nivel (0-based)
            int totalEnNivel = 1 << nivel; // total de nodos posibles en este nivel
            // Distribuir uniformemente en el ancho
            int x = (int) (anchoPanelReal * (2.0 * posEnNivel + 1.0) / (2.0 * totalEnNivel));
            int y = MARGEN_SUPERIOR + nivel * ESPACIO_VERTICAL;
            return new int[]{x, y};
        }

        /**
         * Dibuja un nodo individual (circulo con texto).
         *
         * @param g2      Contexto grafico.
         * @param x       Posicion X del centro.
         * @param y       Posicion Y del centro.
         * @param reg     Registro de impresion del nodo.
         * @param indice  Indice en el arreglo.
         */
        private void dibujarNodo(Graphics2D g2, int x, int y,
                                  RegistroImpresion reg, int indice) {
            // Color del nodo: la raiz en verde, el resto en azul claro
            if (indice == 1) {
                g2.setColor(new Color(76, 175, 80));
            } else {
                g2.setColor(new Color(100, 149, 237));
            }
            g2.fillOval(x - RADIO_NODO, y - RADIO_NODO,
                RADIO_NODO * 2, RADIO_NODO * 2);
            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(x - RADIO_NODO, y - RADIO_NODO,
                RADIO_NODO * 2, RADIO_NODO * 2);

            // Texto: etiqueta de tiempo
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            String textoTiempo = String.format("%.2f", reg.getEtiquetaTiempo());
            FontMetrics fm = g2.getFontMetrics();
            int anchoTexto = fm.stringWidth(textoTiempo);
            g2.drawString(textoTiempo, x - anchoTexto / 2, y - 2);

            // Texto: nombre del documento (abreviado si es largo)
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            fm = g2.getFontMetrics();
            String nombreDoc = reg.getNombreDocumento();
            if (nombreDoc.length() > 8) {
                nombreDoc = nombreDoc.substring(0, 7) + "..";
            }
            anchoTexto = fm.stringWidth(nombreDoc);
            g2.drawString(nombreDoc, x - anchoTexto / 2, y + 12);

            // Indice pequeño arriba del nodo
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            String textoIndice = "[" + indice + "]";
            anchoTexto = fm.stringWidth(textoIndice);
            g2.drawString(textoIndice, x - anchoTexto / 2, y - RADIO_NODO - 3);
        }

        /**
         * Calcula el nivel (profundidad) de un nodo dado su indice.
         * Nivel 0 = raiz.
         *
         * @param indice Indice del nodo (1-based).
         * @return El nivel del nodo.
         */
        private int calcularNivel(int indice) {
            int nivel = 0;
            int temp = indice;
            while (temp > 1) {
                temp >>= 1;
                nivel++;
            }
            return nivel;
        }
    }
}