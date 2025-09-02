package theknife;
/**

* Classe principale del programma.

* Avvia l'applicazione "TheKnife", che consente la gestione e la consultazione

* di ristoranti, recensioni e utenti (clienti e ristoratori).

*/

public class TheKnife {
	/**

	* Metodo main: punto d'ingresso dell'applicazione.

	*

	* @param args Argomenti da linea di comando (non utilizzati)

	*/
    public static void main(String[] args) {
        System.out.println("Benvenuto/a su TheKnife!");
     // Istanzia il menu principale dell'app
        Menu menu = new Menu();
     // Avvia il menu interattivo
        menu.visualizzareMenuPrincipale();
    }
}

