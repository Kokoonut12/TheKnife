package theknife;

import java.util.ArrayList;
import java.util.List;

/**

 * La classe {@code Ristorante} rappresenta un ristorante registrato nella piattaforma.

 * <p>

 * Contiene le informazioni di base sul ristorante (nome, posizione, fascia di prezzo, 

 * servizi offerti, tipo di cucina) e gestisce le {@link Recensione} lasciate dagli utenti.

 * </p>

 */

public class Ristorante {
    private String nome;
    private String paese;
    private String citta;
    private String indirizzo;
    private double latitudine;
    private double longitudine;
    private double fasciadelprezzo;
    private boolean delivery;
    private boolean prenotazione;
    private String tipoCucina;
    
    /** Lista delle recensioni ricevute dal ristorante */
    private List<Recensione> recensione = new ArrayList<>();
    
    /**

         * Costruttore della classe Ristorante.

         *

         * @param nome           nome del ristorante

         * @param paese          paese in cui si trova

         * @param citta          città in cui si trova

         * @param adresse        indirizzo del ristorante

         * @param latitudine     coordinate GPS (latitudine)

         * @param longitudine    coordinate GPS (longitudine)

         * @param fasciadelprezzo fascia di prezzo indicativa (€)

         * @param delivery       indica se il ristorante offre consegna a domicilio

         * @param prenotazione   indica se il ristorante accetta prenotazioni

         * @param tipodicucina   tipo di cucina (es. "Italiana", "Giapponese", ecc.)

         */

    public Ristorante(String nome, String paese, String citta, String indirizzo, double latitudine, double longitudine,
                      double fasciadelprezzo, boolean delivery, boolean prenotazione, String tipoCucina) {
        this.nome = nome;
        this.paese = paese;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.fasciadelprezzo = fasciadelprezzo;
        this.delivery = delivery;
        this.prenotazione = prenotazione;
        this.tipoCucina = tipoCucina;
    }
 // --- GETTER principali ---

    public String getNome() { return nome; }
    public String getCitta() { return citta; }
    public double getFasciaDelPrezzo() { return fasciadelprezzo; }
    public String getTipoCucina() { return tipoCucina; }
    
    /**

         * Aggiunge una recensione alla lista del ristorante.

         *

         * @param r recensione da aggiungere

         */
    public void aggiungereRecensione(Recensione r) { recensione.add(r); }
    
    /**

         * Restituisce tutte le recensioni ricevute.

         *

         * @return lista delle recensioni

         */
    
    public List<Recensione> getRecensione() { return recensione; }

    /**

         * Calcola la media delle stelle date dagli utenti.

         *

         * @return media delle stelle (0 se non ci sono recensioni)

         */
    
    public double getMediaStelle() {
        if (recensione.isEmpty()) return 0.0;
        double total = 0;
        for (Recensione r : recensione) total += r.getStelle();
        return total / recensione.size();
    }
    /**

         * Restituisce una stringa descrittiva del ristorante.

         *

         * @return stringa con nome, città, paese, fascia di prezzo e media delle recensioni

         */

    @Override
    public String toString() {
        return nome + " (" + citta + ", " + paese + ") - " + fasciadelprezzo + "€ | Media stelle: " + getMediaStelle() + "★";
    }
}
