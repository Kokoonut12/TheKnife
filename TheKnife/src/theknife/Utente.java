package theknife;
/**

 * Classe astratta che rappresenta un utente generico della piattaforma (cliente o ristoratore).

 * Viene estesa da {@link Cliente} e {@link Ristoratore}.

 */

public abstract class Utente {
	// === Attributi protetti accessibili dalle sottoclassi ===
    protected String nome;// Nome dell'utente
    protected String cognome;//Cognome dell'utente
    protected String username;//Nome utente
    protected String passwordHash;// Hash della password (per sicurezza)
    protected String domicilio;// Città o domicilio
    protected String ruolo;// Ruolo: "cliente" o "ristoratore"
    /**

         * Costruttore per creare un utente generico.

         *

         * @param nome Nome dell’utente

         * @param cognome Cognome dell’utente

         * @param username Username unico

         * @param passwordHash Hash della password

         * @param domicile Città o luogo di residenza

         * @param role Ruolo dell’utente (cliente o ristoratore)

         */



    public Utente(String nome, String cognome, String username, String passwordHash, String domicilio, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordHash = passwordHash;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }
    /**

         * Restituisce lo username dell’utente.

         *

         * @return Lo username

         */

    public String getUsername() { return username; }
    /**

         * Restituisce il ruolo dell’utente (cliente o ristoratore).

         *

         * @return Il ruolo dell’utente

         */
    public String getRuolo() { return ruolo; }
}
