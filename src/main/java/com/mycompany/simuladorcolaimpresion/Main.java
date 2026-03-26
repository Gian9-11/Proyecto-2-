package com.mycompany.simuladorcolaimpresion;


import controlador.SistemaOperativo;
import vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal de la aplicacion.
 * Inicializa el sistema operativo (modelo/controlador) y
 * lanza la interfaz grafica en el hilo de despacho de eventos de Swing.
 *
 * @author Equipo
 */
public class Main {

    /**
     * Punto de entrada de la aplicacion.
     *
     * @param args Argumentos de linea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Intentar usar el look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el look and feel por defecto de Java
        }

        // Lanzar la GUI en el hilo de despacho de eventos (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaOperativo sistema = new SistemaOperativo();
                VentanaPrincipal ventana = new VentanaPrincipal(sistema);
                ventana.setVisible(true);
            }
        });
    }
}