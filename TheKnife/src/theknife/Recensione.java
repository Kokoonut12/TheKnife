package theknife;
/**

 * Classe che rappresenta una recensione lasciata da un utente su un ristorante.

 */
public class Recensione {
    private Utente utente;//Utente che ha scritto la recensione
    private Ristorante ristorante;//Ristorsnte recensito
    private int stelle;//valutazione da1-5
    private String testo;//testo della recensione
    private String risposta;//Risposta opzionale del ristoratore
    /**

         * Costruttore completo.

         *

         * @param utente L'utente autore della recensione

         * @param ristorante Il ristorante oggetto della recensione

         * @param stelle Numero di stelle assegnate (1-5)

         * @param texte Testo della recensione

         */
    

    public Recensione(Utente auteur, Ristorante restaurant, int etoiles, String texte) {
        this.utente = auteur;
        this.ristorante = restaurant;
        this.stelle = etoiles;
        this.testo = texte;
        this.risposta = null;
    }
    // === Getter ===

    public Ristorante getRistorante() { return ristorante; }
    public Utente getUtente() { return utente; }
    public int getStelle() { return stelle; }
    public String getTesto() { return testo; }
    public String getRisposta() { return risposta; }
    // === Setter ===



    /**

         * Imposta una nuova valutazione in stelle (1-5).

         *

         * @param stelle Numero di stelle (1-5)

         */

    // Setter per permettere la modifica
    public void setStelle(int etoiles) {
        if (etoiles >= 1 && etoiles <= 5) {
            this.stelle = etoiles;
        }
    }
    /**

         * Imposta un nuovo testo per la recensione.

         *

         * @param texte Testo aggiornato

         */

    public void setTesto(String texte) {
        this.testo = texte;
    }
    /**

         * Imposta una risposta del ristoratore. Può essere settata solo una volta.

         *

         * @param risposta La risposta del ristoratore

         */

    // Il ristoratore può rispondere una sola volta
    public void setRisposta(String reponse) {
        if (this.risposta == null) {
            this.risposta = reponse;
        }
    }
    /**

         * Rappresentazione leggibile della recensione, inclusa l'eventuale risposta del ristoratore.

         */

    @Override
    public String toString() {
        String avis = stelle + "★ - " + testo + " (di " + utente.getUsername() + ")";
        if (risposta != null) {
            avis += "\nRisposta del ristoratore: " + risposta;
        }
        return avis;
    }
}