import java.util.*;

/**
 * Representa una gramática libre de contexto
 * Almacena los no terminales y sus producciones
 */
public class Gramatica {
    // Mapa que asocia cada no terminal con sus producciones
    private LinkedHashMap<Character, List<String>> producciones;

    // Lista ordenada de no terminales (importante para el algoritmo)
    private List<Character> noTerminales;

    private static final String VERDE = "\u001B[32m";
    private static final String AZUL = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    public Gramatica() {
        this.producciones = new LinkedHashMap<>();
        this.noTerminales = new ArrayList<>();
    }

    /**
     * Agrega una producción a la gramática
     * Formato esperado: "A -> Aa b c"
     */
    public void agregarProduccion(String linea) {
        String[] partes = linea.split("->");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato de producción inválido: " + linea);
        }

        char noTerminal = partes[0].trim().charAt(0);
        String[] alternativas = partes[1].trim().split("\\s+");

        if (!producciones.containsKey(noTerminal)) {
            noTerminales.add(noTerminal);
            producciones.put(noTerminal, new ArrayList<>());
        }

        for (String alternativa : alternativas) {
            if (!alternativa.isEmpty()) {
                producciones.get(noTerminal).add(alternativa);
            }
        }
    }

    /**
     * Nuevo método que normaliza la entrada para manejar ambos formatos
     * Acepta tanto "A -> Aa b" como "A -> Aa | b"
     * Convierte todo al formato estándar separado por espacios
     */
    public void agregarProduccionNormalizada(String linea) {
        String[] partes = linea.split("->");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato de producción inválido: " + linea);
        }

        char noTerminal = partes[0].trim().charAt(0);
        String ladoDerecho = partes[1].trim();

        // Normalizar: reemplazar "|" por espacios para unificar el formato
        // Esto convierte "Aa | b" en "Aa b"
        ladoDerecho = ladoDerecho.replace("|", " ");

        // Dividir por espacios y filtrar elementos vacíos
        String[] alternativas = ladoDerecho.split("\\s+");

        if (!producciones.containsKey(noTerminal)) {
            noTerminales.add(noTerminal);
            producciones.put(noTerminal, new ArrayList<>());
        }

        for (String alternativa : alternativas) {
            if (!alternativa.isEmpty()) {
                producciones.get(noTerminal).add(alternativa);
            }
        }
    }

    /**
     * Agrega una producción directamente con el no terminal y sus alternativas
     */
    public void agregarProduccion(char noTerminal, List<String> alternativas) {
        if (!producciones.containsKey(noTerminal)) {
            noTerminales.add(noTerminal);
            producciones.put(noTerminal, new ArrayList<>());
        }
        producciones.get(noTerminal).addAll(alternativas);
    }

    /**
     * Obtiene las producciones de un no terminal
     */
    public List<String> obtenerProducciones(char noTerminal) {
        return producciones.getOrDefault(noTerminal, new ArrayList<>());
    }

    /**
     * Obtiene todos los no terminales en orden
     */
    public List<Character> obtenerNoTerminales() {
        return new ArrayList<>(noTerminales);
    }

    /**
     * Reemplaza las producciones de un no terminal
     */
    public void reemplazarProducciones(char noTerminal, List<String> nuevasProducciones) {
        producciones.put(noTerminal, nuevasProducciones);
    }

    /**
     * Verifica si existe un no terminal en la gramática
     */
    public boolean contieneNoTerminal(char noTerminal) {
        return producciones.containsKey(noTerminal);
    }

    /**
     * Muestra la gramática de forma visual (para modo interactivo)
     */
    public void mostrar() {
        for (char noTerminal : noTerminales) {
            System.out.print(AZUL + "  " + noTerminal + " -> " + RESET);
            List<String> prods = producciones.get(noTerminal);
            System.out.println(VERDE + String.join(" ", prods) + RESET);
        }
    }

    /**
     * Imprime la gramática en formato estándar (para salida del programa)
     */
    public void imprimir() {
        for (char noTerminal : noTerminales) {
            System.out.print(noTerminal + " -> ");
            List<String> prods = producciones.get(noTerminal);
            System.out.println(String.join(" ", prods));
        }
    }

    /**
     * Crea una copia de la gramática
     */
    public Gramatica clonar() {
        Gramatica clon = new Gramatica();
        for (char noTerminal : noTerminales) {
            clon.agregarProduccion(noTerminal, new ArrayList<>(producciones.get(noTerminal)));
        }
        return clon;
    }
}
