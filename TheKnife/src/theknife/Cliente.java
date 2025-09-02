package theknife;

import java.util.*;
/**

 * La classe Cliente estende {@link Utente} e rappresenta un utente registrato 

 * di tipo "client" all'interno dell'applicazione. 

 * 

 * Un cliente può:

 * <ul>

 *   <li>Aggiungere ed eliminare ristoranti dalla lista dei preferiti</li>

 *   <li>Visualizzare i propri ristoranti preferiti</li>

 *   <li>Lasciare, modificare o eliminare recensioni sui ristoranti</li>

 *   <li>Visualizzare le proprie recensioni</li>

 * </ul>

 */

public class Cliente extends Utente {
	 /** Lista dei ristoranti preferiti del cliente */
	
    private ArrayList<Ristorante> preferiti = new ArrayList<>();
    
    /** Mappa che associa ciascun ristorante recensito alla recensione lasciata dal cliente */
    
    private Map<Ristorante, Recensione> mierecensione = new HashMap<>();
    
    /**

         * Costruttore della classe Cliente.

         * 

         * @param nome Nome del cliente

         * @param cognome Cognome del cliente

         * @param username Username univoco

         * @param passwordHash Hash della password

         * @param domicile Indirizzo del cliente

         */
    public Cliente(String nome, String cognome, String username, String passwordHash, String domicilio) {
        super(nome, cognome, username, passwordHash, domicilio, "client");
    }
    /** @return La lista dei ristoranti preferiti */

    public ArrayList<Ristorante> getPreferiti() { return preferiti; }
    /** @return La mappa delle recensioni lasciate dal cliente */
    public Map<Ristorante, Recensione> getMieRecensione() { return mierecensione; }

    // --- Preferiti ---
    /**

         * Aggiunge un ristorante alla lista dei preferiti, se non già presente.

         * @param r Ristorante da aggiungere

         */
    public void aggiungerePreferiti(Ristorante r) {
        if (!preferiti.contains(r)) preferiti.add(r);
    }
    /**

         * Rimuove un ristorante dalla lista dei preferiti.

         * @param r Ristorante da rimuovere

         */

    public void eliminaPreferiti(Ristorante r) {
        preferiti.remove(r);
    }
    /**

         * Visualizza la lista dei preferiti del cliente.

         * Stampa un messaggio se non ci sono preferiti.

         */

    public void visuallizzarePreferiti() {
        if (preferiti.isEmpty()) {
            System.out.println("Nessun ristorante tra i preferiti.");
            return;
        }
        for (Ristorante r : preferiti) System.out.println(r);
    }

    // --- Recensioni ---
    /**

         * Permette al cliente di lasciare una recensione per un ristorante.

         * 

         * @param r        Il ristorante da recensire

         * @param stelle  Numero di stelle assegnate

         * @param testo   Testo della recensione

         * @return true se la recensione è stata aggiunta, false se il cliente aveva già recensito lo stesso ristorante

         */
 // Cliente.java
    public boolean lasciareRecensione(Ristorante r, int stelle, String testo) {
        // Controlla se esiste già una recensione per questo ristorante (case-insensitive)
        for (Ristorante exist : mierecensione.keySet()) {
            if (exist.getNome().equalsIgnoreCase(r.getNome())) {
                System.out.println("Hai già lasciato una recensione per questo ristorante.");
                return false;
            }
        }
        Recensione rec = new Recensione(this, r, stelle, testo);
        r.aggiungereRecensione(rec);
        mierecensione.put(r, rec);
        return true;
    }
    
    /**

         * Mostra tutte le recensioni lasciate dal cliente.

         * Se non ci sono recensioni, stampa un messaggio.

         */
    

    public void visualizzareMieRecensione() {
        if (mierecensione.isEmpty()) {
            System.out.println("Non hai ancora lasciato nessuna recensione.");
            return;
        }
        for (Ristorante r : mierecensione.keySet()) {
            System.out.println(r.getNome() + " → " + mierecensione.get(r));
        }
    }
    /**

         * Elimina la recensione lasciata su un ristorante (ricerca per nome).

         * 

         * @param nomResto Nome del ristorante di cui eliminare la recensione

         */

    public void eliminareRecensione(String nomResto) {
        Ristorante cible = null;
        for (Ristorante r : mierecensione.keySet()) {
            if (r.getNome().equalsIgnoreCase(nomResto)) {
                cible = r;
                break;
            }
        }
        if (cible != null) {
            Recensione recensione = mierecensione.remove(cible);
            cible.getRecensione().remove(recensione);
            System.out.println("Recensione rimossa per " + cible.getNome());
        } else {
            System.out.println(" Nessuna recensione trovata per questo ristorante.");
        }
    }
    /**

         * Modifica una recensione esistente lasciata per un ristorante.

         * 

         * @param nomResto Nome del ristorante

         * @param stelle  Nuovo numero di stelle

         * @param testo    Nuovo testo della recensione

         * @return true se la recensione è stata modificata, false se non è stata trovata

         */

    public boolean modificareRecensione(String nomResto, int stelle, String testo) {
        for (Ristorante r : mierecensione.keySet()) {
            if (r.getNome().equalsIgnoreCase(nomResto)) {
                Recensione rec = mierecensione.get(r);
                rec.setStelle(stelle);
                rec.setTesto(testo);
                return true; // Modifica riuscita
            }
        }
        return false; // Nessuna recensione trovata

    }

    
}