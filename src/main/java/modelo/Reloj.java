package modelo;

/**
 * Reloj de la simulacion. Mide el tiempo transcurrido en segundos
 * desde el momento en que se inicializa la simulacion.
 * Utiliza System.currentTimeMillis() internamente.
 *
 * @author Equipo
 */
public class Reloj {

    /** Marca de tiempo en milisegundos del inicio de la simulacion. */
    private long tiempoInicio;

    /**
     * Construye el reloj e inicia el conteo inmediatamente.
     */
    public Reloj() {
        this.tiempoInicio = System.currentTimeMillis();
    }

    /**
     * Reinicia el reloj, comenzando el conteo desde cero.
     */
    public void reiniciar() {
        this.tiempoInicio = System.currentTimeMillis();
    }

    /**
     * Obtiene el tiempo transcurrido desde el inicio de la simulacion,
     * expresado en segundos con precision de milisegundos.
     *
     * @return Tiempo transcurrido en segundos (double).
     */
    public double tiempoTranscurrido() {
        long ahora = System.currentTimeMillis();
        return (ahora - tiempoInicio) / 1000.0;
    }

    /**
     * Obtiene el tiempo transcurrido formateado como cadena legible.
     *
     * @return Cadena con formato "Xm Ys" o "X.XXs".
     */
    public String tiempoFormateado() {
        double segundos = tiempoTranscurrido();
        if (segundos < 60) {
            return String.format("%.1f s", segundos);
        } else {
            int minutos = (int) (segundos / 60);
            double segs = segundos % 60;
            return String.format("%d m %.1f s", minutos, segs);
        }
    }
}