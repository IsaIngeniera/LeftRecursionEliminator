import java.util.Scanner;

/**
 * Clase principal del programa
 * Maneja la interfaz de usuario y la ejecución del algoritmo
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String VERDE = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String ROJO = "\u001B[31m";
    private static final String AZUL = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String NEGRITA = "\u001B[1m";

    public static void main(String[] args) {
        mostrarBienvenida();

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    ejecutarModoInteractivo();
                    break;
                case 2:
                    mostrarAyuda();
                    break;
                case 3:
                    mostrarDespedida();
                    continuar = false;
                    break;
                default:
                    System.out.println(ROJO + "❌ Opción inválida. Intente nuevamente." + RESET);
            }
        }

        scanner.close();
    }

    private static void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println(CYAN + NEGRITA + "╔════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║                                                                ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║     ELIMINADOR DE RECURSIÓN POR LA IZQUIERDA                   ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║     Gramáticas Libres de Contexto                              ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║                                                                ║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void mostrarMenu() {
        System.out.println(AMARILLO + "\n┌─────────────────────────────────────┐" + RESET);
        System.out.println(AMARILLO + "│         MENÚ PRINCIPAL              │" + RESET);
        System.out.println(AMARILLO + "└─────────────────────────────────────┘" + RESET);
        System.out.println(VERDE + "  1. " + RESET + "Ingresar gramática");
        System.out.println(VERDE + "  2. " + RESET + "Ayuda y ejemplos");
        System.out.println(VERDE + "  3. " + RESET + "Salir");
        System.out.print(CYAN + "\n➤ Seleccione una opción: " + RESET);
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void ejecutarModoInteractivo() {
        System.out.println(MAGENTA + "\n╔════════════════════════════════════════╗" + RESET);
        System.out.println(MAGENTA + "║      INGRESAR GRAMÁTICA                ║" + RESET);
        System.out.println(MAGENTA + "╚════════════════════════════════════════╝" + RESET);

        try {
            System.out.print(CYAN + "\n¿Cuántas gramáticas desea procesar? " + RESET);
            int numeroCasos = Integer.parseInt(scanner.nextLine().trim());

            if (numeroCasos <= 0) {
                System.out.println(ROJO + "❌ El número de casos debe ser mayor a 0." + RESET);
                return;
            }

            for (int i = 1; i <= numeroCasos; i++) {
                System.out.println(AMARILLO + "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
                System.out.println(AMARILLO + "  GRAMÁTICA " + i + " de " + numeroCasos + RESET);
                System.out.println(AMARILLO + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);

                System.out.print(CYAN + "\n¿Cuántos no terminales tiene la gramática? " + RESET);
                int numeroNoTerminales = Integer.parseInt(scanner.nextLine().trim());

                if (numeroNoTerminales <= 0) {
                    System.out.println(ROJO + "❌ El número de no terminales debe ser mayor a 0." + RESET);
                    return;
                }

                Gramatica gramatica = new Gramatica();

                System.out.println(VERDE + "\nIngrese las producciones (formato: A -> Aa b | c):" + RESET);
                for (int j = 0; j < numeroNoTerminales; j++) {
                    System.out.print(AZUL + "  Producción " + (j + 1) + ": " + RESET);
                    String linea = scanner.nextLine().trim();
                    gramatica.agregarProduccionNormalizada(linea);
                }

                System.out.println(VERDE + "\n✓ Gramática original:" + RESET);
                gramatica.mostrar();

                System.out.println(AMARILLO + "\n⚙ Aplicando algoritmo de eliminación de recursión..." + RESET);
                EliminadorRecursion eliminador = new EliminadorRecursion();
                Gramatica gramaticaSinRecursion = eliminador.eliminarRecursionIzquierda(gramatica);

                System.out.println(VERDE + "\n✓ Gramática sin recursión por la izquierda:" + RESET);
                gramaticaSinRecursion.mostrar();

                if (i < numeroCasos) {
                    System.out.println();
                }
            }

            System.out.println(VERDE + "\n✓ Procesamiento completado exitosamente." + RESET);
            esperarEnter();

        } catch (NumberFormatException e) {
            System.out.println(ROJO + "❌ Error: Debe ingresar un número válido." + RESET);
        } catch (Exception e) {
            System.out.println(ROJO + "❌ Error al procesar la gramática: " + e.getMessage() + RESET);
        }
    }

    private static void mostrarAyuda() {
        limpiarPantalla();
        System.out.println(CYAN + NEGRITA + "╔════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║                    AYUDA Y EJEMPLOS                            ║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚════════════════════════════════════════════════════════════════╝" + RESET);

        System.out.println(AMARILLO + "\n📖 FORMATO DE ENTRADA:" + RESET);
        System.out.println(VERDE + "  • Los no terminales se representan con letras MAYÚSCULAS" + RESET);
        System.out.println(VERDE + "  • Los terminales se representan con letras minúsculas" + RESET);
        System.out.println(VERDE + "  • El símbolo 'e' representa la cadena vacía (ε)" + RESET);
        System.out.println(VERDE + "  • Formato: NoTerminal -> producción1 producción2 ..." + RESET);
        System.out.println(VERDE + "  • También acepta: NoTerminal -> prod1 | prod2 | ..." + RESET);

        System.out.println(AMARILLO + "\n📝 EJEMPLO 1:" + RESET);
        System.out.println(AZUL + "  Entrada:" + RESET);
        System.out.println("    S -> Sa b");
        System.out.println(AZUL + "  Salida:" + RESET);
        System.out.println("    S -> bZ");
        System.out.println("    Z -> aZ e");

        System.out.println(AMARILLO + "\n📝 EJEMPLO 2:" + RESET);
        System.out.println(AZUL + "  Entrada:" + RESET);
        System.out.println("    S -> Aa b");
        System.out.println("    A -> Ac Sd m");
        System.out.println(AZUL + "  Salida:" + RESET);
        System.out.println("    S -> Aa b");
        System.out.println("    A -> bdZ mZ");
        System.out.println("    Z -> cZ adZ e");

        System.out.println(AMARILLO + "\n📝 EJEMPLO 3:" + RESET);
        System.out.println(AZUL + "  Entrada:" + RESET);
        System.out.println("    S -> Sa Ab");
        System.out.println("    A -> Ac Sc c");
        System.out.println(AZUL + "  Salida:" + RESET);
        System.out.println("    S -> AbZ");
        System.out.println("    A -> cY");
        System.out.println("    Z -> aZ e");
        System.out.println("    Y -> cY bZcY e");

        System.out.println(AMARILLO + "\n💡 NOTAS:" + RESET);
        System.out.println(VERDE + "  • El algoritmo elimina tanto recursión inmediata como indirecta" + RESET);
        System.out.println(VERDE + "  • Se crean nuevos no terminales (Z, Y, X, etc.) según sea necesario" + RESET);
        System.out.println(VERDE + "  • La gramática de salida es equivalente a la de entrada" + RESET);

        esperarEnter();
    }

    private static void mostrarDespedida() {
        limpiarPantalla();
        System.out.println(CYAN + NEGRITA + "\n╔════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║                                                                ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║              ¡Gracias por usar el programa!                    ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║                                                                ║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void esperarEnter() {
        System.out.print(AMARILLO + "\nPresione ENTER para continuar..." + RESET);
        scanner.nextLine();
        limpiarPantalla();
    }

    private static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, simplemente imprime líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
