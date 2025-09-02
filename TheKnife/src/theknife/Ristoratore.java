package theknife;

import java.util.ArrayList;
import java.util.List;

/**

 * La classe {@code Ristoratore} rappresenta un utente con ruolo di ristoratore.

 * 

 * Un ristoratore può:

 * <ul>

 *   <li>Gestire una lista dei propri {@link Ristorante}</li>

 *   <li>Visualizzare i ristoranti con le relative recensioni ({@link Recensione})</li>

 *   <li>Rispondere alle recensioni lasciate dai clienti</li>

 * </ul>

 * 

 * Estende {@link Utente}.

 */

public class Ristoratore extends Utente {
	/** Lista dei ristoranti posseduti dal ristoratore */
    private ArrayList<Ristorante> mieiRistorante = new ArrayList<>();
    /**

         * Costruttore della classe Ristoratore.

         *

         * @param nome      nome del ristoratore

         * @param cognome          cognome del ristoratore

         * @param username     username per l'accesso

         * @param passwordHash password cifrata dell'utente

         * @param domicilio     indirizzo o domicilio del ristoratore

         */

    public Ristoratore(String nome, String cognome, String username, String passwordHash, String domicilio) {
        super(nome, cognome, username, passwordHash, domicilio, "ristoratore");
    }
    /**

         * Aggiunge un ristorante alla lista del ristoratore.

         *

         * @param r il ristorante da aggiungere

         */

    public void aggiungereRistorante(Ristorante r) { mieiRistorante.add(r); }
    /**

         * Restituisce la lista di ristoranti posseduti.

         *

         * @return lista dei ristoranti del ristoratore

         */
    public List<Ristorante> getMesRestaurants() {
        return mieiRistorante;
    }
    /**

         * Visualizza a console i ristoranti del ristoratore con eventuali recensioni e risposte.

         * 

         * Se non ci sono ristoranti o recensioni, viene stampato un messaggio informativo.

         */

    public void visualizzareMieiRistorante() {
        if (mieiRistorante.isEmpty()) {
            System.out.println("Non hai ancora aggiunto alcun ristorante.");
            return;
        }

        System.out.println("=== I miei ristoranti ===");
        for (Ristorante r : mieiRistorante) {
            System.out.println(r);
            if (r.getRecensione().isEmpty()) {
                System.out.println("Nessuna recensione al momento.");
            } else {
                for (int i = 0; i < r.getRecensione().size(); i++) {
                    Recensione rec = r.getRecensione().get(i);
                    System.out.println("   " + i + ". " + rec);
                    if (rec.getRisposta() != null) {
                        System.out.println("Risposta del ristoratore: " + rec.getRisposta());
                    }
                }
            }
        }
    }
    /**

         * Permette al ristoratore di rispondere a una recensione specifica.

         *

         * @param r          ristorante di riferimento

         * @param indexAvis  indice della recensione

         * @param risposta   testo della risposta del ristoratore

         */


    public void rispondereRecensione(Ristorante r, int indexAvis, String risposta) {
        if (indexAvis >= 0 && indexAvis < r.getRecensione().size()) {
            r.getRecensione().get(indexAvis).setRisposta(risposta);
        } else {
            System.out.println("Recensione non trovata.");
        }
    }
}