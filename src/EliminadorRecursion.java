import java.util.*;

/**
 * Implementa el algoritmo de eliminación de recursión por la izquierda
 * Basado en el algoritmo de Aho et al. (2006), Sección 4.3.3
 */
public class EliminadorRecursion {

    // Contador para generar nuevos no terminales (Z, Y, X, W, V, ...)
    private int contadorNoTerminales;

    public EliminadorRecursion() {
        this.contadorNoTerminales = 0;
    }

    /**
     * Algoritmo principal para eliminar recursión por la izquierda
     *
     * Algoritmo (Aho et al., 2006):
     * 1. Ordenar los no terminales A1, A2, ..., An
     * 2. Para cada i desde 1 hasta n:
     *    a. Para cada j desde 1 hasta i-1:
     *       - Reemplazar cada producción Ai -> Aj γ por Ai -> δ1 γ | δ2 γ | ... | δk γ
     *         donde Aj -> δ1 | δ2 | ... | δk son todas las producciones de Aj
     *    b. Eliminar recursión inmediata de Ai
     */
    public Gramatica eliminarRecursionIzquierda(Gramatica gramaticaOriginal) {
        Gramatica gramatica = gramaticaOriginal.clonar();
        List<Character> noTerminales = gramatica.obtenerNoTerminales();

        // Paso 1: Procesar cada no terminal en orden
        for (int i = 0; i < noTerminales.size(); i++) {
            char Ai = noTerminales.get(i);

            // Paso 2: Eliminar recursión indirecta
            for (int j = 0; j < i; j++) {
                char Aj = noTerminales.get(j);
                sustituirProducciones(gramatica, Ai, Aj);
            }

            // Paso 3: Eliminar recursión inmediata
            eliminarRecursionInmediata(gramatica, Ai);
        }

        return gramatica;
    }

    /**
     * Sustituye las producciones de la forma Ai -> Aj γ
     * por Ai -> δ1 γ | δ2 γ | ... | δk γ
     * donde Aj -> δ1 | δ2 | ... | δk
     */
    private void sustituirProducciones(Gramatica gramatica, char Ai, char Aj) {
        List<String> produccionesAi = gramatica.obtenerProducciones(Ai);
        List<String> produccionesAj = gramatica.obtenerProducciones(Aj);
        List<String> nuevasProduccionesAi = new ArrayList<>();

        for (String produccion : produccionesAi) {
            // Si la producción comienza con Aj
            if (!produccion.isEmpty() && produccion.charAt(0) == Aj) {
                // Obtener el resto de la producción (γ)
                String gamma = produccion.substring(1);

                // Crear nuevas producciones: δ1 γ, δ2 γ, ..., δk γ
                for (String delta : produccionesAj) {
                    nuevasProduccionesAi.add(delta + gamma);
                }
            } else {
                // Mantener la producción original
                nuevasProduccionesAi.add(produccion);
            }
        }

        gramatica.reemplazarProducciones(Ai, nuevasProduccionesAi);
    }

    /**
     * Elimina la recursión inmediata de un no terminal
     *
     * Si A -> Aα1 | Aα2 | ... | Aαm | β1 | β2 | ... | βn
     * donde ningún βi comienza con A, entonces se reemplaza por:
     *
     * A -> β1A' | β2A' | ... | βnA'
     * A' -> α1A' | α2A' | ... | αmA' | ε
     */
    private void eliminarRecursionInmediata(Gramatica gramatica, char A) {
        List<String> producciones = gramatica.obtenerProducciones(A);
        List<String> alfa = new ArrayList<>();  // Producciones recursivas (sin el A inicial)
        List<String> beta = new ArrayList<>();  // Producciones no recursivas

        // Clasificar las producciones
        for (String produccion : producciones) {
            if (!produccion.isEmpty() && produccion.charAt(0) == A) {
                // Producción recursiva: A -> Aα
                alfa.add(produccion.substring(1));
            } else {
                // Producción no recursiva: A -> β
                beta.add(produccion);
            }
        }

        // Si no hay recursión inmediata, no hacer nada
        if (alfa.isEmpty()) {
            return;
        }

        // Generar un nuevo no terminal
        char nuevoNoTerminal = generarNuevoNoTerminal(gramatica);

        // Crear nuevas producciones para A: β1A' | β2A' | ... | βnA'
        List<String> nuevasProduccionesA = new ArrayList<>();
        for (String b : beta) {
            nuevasProduccionesA.add(b + nuevoNoTerminal);
        }

        // Crear producciones para A': α1A' | α2A' | ... | αmA' | ε
        List<String> produccionesNuevoNoTerminal = new ArrayList<>();
        for (String a : alfa) {
            produccionesNuevoNoTerminal.add(a + nuevoNoTerminal);
        }
        produccionesNuevoNoTerminal.add("e");  // ε representado como 'e'

        // Actualizar la gramática
        gramatica.reemplazarProducciones(A, nuevasProduccionesA);
        gramatica.agregarProduccion(nuevoNoTerminal, produccionesNuevoNoTerminal);
    }

    /**
     * Genera un nuevo no terminal que no exista en la gramática
     * Usa el orden: Z, Y, X, W, V, U, T, R, Q, P, O, N, M, L, K, J, I, H, G, F, E, D, C, B
     */
    private char generarNuevoNoTerminal(Gramatica gramatica) {
        // Orden de preferencia para nuevos no terminales
        char[] orden = {'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'R', 'Q', 'P',
                'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F',
                'E', 'D', 'C', 'B'};

        for (char c : orden) {
            if (!gramatica.contieneNoTerminal(c)) {
                return c;
            }
        }

        // Si todos están ocupados (caso muy raro), usar números
        throw new RuntimeException("Se agotaron los no terminales disponibles");
    }
}

