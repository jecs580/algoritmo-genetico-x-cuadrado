import java.util.Random;

class Principal {
    static int filas = 6;
    static int columnas = 5;
    static int nGanadores = 3;
    static int nGenes = 5;
    static String[][] poblacion = new String[filas][columnas];
    static String[][] poblacionTemp = new String[filas][columnas];
    static String[] parejas = new String[filas]; // Vector de las posiciones de la pareja por poblacion
    static String[] ganadores = new String[nGanadores]; // Vector de las posiciones de los mejores individuos
    static double sumatoria = 0;

    // Poblacion Inicial
    public static void PoblacionInicial(String[][] poblacion) {
        System.out.println("******************************************************");
        System.out.println("************************Iniciando Poblacion***********************");
        System.out.println("******************************************************");
        String individuo = "";
        Random r = new Random();
        for (int i = 0; i < filas; i++) { // Recorre la poblacion inicial
            individuo = "";
            for (int j = 0; j < nGenes; j++) { // Recorre el numero de genes
                individuo += r.nextInt(2) + ",";
            }
            poblacion[i][0] = "" + i; // Numero del individuo
            poblacion[i][1] = individuo;
        }
    }

    // Convertimos a decimal los genes del individuo
    public static void convertir_individuo(String[][] poblacion) {
        double valorInteger = 0;
        for (int i = 0; i < filas; i++) {
            valorInteger = 0;
            String[] valores = poblacion[i][1].split(",");
            int indice = 0;
            for (int j = valores.length - 1; j >= 0; j--) {
                valorInteger = valorInteger + (Double.parseDouble(valores[j]) * (Math.pow(2, indice)));
                indice++;
            }
            poblacion[i][2] = valorInteger + "";
            // sumatoria+=valorInteger;
        }
    }

    public static double Funcion_Fx(double x) {
        // Obtenemos valor del individuo al cuadrado
        return x * x;
    }

    // Funcion de Adaptabilidad por Individuo
    public static double calidad_individuo(String[][] poblacion) {
        double mayor = Double.parseDouble(poblacion[0][2]); // Obtenemos el numero del primer individuo en decimal
        double valor = 0;
        for (int i = 0; i < filas; i++) {
            valor = Funcion_Fx(Double.parseDouble(poblacion[i][2]));
            poblacion[i][3] = valor + "";
            sumatoria += valor;
            if (mayor < valor) {
                mayor = valor;
            }
        }
        System.out.println("*********************** Individuo mas Apto *******************");
        System.out.println(" --> " + mayor);
        return mayor;
    }

    // Cruce y Mutacion
    public static void Cruce_Mutacion(String[][] poblacion, String[][] poblacionTemp) {
        System.out.println("*********************** Cruce y Mutacion ************************");
        System.out.println("__________________________________________");
        Random r = new Random();
        int puntoCruce = 0;
        String[] individuoA;
        String[] individuoB;
        String ParejaA = "";
        // *************************** Crce **************************
        for (int i = 0; i < filas / 2; i++) {
            individuoA = poblacion[i][1].split(",");
            ParejaA = parejas[i]; // Vector de posiciones de parejas
            String cadAdn = "";
            individuoB = poblacion[Integer.parseInt(ParejaA)][1].split(",");
            puntoCruce = r.nextInt(4); // Punto de cruce [0,3] OJO
            System.out.println("Punto de cruce :[" + puntoCruce + "]" + "-Posicion IndividuoA: [" + poblacion[i][0]
                    + "]" + "- IndividuoA: [" + poblacion[i][1] + "] --> Cruzado con: " + " Posicion individuoB: ["
                    + poblacion[Integer.parseInt(ParejaA)][0] + "] " + "- IndividuoB: ["
                    + poblacion[Integer.parseInt(ParejaA)][1] + "]");
            // Genes del primero Individuo en formato String
            for (int j = 0; j < puntoCruce; j++) {
                cadAdn += individuoA[j] + ",";
            }
            // Genes del segundo Individuo en formato String
            for (int j = puntoCruce; j < individuoA.length; j++) {
                cadAdn += individuoB[j] + ",";
            }
            System.out.println("******************* Nuevo Individuo " + cadAdn + "*******************");
            poblacionTemp[i][0] = i + "";
            poblacionTemp[i][1] = cadAdn;
        }
        for (int i = 0; i < parejas.length; i++) {
            poblacion[i][0] = poblacionTemp[i][0];
            poblacion[i][1] = poblacionTemp[i][1];
        }
        // *************************** Mutacion ***************************
        int mutado = (parejas.length / 2) + 1; // mutado =4;
        System.out.println("El individuo a ser mutado esta en la posicion: [" + mutado + "][" + 1 + "]");
        individuoA = poblacion[mutado][1].split(","); // Obtenemos al individuo en un formato vector de strings
        System.out.println("*********************** Mutacion **********************");
        System.out.println("****** Individuo ***************** Resultado ***********");
        int gen = r.nextInt(4); // OJO
        if (individuoA[gen].equals("0")) {
            individuoA[gen] = "1";
        } else {
            individuoA[gen] = "0";
        }
        String cadAdn = "";
        for (int i = 0; i < individuoA.length; i++) {
            cadAdn += individuoA[i] + ",";
        }
        System.out.println("[" + poblacion[mutado][0] + "]-[" + poblacion[mutado][1] + "]" + " Posicion gen Mutado: ["
                + gen + "] Resultado => Posicion individuo [" + poblacion[mutado][0] + "]-[" + cadAdn + "]");
        poblacion[mutado][1] = cadAdn;
    }

    // Funcion que genera una nueva poblacion copiando 2 veces por ganador.
    public static void copiarse(String[][] poblacion, String[][] poblacionTemp) {
        System.out.println("***************** Copiarse ************************");
        int indice = 0;
        int t = 0;
        for (int i = 0; i < ganadores.length; i++) {
            int ganador = Integer.parseInt(ganadores[i]);
            poblacionTemp[indice][0] = (i + t) + "";
            poblacionTemp[indice + 1][0] = (i + t + 1) + "";
            for (int j = 1; j < columnas; j++) {
                poblacionTemp[indice][j] = poblacion[ganador][j];
                poblacionTemp[indice + 1][j] = poblacion[ganador][j];
            }
            indice += 2;
            t++;
        }

        // Hacemos la copia a la poblacion Original con los nuevos genes
        for (int i = 0; i < filas; i++) {
            poblacion[i][0] = poblacionTemp[i][0];
            poblacion[i][1] = poblacionTemp[i][1];
        }
    }

    public static void verGanadores(String[] ganadores) {
        System.out.println("*************** Ganadores ****************");
        int win = 0;
        for (int i = 0; i < ganadores.length; i++) {
            win = Integer.parseInt(ganadores[i]);
            System.out.println("[" + poblacion[win][0] + "][" + poblacion[win][1] + "][" + poblacion[win][2] + "]["
                    + poblacion[win][3] + "]");
        }
    }

    public static void torneo(String[][] poblacion) {
        System.out.println("***************** Torneo *********************");
        String desempenoA = "";
        String parejaA = "";
        String desempenoB = "";
        int indP = 0;
        for (int i = 0; i < filas / 2; i++) {
            desempenoA = poblacion[i][3]; // Numero elevado al cuadrado
            parejaA = parejas[i];
            desempenoB = poblacion[Integer.parseInt(parejaA)][3];
            System.out.println("[" + poblacion[i][0] + "][" + poblacion[i][1] + "][" + poblacion[i][2] + "]["
                    + desempenoA + "] contra => [" + poblacion[Integer.parseInt(parejaA)][0] + "]["
                    + poblacion[Integer.parseInt(parejaA)][1] + "][" + poblacion[Integer.parseInt(parejaA)][2] + "]["
                    + desempenoB + "]");
            if (Double.parseDouble(desempenoA) >= Double.parseDouble(desempenoB)) {
                ganadores[indP] = poblacion[i][0];
            } else {
                ganadores[indP] = parejaA;
            }
            indP++;
        }
    }

    public static void seleccion_parejas(String[][] poblacion) {
        System.out.println("**************************************************************");
        System.out.println("****************** Seleccion de Parejas ******************");
        // String aux=poblacion[1][0];
        for (int i = 0; i < filas; i++) {
            parejas[(filas - 1) - i] = poblacion[i][0];
        }
    }

    public static void Adaptabilidad(String[][] poblacion, double sumatoria) {
        for (int i = 0; i < filas; i++) {
            poblacion[i][4] = (Double.parseDouble(poblacion[i][3]) / sumatoria) + "";
        }
    }

    public static void verPoblacion(String[][] poblacion, boolean pareja) {
        System.out.println("***************** Poblacion Actual *********************");
        String cadena = "";
        for (int i = 0; i < filas; i++) {
            for (int k = 0; k < columnas; k++) {
                cadena += "[" + poblacion[i][k] + "]";
            }
            if (pareja) {
                cadena += "Pareja " + parejas[i] + "\n";
            } else {
                cadena += "" + "\n";
            }
        }
        System.out.println(cadena);
    }

    public static void main(String[] args) {
        PoblacionInicial(poblacion);
        double adaptados = 0;
        while (adaptados < 961) {
            convertir_individuo(poblacion);
            adaptados = calidad_individuo(poblacion);
            Adaptabilidad(poblacion, sumatoria);
            verPoblacion(poblacion, true);
            seleccion_parejas(poblacion);
            torneo(poblacion);
            verGanadores(ganadores);
            copiarse(poblacion, poblacionTemp);
            verPoblacion(poblacionTemp, true);
            seleccion_parejas(poblacion);
            Cruce_Mutacion(poblacion, poblacionTemp);
        }
        convertir_individuo(poblacion);
        adaptados = calidad_individuo(poblacion);
        Adaptabilidad(poblacion, sumatoria);
        verPoblacion(poblacion, false);
    }
}